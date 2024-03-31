package server.question.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import server.answer.dto.AnswerResponse;
import server.answer.entity.Answer;
import server.question.entity.Question;
import server.response.MultiResponse;
import server.user.dto.response.UserProfileResponse;

@Getter
@Builder(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class QuestionDetailResponse {

    Long questionId;
    String content;
    String mainCategory;
    String subCategory;
    int views;
    UserProfileResponse user;
    MultiResponse<AnswerResponse> answers;

    public static QuestionDetailResponse of(Question question, Page<Answer> answerPage) {
        return QuestionDetailResponse.builder()
                .questionId(question.getQuestionId())
                .content(question.getContent())
                .mainCategory(question.getMainCategory())
                .subCategory(question.getSubCategory())
                .views(question.getViews())
                .user(UserProfileResponse.from(question.getUser()))
                .answers(createMultiResponse(answerPage))
                .build();
    }

    private static MultiResponse<AnswerResponse> createMultiResponse(Page<Answer> answerPage) {
        List<Answer> answers = answerPage.getContent();
        List<AnswerResponse> responses = answers.stream()
                .map(AnswerResponse::from)
                .collect(Collectors.toList());

        return new MultiResponse<>(responses, answerPage);
    }
}
