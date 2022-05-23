package main.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Customer;
import main.entity.Appointment;
import main.entity.Room;
import main.model.AppointmentModel;
import main.repository.CustomerRepository;
import main.repository.AppointmentRepository;
import main.repository.RoomRepository;
import main.service.CustomerService;
import main.service.AppointmentService;
import main.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AppointmentService AppointmentService;

    @MockBean
    private AppointmentRepository AppointmentRepository;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    // 12
    @WithMockUser("/user")
    @Test
    public void shouldAddAppointment() throws Exception {
        AppointmentModel AppointmentModel = new AppointmentModel();
        long customerId = 1;
        long roomId = 1;
        AppointmentModel.setCustomerId(customerId);
        AppointmentModel.setRoomId(roomId);
        AppointmentModel.setDate(LocalDate.now().toString());
        when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(new Customer()));
        when(roomRepository.findById(roomId)).thenReturn(java.util.Optional.of(new Room()));
        String jsonRequest = mapper.writeValueAsString(AppointmentModel);
        mockMvc.perform(post("/Appointments/add").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(AppointmentRepository, times(1)).save(any(Appointment.class));
    }

    // 13
    @WithMockUser("/user")
    @Test
    public void shouldDeleteAppointment() throws Exception {
        Appointment AppointmentToDelete = new Appointment();
        when(AppointmentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(AppointmentToDelete));
        mockMvc.perform(delete("/Appointments/customer/1"))
                .andExpect(status().isOk());
        verify(AppointmentRepository, times(1)).delete(AppointmentToDelete);
    }

    // 14
    @WithMockUser("/user")
    @Test
    public void shouldChangeAppointmentStatus() throws Exception {
        Appointment Appointment = new Appointment();
        boolean initialStatus = false;
        Appointment.setId(1L);
        Appointment.setDate(Date.valueOf(LocalDate.now()));
        Appointment.setConfirmed(initialStatus);

        when(AppointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(Appointment));

        mockMvc.perform(put("/Appointments/1"))
                .andExpect(status().isOk());

        Optional<Appointment> updatedAppointment = AppointmentRepository.findById(1L);
        assertEquals(updatedAppointment.get().isConfirmed(), !initialStatus);
    }

    // 15

    // 16
    @WithMockUser("/user")
    @Test
    public void shouldReturnCustomersAppointments() throws Exception {
        Appointment Appointment = new Appointment();
        Customer customer = new Customer();
        customer.setId(1L);
        Appointment.setCustomer(customer);
        List<Appointment> expectedAppointments = Collections.singletonList(Appointment);
        when(AppointmentRepository.findAll()).thenReturn(expectedAppointments);
        String expectedJsonContent = mapper.writeValueAsString(expectedAppointments);

        mockMvc.perform(get("/Appointments/customer/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonContent));
    }

    // 17
    @WithMockUser("/user")
    @Test
    public void shouldReturnAllAppointments() throws Exception {
        List<Appointment> expectedAppointments = Arrays.asList(new Appointment(), new Appointment());
        when(AppointmentRepository.findAll()).thenReturn(expectedAppointments);
        String expectedJsonContent = mapper.writeValueAsString(expectedAppointments);

        mockMvc.perform(get("/Appointments/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(content().json(expectedJsonContent));
    }
}
