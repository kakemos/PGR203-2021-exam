package no.kristiania.db.dao;

import no.kristiania.db.objects.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionDao extends AbstractDao<Question> {

    public QuestionDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public List<Question> listAll() throws SQLException {
        return super.listAll("SELECT * FROM question");
    }

    @Override
    protected Question rowToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestionId(rs.getLong("id"));
        question.setQuestionTitle(rs.getString("question_title"));
        question.setQuestionDescription(rs.getString("question_description"));
        return question;
    }

    @Override
    protected void insertObject(Question obj, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, obj.getQuestionTitle());
        insertStatement.setString(2, obj.getQuestionDescription());
    }

    public Question retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM question WHERE id = ?", id);
    }

    public long insert(Question question) throws SQLException {
        return insert(question, "INSERT into question (question_title, question_description) values(?, ?)");
    }

    public void updateQuestionTitle(String questionTitle, long id) throws SQLException {
        updateRow(questionTitle, id, "UPDATE question set question_title = ? WHERE id = ?");
    }

    public void updateQuestionDescription(String questionDescription, long id) throws SQLException {
        updateRow(questionDescription, id, "UPDATE question set question_description = ? WHERE id = ?");
    }
}
