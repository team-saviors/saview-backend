package server.response;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import server.answer.entity.Answer;
import server.question.entity.Question;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerUserResponse {

    private final long questionId;
    private final String questionContent;
    private final String subCategory;
    private final LocalDateTime createdAt;
    private final String content;

    public static AnswerUserResponse from(Answer answer) {
        if (Objects.isNull(answer)) {
            return null;
        }
        Question question = answer.getQuestion();
        return new AnswerUserResponse(
                question.getQuestionId(),
                question.getContent(),
                question.getSubCategory(),
                answer.getCreatedAt(),
                answer.getContent()
        );
    }
}
