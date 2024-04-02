package server.fixture;

import static server.fixture.QuestionFixture.질문1;
import static server.fixture.UserFixture.유저_경륜;

import server.answer.entity.Answer;

@SuppressWarnings("NonAsciiCharacters")
public class AnswerFixture {

    public final static Answer 답변1 = Answer.builder()
            .user(유저_경륜)
            .content("비밀입니다.")
            .question(질문1)
            .build();
}
