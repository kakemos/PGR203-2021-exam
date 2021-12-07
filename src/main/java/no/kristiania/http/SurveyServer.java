package no.kristiania.http;

import no.kristiania.db.dao.AnswerDao;
import no.kristiania.http.controllers.*;
import no.kristiania.db.dao.OptionDao;
import no.kristiania.db.dao.PersonDao;
import no.kristiania.db.dao.QuestionDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SurveyServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();

        PersonDao personDao = new PersonDao(dataSource);
        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);
        OptionDao optionDao = new OptionDao(dataSource);

        HttpServer httpServer = new HttpServer(1963);
        httpServer.addController(new AddQuestionController(questionDao));
        httpServer.addController(new ListQuestionAndOptionController(questionDao, optionDao));
        httpServer.addController(new AddOptionController(optionDao));
        httpServer.addController(new ListAnswersController(answerDao));
        httpServer.addController(new listQuestionController(questionDao));
        httpServer.addController(new AddNewAnswerController(answerDao));
        httpServer.addController(new EditQuestionController(questionDao));
        httpServer.addController(new AddPersonController(personDao));
        httpServer.addController(new RedirectController());
        httpServer.addController(new editPersonController(personDao));

        logger.info("Go to http://localhost:{}/index.html", httpServer.getPort());
    }

    public static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty(
                "dataSource.url",
                "jdbc:postgresql://localhost:5432/survey_db"));
        dataSource.setUser(properties.getProperty(
                "dataSource.user",
                "survey_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
