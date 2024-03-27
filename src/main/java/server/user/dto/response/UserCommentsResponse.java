package server.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import server.comment.entity.Comment;
import server.response.CommentUserResponse;
import server.response.MultiResponse;

@Getter
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class UserCommentsResponse {

    MultiResponse<CommentUserResponse> myPosts;

    public static UserCommentsResponse from(Page<Comment> commentPage) {
        List<Comment> comments = commentPage.getContent();
        List<CommentUserResponse> responses = comments.stream()
                .map(CommentUserResponse::from)
                .collect(Collectors.toList());

        return new UserCommentsResponse(new MultiResponse<>(responses, commentPage));
    }
}
