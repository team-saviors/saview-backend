package server.response;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import server.comment.entity.Comment;
import server.question.entity.Question;

@Getter
@Setter
public class AnswerCommentUserResponse {

    private long questionId;
    private String questionContent;
    private String subCategory;
    private LocalDateTime createdAt;
    private String content;

    public AnswerCommentUserResponse() {
    }

    private AnswerCommentUserResponse(long questionId,
                                      String questionContent,
                                      String subCategory,
                                      LocalDateTime createdAt,
                                      String content) {
        this.questionId = questionId;
        this.questionContent = questionContent;
        this.subCategory = subCategory;
        this.createdAt = createdAt;
        this.content = content;
    }

    public static AnswerCommentUserResponse from(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        Question question = comment.getAnswer().getQuestion();
        return new AnswerCommentUserResponse(
                question.getQuestionId(),
                question.getContent(),
                question.getSubCategory(),
                comment.getCreatedAt(),
                comment.getContent()
        );
    }
}
