package server.answer.mapper;

import org.mapstruct.Mapper;
import server.answer.dto.AnswerPostPutDto;
import server.answer.dto.AnswerResponseDto;
import server.answer.entity.Answer;
import server.comment.service.CommentService;
import server.response.AnswerCommentUserResponse;
import server.user.dto.response.UserProfileResponse;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface AnswerMapper {

    Answer answerPostPutDtoToAnswer(AnswerPostPutDto answerPostPutDto);
    default List<AnswerResponseDto> answersToAnswersResponseDtos(List<Answer> answers,
                                                                 CommentService commentService) {
        if ( answers == null ) {
            return null;
        }

        List<AnswerResponseDto> list = new ArrayList<>(answers.size());
        for ( Answer answer : answers ) {
            list.add( answerToAnswerResponseDto( answer, commentService ) );
        }

        return list;
    }


    default AnswerResponseDto answerToAnswerResponseDto(Answer answer,
                                                        CommentService commentService) {
        AnswerResponseDto answerResponseDto = new AnswerResponseDto();

        answerResponseDto.setAnswerId(answer.getAnswerId());
        answerResponseDto.setCreatedAt(answer.getCreatedAt());
        answerResponseDto.setModifiedAt(answer.getModifiedAt());
        answerResponseDto.setContent(answer.getContent());
        answerResponseDto.setVotes(answer.getVotes());

        answerResponseDto.setUser(UserProfileResponse.from(answer.getUser()));

        answerResponseDto.setComments(commentService.findComments(answer));

        return answerResponseDto;
    }

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
        if ( answers == null ) {
            return null;
        }

        List<AnswerCommentUserResponse> list = new ArrayList<>(answers.size());
        for ( Answer answer : answers ) {
            list.add( answerToAnswerCommentUserResponseDto( answer) );
        }

        return list;
    }
}
