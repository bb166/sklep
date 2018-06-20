package pl.polsl.aei.sklep.service;

import pl.polsl.aei.sklep.dto.UserDTO;
import pl.polsl.aei.sklep.exception.UserAlreadyRegister;
import pl.polsl.aei.sklep.exception.UserNotExist;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserService {
   void registerUser(UserDTO userDTO) throws UserAlreadyRegister;
   void deleteUser(String username) throws UserNotExist;
   UserDTO getUserByName(String username) throws UserNotExist;
   List<String> getAllUsernamesInDatabase();
   void editUser(String oldUsername, UserDTO editUserDTO) throws UserAlreadyRegister, UserNotExist;
}
