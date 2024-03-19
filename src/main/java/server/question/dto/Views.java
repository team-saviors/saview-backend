package server.question.dto;

import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;

@Getter
public class Views {

    @PositiveOrZero
    private int views;
}
