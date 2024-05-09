package server.fixture;

import server.user.entity.User;

@SuppressWarnings("NonAsciiCharacters")
public class UserFixture {

    public static final User 유저_세이뷰 = User.builder()
            .email("saview@gmail.com")
            .nickname("saview")
            .password("saview12345!@#")
            .build();

    public static final User 유저_경륜 = User.builder()
            .email("klkim1913@gmail.com")
            .nickname("klkim1913")
            .password("klkim1913!@#")
            .build();

    public static final User 유저_성연 = User.builder()
            .email("yeonkkk@gmail.com")
            .nickname("yeonkkk")
            .password("yeonkkk12345!@#")
            .build();
}
