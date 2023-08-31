package com.mi.iam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Clients;
import com.mi.iam.models.repositories.ClientsRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClientsService {
  @Autowired
  private ClientsRepo clientsRepo;

  public Clients insert(Clients clients) throws Exception {
    return clientsRepo.save(clients);
  }

  public MyPagination<Clients> getAll(String searchTerm, Pageable pageable) {
    Page<Clients> cl = clientsRepo.getAllQuery(searchTerm.toLowerCase(), pageable);

    return PaginationHelper.PaginationService(cl);
  }

  public Clients getById(String id) {
    return clientsRepo.findByIdAndIsActive(id, 1).orElse(null);
  }

  public Clients update(Clients clients) throws Exception {
    Clients client = clientsRepo.findByIdAndIsActive(clients.getId(), 1).orElse(null);
    if(client != null) {
      clients.setCreatedAt(client.getCreatedAt());
      return clientsRepo.save(clients);
    }
    return null;
  }

  public Clients disable(String id) {
    Clients client = clientsRepo.findByIdAndIsActive(id, 1).orElse(null);
    if (client != null) {
      clientsRepo.disableClient(id);
      client.setIsActive(0);
      return client;
    }
    return null;
  }

  public Clients delete(String id) {
    Clients client = clientsRepo.findByIdAndIsActive(id, 1).orElse(null);
    if (client != null) {
      clientsRepo.deleteById(id);
      return client;
    }
    return null;
  }
}