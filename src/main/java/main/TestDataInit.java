package main;

import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import main.repository.*;

import java.sql.Date;

@Component
public class TestDataInit implements CommandLineRunner {
    @Autowired
    CustomerRepository customerRepository;



    @Autowired
    MethodRepository MethodRepository;

    @Autowired
    AppointmentRepository AppointmentRepository;

    @Autowired
    RoomMethodRepository roomMethodRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder pwdEncoder;

    @Override
    public void run(String[] args) {
//        Room room = new Room("Room", "description", 100L);
//        Room room1 = new Room("Room 1", "description", 1100L);
//        Room room2 = new Room("Room 2", "description", 900L);
//        Room room3 = new Room("Room 3", "description", 200L);
//        Room room4 = new Room("Room 4", "description", 1400L);
//
//        roomRepository.save(room);
//        roomRepository.save(room1);
//        roomRepository.save(room2);
//        roomRepository.save(room3);
//        roomRepository.save(room4);
//
//        Method Method = new Method("Fender guitar", "description");
//        Method Method1 = new Method("Gibson guitar", "description");
//        Method Method2 = new Method("Pearl drum kit", "description");
//        Method Method3 = new Method("Peavey guitar amp", "description");
//        Method Method4 = new Method("Tama snare", "description");
//
//        MethodRepository.save(Method);
//        MethodRepository.save(Method1);
//        MethodRepository.save(Method2);
//        MethodRepository.save(Method3);
//        MethodRepository.save(Method4);
//
//        roomMethodRepository.save(new RoomMethod(room, Method));
//
//        User user = new User("admin", pwdEncoder.encode("password"), "ADMIN");
//        userRepository.save(user);
//
//        Customer customer = new Customer("name", "+79643423523", user);
//        customerRepository.save(customer);
//
//        Appointment Appointment = new Appointment(Date.valueOf("2021-12-18"), room, customer);
//        Appointment Appointment1 = new Appointment(Date.valueOf("2022-01-11"), room1, customer);
//        Appointment Appointment2 = new Appointment(Date.valueOf("2022-01-12"), room2, customer);
//        AppointmentRepository.save(Appointment);
//        AppointmentRepository.save(Appointment1);
//        AppointmentRepository.save(Appointment2);
    }
}
