package server.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import server.answer.dto.AnswerResponseDto;
import server.question.entity.Question;
import server.response.MultiResponseDto;
import server.user.dto.response.UserProfileResponse;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@Builder(access = PRIVATE)
public class QuestionDetailResponse {
    private Long questionId;
    private String content;
    private String mainCategory;
    private String subCategory;
    private int views;
    private UserProfileResponse user;
    private MultiResponseDto<AnswerResponseDto> answers;

    public static QuestionDetailResponse of(Question question, MultiResponseDto<AnswerResponseDto> answers) {
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
