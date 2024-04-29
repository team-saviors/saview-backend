package server.user.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.user.dto.request.UserPostRequest;
import server.user.dto.request.UserPutRequest;
import server.user.entity.Badge;
import server.user.entity.User;
import server.user.fixture.UserFixture;
import server.user.repository.BadgeRepository;
import server.user.repository.RefreshTokenRepository;
import server.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static server.user.entity.User.UserStatus.USER_QUIT;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters") // 한글 함수명을 쓰기 위함
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private final String NON_EXISTED_EMAIL = "nonexisted@gmail.com";

    @Test
    void 유저_정보를_입력받으면_유저를_등록한다() {
        // Given
        UserPostRequest userPostRequest = UserPostRequest.builder()
            .email("saview@gmail.com")
            .password("saview!@#")
            .nickname("saview")
            .build();
        given(bCryptPasswordEncoder.encode(anyString())).willReturn("encryptedPassword");
        given(userRepository.save(any(User.class))).willAnswer(i -> i.getArguments()[0]);
        given(badgeRepository.save(any(Badge.class))).willReturn(new Badge(any(User.class)));

        // When
        User createdUser = userService.createUser(userPostRequest);

        // Then
        assertThat(createdUser.getEmail()).isEqualTo("saview@gmail.com");
        assertThat(createdUser.getPassword()).isEqualTo("encryptedPassword");
        then(badgeRepository).should(times(1)).save(any(Badge.class));
    }

    @Test
    void 입력한_비밀번호가_현재_비밀번호와_일치하면_새로운_비밀번호로_변경한다() {
        // Given
        String email = "saview@gmail.com";
        User user = UserFixture.createUser(email);

        given(userRepository.findByEmail(email)).willReturn(user);
        given(bCryptPasswordEncoder.matches(anyString(), user.getPassword())).willReturn(true);

        // When
        userService.updatePassword(email, "saview!@#", "newSaview$%^");

        // Then
        then(bCryptPasswordEncoder).should(times(1)).encode("newSaview$%^");
        then(userRepository).should(times(1)).findByEmail(email);
    }

    @Test
    void 존재하는_이메일이면_해당_유저를_반환한다() {
        // Given
        String email = "saview@gmail.com";
        User user = UserFixture.createUser(email);

        given(userRepository.findByEmail("saview@gmail.com")).willReturn(user);

        // When
        User foundUser = userService.findUser("saview@gmail.com");

        // Then
        assertThat(foundUser.getEmail()).isEqualTo("saview@gmail.com");
        then(userRepository).should(times(1)).findByEmail("saview@gmail.com");
    }

    @Test
    void 존재하지_않는_이메일이면_USER_NOT_FOUND가_반환된다() {
        // Given
        given(userRepository.findByEmail(NON_EXISTED_EMAIL)).willThrow(new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // When, Then
        assertThatThrownBy(() -> userService.findUser(NON_EXISTED_EMAIL))
            .isInstanceOf(BusinessLogicException.class)
            .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void 존재하는_유저ID일_경우_해당_유저를_반환한다() {
        // Given
        String email = "saview@gmail.com";
        User user = UserFixture.createUser(email);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // When
        User foundUser = userService.findUserById(anyLong());

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("saview@gmail.com");
        then(userRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void 존재하는_이메일이면_입력받은_정보로_유저정보를_수정한다() {
        // Given
        String email = "saview@gmail.com";
        User user = UserFixture.createUser(email);
        UserPutRequest userPutRequest = UserPutRequest.builder()
            .nickname("newNickname")
            .profile("newProfile")
            .build();
        given(userRepository.findByEmail("saview@gmail.com")).willReturn(user);

        // When
        userService.updateUser("saview@gmail.com", userPutRequest);

        // Then
        assertThat(user.getNickname()).isEqualTo("newNickname");
        then(userRepository).should(times(1)).findByEmail("saview@gmail.com");
    }

    @Test
    void 존재하는_이메일이면_유저상태를_탈퇴상태로_변경한다() {
        // Given
        String email = "saview@gmail.com";
        User user = UserFixture.createUser(email);
        given(userRepository.findByEmail("saview@gmail.com")).willReturn(user);

        // When
        userService.deleteUser("saview@gmail.com");

        // Then
        assertThat(user.getUserStatus()).isEqualTo(USER_QUIT);
        then(refreshTokenRepository).should(times(1)).deleteByEmail("saview@gmail.com");
    }

    @Test
    void 존재하는_이메일이면_해당_유저의_비밀번호를_입력받은_임시비밀번호로_변경한다() {
        // Given
        String email = "saview@gmail.com";
        String password = "saview!@#";
        User user = UserFixture.createUser(email, password);

        String encPassword = "encryptedPassword";
        given(userRepository.findByEmail("saview@gmail.com")).willReturn(user);
        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encPassword);

        // When
        userService.setTempPassword("saview@gmail.com", password);

        // Then
        assertThat(user.getPassword()).isEqualTo(encPassword);
        then(userRepository).should(times(1)).findByEmail("saview@gmail.com");
    }
}