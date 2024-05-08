package server.user.fixture;

import server.user.entity.User;

public class UserFixture {
    public static User createUser(String email) {
        return User.builder()
            .email(email)
            .password("password")
            .nickname("nickname")
            .build();
    }

    public static User createUser(String email, String password) {
        return User.builder()
            .email(email)
            .password(password)
            .nickname("nickname")
            .build();
    }

    public static User createUser(String email, String password, String nickname) {
        return User.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
