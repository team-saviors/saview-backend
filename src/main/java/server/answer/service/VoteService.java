package server.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.answer.entity.Answer;
import server.answer.entity.Vote;
import server.answer.repository.AnswerRepository;
import server.answer.repository.VoteRepository;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.user.entity.User;
import server.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {

    public static final int VOTE_BADGE_SCORE = 50;

    private final VoteRepository voteRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public void createVote(long answerId, Long userId, int votes) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANSWER_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        verifiedVotesByAnswerIdAndUserId(answer, user);

        voteRepository.save(Vote.builder()
                .answer(answer)
                .user(user)
                .build());

        answer.updateVotes(votes);
        user.addBadgeScore(VOTE_BADGE_SCORE);
    }

    private void verifiedVotesByAnswerIdAndUserId(Answer answer, User user) {
        if (voteRepository.existsByAnswerAndUser(answer, user)) {
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_VOTE);
        }
    }
}
