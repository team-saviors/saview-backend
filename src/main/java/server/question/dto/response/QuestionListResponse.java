package server.question.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import server.question.entity.Question;
import server.user.dto.response.UserProfileResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@FieldDefaults(level = PRIVATE)
@Builder(access = PRIVATE)
public class QuestionListResponse {
    private Long questionId;
    private String content;
    private String mainCategory;
    private String subCategory;
    private int views;
    private int answerNum;
    private UserProfileResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static QuestionListResponse from(Question question) {
        return QuestionListResponse.builder()
            .questionId(question.getQuestionId())
            .content(question.getContent())
            .mainCategory(question.getMainCategory())
            .subCategory(question.getSubCategory())
            .views(question.getViews())
            .answerNum(question.getAnswers().size())
            .user(UserProfileResponse.from(question.getUser()))
            .createdAt(question.getCreatedAt())
            .modifiedAt(question.getModifiedAt())
            .build();
    }

    public static List<QuestionListResponse> fromList(List<Question> questionList) {
        if (questionList == null) {
            return Collections.emptyList();
        } else {
            List<QuestionListResponse> list = new ArrayList<>(questionList.size());
            for (Question question : questionList) {
                list.add(QuestionListResponse.from(question));
            }

            return list;
        }
    }
}
