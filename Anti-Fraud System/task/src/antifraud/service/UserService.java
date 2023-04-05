package antifraud.service;

import antifraud.dto.*;
import antifraud.exception.*;
import antifraud.model.User;
import antifraud.model.UserDetailsImpl;
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

        return new UserDetailsImpl(user);
    }

    public UserResponse saveUser(UserRequest userRequest) {
        if (userRequest.getName()
                .isEmpty()
                || userRequest.getUsername()
                .isEmpty()
                || userRequest.getPassword()
                .isEmpty()) {
            throw new InvalidInputUserParameters();
        }

        User byUsername = userRepository.findByUsername(userRequest.getUsername());

        User user = new User();

        if (byUsername == null) {

            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

            int numberOfUsers = userRepository.findAll().size();
            if (numberOfUsers > 0) {
                user.setRole("MERCHANT");
                user.setEnabled(false);
            } else {
                user.setRole("ADMINISTRATOR");
                user.setEnabled(true);
            }

            userRepository.save(user);
        } else throw new InvalidRegisterAnExistingUser();

        return new UserResponse(user);
    }

    public List<UserResponse> findAllUsers() {
        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public DeleteUserResponse deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new InvalidUser();
        }

        userRepository.delete(user);

        return new DeleteUserResponse(username);
    }

    public UserResponse changeUserRole(EditUserRoleRequest editUserRoleRequest) {
        User user = userRepository.findByUsername(editUserRoleRequest.getUsername());

        if (user == null) {
            throw new InvalidUser();
        }

        String role = editUserRoleRequest.getRole();

        if (!"SUPPORT".equalsIgnoreCase(role) &&
                !"MERCHANT".equalsIgnoreCase(role)) {
            throw new InvalidAllowedRoleException();
        }

        if (user.getRole()
                .equals(role)) {
            throw new AlreadyAssignedRoleException();
        }

        user.setRole(role);
        userRepository.save(user);

        return new UserResponse(user);
    }


    public StatusResponse lockUnlockUser(UnlockUserRequest unlockUserRequest) {
        User user = userRepository.findByUsername(unlockUserRequest.getUsername());

        if (user == null) {
            throw new InvalidUser();
        }

        if (user.getRole().equals("ADMINISTRATOR")) {
            throw new InvalidTryToLockAdministrator();
        }

        boolean enabled = unlockUserRequest.getOperation().equals("UNLOCK");
        user.setEnabled(enabled);
        userRepository.save(user);
        String status = String.format("User %s %sed!", user.getUsername(), unlockUserRequest.getOperation()
                .toLowerCase());
        return new StatusResponse(status);
    }

}
