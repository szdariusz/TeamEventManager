package com.DariusApp.TeamEventManager.Service;

import com.DariusApp.TeamEventManager.Entity.Role;
import com.DariusApp.TeamEventManager.Entity.TEMUser;
import com.DariusApp.TeamEventManager.Entity.UserRoleTypes;
import com.DariusApp.TeamEventManager.Payload.Request.SignupRequest;
import com.DariusApp.TeamEventManager.Payload.Response.MessageResponse;
import com.DariusApp.TeamEventManager.Repository.RoleRepository;
import com.DariusApp.TeamEventManager.Repository.TEMUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.DariusApp.TeamEventManager.Payload.Response.ResponseMessages.EMAIL_TAKEN;
import static com.DariusApp.TeamEventManager.Payload.Response.ResponseMessages.USERNAME_TAKEN;

@Service
public class AuthService {
    @Autowired
    TEMUserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<MessageResponse> createUser(SignupRequest signUpRequest) {

        if (isEmailTaken(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(EMAIL_TAKEN));
        } else if (isUsernameTaken(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(USERNAME_TAKEN));
        }

        TEMUser user = new TEMUser(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = convertRoles(signUpRequest.getRole());

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private Set<Role> convertRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(getUserRole(UserRoleTypes.ROLE_USER));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "mod":
                        roles.add(getUserRole(UserRoleTypes.ROLE_MODERATOR));
                        break;
                    case "admin":
                        roles.add(getUserRole(UserRoleTypes.ROLE_ADMIN));
                        break;
                    default:
                        roles.add(getUserRole(UserRoleTypes.ROLE_USER));
                }
            });
        }
        return roles;
    }

    private Role getUserRole(UserRoleTypes roleUser) {
        return roleRepository.findByName(roleUser)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    private boolean isUsernameTaken(String username) {
        return !userRepository.existsByUsername(username);
    }

    private boolean isEmailTaken(String email) {
        return !userRepository.existsByEmailAddress(email);
    }
}
