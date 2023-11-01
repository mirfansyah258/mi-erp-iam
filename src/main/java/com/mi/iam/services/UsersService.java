package com.mi.iam.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mi.iam.exception.ResourceNotFoundException;
import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Users;
import com.mi.iam.models.repositories.UsersRepository;

import javax.transaction.Transactional;
@Service
@Transactional
public class UsersService {
  private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

  @Autowired
  private UsersRepository usersRepository;

  public Users insert(Users users) {
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
    Users user = usersRepository.findByIdAndIsActive(users.getId(), 1).orElse(null);
    if(user != null) {

      users.setCreatedAt(user.getCreatedAt());
      return usersRepository.save(users);
    }
    throw new ResourceNotFoundException("User with ID " + users.getId() + " not found.");
  }

  public Users emailVerify(String id) {
    Users user = usersRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (user != null) {
      usersRepository.verifyEmailUser(id);
      user.setIsEmailVerified(1);
      return user;
    }
    throw new ResourceNotFoundException("User with ID " + id + " not found.");
  }

  public Users disable(String id) {
    Users user = usersRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (user != null) {
      usersRepository.disableUser(id);
      user.setIsActive(0);
      return user;
    }
    throw new ResourceNotFoundException("User with ID " + id + " not found.");
  }

  public Users delete(String id) {
    Users user = usersRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (user != null) {
      usersRepository.deleteById(id);
      return user;
    }
    throw new ResourceNotFoundException("User with ID " + id + " not found.");
  }
}