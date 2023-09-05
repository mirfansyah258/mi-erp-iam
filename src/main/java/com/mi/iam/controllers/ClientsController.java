package com.mi.iam.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mi.iam.helpers.PaginationHelper;
import com.mi.iam.helpers.ResponseHandler;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Clients;
import com.mi.iam.services.ClientsService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/clients")
public class ClientsController {
  @Autowired
  private ClientsService clientsService;

  @PostMapping({"", "/"})
  public ResponseEntity<Object> create(@Valid @RequestBody Clients client) {
    Clients cl = clientsService.insert(client);
    return ResponseHandler.generateResponse(HttpStatus.CREATED, "Create client data success", cl);
  }

  @GetMapping({"", "/"})
  public ResponseEntity<Object> getAll(
    @RequestParam String searchTerm,
    @RequestParam(required = false) String sortBy, // Custom sorting parameter
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    MyPagination<Clients> cl = clientsService.getAll(searchTerm, new PaginationHelper().PaginationController(sortBy, pageable));
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetAll client data success", cl);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable("id") String id) {
    Clients cl = clientsService.getById(id);
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetById client data success", cl);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Clients client) {
    client.setId(id);
    Clients cl = clientsService.update(client);
    return ResponseHandler.generateResponse(HttpStatus.OK, "Update client data success", cl);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> disable(@PathVariable("id") String id) {
    Clients cl = clientsService.disable(id);
    return ResponseHandler.generateResponse(HttpStatus.OK, "Disable client data success", cl);
  }
}
