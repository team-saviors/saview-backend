package server.question.dto.request;

import lombok.Getter;
import server.question.entity.Question;
import server.user.entity.User;

import javax.validation.constraints.NotBlank;

@Getter
public class QuestionPostRequest {

    @NotBlank(message = "내용을 작성해주세요.")
    private String content;

    @NotBlank(message = "대분류를 선택해주세요.")
    private String mainCategory;

    @NotBlank(message = "소분류를 선택해주세요.")
    private String subCategory;

    public Question toEntity(User user) {
        return Question.builder()
            .content(content)
            .mainCategory(mainCategory)
            .subCategory(subCategory)
            .user(user)
            .build();
    }
}
