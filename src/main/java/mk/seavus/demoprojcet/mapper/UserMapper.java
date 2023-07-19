package mk.seavus.demoprojcet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import mk.seavus.demoprojcet.entity.User;
import mk.seavus.model.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(source = "userName", target = "username")
	UserDto convertToUserDto(User user);
	
	@Mapping(source = "username", target = "userName")
	User convertToUser(UserDto userDto);
	
	@Mapping(source = "username", target = "userName")
	void updateUserFromDto(UserDto userDto, @MappingTarget User user);
	
}
