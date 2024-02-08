package server.answer.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.answer.dto.AnswerPostPutDto;
import server.answer.entity.Answer;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:10:18+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public Answer answerPostPutDtoToAnswer(AnswerPostPutDto answerPostPutDto) {
        if ( answerPostPutDto == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setContent( answerPostPutDto.getContent() );

        return answer;
    }
}
