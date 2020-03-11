package repository;

import com.ws.person.Factory.PersonFactory;
import com.ws.person.Model.Dto.Entity.PersonEntity;
import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PersonRepositoryTests {
    @Mock
    PersonRepository personRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mustReturnSuccessWhenPersonIsSaved() {
        PersonDto personDto = buildMockPerson();
        PersonEntity personEntity = PersonFactory.buildPersonEntity(personDto);
        when(personRepository.save(personEntity)).thenReturn(new PersonEntity());
        //When
        personRepository.save(personEntity);
        //Then
        verify(personRepository, times(1)).save(personEntity);
    }

    @Test
    public void mustReturnDataAccessExceptionWhenPersonIsNotSaved() {
        //Given

        PersonDto personDto = buildMockPerson();
        PersonEntity personEntity = PersonFactory.buildPersonEntity(personDto);
        when(personRepository.save(personEntity)).thenThrow(new DataAccessException("Error For saving entity"){});
        //When
        Throwable exception = assertThrows(DataAccessException.class, () -> personRepository.save(personEntity));
        //Then
        assertEquals("Error For saving entity", exception.getMessage());
    }

    private PersonDto buildMockPerson() {
        return PersonDto.builder().type("PF").documentNumber("12345678901").score(5).name("Alberto Silva").build();
    }

}
