package server.comment.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class CommentPutRequest {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
