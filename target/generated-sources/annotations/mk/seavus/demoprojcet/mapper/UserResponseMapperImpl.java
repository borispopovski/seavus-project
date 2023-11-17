package mk.seavus.demoprojcet.mapper;

import javax.annotation.processing.Generated;
import mk.seavus.demoprojcet.entity.User;
import mk.seavus.model.UserResponseDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-06T18:35:25+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.13 (Oracle Corporation)"
)
@Component
public class UserResponseMapperImpl implements UserResponseMapper {

    @Override
    public UserResponseDto convertToUserResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId( user.getId() );

        return userResponseDto;
    }
}
