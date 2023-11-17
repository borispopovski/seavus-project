package mk.seavus.demoprojcet.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import mk.seavus.demoprojcet.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTests {

	@Autowired
	private UserRepository repository;

	@Test
	void UserRepo_Save() {

		// Arange
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);

		// Act
		User saveUser = repository.save(user);

		// Assert
		Assertions.assertThat(saveUser).isNotNull();
		Assertions.assertThat(saveUser.getId()).isGreaterThan(1L);
	}

	@Test
	void UserRepo_GetById() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);
		
		User u = repository.findById(user.getId()).get();
		
		Assertions.assertThat(u).isNotNull();
	}
	
	@Test
	void UserRepo_GetByName() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);
		
		User u = repository.findById(user.getId()).get();
		
		Assertions.assertThat(u).isNotNull();
		Assertions.assertThat(u .getUserName()).isEqualTo("ab");
	}

	@Test
	void UserRepo_GetAll() {
		// Arange
		User user1 = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		User user2 = new User(2L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		
		repository.save(user1);
		repository.save(user2);
		
		List<User> users = repository.findAll();
		
		Assertions.assertThat(users).isNotNull();
		Assertions.assertThat(users.size()).isEqualTo(2);
	}
	
	@Test
	void UserRepo_UpdateUser() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);
		
		User u = repository.findById(user.getId()).get();
		u.setEmail("nov@yahoo.com");
		u.setPhone("235345");
		
		User updateUser = repository.save(u);
		
		Assertions.assertThat(updateUser.getUserName()).isNotNull();
	}
	
	@Test
	void UserRepo_DeleteUser() {
		User user = new User(1L, "ab", "aaa", "bbb", "test@gmail.com", "123", "456", 2);
		repository.save(user);
		
		repository.deleteById(user.getId());
		User u = repository.findById(user.getId()).get();
		
		Assertions.assertThat(u).isNull();
	}
}
