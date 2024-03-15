package server.question.dto;

import lombok.Getter;
import lombok.Setter;
import server.user.dto.response.UserProfileResponse;

import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionsResponseDto {
    private Long questionId;
    private String content;
    private String mainCategory;
    private String subCategory;
    private int views;
    private int answerNum;
    private UserProfileResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
