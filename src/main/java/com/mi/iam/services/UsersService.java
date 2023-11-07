package com.mi.iam.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mi.iam.exception.ResourceNotFoundException;
import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.dto.UsersChangePassword;
import com.mi.iam.models.entities.Users;
import com.mi.iam.models.repositories.UsersRepository;

import javax.transaction.Transactional;
@Service
@Transactional
public class UsersService {
  private static final Logger logger = LoggerFactory.getLogger(UsersService.class);
  
  @Autowired
  private UsersRepository usersRepository;
  
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public Users insert(Users users) {
    logger.info("Password Encoded: " + bCryptPasswordEncoder.encode(users.getPassword()));
    users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
    return usersRepository.save(users);
  }

  public MyPagination<Users> getAll(String searchTerm, Pageable pageable) {
    Page<Users> cl = usersRepository.getAllQuery(searchTerm.toLowerCase(), pageable);

    return PaginationHelper.PaginationService(cl);
  }

  public Users getById(String id) {
    Users user = usersRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (user != null) {
      return user;
    }
    throw new ResourceNotFoundException("User with ID " + id + " not found.");
  }

  public Users update(Users users) {
    Users user = getById(users.getId());
    
    users.setPassword(user.getPassword());
    users.setCreatedAt(user.getCreatedAt());
    return usersRepository.save(users);
  }

  public Users changePassword(String id, UsersChangePassword pwd) throws Exception {
    Users user = getById(id);

    if (bCryptPasswordEncoder.matches(pwd.getOldPassword(), user.getPassword())) {
      user.setPassword(bCryptPasswordEncoder.encode(pwd.getNewPassword()));
      return usersRepository.save(user);
    } else {
      throw new Exception("Invalid old password");
    }
  }

  public Users emailVerify(String id) {
    Users user = getById(id);

    usersRepository.verifyEmailUser(id);
    user.setIsEmailVerified(1);
    return user;
  }

  public Users disable(String id) {
    Users user = getById(id);

    usersRepository.disableUser(id);
    user.setIsActive(0);
    return user;
  }

  public Users delete(String id) {
    Users user = getById(id);

    usersRepository.deleteById(id);
    return user;
  }
}