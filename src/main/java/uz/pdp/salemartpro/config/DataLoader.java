package uz.pdp.salemartpro.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.salemartpro.entity.Role;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.repo.RoleRepository;
import uz.pdp.salemartpro.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataLoader(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = roleRepository.findAll();
        System.out.println(roles.size());
        User user = new User();
        user.setUsername("1");
        user.setPassword(passwordEncoder.encode("1"));
        user.setRoles(new ArrayList<>(List.of(roles.get(0))));

        User user2 = new User();
        user2.setUsername("2");
        user2.setPassword(passwordEncoder.encode("2"));
        user2.setRoles(new ArrayList<>(List.of(roles.get(1))));

        userRepository.save(user);
        userRepository.save(user2);

    }
}
