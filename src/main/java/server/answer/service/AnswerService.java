package server.answer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.answer.dto.AnswerPostRequest;
import server.answer.dto.AnswerResponseDto;
import server.answer.entity.Answer;
import server.answer.entity.Vote;
import server.answer.mapper.AnswerMapper;
import server.answer.repository.AnswerRepository;
import server.answer.repository.VoteRepository;
import server.comment.service.CommentService;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;
import server.question.service.QuestionService;
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponseDto;
import server.user.entity.Badge;
import server.user.entity.User;
import server.user.repository.BadgeRepository;
import server.user.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    public static final int ANSWER_BADGE_SCORE = 20;

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final VoteRepository voteRepository;
    private final BadgeRepository badgeRepository;
    private final QuestionService questionService;
    private final UserService userService;

    public Long createdAnswer(AnswerPostRequest request,
                              Long questionId,
                              String email) {
        /** TODO
         * 1. tansaction 전파 문제로 badge update가 영향을 받는다.
         * 2. 만약 service에서 변환 로직을 수행할거면, dto 위치가 service에 있어야 양방향 의존이 일어나지 않는다.
         */
        Question question = questionService.findVerifiedQuestion(questionId);
        User user = userService.findUser(email);
        Answer answer = request.toEntity(user, question);
        answer.addUserBadgeScore(ANSWER_BADGE_SCORE);

        return answerRepository.save(answer).getAnswerId();
    }

    public void updateContent(Long answerId, String content) {
        Answer answer = findVerifiedAnswer(answerId);
        answer.updateContent(content);
    }

    public void deleteAnswer(long answerId) {
        Answer findAnswer = findVerifiedAnswer(answerId);
        answerRepository.delete(findAnswer);
    }

    public Answer findVerifiedAnswer(long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(
                        () -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND)
                );
    }

    public MultiResponseDto<AnswerResponseDto> findAnswers(Question question,
                                                           CommentService commentService,
                                                           int page, int size, String sort) {
        try {
            Page<Answer> pageAnswers = answerRepository.findAllByQuestion(question,
                    PageRequest.of(page - 1, size, Sort.by(sort).descending()));
            List<Answer> answers = pageAnswers.getContent();
            return new MultiResponseDto<>(answerMapper.answersToAnswersResponseDtos(answers, commentService),
                    pageAnswers);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT_PARAMETER);
        }
    }

    public MultiResponseDto<AnswerCommentUserResponse> userInfoAnswers(User user,
                                                                       int page, int size) {
        Page<Answer> pageAnswers = answerRepository.findAllByUser(user,
                PageRequest.of(page - 1, size, Sort.by("answerId").descending()));
        List<Answer> answers = pageAnswers.getContent();
        return new MultiResponseDto<>(answerMapper.answersToAnswerCommentUserResponseDtos(answers), pageAnswers);
    }

    public void verifiedVotes(long answerId, Long userId, int votes) {
        if (!voteRepository.existsByAnswerIdAndUserId(answerId, userId)) {
            answerRepository.updateVotes(votes, answerId);
            Vote vote = Vote.builder().answerId(answerId).userId(userId).build();
            voteRepository.save(vote);
        } else {
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_VOTE);
        }
    }

    public void addVotedScore(long answerId) {
        User votedUser = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND)).getUser();

        Badge badge = votedUser.getBadge();
        badgeRepository.updateScore(badge.getScore() + 50, badge.getBadgeId());
    }
}
