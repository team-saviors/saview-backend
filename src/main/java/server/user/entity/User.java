package server.user.entity;

import static server.user.entity.User.UserStatus.USER_ACTIVE;
import static server.user.entity.User.UserStatus.USER_QUIT;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.answer.entity.Answer;
import server.audit.Auditable;
import server.comment.entity.Comment;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;

@Entity
@Getter
@Table(name = "USER_TABLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String nickname;

    private String profile = "/Saview/logo_circle.png";

    private String role = "ROLE_USER";

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus userStatus = USER_ACTIVE;

    @OneToMany(mappedBy = "user")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Badge badge;

    @Getter
    public enum UserStatus {
        USER_ACTIVE("활동중"), USER_QUIT("탈퇴 상태");

        private final String status;

        UserStatus(String status) {
            this.status = status;
        }
    }

    @Builder
    public User(String password, String email, String nickname) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public void checkQuitUser() {
        if (userStatus.equals(USER_QUIT)) {
            throw new BusinessLogicException(ExceptionCode.QUIT_USER);
        }
    }

    public void updateNicknameAndProfile(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }

    public void initBadge(Badge badge) {
        this.badge = badge;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateStatus(UserStatus status) {
        userStatus = status;
    }

    public void addBadgeScore(int addValue) {
        this.badge.addScore(addValue);
    }
}
