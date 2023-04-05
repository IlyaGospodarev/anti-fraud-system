package antifraud.controller;

import antifraud.dto.*;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponse> registerNewUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(userRequest));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<DeleteUserResponse> deleteUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUserByUsername(username));
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponse> changeUserRole(@RequestBody @Valid EditUserRoleRequest editUserRoleRequest) {
        return ResponseEntity.ok(userService.changeUserRole(editUserRoleRequest));
    }

    @PutMapping("/access")
    public ResponseEntity<StatusResponse> lockUnlockUser(@RequestBody @Valid UnlockUserRequest unlockUserRequest) {
        return ResponseEntity.ok(userService.lockUnlockUser(unlockUserRequest));
    }
}
