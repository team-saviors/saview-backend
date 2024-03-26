package server.answer.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import server.answer.entity.Answer;
import server.comment.dto.CommentResponse;
import server.user.dto.response.UserProfileResponse;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AnswerResponse {
    private long answerId;
    private String content;
    private int votes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private UserProfileResponse user;
    private List<CommentResponse> comments;

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .votes(answer.getVotes())
                .createdAt(answer.getCreatedAt())
                .modifiedAt(answer.getModifiedAt())
                .user(UserProfileResponse.from(answer.getUser()))
                .comments(
                        answer.getComments().stream()
                                .map(CommentResponse::from)
                                .collect(Collectors.toUnmodifiableList()))
                .build();

    }
}
