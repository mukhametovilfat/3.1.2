package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.List;

public interface RoleService {

    HashSet<Role> findAll(String[] roles);

    Role getRoleByName(String name);

    List<Role> getAllRoles();

}
