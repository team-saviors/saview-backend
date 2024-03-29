package server.comment.dto;


import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import server.comment.entity.Comment;
import server.user.dto.response.UserProfileResponse;

@Getter
public class CommentResponse {

    private final long commentId;
    private final UserProfileResponse user;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String content;

    private CommentResponse(long commentId,
                            UserProfileResponse user,
                            LocalDateTime createdAt,
                            LocalDateTime modifiedAt,
                            String content) {
        this.commentId = commentId;
        this.user = user;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.content = content;
    }

    public static CommentResponse from(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }

        return new CommentResponse(
                comment.getCommentId(),
                UserProfileResponse.from(comment.getUser()),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getContent()
        );
    }
}
