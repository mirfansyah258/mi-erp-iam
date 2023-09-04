package com.mi.iam.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mi.iam.exception.ResourceNotFoundException;
import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Clients;
import com.mi.iam.models.repositories.ClientsRepository;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Service
@Transactional
public class ClientsService {
  private static final Logger logger = LoggerFactory.getLogger(ClientsService.class);
  private final ClientsRepository clientsRepository;
  private final LocalValidatorFactoryBean validator;

  public ClientsService(ClientsRepository clientsRepository, LocalValidatorFactoryBean validator) {
    this.clientsRepository = clientsRepository;
    this.validator = validator;
  }

  public Clients insert(Clients clients) {
    Set<ConstraintViolation<Clients>> violations = validator.validate(clients);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    return clientsRepository.save(clients);
  }

  public MyPagination<Clients> getAll(String searchTerm, Pageable pageable) {
    Page<Clients> cl = clientsRepository.getAllQuery(searchTerm.toLowerCase(), pageable);

    return PaginationHelper.PaginationService(cl);
  }

  public Clients getById(String id) {
    Clients client = clientsRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (client != null) {
      return client;
    }
    throw new ResourceNotFoundException("Client with ID " + id + " not found.");
  }

  public Clients update(Clients clients) {
    Clients client = clientsRepository.findByIdAndIsActive(clients.getId(), 1).orElse(null);
    if(client != null) {
      Set<ConstraintViolation<Clients>> violations = validator.validate(clients);
      if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }

      clients.setCreatedAt(client.getCreatedAt());
      return clientsRepository.save(clients);
    }
    throw new ResourceNotFoundException("Client with ID " + clients.getId() + " not found.");
  }

  public Clients disable(String id) {
    Clients client = clientsRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (client != null) {
      clientsRepository.disableClient(id);
      client.setIsActive(0);
      return client;
    }
    throw new ResourceNotFoundException("Client with ID " + id + " not found.");
  }

  public Clients delete(String id) {
    Clients client = clientsRepository.findByIdAndIsActive(id, 1).orElse(null);
    if (client != null) {
      clientsRepository.deleteById(id);
      return client;
    }
    throw new ResourceNotFoundException("Client with ID " + id + " not found.");
  }
}