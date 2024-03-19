package server.question.entity;

import lombok.*;
import server.answer.entity.Answer;

import server.audit.Auditable;
import server.question.dto.request.QuestionPutRequest;
import server.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(nullable = false, length = 30)
    private String mainCategory;

    @Column(nullable = false, length = 30)
    private String subCategory;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Question(String mainCategory, String subCategory, String content, User user) {
        this.content = content;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.user = user;
    }

    public void updateQuestion(QuestionPutRequest questionPutRequest) {
        this.content = questionPutRequest.getContent();
        this.mainCategory = questionPutRequest.getMainCategory();
        this.subCategory = questionPutRequest.getSubCategory();
    }
}
