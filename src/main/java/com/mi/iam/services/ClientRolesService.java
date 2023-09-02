package com.mi.iam.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.ClientRoles;
import com.mi.iam.models.repositories.ClientRolesRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Service
@Transactional
public class ClientRolesService {
  private static final Logger logger = LoggerFactory.getLogger(ClientRolesService.class);
  private final ClientRolesRepository clientRolesRepository;
  private final LocalValidatorFactoryBean validator;

  public ClientRolesService(ClientRolesRepository clientRolesRepository, LocalValidatorFactoryBean validator) {
    this.clientRolesRepository = clientRolesRepository;
    this.validator = validator;
  }

  public ClientRoles insert(ClientRoles role) {
    Set<ConstraintViolation<ClientRoles>> violations = validator.validate(role);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    return clientRolesRepository.save(role);
  }

  public MyPagination<ClientRoles> getAll(String searchTerm, Pageable pageable) {
    Page<ClientRoles> cl = clientRolesRepository.getAllQuery(searchTerm.toLowerCase(), pageable);

    return PaginationHelper.PaginationService(cl);
  }

  public ClientRoles getById(String id) {
    return clientRolesRepository.findById(id).orElse(null);
  }

  public ClientRoles update(ClientRoles role) {
    ClientRoles cr = clientRolesRepository.findById(role.getId()).orElse(null);
    if(cr != null) {
      Set<ConstraintViolation<ClientRoles>> violations = validator.validate(role);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }

      role.setCreatedAt(cr.getCreatedAt());
      return clientRolesRepository.save(role);
    }
    return null;
  }

  public ClientRoles delete(String id) {
    ClientRoles client = clientRolesRepository.findById(id).orElse(null);
    if (client != null) {
      clientRolesRepository.deleteById(id);
      return client;
    }
    return null;
  }
}