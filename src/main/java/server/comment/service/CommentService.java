package server.comment.service;

import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import server.answer.entity.Answer;
import server.answer.repository.AnswerRepository;
import server.comment.dto.CommentPostRequest;
import server.comment.dto.CommentPutRequest;
import server.comment.entity.Comment;
import server.comment.repository.CommentRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.user.entity.User;
import server.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    public static final int COMMENT_BADGE_SCORE = 10;

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public Long createComment(CommentPostRequest request, Long answerId, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));

        user.addBadgeScore(COMMENT_BADGE_SCORE);
        Comment comment = request.toEntity(answer, user);

        commentRepository.save(comment);
        return comment.getCommentId();
    }

    public void updateComment(Long commentId, CommentPutRequest request) {
        Comment comment = findVerifiedComment(commentId);
        comment.updateContent(request.getContent());
    }

    public void deleteComment(long commentId) {
        Comment findComment = findVerifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    private Comment findVerifiedComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    public Page<Comment> findCommentsByUser(User user, int page, int size) {
        return commentRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("commentId").descending()));
    }
}
