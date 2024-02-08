package server.user.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import server.user.dto.UserPostDto;
import server.user.dto.UserPutDto;
import server.user.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-08T21:10:18+0900",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userPostDtoToUser(UserPostDto userPostDto) {
        if ( userPostDto == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( userPostDto.getPassword() );
        user.setEmail( userPostDto.getEmail() );
        user.setNickname( userPostDto.getNickname() );

        return user;
    }

    @Override
    public User userPutDtoToUser(UserPutDto userPutDto) {
        if ( userPutDto == null ) {
            return null;
        }

        User user = new User();

        user.setNickname( userPutDto.getNickname() );
        user.setProfile( userPutDto.getProfile() );

        return user;
    }
}
