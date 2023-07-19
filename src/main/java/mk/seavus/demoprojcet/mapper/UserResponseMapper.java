package mk.seavus.demoprojcet.mapper;

import org.mapstruct.Mapper;

import mk.seavus.model.UserDto;
import mk.seavus.model.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

	UserResponseDto convertToUserResponseDto(UserDto user);
	
}
