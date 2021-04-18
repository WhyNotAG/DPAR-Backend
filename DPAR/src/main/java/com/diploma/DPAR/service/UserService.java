package com.diploma.DPAR.service;

import com.diploma.DPAR.domain.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User findByUsername(String username);
    User findById(Long id);
    User save(User user);
    void delete(Long id);
    User update(Long id, User user);
}
