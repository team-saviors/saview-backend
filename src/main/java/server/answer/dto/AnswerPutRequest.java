package server.answer.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AnswerPutRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
