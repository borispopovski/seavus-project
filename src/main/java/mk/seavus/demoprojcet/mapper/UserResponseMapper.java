package mk.seavus.demoprojcet.mapper;

import org.mapstruct.Mapper;

import mk.seavus.demoprojcet.entity.User;
import mk.seavus.model.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

	UserResponseDto convertToUserResponseDto(User user);

}
