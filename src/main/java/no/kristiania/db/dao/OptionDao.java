package no.kristiania.db.dao;

import no.kristiania.db.objects.Option;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OptionDao extends AbstractDao<Option> {

    public OptionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Option> listAll() throws SQLException {
        return super.listAll("SELECT * FROM option");
    }

    @Override
    protected Option rowToObject(ResultSet rs) throws SQLException {
        Option option = new Option();
        option.setOptionId(rs.getLong("id"));
        option.setOptionName(rs.getString("option_name"));
        option.setQuestionId(rs.getLong("question_id"));
        return option;
    }

    @Override
    protected void insertObject(Option obj, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, obj.getOptionName());
        insertStatement.setLong(2, obj.getQuestionId());
    }

    public Option retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM option WHERE id = ?", id);
    }

    public long insert(Option option) throws SQLException {
        return insert(option, "INSERT into option (option_name, question_id) values (?, ?)");
    }
}