package no.kristiania.db;

import no.kristiania.TestData;
import no.kristiania.db.dao.OptionDao;
import no.kristiania.db.objects.Option;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionDaoTest {
    private OptionDao dao = new OptionDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedOption() throws SQLException {

        dao.insert(TestData.exampleOption());
        dao.insert(TestData.exampleOption());

        Option option = TestData.exampleOption();
        option.setOptionId(dao.insert(option));

        assertThat(dao.retrieve(option.getOptionId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(option)
        ;
    }

    @Test
    void shouldListAllPeople() throws SQLException {
        Option option1 = TestData.exampleOption();
        dao.insert(option1);

        Option anotherOption = TestData.exampleOption();
        dao.insert(anotherOption);

        Option option = TestData.exampleOption();
        option.setOptionId(dao.insert(option));

        assertThat(dao.listAll())
                .extracting((Option::getOptionName))
                .contains(option.getOptionName(), anotherOption.getOptionName());
    }

}
