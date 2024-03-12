package server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponseDto;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class UserCommentsResponse {
    MultiResponseDto<AnswerCommentUserResponse> myPosts;
    public static UserCommentsResponse from(MultiResponseDto<AnswerCommentUserResponse> myPosts) {
        return new UserCommentsResponse(myPosts);
    }
}
