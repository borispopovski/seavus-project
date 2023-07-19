package mk.seavus.demoprojcet.service;

import java.util.List;

import mk.seavus.model.UserDto;

public interface IUserService {

	void createUser(UserDto user);
	void deleteUser(Long id);
	List<UserDto> getUsers();
	UserDto getUserById(Long id);
	UserDto getUserByName(String userName);
	void updateUser(Long id, UserDto userDto);
}
