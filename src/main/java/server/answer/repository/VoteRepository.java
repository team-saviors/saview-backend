package server.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.answer.entity.Answer;
import server.answer.entity.Vote;
import server.user.entity.User;


public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByAnswerAndUser(Answer answer, User user);
}
