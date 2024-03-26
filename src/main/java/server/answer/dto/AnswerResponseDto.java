package server.answer.dto;

import lombok.Getter;
import lombok.Setter;
import server.comment.dto.CommentResponse;
import server.user.dto.response.UserProfileResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AnswerResponseDto {
    private long answerId;
    private String content;
    private int votes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private UserProfileResponse user;
    private List<CommentResponse> comments;
}
