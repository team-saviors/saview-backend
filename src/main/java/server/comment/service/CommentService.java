package server.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import server.answer.entity.Answer;
import server.answer.service.AnswerService;
import server.comment.dto.CommentPostPutDto;
import server.comment.dto.CommentResponseDto;
import server.comment.entity.Comment;
import server.comment.mapper.CommentMapper;
import server.comment.repository.CommentRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponseDto;
import server.user.entity.User;
import server.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    public static final int COMMENT_BADGE_SCORE = 10;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final AnswerService answerService;

    public Long createdComment(CommentPostPutDto commentDto, Long answerId, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        user.addBadgeScore(COMMENT_BADGE_SCORE);

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .answer(answerService.findVerifiedAnswer(answerId))
                .build();

        commentRepository.save(comment);
        return comment.getCommentId();
    }

    public void updateComment(Long commentId, CommentPostPutDto commentDto) {
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

    public List<CommentResponseDto> findComments(Answer answer) {
        List<Comment> findAllComments = commentRepository.findAllByAnswer(answer);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : findAllComments) {
            commentResponseDtos.add(commentMapper.commentToCommentResponseDto(comment));
        }
        return commentResponseDtos;
    }

    public MultiResponseDto<AnswerCommentUserResponse> userInfoComments(User user,
                                                                        int page, int size) {
        Page<Comment> pageComments = commentRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("commentId").descending()));
        List<Comment> comments = pageComments.getContent();
        return new MultiResponseDto<>(commentMapper.commentsToAnswerCommentUserResponseDtos(comments), pageComments);
    }
}
