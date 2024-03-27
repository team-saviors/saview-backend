package server.user.controller;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.answer.service.AnswerService;
import server.comment.entity.Comment;
import server.comment.service.CommentService;
import server.jwt.oauth.PrincipalDetails;
import server.response.MultiResponse;
import server.user.dto.request.PasswordRequest;
import server.user.dto.request.UserPostRequest;
import server.user.dto.request.UserPutRequest;
import server.user.dto.response.UserAnswersResponse;
import server.user.dto.response.UserCommentsResponse;
import server.user.dto.response.UserResponse;
import server.user.entity.User;
import server.user.service.UserService;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AnswerService answerService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> join(@Valid @RequestBody UserPostRequest userPostDto) {
        final User user = userService.createUser(userPostDto);
        return ResponseEntity.created(URI.create("/users/" + user.getUserId())).build();
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUser(@Positive @PathVariable("user-id") long userId) {
        User findUser = userService.findUserById(userId);
        return ResponseEntity.ok(UserResponse.from(findUser));
    }

    @PutMapping("/modify")
    public ResponseEntity<Void> putUser(@Valid @RequestBody UserPutRequest userPutRequest,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updateUser(principalDetails.getUsername(), userPutRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> putPassword(@Valid @RequestBody PasswordRequest passwordRequest,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.updatePassword(principalDetails.getUsername(), passwordRequest.getCurPassword(),
                passwordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    // UserInfo Page Answers
    @GetMapping("/{user-id}/user-answers")
    public ResponseEntity<UserAnswersResponse> userInfoAnswers(@Positive @PathVariable("user-id") long userId,
                                                               @Positive @RequestParam int page,
                                                               @Positive @RequestParam int size) {
        User findUser = userService.findUserById(userId);
        return ResponseEntity.ok(UserAnswersResponse.from(answerService.userInfoAnswers(findUser, page, size)));
    }


    // UserInfo Page Comments
    @GetMapping("/{user-id}/user-comments")
    public ResponseEntity<UserCommentsResponse> userInfoComments(@Positive @PathVariable("user-id") long userId,
                                                                 @Positive @RequestParam int page,
                                                                 @Positive @RequestParam int size) {
        User findUser = userService.findUserById(userId);
        Page<Comment> comments = commentService.findCommentsByUser(findUser, page, size);
        return ResponseEntity.ok(UserCommentsResponse.from(MultiResponse.from(comments)));
    }
}
