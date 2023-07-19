package mk.seavus.demoprojcet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mk.seavus.demoprojcet.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUserName(String userName);

}
