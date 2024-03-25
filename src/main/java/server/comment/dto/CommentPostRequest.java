package server.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import server.answer.entity.Answer;
import server.comment.entity.Comment;
import server.user.entity.User;

@Getter
public class CommentPostRequest {

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    public Comment toEntity(Answer answer, User user) {
        return Comment.builder()
                .content(this.content)
                .user(user)
                .answer(answer)
                .build();
    }
}
