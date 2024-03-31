package server.answer.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.audit.Auditable;
import server.comment.entity.Comment;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;
import server.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int votes = 0;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    private Answer(String content, Question question, User user) {
        this.content = content;
        this.question = question;
        this.user = user;
    }

    public void updateContent(String content) {
        if (Objects.isNull(content)) {
            throw new BusinessLogicException(ExceptionCode.BLANK_CONTENT);
        }

        this.content = content;
    }

    public void addUserBadgeScore(int addValue) {
        this.user.addBadgeScore(addValue);
    }

    public void updateVotes(int votes) {
        this.votes = votes;
    }
}
