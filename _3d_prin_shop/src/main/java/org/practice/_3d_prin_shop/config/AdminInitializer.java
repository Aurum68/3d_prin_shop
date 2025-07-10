package org.practice._3d_prin_shop.config;

import org.practice._3d_prin_shop.model.User;
import org.practice._3d_prin_shop.repository.UserRepository;
import org.practice._3d_prin_shop.util.Roles;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setBlocked(false);
            admin.setRole(Roles.ROLE_ADMIN.getRole());
            userRepository.save(admin);
        }
    }
}
