package server.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentPostRequest {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
