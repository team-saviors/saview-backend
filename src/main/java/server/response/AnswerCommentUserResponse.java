package server.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerCommentUserResponse {
    private long questionId;
    private String questionContent;
    private String subCategory;
    private LocalDateTime createdAt;
    private String content;
}
