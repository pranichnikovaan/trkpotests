package main.service;

import main.entity.Appointment;
import main.exception.EntityNotFoundException;
import main.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository AppointmentRepository;

    @Override
    public boolean add(Appointment Appointment) {
        if (Appointment.getDate().toLocalDate().isBefore(LocalDate.now())) {
            return false;
        }
        Optional<Appointment> AppointmentFromDB = AppointmentRepository.findById(Appointment.getId());
        if (AppointmentFromDB.isPresent()) {
            return false;
        }
        AppointmentRepository.save(Appointment);
        return true;
    }

    @Override
    public void delete(long id) {
        Optional<Appointment> Appointment = AppointmentRepository.findById(id);
        if (Appointment.isEmpty()) {
            throw new EntityNotFoundException("Appointment not found");
        }
        AppointmentRepository.delete(Appointment.get());
    }

    @Override
    public List<Appointment> getAllByCustomer(long id) {
        return ((List<Appointment>) AppointmentRepository.findAll())
                .stream()
                .filter(Appointment -> Appointment.getCustomer().getId().compareTo(id) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAll() {
        return (List<Appointment>) AppointmentRepository.findAll();
    }

    @Override
    public Appointment getById(long id) {
        Optional<Appointment> Appointment = AppointmentRepository.findById(id);
        return Appointment.orElse(null);
    }
}
