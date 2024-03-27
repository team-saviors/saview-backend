package server.answer.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import server.answer.entity.Answer;
import server.question.entity.Question;
import server.user.entity.User;

@Getter
public class AnswerPostRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    public Answer toEntity(User user, Question question) {
        return Answer.builder()
                .user(user)
                .question(question)
                .content(this.content)
                .build();
    }
}
