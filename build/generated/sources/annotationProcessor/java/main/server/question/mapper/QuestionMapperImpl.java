package server.question.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.question.dto.QuestionPostPutDto;
import server.question.entity.Question;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:10:18+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public Question questionPostPutDtoToQuestion(QuestionPostPutDto questionPostPutDto) {
        if ( questionPostPutDto == null ) {
            return null;
        }

        Question question = new Question();

        question.setMainCategory( questionPostPutDto.getMainCategory() );
        question.setSubCategory( questionPostPutDto.getSubCategory() );
        question.setContent( questionPostPutDto.getContent() );

        return question;
    }
}
