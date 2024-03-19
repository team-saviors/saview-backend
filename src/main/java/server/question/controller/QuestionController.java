package server.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.answer.dto.AnswerResponseDto;
import server.answer.service.AnswerService;
import server.comment.service.CommentService;
import server.question.dto.Views;
import server.question.dto.request.QuestionPostRequest;
import server.question.dto.request.QuestionPutRequest;
import server.question.dto.response.QuestionDetailResponse;
import server.question.dto.response.QuestionListResponse;
import server.question.entity.Question;
import server.question.service.QuestionService;
import server.response.MultiResponseDto;
import server.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> postQuestion(@Valid @RequestBody QuestionPostRequest questionPostRequest,
                                             Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Question question = questionPostRequest.toEntity(userService.findUser(email));

        final Long questionId = questionService.createdQuestion(question);

        return ResponseEntity.created(URI.create("/questions/" + questionId)).build();
    }

    @GetMapping("/{question-id}")
    public ResponseEntity<QuestionDetailResponse> getQuestion(@Positive @RequestParam int page,
                                                              @Positive @RequestParam int size,
                                                              @RequestParam String sort,
                                                              @Positive @PathVariable("question-id") long questionId) {
        Question question = questionService.findQuestion(questionId);
        MultiResponseDto<AnswerResponseDto> answers = answerService.findAnswers(question, commentService, page, size, sort);
        return ResponseEntity.ok(QuestionDetailResponse.of(question, answers));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<MultiResponseDto<QuestionListResponse>> getQuestions(@Positive @RequestParam int page,
                                                                               @Positive @RequestParam int size) {
        Page<Question> pageQuestions = questionService.findQuestions(page - 1, size);
        List<Question> questions = pageQuestions.getContent();

        return ResponseEntity.ok(new MultiResponseDto<>(QuestionListResponse.fromList(questions), pageQuestions));
    }

    @PutMapping("/{question-id}")
    public ResponseEntity<Void> putQuestion(@Positive @PathVariable("question-id") long questionId,
                                            @Valid @RequestBody QuestionPutRequest questionPutRequest) {
        questionService.updateQuestion(questionPutRequest, questionId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity<Void> deleteQuestion(@Positive @PathVariable("question-id") long questionId) {
        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{question-id}/views")
    public ResponseEntity<Void> putViews(@Positive @PathVariable("question-id") long questionId,
                                         @Valid @RequestBody Views views) {
        questionService.updateViews(questionId, views.getViews());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/tags")
    @Transactional(readOnly = true)
    public ResponseEntity<MultiResponseDto<QuestionListResponse>> getQuestionsByCategory(@RequestParam String mainCategory,
                                                                                         @RequestParam String subCategory,
                                                                                         @Positive @RequestParam int page,
                                                                                         @Positive @RequestParam int size,
                                                                                         @RequestParam String sort) {
        Page<Question> pageQuestions = questionService.findQuestionsByCategory(mainCategory, subCategory, page - 1, size, sort);
        List<Question> questions = pageQuestions.getContent();

        return ResponseEntity.ok(new MultiResponseDto<>(QuestionListResponse.fromList(questions), pageQuestions));
    }

    @GetMapping("/search")
    public ResponseEntity<MultiResponseDto<QuestionListResponse>> searchQuestion(@RequestParam(value = "keyword") String keyword,
                                                                                 @Positive @RequestParam int page,
                                                                                 @Positive @RequestParam int size,
                                                                                 @RequestParam String sort) {
        Page<Question> pageQuestions = questionService.search(keyword, page - 1, size, sort);
        List<Question> questions = pageQuestions.getContent();
        return ResponseEntity.ok(new MultiResponseDto<>(QuestionListResponse.fromList(questions), pageQuestions));
    }
}
