package server.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import server.user.entity.User;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@Builder(access = PRIVATE)
public class UserProfileResponse {
    long userId;
    String nickname;
    String profile;
    String status;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
            .userId(user.getUserId())
            .profile(user.getProfile())
            .nickname(user.getNickname())
            .status(user.getUserStatus().getStatus())
            .build();
    }
}
