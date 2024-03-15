package server.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.user.entity.User;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
public class UserProfileResponse {
    private long userId;
    private String nickname;
    private String profile;
    private String status;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
            .userId(user.getUserId())
            .profile(user.getProfile())
            .nickname(user.getNickname())
            .status(user.getUserStatus().getStatus())
            .build();
    }
}
