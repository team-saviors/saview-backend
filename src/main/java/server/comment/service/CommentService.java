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
import server.comment.dto.CommentPostPutDto;
import server.comment.dto.CommentResponseDto;
import server.comment.entity.Comment;
import server.comment.mapper.CommentMapper;
import server.comment.repository.CommentRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.response.AnswerCommentUserResponseDto;
import server.response.MultiResponseDto;
import server.user.entity.User;
import server.user.mapper.UserMapper;
import server.user.repository.UserRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    public static final int COMMENT_BADGE_SCORE = 10;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public Long createdComment(CommentPostPutDto commentDto, Long answerId, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .answer(user.findAnswerByAnswerId(answerId))
                .build();

        user.addBadgeScore(COMMENT_BADGE_SCORE);
        commentRepository.save(comment);

        return comment.getCommentId();
    }

    public void updateComment(Comment comment) {
        Comment findComment = findVerifiedComment(comment.getCommentId());
        findComment.setContent(comment.getContent());
        commentRepository.save(findComment);
    }

    public void deleteComment(long commentId) {
        Comment findComment = findVerifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    public Comment findVerifiedComment(long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    public List<CommentResponseDto> findComments(Answer answer, UserMapper userMapper) {
        List<Comment> findAllComments = commentRepository.findAllByAnswer(answer);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : findAllComments) {
            commentResponseDtos.add(commentMapper.commentToCommentResponseDto(comment, userMapper));
        }
        return commentResponseDtos;
    }

    public MultiResponseDto<AnswerCommentUserResponseDto> userInfoComments(User user,
                                                                           int page, int size) {
        Page<Comment> pageComments = commentRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("commentId").descending()));
        List<Comment> comments = pageComments.getContent();
        return new MultiResponseDto<>(commentMapper.commentsToAnswerCommentUserResponseDtos(comments), pageComments);
    }
}
