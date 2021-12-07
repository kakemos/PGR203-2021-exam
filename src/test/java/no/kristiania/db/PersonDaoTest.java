package no.kristiania.db;

import no.kristiania.TestData;
import no.kristiania.db.dao.PersonDao;
import no.kristiania.db.objects.Person;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonDaoTest {

    private PersonDao dao = new PersonDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedPerson() throws SQLException {
        dao.insert(TestData.examplePerson());
        dao.insert(TestData.examplePerson());

        Person person = TestData.examplePerson();
        person.setPersonId(dao.insert(person));

        assertThat(dao.retrieve(person.getPersonId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(person)
        ;
    }

    @Test
    void shouldListAllPeople() throws SQLException {
        Person person1 = TestData.examplePerson();
        dao.insert(person1);

        Person anotherPerson = TestData.examplePerson();
        dao.insert(anotherPerson);

        Person person = TestData.examplePerson();
        person.setPersonId(dao.insert(person));

        assertThat(dao.listAll())
                .extracting((Person::getFirstName))
                .contains(person.getFirstName(), anotherPerson.getFirstName());
    }
}
