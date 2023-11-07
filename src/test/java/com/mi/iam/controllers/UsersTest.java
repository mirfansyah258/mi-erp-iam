package com.mi.iam.controllers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Users;
import com.mi.iam.models.repositories.UsersRepository;
import com.mi.iam.services.UsersService;

@SpringBootTest
// @WebMvcTest(UsersController.class)
@AutoConfigureMockMvc
public class UsersTest {
  private static final Logger logger = LoggerFactory.getLogger(UsersTest.class);
  
  @Autowired
	private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private UsersService usersService;

  @MockBean
  private UsersRepository usersRepository;

  @InjectMocks
  private UsersController usersController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
  }

  @Test
  public void insertUsers_success() throws Exception {
    Users newUser = new Users();
    String username = "username";
    newUser.setUsername(username);
    newUser.setEmail("email@mail.com");
    newUser.setFirstname("first");
    newUser.setLastname("last");
    newUser.setPassword("password");

    // Mock the service method
    Mockito.when(usersService.insert(newUser)).thenReturn(newUser);

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(newUser)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.username").value(username));
  }

  @Test
  public void testInsertUserWithBlankUsername() throws Exception {
    // Create a Users object with a blank username
    Users user = new Users();
    user.setUsername(""); // Blank username violates @NotBlank constraint

    // Perform a POST request to the insert endpoint
    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest()); // Expect HTTP 400 Bad Request
  }
  
  @Test
  public void getAll_success() throws Exception {
    // Create a list of Users for testing
    List<Users> usersList = new ArrayList<>();
    usersList.add(new Users());
    usersList.add(new Users());

    // Mock the service method
    Mockito.when(usersService.getAll(Mockito.anyString(), Mockito.any())).thenReturn(new MyPagination<>(usersList, usersList.size(), 1, 1, usersList.size()));

    // Act & Assert
    mockMvc.perform(
      get("/users")
      .param("searchTerm", "")
      .param("page", "1") // Specify the page and size parameters
      .param("size", "10")
      .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.data").isArray())
        .andExpect(jsonPath("$.data.totalData").value(2))
        .andExpect(jsonPath("$.data.page").value(1))
        .andExpect(jsonPath("$.data.totalPage").value(1))
        .andExpect(jsonPath("$.data.size").value(2));
  }
  
  @Test
  public void getById_success() throws Exception {
    // Arrange: Create a sample user and retrieve its ID
    // Users user = new Users(null, "test-1", "test-2", null, 1, null, LocalDateTime.now(), LocalDateTime.now(), null);
    Users user = new Users();
    // Set user properties here...
    // Create a UUID
    UUID uuid = UUID.randomUUID();

    // Convert UUID to String
    String uuidStr = uuid.toString();
    user.setId(uuidStr);
    user.setUsername("username");
    user.setEmail("email@mail.com");
    user.setFirstname("first");
    user.setLastname("last");

    Mockito.when(usersService.insert(user)).thenReturn(user);

    // Act: Insert the user using the service
    // Users savedUser = usersService.insert(user);

    // Assertions.assertEquals(savedUser.getUsername(), user.getUsername());

    // Mock the service method
    // Mockito.when(usersService.getById(user.getId())).thenReturn(user).thenThrow(new RuntimeException());

    // Assert: Send a GET request to retrieve the user by ID
    mockMvc.perform(
      get("/users/{id}", uuidStr)
      .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}