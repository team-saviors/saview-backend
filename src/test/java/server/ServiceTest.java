package server;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;
import server.answer.entity.Answer;
import server.answer.repository.AnswerRepository;
import server.comment.entity.Comment;
import server.comment.repository.CommentRepository;
import server.question.entity.Question;
import server.question.repository.QuestionRepository;
import server.user.entity.Badge;
import server.user.entity.User;
import server.user.repository.BadgeRepository;
import server.user.repository.UserRepository;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServiceTest {

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected BadgeRepository badgeRepository;

    protected Comment saveComment(User user, Answer answer) {
        return commentRepository.save(Comment.builder()
                .answer(answer)
                .user(user)
                .content("댓글입니다.")
                .build());
    }

    protected Answer saveAnswer(User user, Question question) {
        return answerRepository.save(
                Answer.builder()
                        .content("답변입니다")
                        .question(question)
                        .user(user)
                        .build()
        );
    }

    protected Question saveQuestion(User user) {
        return questionRepository.save(
                Question.builder()
                        .content("질문입니다")
                        .mainCategory("메인 카테고리")
                        .subCategory("서브 카테고리")
                        .user(user)
                        .build()
        );
    }

    protected User saveUser() {
        User user = userRepository.save(
                User.builder()
                        .email("saview@gmail.com")
                        .nickname("saview")
                        .password("saview12345!@#")
                        .build()
        );
        Badge badge = saveBadge(user);
        user.initBadge(badge);

        return user;
    }

    private Badge saveBadge(User user) {
        return badgeRepository.save(new Badge(user));
    }
}
