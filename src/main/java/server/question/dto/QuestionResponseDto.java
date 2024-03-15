package server.question.dto;

import lombok.Getter;
import lombok.Setter;
import server.answer.dto.AnswerResponseDto;
import server.response.MultiResponseDto;
import server.user.dto.response.UserProfileResponse;

@Getter
@Setter
public class QuestionResponseDto {
    private Long questionId;
    private String content;
    private String mainCategory;
    private String subCategory;
    private int views;
    private UserProfileResponse user;
    private MultiResponseDto<AnswerResponseDto> answers;

}
