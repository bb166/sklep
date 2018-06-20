package pl.polsl.aei.sklep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.polsl.aei.sklep.dto.UserDTO;
import pl.polsl.aei.sklep.exception.UserAlreadyRegister;
import pl.polsl.aei.sklep.exception.UserNotExist;
import pl.polsl.aei.sklep.repository.entity.User;
import pl.polsl.aei.sklep.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public ModelAndView showLoginPage() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("endpoint", "/login");
        modelAndView.addObject("label", "Logowanie");
        modelAndView.addObject("labelButton","Zaloguj");
        modelAndView.setViewName("security");

        return modelAndView;
    }

    @RequestMapping("/register")
    public ModelAndView showRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("endpoint", "/register");
        modelAndView.addObject("label", "Rejestracja");
        modelAndView.addObject("labelButton","Rejestruj");
        modelAndView.setViewName("security");

        return modelAndView;
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ModelAndView receiveRegisterRequest(UserDTO userDTO) throws UserAlreadyRegister{
        ModelAndView modelAndView = new ModelAndView();

        userService.registerUser(userDTO);
        modelAndView.addObject("success", "");
        modelAndView.addObject("endpoint", "/register");
        modelAndView.addObject("label", "Rejestracja");
        modelAndView.addObject("labelButton","Rejestruj");
        modelAndView.setViewName("security");

        return modelAndView;
    }

    @ExceptionHandler(UserAlreadyRegister.class)
    public ModelAndView handleUserAlreadyRegisterException(UserAlreadyRegister ex) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("endpoint", "/register");
        modelAndView.addObject("label", "Rejestracja");
        modelAndView.addObject("userAlreadyRegister", "");
        modelAndView.addObject("labelButton","Rejestruj");
        modelAndView.setViewName("security");

        return modelAndView;
    }

    @RequestMapping("/deleteMe")
    public String deleteUser(Principal principal) throws UserNotExist {

        userService.deleteUser(principal.getName());

        return "redirect:/logout";
    }

    @RequestMapping("/profileDetails")
    public ModelAndView showProfileDetails(Principal principal) throws UserNotExist{
        ModelAndView modelAndView = new ModelAndView();

        UserDTO userDTO = userService.getUserByName(principal.getName());

        modelAndView.setViewName("profileDetails");
        modelAndView.addObject("user", userDTO);

        return modelAndView;
    }

    @RequestMapping("/showAllUsers")
    public ModelAndView showAllUsers() {
        ModelAndView modelAndView = new ModelAndView();

        List<String> usernames = userService.getAllUsernamesInDatabase();
        modelAndView.addObject("usernames", usernames);
        modelAndView.setViewName("userList");

        return modelAndView;
    }

    @RequestMapping("/deleteUser/{username}")
    public String deleteUser(@PathVariable String username) throws UserNotExist{
        userService.deleteUser(username);

        return "redirect:/showAllUsers";
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public ModelAndView editUser(Principal principal, UserDTO editUserDTO) throws UserNotExist{
        ModelAndView modelAndView = new ModelAndView();

        try {
            userService.editUser(principal.getName(), editUserDTO);
            modelAndView.setViewName("redirect:/logout");

        } catch (UserAlreadyRegister ex) {
            modelAndView.setViewName("profileDetails");
            UserDTO user = userService.getUserByName(principal.getName());
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAlreadyRegister", "");
        }

        return modelAndView;
    }
}