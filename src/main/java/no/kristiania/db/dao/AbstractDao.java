package no.kristiania.db.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {

    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public long insert(T object, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertObject(object, statement);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong("id");
            }
        }
    }

    protected T retrieve(String sql, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return rowToObject(rs);
                }
            }
        }
    }

    public void updateRow(String updateString, long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, updateString);
                statement.setLong(2, id);
                statement.executeUpdate();
            }
        }
    }

    public abstract List<T> listAll() throws SQLException;

    protected List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<T> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(rowToObject(rs));
                    }
                    return result;
                }
            }
        }
    }

    abstract protected T rowToObject(ResultSet rs) throws SQLException;

    protected abstract void insertObject(T obj, PreparedStatement insertStatement) throws SQLException;
}
