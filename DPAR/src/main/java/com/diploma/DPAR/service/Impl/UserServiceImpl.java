package com.diploma.DPAR.service.Impl;

import com.diploma.DPAR.domain.User;
import com.diploma.DPAR.repo.UserRepo;
import com.diploma.DPAR.service.UserService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.beancontext.BeanContext;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User update(Long id, User user) {
        User userFromDb = userRepo.findById(id).orElse(null);
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userRepo.save(userFromDb);
    }
}
