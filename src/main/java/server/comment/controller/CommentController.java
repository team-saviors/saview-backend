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
import server.comment.dto.CommentPostRequest;
import server.comment.dto.CommentPutRequest;
import server.comment.service.CommentService;
import server.jwt.oauth.PrincipalDetails;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/answers/{answer-id}/comments")
    public ResponseEntity<Void> postComment(@Positive @PathVariable("answer-id") long answerId,
                                            @Valid @RequestBody CommentPostRequest request,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long commentId = commentService.createComment(request, answerId,
                principalDetails.getUsername());

        return ResponseEntity.created(URI.create("/comments/" + commentId)).build();
    }

    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<Void> putComment(@Positive @PathVariable("comment-id") long commentId,
                                           @Valid @RequestBody CommentPutRequest request) {
        commentService.updateComment(commentId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> deleteComment(@Positive @PathVariable("comment-id") long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }
}
