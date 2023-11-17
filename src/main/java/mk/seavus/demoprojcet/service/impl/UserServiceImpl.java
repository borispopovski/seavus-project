package mk.seavus.demoprojcet.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mk.seavus.demoprojcet.entity.User;
import mk.seavus.demoprojcet.exception.UserErrorMessages;
import mk.seavus.demoprojcet.exception.UserException;
import mk.seavus.demoprojcet.kafka.KafkaPublisher;
import mk.seavus.demoprojcet.mapper.UserMapper;
import mk.seavus.demoprojcet.mapper.UserResponseMapper;
import mk.seavus.demoprojcet.repository.UserRepository;
import mk.seavus.demoprojcet.service.IUserService;
import mk.seavus.model.UserDto;
import mk.seavus.model.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private UserRepository repository;
    private UserMapper userMapper;
    private UserResponseMapper userResponseMapper;
    private KafkaPublisher kafkaPublisher;

    public UserServiceImpl(UserRepository repository, UserMapper userMapper, UserResponseMapper userResponseMapper, KafkaPublisher kafkaPublisher) {
        this.userMapper = userMapper;
        this.repository = repository;
        this.userResponseMapper = userResponseMapper;
        this.kafkaPublisher = kafkaPublisher;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    DefaultPrettyPrinter printer = new DefaultPrettyPrinter();

    @Override
    public UserResponseDto createUser(UserDto userDto) {
        UserResponseDto userResponseDto;
        try {
            if (repository.findByUserName(userDto.getUsername()).isPresent())
                throw new UserException(UserErrorMessages.USER_USERNAME.getMessage(), userDto.getUsername(), UserErrorMessages.EXISTS.getMessage());
            User user = repository.save(userMapper.convertToUser(userDto));
            userResponseDto = userResponseMapper.convertToUserResponseDto(user);
            log.info("User is stored");
            kafkaPublisher.sendMessage(convertObjToString(userResponseDto));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
        return userResponseDto;
    }

    @Override
    public void deleteUser(Long id) {
        try {
            repository.deleteById(id);
            log.info("User is deleted!");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        try {
            List<User> users = repository.findAll();
            users.stream().forEach(u -> {
                UserDto userDto = userMapper.convertToUserDto(u);
                usersDto.add(userDto);
            });
            log.info("All users are read");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
        return usersDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto;
        try {
            User user = repository.findById(id)
                    .orElseThrow(() -> new UserException(UserErrorMessages.USER_ID.getMessage(), id.toString(),
                            UserErrorMessages.DOES_NOT_EXISTS.getMessage()));
            userDto = userMapper.convertToUserDto(user);
            log.info("User is read");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto getUserByName(String userName) {
        UserDto userDto;
        try {
            User user = repository.findByUserName(userName)
                    .orElseThrow(() -> new UserException(UserErrorMessages.USER_USERNAME.getMessage(), userName,
                            UserErrorMessages.DOES_NOT_EXISTS.getMessage()));
            userDto = userMapper.convertToUserDto(user);
            log.info("User is read");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserResponseDto updateUser(UserDto userDto) {
        UserResponseDto userResponseDto;
        try {
            User user = repository.findById(userDto.getId())
                    .orElseThrow(() -> new UserException(UserErrorMessages.USER_ID.getMessage(), userDto.getId().toString(),
                            UserErrorMessages.DOES_NOT_EXISTS.getMessage()));
            if (userDto.getUsername() != null)
                throw new UserException(UserErrorMessages.USERNAME_CANT_CHANGE.getMessage());
            userMapper.updateUserFromDto(userDto, user);
            userResponseDto = userResponseMapper.convertToUserResponseDto(repository.save(user));
            log.info("User is updated");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException(e.getMessage());
        }
        return userResponseDto;
    }

    private String convertObjToString(UserResponseDto userResponseDtoDto) {
        String result = "";
        try {
            result = objectMapper.writer(printer).writeValueAsString(userResponseDtoDto);
        } catch (JsonProcessingException e) {
            log.error("An exception occured: {}", e.getMessage());
        }
        return result;
    }

}
