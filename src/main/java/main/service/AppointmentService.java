package main.service;

import main.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    boolean add(Appointment Appointment);

    void delete(long id);

    List<Appointment> getAllByCustomer(long id);

    List<Appointment> getAll();

    Appointment getById(long id);
}
