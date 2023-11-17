package mk.seavus.demoprojcet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mk.seavus.demoprojcet.exception.UserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import mk.seavus.demoprojcet.entity.User;
import mk.seavus.demoprojcet.mapper.UserMapper;
import mk.seavus.demoprojcet.repository.UserRepository;
import mk.seavus.demoprojcet.service.impl.UserServiceImpl;
import mk.seavus.model.UserDto;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTests {

	@Mock
	private UserRepository repository;
	
	@Mock
	private UserMapper userMapper;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	void UserService_CreateUser_ReturnUser() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("ab");
		userDto.setFirstName("aaa");
		userDto.setLastName("bbb");
		userDto.email("test@gmail.com");
		userDto.setPassword("123");
		userDto.setPhone("456");
		userDto.setUserStatus(2);
		
		when(repository.save(Mockito.any(User.class))).thenReturn(user);
		
		userService.createUser(userDto);
		Assertions.assertThat(userDto).isNotNull();
	}
	
	@Test
	void UserSerice_GetUsers() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		User user2 = new User(2L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		when(repository.findAll()).thenReturn(users);
		List<UserDto> result = userService.getUsers();
		
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).hasSize(2);
	}
	
	@Test
	void UserService_GetUserById() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(user1));
		
		UserDto userDto = userService.getUserById(1L);
		Assertions.assertThat(userDto).isNotNull();
	}
	
	@Test
	void UserService_GetUserByName() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		
		when(repository.findByUserName("ab")).thenReturn(Optional.ofNullable(user1));
		
		UserDto userDto = userService.getUserByName("ab");
		Assertions.assertThat(userDto).isNotNull();
	}
	
	@Test
	void UserService_UpdateUser() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		UserDto userDto = new UserDto();
		userDto.setFirstName("ccc");
		userDto.setLastName("ddd");
		
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(user1));
		when(repository.save(Mockito.any(User.class))).thenReturn(user1);
		
		userService.updateUser(userDto);
		
		Assertions.assertThat(userDto).isNotNull();
	}
	
	@Test
	void UserServce_DeleteUser() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		when(repository.findById(1L)).thenReturn(Optional.ofNullable(user1));
		assertAll(() -> userService.deleteUser(1L));
	}

	@Test
	void UserService_throwExcetionWhenCreateUserWithNoMessage() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);

		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("ab");
		userDto.setFirstName("aaa");
		userDto.setLastName("bbb");
		userDto.email("test@gmail.com");
		userDto.setPassword("123");
		userDto.setPhone("456");
		userDto.setUserStatus(2);

		assertThrows(UserException.class, () -> userService.createUser(userDto));
	}

	@Test
	void UserService_throwExcetionWhenCreateUserWithMessage() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);

		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("ab");
		userDto.setFirstName("aaa");
		userDto.setLastName("bbb");
		userDto.email("test@gmail.com");
		userDto.setPassword("123");
		userDto.setPhone("456");
		userDto.setUserStatus(2);

		UserException userException = assertThrows(UserException.class, () -> userService.createUser(userDto));
		assertEquals("The user with this username '" + userDto.getUsername() + "' exists in DB", userException.getMessage());
	}

	@Test
	void UserService_throwExceptionWhenDeleteUserWithMock() {
		UserServiceImpl userService1 = mock(UserServiceImpl.class);
		doThrow(new UserException("User does not exists in DB"))
				.when(userService1)
				.deleteUser(anyLong());

		assertThrows(UserException.class, () -> userService1.deleteUser(1L));
	}

	@Test
	void UserService_throwExceptionWhenGetById() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user1);

		assertThrows(UserException.class, () -> userService.getUserById(2L), "Different exception thrown!");
	}

	@Test
	void UserService_throwExcptionWhenGetByUserName() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user1);

		assertThrows(UserException.class, () -> userService.getUserByName("cd"), "Different exception thrown!");
	}

	@Test
	void UserService_throwExcetpionWhenUpdateUserChangeUsername() {
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user1);

		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("ab");
		userDto.setFirstName("aaa");
		userDto.setLastName("bbb");
		userDto.email("test@gmail.com");
		userDto.setPassword("123");
		userDto.setPhone("456");
		userDto.setUserStatus(2);

		assertThrows(UserException.class, () -> userService.updateUser(userDto), "Different exception thrown!");
	}
}
