package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Method;
import main.entity.Room;
import main.entity.RoomMethod;
import main.repository.RoomMethodRepository;
import main.repository.RoomRepository;
import main.service.RoomMethodService;
import main.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private RoomMethodService roomMethodService;

    @MockBean
    private RoomMethodRepository roomMethodRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    // 10
    @WithMockUser("/user")
    @Test
    public void shouldReturnRoomsAndOkIfAuthorized() throws Exception {
        List<Room> expectedRooms = Arrays.asList(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(expectedRooms);
        String expectedJsonContent = mapper.writeValueAsString(expectedRooms);

        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }

    // 11
    @Test
    public void shouldNotReturnRoomsIfUnauthorized() throws Exception {
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isForbidden());
    }

    // 18
    @WithMockUser("/user")
    @Test
    public void shouldReturnAllRoomMethods() throws Exception {
        RoomMethod roomMethod1 = new RoomMethod();
        RoomMethod roomMethod2 = new RoomMethod();

        List<RoomMethod> expectedRoomMethods = Arrays.asList(roomMethod1, roomMethod2);

        when(roomMethodRepository.findAll()).thenReturn(expectedRoomMethods);

        String expectedJsonContent = mapper.writeValueAsString(expectedRoomMethods);

        mockMvc.perform(get("/rooms/Methods"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }

    // 19
    @WithMockUser("/user")
    @Test
    public void shouldReturnMethodsFromRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        Method Method1 = new Method();
        RoomMethod roomMethod1 = new RoomMethod();
        roomMethod1.setRoom(room);
        roomMethod1.setMethod(Method1);
        Method Method2 = new Method();
        RoomMethod roomMethod2 = new RoomMethod();
        roomMethod2.setRoom(room);
        roomMethod2.setMethod(Method2);

        List<RoomMethod> roomMethodList = Arrays.asList(roomMethod1, roomMethod2);

        when(roomMethodRepository.findAll()).thenReturn(roomMethodList);

        List<Method> expectedMethod = Arrays.asList(Method1, Method2);

        String expectedJsonContent = mapper.writeValueAsString(expectedMethod);

        mockMvc.perform(get("/rooms/1/Methods"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }
}