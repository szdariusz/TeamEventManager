package com.darius.teamEventManager.service;

import com.darius.teamEventManager.entity.Role;
import com.darius.teamEventManager.entity.UserRoleTypes;
import com.darius.teamEventManager.payload.request.auth.SignupRequest;
import com.darius.teamEventManager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class InitializationService {
    private final RoleRepository roleRepository;

    //@Qualifier({name_of_implementation})
    private final AuthService authService;
    
    public void Initialize() {
        List<Role> roles = Arrays.asList(
                new Role(UserRoleTypes.ROLE_USER),
                new Role(UserRoleTypes.ROLE_MODERATOR),
                new Role(UserRoleTypes.ROLE_ADMIN)
        );

        for (Role role : roles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
            }
        }

        authService.createUser(
                SignupRequest.builder()
                        .email("admin@admin.com")
                        .username("admin")
                        .password("admin")
                        .role((Set<String>) Arrays.asList("admin"))
                        .build());

    }
}
