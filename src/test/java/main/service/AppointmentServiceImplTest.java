package main.service;

import main.entity.Appointment;
import main.exception.EntityNotFoundException;
import main.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * TOTAL: 5
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppointmentServiceImpl.class})
class AppointmentServiceImplTest {

    @MockBean
    private AppointmentRepository AppointmentRepository;

    @Autowired
    private AppointmentService AppointmentService;

    @Test
    void shouldAddAppointment() {
        Appointment Appointment = new Appointment();
        Appointment.setDate(Date.valueOf(LocalDate.now().plusDays(1)));
        boolean isAppointmentAdded = AppointmentService.add(Appointment);
        assertTrue(isAppointmentAdded);
        verify(AppointmentRepository, times(1)).save(Appointment);
    }

    @Test
    void shouldNotAddAppointmentIfAlreadyExists() {
        Appointment Appointment = new Appointment();
        Appointment.setId(1L);
        Appointment.setDate(Date.valueOf(LocalDate.now().plusDays(1)));
        when(AppointmentRepository.findById(Appointment.getId()))
                .thenReturn(Optional.of(Appointment));
        boolean isAppointmentAdded = AppointmentService.add(Appointment);
        assertFalse(isAppointmentAdded);
        verify(AppointmentRepository, times(0)).save(Appointment);
    }

    @Test
    void shouldNotCreateAppointmentForPastDate() {
        Appointment Appointment = new Appointment();
        Appointment.setDate(Date.valueOf(LocalDate.now().minusDays(1)));
        boolean isAppointmentCreated = AppointmentService.add(Appointment);
        assertFalse(isAppointmentCreated);
    }

    @Test
    void shouldDeleteAppointmentIfFound() {
        Appointment Appointment = new Appointment();
        Appointment.setId(1L);
        when(AppointmentRepository.findById(Appointment.getId()))
                .thenReturn(Optional.of(Appointment));
        AppointmentService.delete(Appointment.getId());
        verify(AppointmentRepository, times(1)).delete(Appointment);
    }

    @Test
    void shouldThrowExceptionIfAppointmentNotFound() {
        Appointment Appointment = new Appointment();
        Appointment.setId(1L);
        given(AppointmentRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> AppointmentService.delete(Appointment.getId()));
        verify(AppointmentRepository, times(0)).delete(Appointment);
    }

    @Test
    void shouldReturnAllAppointments() {
        List<Appointment> expectedAppointments = new ArrayList();
        expectedAppointments.add(new Appointment());
        given(AppointmentRepository.findAll()).willReturn(expectedAppointments);
        List<Appointment> Appointments = AppointmentService.getAll();
        assertEquals(expectedAppointments, Appointments);
        verify(AppointmentRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAppointmentById() {
        Appointment expectedAppointment = new Appointment();
        given(AppointmentRepository.findById(anyLong())).willReturn(Optional.of(expectedAppointment));
        Appointment foundAppointment = AppointmentService.getById(1L);
        verify(AppointmentRepository, times(1)).findById(1L);
        assertEquals(expectedAppointment, foundAppointment);
    }
}
