package no.kristiania.db;

import no.kristiania.TestData;
import no.kristiania.db.dao.QuestionDao;
import no.kristiania.db.objects.Question;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    private QuestionDao dao = new QuestionDao(TestData.testDataSource());

    @Test
    void shouldRetrievedInsertedQuestion() throws SQLException {

        dao.insert(TestData.exampleQuestion());
        dao.insert(TestData.exampleQuestion());

        Question question = TestData.exampleQuestion();
        question.setQuestionId(dao.insert(question));

        assertThat(dao.retrieve(question.getQuestionId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void shouldListAllQuestions() throws SQLException {

        Question question1 = TestData.exampleQuestion();
        dao.insert(question1);

        Question anotherQuestion = TestData.exampleQuestion();
        dao.insert(anotherQuestion);

        Question question = TestData.exampleQuestion();
        question.setQuestionId(dao.insert(question));

        assertThat(dao.listAll())
                .extracting(Question :: getQuestionTitle)
                .contains(question.getQuestionTitle(), anotherQuestion.getQuestionTitle());
    }

    @Test
    void shouldUpdateQuestion() throws SQLException {
        Question question = TestData.exampleQuestion();
        question.setQuestionId(dao.insert(question));

        System.out.println(question.getQuestionTitle());

        Question anotherQuestion = TestData.exampleQuestion();

        dao.updateQuestionTitle(anotherQuestion.getQuestionTitle(), question.getQuestionId());

        assertThat(dao.retrieve(question.getQuestionId()))
                .extracting(Question::getQuestionTitle)
                .isEqualTo(anotherQuestion.getQuestionTitle());
    }
}
