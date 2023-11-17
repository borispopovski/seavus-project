package mk.seavus.demoprojcet.service;

import java.util.List;

import mk.seavus.model.UserDto;
import mk.seavus.model.UserResponseDto;

public interface IUserService {

	UserResponseDto createUser(UserDto user);
	void deleteUser(Long id);
	List<UserDto> getUsers();
	UserDto getUserById(Long id);
	UserDto getUserByName(String userName);
	UserResponseDto updateUser(UserDto userDto);
}
