package com.bankingapp.config;

import com.bankingapp.entity.Role;
import com.bankingapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        seedRole("ROLE_CUSTOMER");
        seedRole("ROLE_EMPLOYEE");
        seedRole("ROLE_ADMIN");
    }

    private void seedRole(String roleName) {
        boolean exists = roleRepository.findByName(roleName).isPresent();
        if (!exists) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
            System.out.println("Seeded role: " + roleName);
        }
    }
}