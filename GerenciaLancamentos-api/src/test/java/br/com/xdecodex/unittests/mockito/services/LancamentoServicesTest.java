package br.com.xdecodex.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.exceptions.PersonInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Launch;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.repositories.LaunchRepository;
import br.com.xdecodex.repositories.PersonRepository;
import br.com.xdecodex.services.LaunchService;
import br.com.xdecodex.unittests.mapper.mocks.MockLaunch;

@ExtendWith(MockitoExtension.class)
class LaunchServiceTest {

    @InjectMocks
    private LaunchService service;

    @Mock
    private LaunchRepository launchRepository;

    @Mock
    private PersonRepository personRepository;

    private MockLaunch mockLaunch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockLaunch = new MockLaunch();
    }

    @Test
    void testFindById() {
        Launch launch = mockLaunch.mockEntity(1);
        launch.setId(1L);

        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch));

        LaunchVO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Description Teste 1", result.getDescription());
    }

    @Test
    void testFindByIdNotFound() {
        when(launchRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertEquals("Launch not found by ID: 1", exception.getMessage());
    }

    @Test
    void testCreate() {
        LaunchVO vo = mockLaunch.mockVO(1);
        Person person = mock(Person.class);
        Launch launch = mockLaunch.mockEntity(1);

        when(personRepository.findById(vo.getPerson().getId())).thenReturn(Optional.of(person));
        when(launchRepository.save(any(Launch.class))).thenReturn(launch);
        when(person.isInactive()).thenReturn(false);

        LaunchVO result = service.create(vo);

        assertNotNull(result);
        assertEquals(vo.getId(), result.getId());
        assertEquals(vo.getDescription(), result.getDescription());
    }

    @Test
    void testCreateWithInactivePerson() {
        LaunchVO vo = mockLaunch.mockVO(1);
        Person person = mock(Person.class);

        when(personRepository.findById(vo.getPerson().getId())).thenReturn(Optional.of(person));
        when(person.isInactive()).thenReturn(true);

        Exception exception = assertThrows(PersonInexistenteOuInativaException.class, () -> {
            service.create(vo);
        });

        assertNotNull(exception);
    }

    @Test
    void testUpdate() {
        Launch launch = mockLaunch.mockEntity(1);
        LaunchVO vo = mockLaunch.mockVO(1);

        when(launchRepository.findById(vo.getId())).thenReturn(Optional.of(launch));
        when(launchRepository.save(any(Launch.class))).thenReturn(launch);

        LaunchVO result = service.update(vo);

        assertNotNull(result);
        assertEquals(vo.getId(), result.getId());
        assertEquals(vo.getDescription(), result.getDescription());
    }

    @Test
    void testUpdateNotFound() {
        LaunchVO vo = mockLaunch.mockVO(1);

        when(launchRepository.findById(vo.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(vo);
        });

        assertEquals("Launch not found for update", exception.getMessage());
    }

    @Test
    void testDelete() {
        Launch launch = mockLaunch.mockEntity(1);

        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(launchRepository, times(1)).delete(launch);
    }

    @Test
    void testDeleteNotFound() {
        when(launchRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        assertEquals("Launch not found to delete", exception.getMessage());
    }

}