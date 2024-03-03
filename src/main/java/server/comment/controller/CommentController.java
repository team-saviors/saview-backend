package server.comment.controller;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.answer.service.AnswerService;
import server.comment.dto.CommentPostPutDto;
import server.comment.entity.Comment;
import server.comment.mapper.CommentMapper;
import server.comment.service.CommentService;
import server.jwt.oauth.PrincipalDetails;
import server.user.entity.User;
import server.user.service.UserService;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AnswerService answerService;
    private final UserService userService;

    @PostMapping("/answers/{answer-id}/comments")
    public ResponseEntity<Void> postComment(@Positive @PathVariable("answer-id") long answerId,
                                            @Valid @RequestBody CommentPostPutDto commentPostPutDto,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Comment comment = commentMapper.commentPostPutDtoToComment(commentPostPutDto);
        comment.setAnswer(answerService.findVerifiedAnswer(answerId));
        User user = userService.findUser(principalDetails.getUsername());
        commentService.addCommentScore(user.getBadge());
        comment.setUser(user);

        final Long commentId = commentService.createdComment(comment);

        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<Void> putComment(@Positive @PathVariable("comment-id") long commentId,
                                           @Valid @RequestBody CommentPostPutDto commentPostPutDto) {
        Comment comment = commentMapper.commentPostPutDtoToComment(commentPostPutDto);
        comment.setCommentId(commentId);
        commentService.updateComment(comment);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> deleteComment(@Positive @PathVariable("comment-id") long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }
}
