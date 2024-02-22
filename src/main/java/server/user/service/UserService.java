package server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.user.dto.UserPostDto;
import server.user.dto.UserPutDto;
import server.user.entity.Badge;
import server.user.entity.RefreshToken;
import server.user.entity.User;
import server.user.mapper.UserMapper;
import server.user.repository.BadgeRepository;
import server.user.repository.RefreshTokenRepository;
import server.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BadgeRepository badgeRepository;

    private final UserMapper userMapper;

    public User createUser(UserPostDto userPostDto) {
        // 초기에 도메인 변환 필수(규칙)
        User user = userMapper.userPostDtoToUser(userPostDto);

        checkDuplicationInfo(user);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setBadge(createBadge(user));
        user.setDefaultInfo();

        return userRepository.save(user);
    }

    private void checkDuplicationInfo(User user) {
        verifyExistsEmail(user.getEmail());
        verifyExistsNickname(user.getNickname());
    }

    public Badge createBadge(User user) {
        Badge badge = Badge.builder().score(0).level(1).badgeImg("default img").user(user).build();
        return badgeRepository.save(badge);
    }

    public void updatePassword(String email, String curPassword, String newPassword) {
        User user = findVerifiedUserByEmail(email);
        if (!bCryptPasswordEncoder.matches(curPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "기존 비밀번호와 일치하지 않습니다.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
    }

    public User findUser(String email) {
        return findVerifiedUserByEmail(email);
    }

    public User findUserById(long userId) {
        User user = findVerifiedUser(userId);
        if (user.getUserStatus().equals(User.UserStatus.USER_QUIT)) {
            throw new BusinessLogicException(ExceptionCode.QUIT_USER);
        }
        return user;
    }

    public void updateRefreshToken(String email, String refreshToken) {
        RefreshToken refreshTokenEntity = RefreshToken.builder().email(email).refreshToken(refreshToken).build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void updateUser(String email, UserPutDto userPutDto) {
        User user = userRepository.findByEmail(email);
        user.setNickname(userPutDto.getNickname());
        user.setProfile(userPutDto.getProfile());
    }

    public void deleteUser(String email) {
        User findUser = findVerifiedUserByEmail(email);
        findUser.setUserStatus(User.UserStatus.USER_QUIT);
        refreshTokenRepository.deleteByEmail(email);
    }


    private User findVerifiedUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    private User findVerifiedUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        return user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    private void verifyExistsEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent())
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_EMAIL);
    }

    private void verifyExistsNickname(String nickname) {
        Optional<User> user = Optional.ofNullable(userRepository.findByNickname(nickname));
        if (user.isPresent())
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_NICKNAME);
    }

    public void setTempPassword(String email,
                                String tempPassword) {
        User user = findVerifiedUserByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(tempPassword));
    }
}
