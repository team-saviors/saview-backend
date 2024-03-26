package server.answer.controller;


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
import server.answer.dto.AnswerPostRequest;
import server.answer.dto.AnswerPutRequest;
import server.answer.dto.VoteRequest;
import server.answer.service.AnswerService;
import server.jwt.oauth.PrincipalDetails;

@Validated
@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/questions/{question-id}/answers")
    public ResponseEntity<Void> postAnswer(@Positive @PathVariable("question-id") long questionId,
                                           @Valid @RequestBody AnswerPostRequest request,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long answerId = answerService.createdAnswer(
                request,
                questionId,
                principalDetails.getUsername());

        return ResponseEntity.created(URI.create("/answers/" + answerId)).build();
    }

    // todo: 왜 수정메서드는 사용자 인증이 없을까?
    @PutMapping("/answers/{answer-id}")
    public ResponseEntity<Void> putAnswer(@Positive @PathVariable("answer-id") long answerId,
                                          @Valid @RequestBody AnswerPutRequest request) {
        answerService.updateContent(answerId, request.getContent());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/answers/{answer-id}/votes")
    public ResponseEntity<Void> putVotes(@Positive @PathVariable("answer-id") long answerId,
                                         @Valid @RequestBody VoteRequest request,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        answerService.verifiedVotes(answerId, principalDetails.getUser().getUserId(), request.getVotes());
        answerService.addVotedScore(answerId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/answers/{answer-id}")
    public ResponseEntity<Void> deleteAnswer(@Positive @PathVariable("answer-id") long answerId) {
        answerService.deleteAnswer(answerId);

        return ResponseEntity.noContent().build();
    }
}
