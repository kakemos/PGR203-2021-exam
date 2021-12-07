package no.kristiania.db;

import no.kristiania.TestData;
import no.kristiania.db.dao.AnswerDao;
import no.kristiania.db.objects.Answer;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerDaoTest {

    private AnswerDao dao = new AnswerDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedAnswer() throws SQLException {

        dao.insert(TestData.exampleAnswer());
        dao.insert(TestData.exampleAnswer());

        Answer answer = TestData.exampleAnswer();
        answer.setAnswerId(dao.insert(answer));

        assertThat(dao.retrieve(answer.getAnswerId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(answer);
    }

    @Test
    void shouldListAllAnswers() throws SQLException {
        Answer answer1 = TestData.exampleAnswer();
        dao.insert(answer1);

        Answer anotherAnswer = TestData.exampleAnswer();
        dao.insert(anotherAnswer);

        Answer answer = TestData.exampleAnswer();
        answer.setAnswerId(dao.insert(answer));

        assertThat(dao.listAll())
                .extracting(Answer::getOptionId)
                .contains(answer.getOptionId(), anotherAnswer.getOptionId());

    }
}
