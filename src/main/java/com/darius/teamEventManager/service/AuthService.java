package com.darius.teamEventManager.service;

import com.darius.teamEventManager.entity.Role;
import com.darius.teamEventManager.entity.TEMUser;
import com.darius.teamEventManager.entity.UserRoleTypes;
import com.darius.teamEventManager.payload.request.auth.SignupRequest;
import com.darius.teamEventManager.payload.response.MessageResponse;
import com.darius.teamEventManager.repository.RoleRepository;
import com.darius.teamEventManager.repository.TEMUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.darius.teamEventManager.payload.response.ResponseMessages.EMAIL_TAKEN;
import static com.darius.teamEventManager.payload.response.ResponseMessages.USERNAME_TAKEN;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final TEMUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

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
        return userRepository.existsByUsername(username);
    }

    private boolean isEmailTaken(String email) {
        return userRepository.existsByEmailAddress(email);
    }
}
