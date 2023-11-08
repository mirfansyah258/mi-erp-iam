package com.mi.iam.controllers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.dto.UsersChangePassword;
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

  private Users sampleUser() {
    Users newUser = new Users();

    newUser.setUsername("username");
    newUser.setEmail("email@mail.com");
    newUser.setFirstname("first");
    newUser.setLastname("last");
    newUser.setPassword("password");

    return newUser;
  }

  @Nested
  class InsertUsers {
    @Test
    public void insertUsers_success() throws Exception {
      Users newUser = sampleUser();

      // Mock the service method
      Mockito.when(usersService.insert(newUser)).thenReturn(newUser);

      mockMvc.perform(post("/users")
          .contentType(MediaType.APPLICATION_JSON)
          .content(mapper.writeValueAsString(newUser)))
              .andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.data.username").value(newUser.getUsername()));
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
  }
  
  @Nested
  class GetUsers {
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
      Users user = sampleUser();
      // Set user properties here...
      // Create a UUID
      UUID uuid = UUID.randomUUID();

      // Convert UUID to String
      String uuidStr = uuid.toString();
      user.setId(uuidStr);

      Mockito.when(usersService.getById(uuidStr)).thenReturn(user);

      // Assert: Send a GET request to retrieve the user by ID
      mockMvc.perform(
        get("/users/{id}", uuidStr)
        .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.data.id").value(uuidStr));
    }
  }
  @Nested
  class UpdateUsers {
    @Test
    public void update_success() throws Exception {
      // Arrange: Create a sample user and retrieve its ID
      Users user = sampleUser();
      // Set user properties here...
      // Create a UUID
      UUID uuid = UUID.randomUUID();
  
      // Convert UUID to String
      String uuidStr = uuid.toString();
      user.setId(uuidStr);
  
      Mockito.when(usersService.update(user)).thenReturn(user);
  
      // Assert: Send a PUT request to retrieve the user by ID
      mockMvc.perform(
        put("/users/{id}", uuidStr)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(user)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.data.id").value(uuidStr))
          .andExpect(jsonPath("$.data.username").value("username"));
    }
    
    @Test
    public void changePassword_success() throws Exception {
      // Arrange: Create a sample user and retrieve its ID
      Users user = sampleUser();
      UsersChangePassword pwd = new UsersChangePassword();
      // Set user properties here...
      // Create a UUID
      UUID uuid = UUID.randomUUID();
  
      // Convert UUID to String
      String uuidStr = uuid.toString();
      user.setId(uuidStr);
  
      pwd.setOldPassword("zxc");
      pwd.setNewPassword("123");
  
      Mockito.when(usersService.changePassword(uuidStr, pwd)).thenReturn(user);
  
      // Assert: Send a PUT request to retrieve the user by ID
      MvcResult result = mockMvc.perform(
        put("/users/change-password/{id}", uuidStr)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(pwd)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.data.id").value(uuidStr))
          .andReturn();
  
      // Log the response content and status
      String responseContent = result.getResponse().getContentAsString();
      int responseStatus = result.getResponse().getStatus();
  
      logger.info("Response Status: {}", responseStatus);
      logger.info("Response Content: {}", responseContent);
    }
  }
  
  @Test
  public void disableUser_success() throws Exception {
    // Arrange: Create a sample user and retrieve its ID
    Users user = sampleUser();
    // Set user properties here...
    // Create a UUID
    UUID uuid = UUID.randomUUID();

    // Convert UUID to String
    String uuidStr = uuid.toString();
    user.setId(uuidStr);
    user.setIsActive(0);

    Mockito.when(usersService.disable(uuidStr)).thenReturn(user);

    // Assert: Send a PUT request to retrieve the user by ID
    MvcResult result = mockMvc.perform(
      patch("/users/{id}", uuidStr)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.id").value(uuidStr))
        .andExpect(jsonPath("$.data.isActive").value(0))
        .andReturn();

    // Log the response content and status
    String responseContent = result.getResponse().getContentAsString();
    int responseStatus = result.getResponse().getStatus();

    logger.info("Response Status: {}", responseStatus);
    logger.info("Response Content: {}", responseContent);
  }
}