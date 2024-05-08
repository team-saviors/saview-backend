package server.comment.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static server.exception.ExceptionCode.BLANK_CONTENT;
import static server.fixture.AnswerFixture.답변1;
import static server.fixture.UserFixture.유저_성연;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import server.exception.BusinessLogicException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentTest {

    @Test
    void 댓글_생성_성공() {
        assertDoesNotThrow(
                () -> Comment.builder()
                        .content("궁금 합니다.")
                        .user(유저_성연)
                        .answer(답변1)
                        .build()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"댓글 수정합니다.", "안 궁금 합니다.", "다시 수정 합니다."})
    void 댓글_내용_수정_성공(String content) {
        Comment comment = Comment.builder()
                .content("궁금 합니다.")
                .user(유저_성연)
                .answer(답변1)
                .build();

        comment.updateContent(content);
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 댓글_내용이_NULL_또는_공백일_경우_수정_실패(String content) {
        Comment comment = Comment.builder()
                .content("궁금 합니다.")
                .user(유저_성연)
                .answer(답변1)
                .build();

        assertThatThrownBy(
                () -> comment.updateContent(content)
        ).isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining(BLANK_CONTENT.getMessage());
    }
}
