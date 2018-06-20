package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.dto.UserDTO;
import pl.polsl.aei.sklep.exception.UserAlreadyRegister;
import pl.polsl.aei.sklep.exception.UserNotExist;
import pl.polsl.aei.sklep.repository.UserRepository;
import pl.polsl.aei.sklep.repository.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDTO userDTO) throws UserAlreadyRegister {

        User userFromDatabase = userRepository.findUserByUsername(userDTO.getUsername());

        if (userFromDatabase == null) {

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole("ROLE_USER");

            userRepository.save(user);

        } else throw new UserAlreadyRegister();
    }

    @Override
    public void deleteUser(String username) throws UserNotExist {
        User user = userRepository.findUserByUsername(username);

        if (user != null) {
            userRepository.delete(user);
        } else throw new UserNotExist();
    }

    @Override
    public UserDTO getUserByName(String username) throws UserNotExist {
        User user = userRepository.findUserByUsername(username);
        UserDTO userDTO = new UserDTO();

        if (user != null) {
            userDTO.setUsername(user.getUsername());
        } else throw new UserNotExist();

        return userDTO;
    }

    @Override
    public List<String> getAllUsernamesInDatabase() {
        return StreamSupport
                .stream(userRepository.findAll().spliterator(),false)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    @Override
    public void editUser(String oldUsername, UserDTO editUserDTO) throws UserAlreadyRegister, UserNotExist {
        User user = userRepository.findUserByUsername(oldUsername);
        User userDuplicate = userRepository.findUserByUsername(editUserDTO.getUsername());

        if (userDuplicate != null && oldUsername.equals(editUserDTO.getUsername()))
            throw new UserAlreadyRegister();

        if (user == null)
            throw new UserNotExist();

        if (!oldUsername.equals(editUserDTO.getUsername()))
            user.setUsername(editUserDTO.getUsername());

        if (!editUserDTO.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));

        userRepository.save(user);
    }
}
