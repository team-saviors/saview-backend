package server.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import server.answer.dto.AnswerResponse;
import server.question.entity.Question;
import server.response.MultiResponse;
import server.user.dto.response.UserProfileResponse;

import static lombok.AccessLevel.PRIVATE;

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

    public static QuestionDetailResponse of(Question question, MultiResponse<AnswerResponse> answers) {
        return QuestionDetailResponse.builder()
            .questionId(question.getQuestionId())
            .content(question.getContent())
            .mainCategory(question.getMainCategory())
            .subCategory(question.getSubCategory())
            .views(question.getViews())
            .user(UserProfileResponse.from(question.getUser()))
            .answers(answers)
            .build();
    }
}
