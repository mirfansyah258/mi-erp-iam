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
import com.mi.iam.models.entities.Users;
import com.mi.iam.services.UsersService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UsersController {
  @Autowired
  private UsersService usersService;

  @PostMapping({"", "/"})
  public ResponseEntity<Object> create(@Valid @RequestBody Users user) {
    Users usr = usersService.insert(user);
    return ResponseHandler.generateResponse(HttpStatus.CREATED, "Create user data success", usr);
  }

  @GetMapping({"", "/"})
  public ResponseEntity<Object> getAll(
    @RequestParam String searchTerm,
    @RequestParam(required = false) String sortBy, // Custom sorting parameter
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    MyPagination<Users> usr = usersService.getAll(searchTerm, new PaginationHelper().PaginationController(sortBy, pageable));
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetAll user data success", usr);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable("id") String id) {
    Users usr = usersService.getById(id);
    return ResponseHandler.generateResponse(HttpStatus.OK, "GetById user data success", usr);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Users user) {
    user.setId(id);
    Users usr = usersService.update(user);
    return ResponseHandler.generateResponse(HttpStatus.OK, "Update user data success", usr);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> disable(@PathVariable("id") String id) {
    Users usr = usersService.disable(id);
    return ResponseHandler.generateResponse(HttpStatus.OK, "Disable user data success", usr);
  }
}
