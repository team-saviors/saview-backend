package server.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import server.response.AnswerCommentUserResponse;
import server.response.MultiResponseDto;
import server.user.entity.Badge;
import server.user.entity.User;
import server.user.repository.BadgeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final VoteRepository voteRepository;
    private final BadgeRepository badgeRepository;

    public Long createdAnswer(Answer answer) {
        return answerRepository.save(answer).getAnswerId();
    }

    public void updateAnswer(Answer answer) {
        Answer findAnswer = findVerifiedAnswer(answer.getAnswerId());
        findAnswer.setContent(answer.getContent());
        answerRepository.save(findAnswer);
    }

    public void deleteAnswer(long answerId) {
        Answer findAnswer = findVerifiedAnswer(answerId);
        answerRepository.delete(findAnswer);
    }

    public Answer findVerifiedAnswer(long answerId) {
        Optional<Answer> answer = answerRepository.findById(answerId);
        return answer.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
    }

    public MultiResponseDto<AnswerResponseDto> findAnswers(Question question,
                                                           CommentService commentService,
                                                           int page, int size, String sort) {
        try {
            Page<Answer> pageAnswers = answerRepository.findAllByQuestion(question, PageRequest.of(page - 1, size, Sort.by(sort).descending()));
            List<Answer> answers = pageAnswers.getContent();
            return new MultiResponseDto<>(answerMapper.answersToAnswersResponseDtos(answers, commentService), pageAnswers);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SORT_PARAMETER);
        }
    }

    public MultiResponseDto<AnswerCommentUserResponse> userInfoAnswers(User user,
                                                                       int page, int size) {
        Page<Answer> pageAnswers = answerRepository.findAllByUser(user, PageRequest.of(page - 1, size, Sort.by("answerId").descending()));
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

    public void addAnswerScore(Badge badge) {
        int score = badge.getScore();
        badgeRepository.updateScore(score + 20, badge.getBadgeId());
    }

    public void addVotedScore(long answerId) {
        User votedUser = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND)).getUser();

        Badge badge = votedUser.getBadge();
        badgeRepository.updateScore(badge.getScore() + 50, badge.getBadgeId());
    }
}
