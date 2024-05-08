package server.comment.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import server.ServiceTest;
import server.answer.entity.Answer;
import server.comment.dto.CommentPostRequest;
import server.comment.dto.CommentPutRequest;
import server.comment.entity.Comment;
import server.exception.BusinessLogicException;
import server.exception.ExceptionCode;
import server.question.entity.Question;
import server.user.entity.User;

@SuppressWarnings("NonAsciiCharacters")
class CommentServiceTest extends ServiceTest {

    public static final long INVALID_ANSWER_ID = -1L;
    public static final long INVALID_COMMENT_ID = -1L;

    @Autowired
    private CommentService commentService;

    @Test
    void 코멘트_생성_성공() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);

        CommentPostRequest request = CommentPostRequest.builder()
                .content("답변 내용 이다!")
                .build();

        // when, then
        assertDoesNotThrow(
                () -> commentService.createComment(
                        request,
                        answer.getAnswerId(),
                        user.getEmail())
        );
    }

    @Test
    void 코멘트_생성_시_답변이_존재_하지_않으면_예외_발생() {
        // given
        User user = saveUser();

        CommentPostRequest request = CommentPostRequest.builder()
                .content("답변 내용 이다!")
                .build();

        // when, then
        assertThatThrownBy(
                () -> commentService.createComment(
                        request,
                        INVALID_ANSWER_ID,
                        user.getEmail()
                )).isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining(ExceptionCode.ANSWER_NOT_FOUND.getMessage());
    }

    @Test
    void 코멘트_생성_시_존재하지_않는_사용자면_예외_발생() {
        // given
        String invalidEmail = "saview@saview.com";

        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);

        CommentPostRequest request = CommentPostRequest.builder()
                .content("답변 내용 이다!")
                .build();

        // when, then
        assertThatThrownBy(
                () -> commentService.createComment(
                        request,
                        answer.getAnswerId(),
                        invalidEmail
                )).isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void 코멘트_생성_시_유저_점수가_10점_증가() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);
        CommentPostRequest request = CommentPostRequest.builder()
                .content("답변 내용 이다!")
                .build();

        // when
        commentService.createComment(request, answer.getAnswerId(), user.getEmail());

        // then
        assertThat(user.getScore()).isEqualTo(10);
    }

    @Test
    void 코멘트_내용_수정_성공() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);

        CommentPutRequest request = CommentPutRequest.builder()
                .content("답변 내용 수정할래요.")
                .build();

        // when
        Comment comment = saveComment(user, answer);
        commentService.updateComment(comment.getCommentId(), request);

        // then
        assertThat(comment.getContent()).isEqualTo("답변 내용 수정할래요.");
    }

    @Test
    void 존재하지_않는_코멘트_수정_요청_시_예외_발생() {
        // given
        CommentPutRequest request = CommentPutRequest.builder()
                .content("답변 내용 수정할래요.")
                .build();

        // when, then
        assertThatThrownBy(
                () -> commentService.updateComment(
                        INVALID_COMMENT_ID,
                        request
                )).isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining(ExceptionCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    void 코멘트_삭제_성공() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);

        // when, then
        Comment comment = saveComment(user, answer);
        assertDoesNotThrow(
                () -> commentService.deleteComment(comment.getCommentId())
        );
    }

    @Test
    void 존재하지_않는_코멘트_삭제_요청_시_예외_발생() {
        // given
        // when, then
        assertThatThrownBy(
                () -> commentService.deleteComment(INVALID_COMMENT_ID))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining(ExceptionCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    void 사용자가_작성한_코멘트_전체_조회_성공() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);
        Comment comment = saveComment(user, answer);

        // when
        Page<Comment> comments = commentService.findCommentsByUser(user, 1, 1);

        // then
        assertAll(
                () -> assertThat(comments.getContent()).contains(comment),
                () -> assertThat(comments.getTotalElements()).isEqualTo(1),
                () -> assertThat(comments.getTotalPages()).isEqualTo(1)
        );
    }

    @Test
    void 사용자가_작성한_코멘트_전체_조회_시_최신순으로_정렬() {
        // given
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer answer = saveAnswer(user, question);
        Comment firstComment = saveComment(user, answer);
        Comment secondComment = saveComment(user, answer);

        // when
        Page<Comment> comments = commentService.findCommentsByUser(user, 1, 2);

        // then
        assertThat(comments.getContent()).containsExactly(secondComment, firstComment);
    }
}
