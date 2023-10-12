package com.mi.iam.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.ClientRoles;
import com.mi.iam.models.repositories.ClientRolesRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class ClientRolesService {
  private static final Logger logger = LoggerFactory.getLogger(ClientRolesService.class);
  private final ClientRolesRepository clientRolesRepository;

  public ClientRolesService(ClientRolesRepository clientRolesRepository) {
    this.clientRolesRepository = clientRolesRepository;
  }

  public ClientRoles insert(ClientRoles role) {
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