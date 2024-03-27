package server.answer.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.answer.dto.VoteRequest;
import server.answer.service.VoteService;
import server.jwt.oauth.PrincipalDetails;

@Validated
@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PutMapping("/answers/{answer-id}/votes")
    public ResponseEntity<Void> putVotes(@Positive @PathVariable("answer-id") long answerId,
                                         @Valid @RequestBody VoteRequest request,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        voteService.createVote(answerId,
                principalDetails.getUser().getUserId(),
                request.getVotes());

        return ResponseEntity.ok().build();
    }
}
