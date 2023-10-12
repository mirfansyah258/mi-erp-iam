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
import com.mi.iam.models.entities.ClientRoles;
import com.mi.iam.models.repositories.ClientRolesRepository;
import com.mi.iam.services.ClientRolesService;

@SpringBootTest
// @WebMvcTest(ClientsController.class)
@AutoConfigureMockMvc
public class ClientRolesTest {
  private static final Logger logger = LoggerFactory.getLogger(ClientRolesTest.class);
  
  @Autowired
	private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private ClientRolesService clientRolesService;

  @MockBean
  private ClientRolesRepository clientRolesRepository;

  @InjectMocks
  private ClientRolesController clientRolesController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(clientRolesController).build();
  }

  @Test
  public void insertClientRoles_success() throws Exception {
    ClientRoles newClientRoles = new ClientRoles();
    newClientRoles.setClientId("test-1");
    newClientRoles.setName("test-2");

    logger.info("ClientId" + newClientRoles.getClientId());

    // Mock the service method
    Mockito.when(clientRolesService.insert(newClientRoles)).thenReturn(newClientRoles);

    mockMvc.perform(post("/client-roles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(newClientRoles)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.clientId").value("test-1"));
  }

  @Test
  public void testInsertClientRolesWithBlankClientId() throws Exception {
    // Create a ClientRoles object with a blank clientId
    ClientRoles clientRoles = new ClientRoles();
    clientRoles.setClientId(""); // Blank clientId violates @NotBlank constraint

    // Perform a POST request to the insert endpoint
    mockMvc.perform(post("/client-roles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(clientRoles)))
        .andExpect(status().isBadRequest()); // Expect HTTP 400 Bad Request
  }
  
  @Test
  public void getAll_success() throws Exception {
    // Create a list of ClientRoles for testing
    List<ClientRoles> clientRolesList = new ArrayList<>();
    clientRolesList.add(new ClientRoles());
    clientRolesList.add(new ClientRoles());

    // Mock the service method
    Mockito.when(clientRolesService.getAll(Mockito.anyString(), Mockito.any())).thenReturn(new MyPagination<>(clientRolesList, clientRolesList.size(), 1, 1, clientRolesList.size()));

    // Act & Assert
    mockMvc.perform(
      get("/client-roles")
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
    // Arrange: Create a sample client and retrieve its ID
    // ClientRoles client = new ClientRoles(null, "test-1", "test-2", null, 1, null, LocalDateTime.now(), LocalDateTime.now(), null);
    ClientRoles clientRoles = new ClientRoles();
    // Set clientRoles properties here...
    // Create a UUID
    UUID uuid = UUID.randomUUID();

    // Convert UUID to String
    String uuidStr = uuid.toString();
    clientRoles.setId(uuidStr);
    clientRoles.setClientId("test-1");
    clientRoles.setName("test-2");

    Mockito.when(clientRolesService.insert(clientRoles)).thenReturn(clientRoles);

    // Act: Insert the clientRoles using the service
    // ClientRoles savedClient = clientRolesService.insert(clientRoles);

    // Assertions.assertEquals(savedClient.getClientId(), clientRoles.getClientId());

    // Mock the service method
    // Mockito.when(clientRolesService.getById(clientRoles.getId())).thenReturn(clientRoles).thenThrow(new RuntimeException());

    // Assert: Send a GET request to retrieve the clientRoles by ID
    mockMvc.perform(
      get("/client-roles/{id}", uuidStr)
      .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}