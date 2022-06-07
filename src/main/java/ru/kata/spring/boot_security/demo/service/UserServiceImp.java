package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void createUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(Collections.singleton(roleRepository.getRoleById(2L)));
        userRepository.saveAndFlush(user);
    }

    @PostConstruct
    public void add() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User("Vladimir ", "Putin ", 69, "admin", "admin");
        Role role1 = roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
        Role role2 = roleRepository.saveAndFlush(new Role("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(Collections.singleton(role1));
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }


    @Override
    @Transactional
    public void update(User user, Set<Role> roleSet) {
        user.setRoleSet(roleSet);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByUsername(username);

    }
}
