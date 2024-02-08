package server.crawling.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.crawling.dto.RecruitResponseDto;
import server.crawling.entity.Recruit;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:10:18+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class RecruitMapperImpl implements RecruitMapper {

    @Override
    public List<RecruitResponseDto> recruitsToRecruitResponsesDto(List<Recruit> recruits) {
        if ( recruits == null ) {
            return null;
        }

        List<RecruitResponseDto> list = new ArrayList<RecruitResponseDto>( recruits.size() );
        for ( Recruit recruit : recruits ) {
            list.add( recruitToRecruitResponseDto( recruit ) );
        }

        return list;
    }

    protected RecruitResponseDto recruitToRecruitResponseDto(Recruit recruit) {
        if ( recruit == null ) {
            return null;
        }

        RecruitResponseDto recruitResponseDto = new RecruitResponseDto();

        recruitResponseDto.setRecruitId( recruit.getRecruitId() );
        recruitResponseDto.setCategory( recruit.getCategory() );
        recruitResponseDto.setName( recruit.getName() );
        recruitResponseDto.setTitle( recruit.getTitle() );
        recruitResponseDto.setExperience( recruit.getExperience() );
        recruitResponseDto.setEducation( recruit.getEducation() );
        recruitResponseDto.setLocation( recruit.getLocation() );
        recruitResponseDto.setDate( recruit.getDate() );
        recruitResponseDto.setLink( recruit.getLink() );

        return recruitResponseDto;
    }
}
