package com.mi.iam;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.iam.controllers.ClientsController;
import com.mi.iam.models.dto.MyPagination;
import com.mi.iam.models.entities.Clients;
import com.mi.iam.models.repositories.ClientsRepository;
import com.mi.iam.services.ClientsService;

@SpringBootTest
// @WebMvcTest(ClientsController.class)
@AutoConfigureMockMvc
public class ClientsTest {
  private static final Logger logger = LoggerFactory.getLogger(ClientsTest.class);
  
  @Autowired
	private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private ClientsService clientsService;

  @MockBean
  private ClientsRepository clientsRepository;

  @InjectMocks
  private ClientsController clientsController;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(clientsController).build();
  }

  @Test
  public void insertClients_success() throws Exception {
    Clients newClient = new Clients();
    newClient.setClientId("test-1");
    newClient.setName("test-2");
    newClient.setIsActive(1);

    logger.info("ClientId" + newClient.getClientId());

    // Mock the service method
    Mockito.when(clientsService.insert(newClient)).thenReturn(newClient);

    mockMvc.perform(post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(newClient)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.clientId").value("test-1"));
  }

  @Test
  public void testInsertClientWithBlankClientId() throws Exception {
    // Create a Clients object with a blank clientId
    Clients client = new Clients();
    client.setClientId(""); // Blank clientId violates @NotBlank constraint

    // Perform a POST request to the insert endpoint
    mockMvc.perform(post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(client)))
        .andExpect(status().isBadRequest()); // Expect HTTP 400 Bad Request
  }
  
  @Test
  public void getAll_success() throws Exception {
    // Create a list of Clients for testing
    List<Clients> clientsList = new ArrayList<>();
    clientsList.add(new Clients());
    clientsList.add(new Clients());

    // Mock the service method
    Mockito.when(clientsService.getAll(Mockito.anyString(), Mockito.any())).thenReturn(new MyPagination<>(clientsList, clientsList.size(), 1, 1, clientsList.size()));

    // Act & Assert
    mockMvc.perform(
      get("/clients")
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
    Clients client = new Clients();
    // Set client properties here...
    client.setClientId("test-1");
    client.setName("test-2");
    client.setIsActive(1);

    // Act: Insert the client using the service
    Clients savedClient = clientsService.insert(client);

    Assertions.assertEquals(savedClient.getClientId(), client.getClientId());

    // Mock the service method
    // Mockito.when(clientsService.getById(client.getId())).thenReturn(client).thenThrow(new RuntimeException());

    // Assert: Send a GET request to retrieve the client by ID
    mockMvc.perform(
      get("/clients/{id}", savedClient.getId())
      .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data.id").value(savedClient.getId()))
        .andExpect(jsonPath("$.data.clientId").value("test-1"))
        .andExpect(jsonPath("$.data.name").value("test-2"));
  }
}