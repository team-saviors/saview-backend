package server.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import server.user.entity.User;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@FieldDefaults(level = PRIVATE)
public class UserResponse {
    String email;
    String nickname;
    String profile;
    String status;
    int score;
    int level;
    String badgeImg;

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .email(user.getEmail())
            .nickname(user.getNickname())
            .profile(user.getProfile())
            .status(user.getUserStatus().getStatus())
            .score(user.getBadge().getScore())
            .level(user.getBadge().getLevel())
            .badgeImg(user.getBadge().getBadgeImg())
            .build();
    }
}
