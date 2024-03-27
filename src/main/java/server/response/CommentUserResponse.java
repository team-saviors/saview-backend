package server.response;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import server.comment.entity.Comment;
import server.question.entity.Question;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUserResponse {

    private final long questionId;
    private final String questionContent;
    private final String subCategory;
    private final LocalDateTime createdAt;
    private final String content;

    public static CommentUserResponse from(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        Question question = comment.getAnswer().getQuestion();
        return new CommentUserResponse(
                question.getQuestionId(),
                question.getContent(),
                question.getSubCategory(),
                comment.getCreatedAt(),
                comment.getContent()
        );
    }
}
