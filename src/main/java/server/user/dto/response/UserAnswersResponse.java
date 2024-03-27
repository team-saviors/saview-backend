package server.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import server.answer.entity.Answer;
import server.response.AnswerUserResponse;
import server.response.MultiResponse;

@Getter
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class UserAnswersResponse {

    MultiResponse<AnswerUserResponse> myPosts;

    public static UserAnswersResponse from(Page<Answer> answerPage) {
        List<Answer> answers = answerPage.getContent();
        List<AnswerUserResponse> responses = answers.stream()
                .map(AnswerUserResponse::from)
                .collect(Collectors.toList());

        return new UserAnswersResponse(new MultiResponse<>(responses, answerPage));
    }
}
