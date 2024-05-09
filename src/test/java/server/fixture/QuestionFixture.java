package server.fixture;

import static server.fixture.UserFixture.유저_세이뷰;

import server.question.entity.Question;

@SuppressWarnings("NonAsciiCharacters")
public class QuestionFixture {

    public static final Question 질문1 = Question.builder()
            .content("spring 좋아 하나요?")
            .user(유저_세이뷰)
            .mainCategory("backend")
            .subCategory("spring")
            .build();
}
