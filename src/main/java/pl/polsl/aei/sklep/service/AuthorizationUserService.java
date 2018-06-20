package pl.polsl.aei.sklep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.aei.sklep.repository.UserRepository;
import pl.polsl.aei.sklep.repository.entity.User;

import java.util.Arrays;

@Service
public class AuthorizationUserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public AuthorizationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);

        if (user != null)
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(user.getRole())));

        throw new UsernameNotFoundException(String.format("Nie znaleziono użytkownika %s", s));
    }
}
