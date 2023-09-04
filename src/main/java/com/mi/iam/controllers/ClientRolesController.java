package com.mi.iam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.mi.iam.models.entities.ClientRoles;
import com.mi.iam.services.ClientRolesService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/client-roles")
public class ClientRolesController {
  @Autowired
  private ClientRolesService clientRolesService;

  @PostMapping({"", "/"})
  public ResponseEntity<Object> create(@RequestBody ClientRoles role) {
    ClientRoles cl = clientRolesService.insert(role);
    return ResponseHandler.generateResponse(HttpStatus.CREATED, "Create client role data success", cl);
  }

  @GetMapping({"", "/"})
  public ResponseEntity<Object> getAll(
    @RequestParam String searchTerm,
    @RequestParam(required = false) String sortBy, // Custom sorting parameter
    Pageable pageable
  ) {
    MyPagination<ClientRoles> cl = clientRolesService.getAll(searchTerm, new PaginationHelper().PaginationController(sortBy, pageable));
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetAll client role data success", cl);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable("id") String id) {
    ClientRoles cl = clientRolesService.getById(id);
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetById client role data success", cl);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody ClientRoles client) {
    client.setId(id);
    ClientRoles cl = clientRolesService.update(client);
    return ResponseHandler.generateResponse(HttpStatus.OK, "Update client role data success", cl);
  }
}
