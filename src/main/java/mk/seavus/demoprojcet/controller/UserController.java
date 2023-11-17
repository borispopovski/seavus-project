package mk.seavus.demoprojcet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mk.seavus.controller.UserApi;
import mk.seavus.demoprojcet.service.IUserService;
import mk.seavus.model.UserDto;
import mk.seavus.model.UserResponseDto;

@Slf4j
@RestController
public class UserController implements UserApi {

	private IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}
	
	@Override
	public ResponseEntity<UserResponseDto> addUser(UserDto userDto) {
		log.info("Recieved user: {}", userDto);
		UserResponseDto userResponseDto = userService.createUser(userDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<Void> deleteUserById(Long id) {
		log.info("Recieved used id: {}", id);
		userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<List<UserDto>> getUsers() {
		log.info("Reading all users");
		List<UserDto> usersDto = userService.getUsers();
		return new ResponseEntity<>(usersDto, HttpStatus.OK);
		
	}
	
	@Override
	public ResponseEntity<UserDto> getUserById(Long id) {
		log.info("Recieved used id: {}", id);
		UserDto userDto = userService.getUserById(id);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
		
	}
	
	@Override
	public ResponseEntity<UserDto> getUserByName(String username) {
		log.info("Recieved used name: {}", username);
		UserDto userDto = userService.getUserByName(username);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<UserResponseDto> updateUser(UserDto userDto) {
		log.info("Recieved id: {} and user {}", userDto);
		UserResponseDto userResponseDto = userService.updateUser(userDto);
		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}
}
