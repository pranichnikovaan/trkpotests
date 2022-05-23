package main.service;

import main.entity.Method;
import main.exception.EntityNotFoundException;
import main.repository.MethodRepository;
import main.repository.RoomMethodRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
 * TOTAL: 9
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MethodServiceImpl.class})
class MethodServiceImplTest {

    @MockBean
    private MethodRepository MethodRepository;

    @MockBean
    private RoomMethodRepository roomMethodRepository;

    @Autowired
    private MethodService MethodService;

    @Test
    void shouldAddMethod() {
        Method Method = new Method();
        boolean isMethodAdded = MethodService.add(Method);
        assertTrue(isMethodAdded);
        verify(MethodRepository, times(1)).save(Method);
    }

    @Test
    void shouldNotAddMethodIfAlreadyExists() {
        Method Method = new Method();
        Method.setId(1L);
        when(MethodRepository.findById(Method.getId()))
                .thenReturn(Optional.of(Method));
        boolean isMethodAdded = MethodService.add(Method);
        assertFalse(isMethodAdded);
        verify(MethodRepository, times(0)).save(Method);
    }

    @Test
    void shouldDeleteMethodIfFound() {
        Method Method = new Method();
        Method.setId(1L);
        when(MethodRepository.findById(Method.getId()))
                .thenReturn(Optional.of(Method));
        MethodService.delete(Method.getId());
        verify(MethodRepository, times(1)).delete(Method);
    }

    @Test
    void shouldThrowExceptionIfMethodNotFound() {
        Method Method = new Method();
        Method.setId(666L);
        given(MethodRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> MethodService.delete(Method.getId()));
        verify(MethodRepository, times(0)).delete(Method);
    }

    @Test
    void shouldReturnMethodIfFound() {
        Method Method = new Method();
        Method.setId(1L);
        when(MethodRepository.findById(Method.getId()))
                .thenReturn(Optional.of(Method));
        Method expected = MethodService.getById(Method.getId());
        assertEquals(expected, Method);
        verify(MethodRepository, times(1)).findById(Method.getId());
    }

    @Test
    void shouldThrowExceptionIfNotFound() {
        Method Method = new Method();
        Method.setId(1L);
        given(MethodRepository.findById(anyLong())).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> MethodService.getById(Method.getId()));
        verify(MethodRepository, times(1)).findById(Method.getId());
    }

    @Test
    void shouldFindMethod() {
        Method Method = new Method();
        Method.setId(1L);
        when(MethodRepository.findById(Method.getId()))
                .thenReturn(Optional.of(Method));
        boolean isFound = MethodService.checkById(Method.getId());
        assertTrue(isFound);
        verify(MethodRepository, times(1)).findById(Method.getId());
    }

    @Test
    void shouldNotFindMethodIfAbsent() {
        Method Method = new Method();
        Method.setId(1L);
        given(MethodRepository.findById(anyLong())).willReturn(Optional.empty());
        boolean isFound = MethodService.checkById(Method.getId());
        assertFalse(isFound);
        verify(MethodRepository, times(1)).findById(Method.getId());
    }

    @Test
    void shouldReturnAllMethods() {
        List<Method> Methods = new ArrayList();
        Methods.add(new Method());
        given(MethodRepository.findAll()).willReturn(Methods);
        List<Method> expected = MethodService.getAll();
        assertEquals(expected, Methods);
        verify(MethodRepository, times(1)).findAll();
    }
}
