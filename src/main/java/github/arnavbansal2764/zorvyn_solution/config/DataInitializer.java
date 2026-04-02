package github.arnavbansal2764.zorvyn_solution.config;

import github.arnavbansal2764.zorvyn_solution.model.Role;
import github.arnavbansal2764.zorvyn_solution.model.User;
import github.arnavbansal2764.zorvyn_solution.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build());

                userRepository.save(User.builder()
                        .username("analyst")
                        .password(passwordEncoder.encode("analyst123"))
                        .role(Role.ANALYST)
                        .build());

                userRepository.save(User.builder()
                        .username("viewer")
                        .password(passwordEncoder.encode("viewer123"))
                        .role(Role.VIEWER)
                        .build());

                System.out.println("Default users seeded successfully.");
            }
        };
    }
}
