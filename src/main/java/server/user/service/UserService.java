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
import server.user.repository.BadgeRepository;
import server.user.repository.RefreshTokenRepository;
import server.user.repository.UserRepository;

import java.util.Optional;

import static server.user.entity.User.UserStatus.USER_QUIT;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BadgeRepository badgeRepository;

    public User createUser(UserPostDto userPostDto) {
        checkDuplicationInfo(userPostDto);

        User newUser = User.builder()
            .nickname(userPostDto.getNickname())
            .email(userPostDto.getEmail())
            .password(bCryptPasswordEncoder.encode(userPostDto.getPassword()))
            .build();

        newUser.initBadge(createBadge(newUser));
        return userRepository.save(newUser);
    }

    private void checkDuplicationInfo(UserPostDto userPostDto) {
        verifyExistsEmail(userPostDto.getEmail());
        verifyExistsNickname(userPostDto.getNickname());
    }

    public Badge createBadge(User user) {
        return badgeRepository.save(new Badge(user));
    }

    public void updatePassword(String email, String curPassword, String newPassword) {
        User user = findVerifiedUserByEmail(email);
        if (!bCryptPasswordEncoder.matches(curPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "기존 비밀번호와 일치하지 않습니다.");
        }
        user.changePassword(bCryptPasswordEncoder.encode(newPassword));
    }

    public User findUser(String email) {
        return findVerifiedUserByEmail(email);
    }

    public User findUserById(long userId) {
        User user = findVerifiedUser(userId);
        user.checkQuitUser();
        return user;
    }

    public void updateRefreshToken(String email, String refreshToken) {
        RefreshToken refreshTokenEntity = RefreshToken.builder().email(email).refreshToken(refreshToken).build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public void updateUser(String email, UserPutDto userPutDto) {
        User user = userRepository.findByEmail(email);
        user.updateNicknameAndProfile(userPutDto.getNickname(),userPutDto.getProfile());
    }

    public void deleteUser(String email) {
        User findUser = findVerifiedUserByEmail(email);
        findUser.updateStatus(USER_QUIT);
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
        user.changePassword(bCryptPasswordEncoder.encode(tempPassword));
    }
}
