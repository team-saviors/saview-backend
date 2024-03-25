package server.comment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import server.answer.entity.Answer;
import server.answer.service.AnswerService;
import server.comment.dto.CommentPostRequest;
import server.comment.dto.CommentPutRequest;
import server.comment.dto.CommentResponse;
import server.comment.entity.Comment;
import server.comment.mapper.CommentMapper;
import server.comment.repository.CommentRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponseDto;
import server.user.entity.User;
import server.user.repository.UserRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    public static final int COMMENT_BADGE_SCORE = 10;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final AnswerService answerService;

    public Long createComment(CommentPostRequest request, Long answerId, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        user.addBadgeScore(COMMENT_BADGE_SCORE);
        Answer answer = answerService.findVerifiedAnswer(answerId);
        Comment comment = request.toEntity(answer, user);

        commentRepository.save(comment);
        return comment.getCommentId();
    }

    public void updateComment(Long commentId, CommentPutRequest commentDto) {
        Comment comment = findVerifiedComment(commentId);
        comment.updateContent(commentDto.getContent());
    }

    public void deleteComment(long commentId) {
        Comment findComment = findVerifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    private Comment findVerifiedComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    public List<CommentResponse> findComments(Answer answer) {
        List<Comment> findAllComments = commentRepository.findAllByAnswer(answer);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : findAllComments) {
            commentResponses.add(commentMapper.commentToCommentResponseDto(comment));
        }
        return commentResponses;
    }

    public MultiResponseDto<AnswerCommentUserResponse> userInfoComments(User user,
                                                                        int page, int size) {
        Page<Comment> pageComments = commentRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("commentId").descending()));
        List<Comment> comments = pageComments.getContent();
        return new MultiResponseDto<>(commentMapper.commentsToAnswerCommentUserResponseDtos(comments), pageComments);
    }
}
