package server.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.dto.request.QuestionPostRequest;
import server.question.dto.request.QuestionPutRequest;
import server.question.entity.Question;
import server.question.repository.QuestionRepository;
import server.user.service.UserService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;

    public Question createdQuestion(QuestionPostRequest questionPostRequest, String email) {
        Question question = questionPostRequest.toEntity(userService.findUser(email));
        return questionRepository.save(question);
    }

    public Question findQuestion(long questionId) {
        return findVerifiedQuestion(questionId);
    }

    public Page<Question> findQuestions(int page, int size) {
        return questionRepository.findAll(PageRequest.of(page, size, Sort.by("questionId").descending()));
    }

    public void updateQuestion(QuestionPutRequest questionPutRequest, long questionId) {
        Question updateQuestion = findVerifiedQuestion(questionId);
        updateQuestion.updateQuestion(questionPutRequest);
    }

    public void deleteQuestion(long questionId) {
        Question question = findVerifiedQuestion(questionId);
        questionRepository.delete(question);
    }

    public Question findVerifiedQuestion(long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        return question.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
    }

    public void updateViews(long questionId, int views) {
        Question question = findQuestion(questionId);
        question.updateViews(views);
    }

    public Page<Question> findQuestionsByCategory(String mainCategory, String subCategory, int page, int size, String sort) {

        Sort sortParam = Sort.by(sort).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sortParam);

        if (mainCategory.equals("all")) {
            if (subCategory.equals("all")) {
                return questionRepository.findAll(pageRequest);
            } else {
                return questionRepository.findAllBySubCategory(subCategory, pageRequest);
            }
        } else {
            if (subCategory.equals("all")) {
                return questionRepository.findAllByMainCategory(mainCategory, pageRequest);
            } else {
                return questionRepository.findAllByMainCategoryAndSubCategory(mainCategory, subCategory, pageRequest);
            }
        }
    }

    public Page<Question> search(String keyword, int page, int size, String sort) {
        return questionRepository.findByContentContaining(keyword,
            PageRequest.of(page, size, Sort.by(sort).descending()));
    }
}
