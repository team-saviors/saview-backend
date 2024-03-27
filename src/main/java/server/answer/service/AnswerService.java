package server.answer.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.answer.dto.AnswerPostRequest;
import server.answer.dto.AnswerPutRequest;
import server.answer.entity.Answer;
import server.answer.repository.AnswerRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;
import server.question.repository.QuestionRepository;
import server.user.entity.User;
import server.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    public static final int ANSWER_BADGE_SCORE = 20;
    public static final String SORT_PROPERTY = "answerId";

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public Long createdAnswer(AnswerPostRequest request,
                              Long questionId,
                              String email) {
        Question question = findQuestionById(questionId);
        User user = findVerifiedUserByEmail(email);
        Answer answer = request.toEntity(user, question);
        answer.addUserBadgeScore(ANSWER_BADGE_SCORE);

        return answerRepository.save(answer).getAnswerId();
    }

    private Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
    }

    private User findVerifiedUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        return user.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }


    public void updateContent(Long answerId, AnswerPutRequest request) {
        Answer answer = findVerifiedAnswer(answerId);
        answer.updateContent(request.getContent());
    }

    public void deleteAnswer(long answerId) {
        Answer findAnswer = findVerifiedAnswer(answerId);
        answerRepository.delete(findAnswer);
    }

    private Answer findVerifiedAnswer(long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
    }

    public Page<Answer> findAnswersByQuestion(Question question,
                                              int page,
                                              int size,
                                              String sort) {
        try {
            return answerRepository.findAllByQuestion(question,
                    PageRequest.of(page - 1, size, Sort.by(sort).descending()));
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT_PARAMETER);
        }
    }

    public Page<Answer> findAnswersByUser(User user,
                                          int page,
                                          int size) {
        return answerRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by(SORT_PROPERTY).descending()));
    }
}
