package server.answer.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import server.answer.entity.Answer;
import server.response.AnswerCommentUserResponse;


@Mapper(componentModel = "spring")
public interface AnswerMapper {

    default AnswerCommentUserResponse answerToAnswerCommentUserResponseDto(Answer answer) {
        AnswerCommentUserResponse answerCommentUserResponse = new AnswerCommentUserResponse();

        answerCommentUserResponse.setQuestionId(answer.getQuestion().getQuestionId());
        answerCommentUserResponse.setQuestionContent(answer.getQuestion().getContent());
        answerCommentUserResponse.setSubCategory(answer.getQuestion().getSubCategory());

        answerCommentUserResponse.setCreatedAt(answer.getCreatedAt());
        answerCommentUserResponse.setContent(answer.getContent());

        return answerCommentUserResponse;
    }

    default List<AnswerCommentUserResponse> answersToAnswerCommentUserResponseDtos(List<Answer> answers) {
        if (answers == null) {
            return null;
        }

        List<AnswerCommentUserResponse> list = new ArrayList<>(answers.size());
        for (Answer answer : answers) {
            list.add(answerToAnswerCommentUserResponseDto(answer));
        }

        return list;
    }
}
