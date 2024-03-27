package server.answer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.answer.dto.AnswerPostRequest;
import server.answer.dto.AnswerPutRequest;
import server.answer.dto.AnswerResponse;
import server.answer.entity.Answer;
import server.answer.repository.AnswerRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;
import server.question.repository.QuestionRepository;
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponse;
import server.user.entity.User;
import server.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    public static final int ANSWER_BADGE_SCORE = 20;

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

    public MultiResponse<AnswerResponse> findAnswers(Question question,
                                                     int page,
                                                     int size,
                                                     String sort) {
        try {
            Page<Answer> pageAnswers = answerRepository.findAllByQuestion(question,
                    PageRequest.of(page - 1, size, Sort.by(sort).descending()));
            List<Answer> answers = pageAnswers.getContent();
            List<AnswerResponse> responses = answers.stream()
                    .map(AnswerResponse::from)
                    .collect(Collectors.toUnmodifiableList());

            return new MultiResponse<>(responses, pageAnswers);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT_PARAMETER);
        }
    }


    public MultiResponse<AnswerCommentUserResponse> userInfoAnswers(User user,
                                                                    int page, int size) {
        Page<Answer> pageAnswers = answerRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("answerId").descending()));
        List<Answer> answers = pageAnswers.getContent();
        List<AnswerCommentUserResponse> responses = answers.stream()
                .map(AnswerCommentUserResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new MultiResponse<>(responses, pageAnswers);
    }
}
