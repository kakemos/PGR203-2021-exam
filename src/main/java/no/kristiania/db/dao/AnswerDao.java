package no.kristiania.db.dao;

import no.kristiania.db.objects.Answer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnswerDao extends AbstractDao<Answer> {

    public AnswerDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public List<Answer> listAll() throws SQLException {
        return super.listAll("SELECT * FROM answer");
    }

    @Override
    protected Answer rowToObject(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerId(rs.getLong("id"));
        answer.setQuestionId(rs.getLong("question_id"));
        answer.setOptionId(rs.getLong("option_id"));
        answer.setPersonId(rs.getLong("person_id"));
        return answer;
    }

    @Override
    protected void insertObject(Answer obj, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setLong(1, obj.getQuestionId());
        insertStatement.setLong(2, obj.getPersonId());
        insertStatement.setLong(3, obj.getOptionId());
    }

    public Answer retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM answer WHERE id = ?", id);
    }

    public long insert(Answer answer) throws SQLException {
        return insert(answer, "INSERT into answer (question_id, person_id, option_id) values (?, ?, ?)");
    }
}
