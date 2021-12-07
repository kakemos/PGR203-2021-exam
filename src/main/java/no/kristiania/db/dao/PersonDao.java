package no.kristiania.db.dao;

import no.kristiania.db.objects.Person;
import no.kristiania.http.SurveyServer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonDao extends AbstractDao<Person> {

    public PersonDao(DataSource dataSource) {
        super(dataSource);
    }

    public Person retrieve(long id) throws SQLException {
        return super.retrieve("SELECT * FROM person WHERE id = ?", id);
    }

    public long insert(Person person) throws SQLException {
        return insert(person, "insert into person (first_name, last_name, email) values (?, ?, ?)");
    }

    @Override
    public List<Person> listAll() throws SQLException {
        return super.listAll("SELECT * FROM person");
    }

    @Override
    protected Person rowToObject(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setPersonId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setMailAddress(rs.getString("email"));
        return person;
    }

    @Override
    protected void insertObject(Person obj, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, obj.getFirstName());
        insertStatement.setString(2, obj.getLastName());
        insertStatement.setString(3, obj.getMailAddress());
    }

    public static Person readFromResultSet(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setPersonId(rs.getLong("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setMailAddress(rs.getString("email"));
        return person;
    }

    public void updateFirstName(String firstName, long id) throws SQLException {
        updateRow(firstName, id, "UPDATE person SET first_name = ? where id = ?");
    }

    public void updateLastName(String lastName, long id) throws SQLException {
        updateRow(lastName, id, "UPDATE person SET last_name = ? where id = ?");
    }

    public void updateEmail(String email, long id) throws SQLException {
        updateRow(email, id, "UPDATE person SET email = ? where id = ?");
    }

    public static Person listOutPersonFromCookieName(String cookieName) throws SQLException, IOException {
        DataSource dataSource = SurveyServer.createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM person WHERE first_name LIKE ?;")) {
                statement.setString(1, cookieName);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return readFromResultSet(rs);
                }
            }
        }
    }
}
