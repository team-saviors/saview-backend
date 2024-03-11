package server.comment.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import server.user.dto.UserProfileResponseDto;

@Getter
@Setter
public class CommentResponseDto {

    private long commentId;
    private UserProfileResponseDto user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String content;
}
