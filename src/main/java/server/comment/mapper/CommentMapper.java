package server.comment.mapper;

import org.mapstruct.Mapper;
import server.comment.dto.CommentResponseDto;
import server.comment.entity.Comment;
import server.response.AnswerCommentUserResponse;
import server.user.dto.response.UserProfileResponse;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentResponseDto commentToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setCommentId(comment.getCommentId());
        commentResponseDto.setContent(comment.getContent());
        commentResponseDto.setCreatedAt(comment.getCreatedAt());
        commentResponseDto.setModifiedAt(comment.getModifiedAt());
        commentResponseDto.setUser(UserProfileResponse.from(comment.getUser()));

        return commentResponseDto;
    }

    default AnswerCommentUserResponse commentToAnswerCommentUserResponseDto(Comment comment) {
        AnswerCommentUserResponse answerCommentUserResponse = new AnswerCommentUserResponse();

        answerCommentUserResponse.setQuestionId(comment.getAnswer().getQuestion().getQuestionId());
        answerCommentUserResponse.setQuestionContent(comment.getAnswer().getQuestion().getContent());
        answerCommentUserResponse.setSubCategory(comment.getAnswer().getQuestion().getSubCategory());
        answerCommentUserResponse.setCreatedAt(comment.getCreatedAt());
        answerCommentUserResponse.setContent(comment.getContent());

        return answerCommentUserResponse;
    }

    default List<AnswerCommentUserResponse> commentsToAnswerCommentUserResponseDtos(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        List<AnswerCommentUserResponse> list = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            list.add(commentToAnswerCommentUserResponseDto(comment));
        }

        return list;
    }
}
