package server.comment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.answer.entity.Answer;
import server.audit.Auditable;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder
    private Comment(String content, Answer answer, User user) {
        this.content = content;
        this.answer = answer;
        this.user = user;
    }

    public void updateContent(String content) {
        if (content.isEmpty() || content.isBlank()) {
            throw new BusinessLogicException(ExceptionCode.BLANK_CONTENT);
        }

        this.content = content;
    }
}
