package antifraud.service;

import antifraud.dto.DeleteUser;
import antifraud.dto.UserDto;
import antifraud.exception.InvalidInputUserParameters;
import antifraud.exception.InvalidRegisterAnExistingUser;
import antifraud.exception.InvalidUser;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserDto saveUser(User user) {
        if (user.getName().isEmpty()
                || user.getUsername().isEmpty()
                || user.getPassword().isEmpty()) {
            throw new InvalidInputUserParameters();
        }

        if(userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userRepository.save(user);
        } else throw new InvalidRegisterAnExistingUser();

        return new UserDto(user);
    }

    public List<UserDto> findAllUsers() {
       List<User> userList = userRepository.findAll();

       return userList.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public DeleteUser deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw new InvalidUser();
        }

        userRepository.delete(user);

        return new DeleteUser(username);
    }

}
