package server.comment.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.comment.dto.CommentPostPutDto;
import server.comment.entity.Comment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:10:18+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment commentPostPutDtoToComment(CommentPostPutDto commentPostPutDto) {
        if ( commentPostPutDto == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setContent( commentPostPutDto.getContent() );

        return comment;
    }
}
