package com.mi.iam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<Object> create(@RequestBody Clients client) {
    try {
      Clients cl = clientsService.insert(client);
      return ResponseHandler.generateResponse(HttpStatus.OK, "Create client data success", cl);
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "Create client data failed", e.getMessage());
    }
  }

  @GetMapping({"", "/"})
  public ResponseEntity<Object> getAll(
    @RequestParam String searchTerm,
    @RequestParam(required = false) String sortBy, // Custom sorting parameter
    Pageable pageable
  ) {
    try {
      MyPagination<Clients> cl = clientsService.getAll(searchTerm, new PaginationHelper().PaginationController(sortBy, pageable));
      return ResponseHandler.generateResponse(HttpStatus.OK, "GetAll client data success", cl);
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "GetAll client data failed", e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable("id") String id) {
    try {
      Clients cl = clientsService.getById(id);
      if (cl != null) {
        return ResponseHandler.generateResponse(HttpStatus.OK, "GetById client data success", cl);
      } else {
        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "Not found", "Client id %s is not exist".formatted(id));
      }
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "GetById client data failed", e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Clients client) {
    try {
      client.setId(id);
      Clients cl = clientsService.update(client);
      if (cl != null) {
        return ResponseHandler.generateResponse(HttpStatus.OK, "Update client data success", cl);
      } else {
        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "Not found", "Client id %s is not exist".formatted(id));
      }
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "Update client data failed", e.getMessage());
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Object> disable(@PathVariable("id") String id) {
    try {
      Clients cl = clientsService.disable(id);
      if (cl != null) {
        return ResponseHandler.generateResponse(HttpStatus.OK, "Disable client data success", cl);
      } else {
        return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "Not found", "Client id %s is not exist".formatted(id));
      }
    } catch (Exception e) {
      return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "Disable client data failed", e.getMessage());
    }
  }
}
