package server.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentPutRequest {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
