package server.answer.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import server.answer.entity.Answer;
import server.comment.dto.CommentResponse;
import server.comment.entity.Comment;
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
                .comments(createComments(answer.getComments()))
                .build();
    }

    private static List<CommentResponse> createComments(List<Comment> comments) {
        if (Objects.isNull(comments)) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }
}
