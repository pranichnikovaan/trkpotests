package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Method;
import main.entity.Room;
import main.entity.RoomMethod;
import main.model.MethodModel;
import main.model.RoomMethodModel;
import main.model.RoomModel;
import main.repository.MethodRepository;
import main.repository.RoomMethodRepository;
import main.repository.RoomRepository;
import main.service.MethodService;
import main.service.RoomMethodService;
import main.service.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MethodService MethodService;

    @MockBean
    private MethodRepository MethodRepository;

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

    public static RequestPostProcessor admin() {
        return user("admin").roles("ADMIN");
    }

    public static RequestPostProcessor regularUser() {
        return user("user").roles("USER");
    }

    // 3
    @Test
    public void addRoomAndReturnOkIfAdmin() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    // 4
    @Test
    public void addRoomIsForbiddenIfRegularUser() throws Exception {
        RoomModel room = new RoomModel();
        room.setName("Test room");
        room.setDescription("Test description");
        room.setPrice(100L);
        String jsonRequest = mapper.writeValueAsString(room);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(regularUser()))
                .andExpect(status().isForbidden());
        verify(roomRepository, times(0)).save(any(Room.class));
    }

    // 5
    @Test
    public void addMethodAndReturnOkIfAdmin() throws Exception {
        MethodModel Method = new MethodModel();
        Method.setName("Test Method");
        Method.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(Method);
        mockMvc.perform(post("/data/Method").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(MethodRepository, times(1)).save(any(Method.class));
    }

    // 6
    @Test
    public void addMethodIsForbiddenIfRegularUser() throws Exception {
        MethodModel Method = new MethodModel();
        Method.setName("Test Method");
        Method.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(Method);
        mockMvc.perform(post("/data/Method").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(regularUser()))
                .andExpect(status().isForbidden());
        verify(MethodRepository, times(0)).save(any(Method.class));
    }

    // 6
    @Test
    public void addMethodIsForbiddenIfUnauthorized() throws Exception {
        MethodModel Method = new MethodModel();
        Method.setName("Test Method");
        Method.setDescription("Test description");
        String jsonRequest = mapper.writeValueAsString(Method);
        mockMvc.perform(post("/data/room").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // 7
    @Test
    public void shouldDeleteRoomIfAdmin() throws Exception {
        Room roomToDelete = new Room();
        when(roomRepository.findById(anyLong())).thenReturn(java.util.Optional.of(roomToDelete));
        mockMvc.perform(delete("/data/room/1").with(admin()))
                .andExpect(status().isOk());
        verify(roomRepository, times(1)).delete(roomToDelete);
    }

    // 8
    @Test
    public void shouldDeleteMethodIfAdmin() throws Exception {
        Method MethodToDelete = new Method();
        when(MethodRepository.findById(anyLong())).thenReturn(java.util.Optional.of(MethodToDelete));
        mockMvc.perform(delete("/data/Method/1").with(admin()))
                .andExpect(status().isOk());
        verify(MethodRepository, times(1)).delete(MethodToDelete);
    }

    // 9
    @Test
    public void shouldDeleteRoomMethodIfAdmin() throws Exception {
        RoomMethod roomMethodToDelete = new RoomMethod();
        when(roomMethodRepository.findById(1L)).thenReturn(java.util.Optional.of(roomMethodToDelete));
        mockMvc.perform(delete("/data/room_Method/1").with(admin()))
                .andExpect(status().isOk());
        verify(roomMethodRepository, times(1)).delete(roomMethodToDelete);
    }

    // 20
    @Test
    public void shouldAddRoomMethodIfAdmin() throws Exception {
        RoomMethodModel model = new RoomMethodModel();
        long roomId = 1L;
        long MethodId = 1L;
        model.setRoomId(roomId);
        model.setMethodId(MethodId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(new Room()));
        when(MethodRepository.findById(MethodId)).thenReturn(Optional.of(new Method()));

        String jsonRequest = mapper.writeValueAsString(model);

        mockMvc.perform(post("/data/room_Method").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                        .with(admin()))
                .andExpect(status().isOk());
        verify(roomMethodRepository, times(1)).save(any(RoomMethod.class));
    }
}
