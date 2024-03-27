package server.answer.dto;

import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;

@Getter
public class VoteRequest {

    @PositiveOrZero
    private int votes;
}
