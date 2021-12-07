package no.kristiania.http;

import no.kristiania.TestData;
import no.kristiania.db.objects.Answer;
import no.kristiania.db.dao.AnswerDao;
import no.kristiania.db.objects.Option;
import no.kristiania.db.dao.OptionDao;
import no.kristiania.db.dao.PersonDao;
import no.kristiania.db.objects.Question;
import no.kristiania.db.dao.QuestionDao;
import no.kristiania.http.controllers.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class HttpServerTest {
    private HttpServer server = new HttpServer(0); // server port 0 gir tilfeldig port

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknownRequestTarget() throws IOException {
        // InetSocketAddress
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReturnWithRequestTargetIn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals("File not found: /non-existing", client.getMessageBody());
    }

    @Test
    void shouldRespondWith200forKnownRequestTarget() throws IOException {
        server.addController(new FileTargetController());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/hello");
        assertAll(
                () -> assertEquals(200, client.getStatusCode()),
                //        () -> assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type")),
                () -> assertEquals("<p>Hello world!</p>", client.getMessageBody())
        );
    }

    @Test
    void shouldHandleMoreThanOneRequest() throws IOException {
        server.addController(new FileTargetController());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello").getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello").getStatusCode());
    }

    @Test
    void shouldEchoQueryParameter() throws IOException {
        server.addController(new FileTargetController());
        HttpClient client = new HttpClient(
                "localhost",
                server.getPort(),
                "/hello?firstName=Test&lastName=Persson");
        assertEquals("<p>Hello Persson, Test!</p>", client.getMessageBody());
    }

    @Test
    void shouldServeFiles() throws IOException {
        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadFileFromDisk() throws IOException {
        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/file.txt"), fileContent.getBytes());

        String file = "Testing read file from disk";
        File filePath = new File("target/test-classes/");

        Files.writeString(new File(filePath, "file.txt").toPath(), file);

        HttpClient client = new HttpClient("localhost", server.getPort(), "/file.txt");
        assertEquals(file, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadCSSFileFromDisk() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/style.css");
        assertEquals("text/css", client.getHeader("Content-Type"));
    }

    @Test
    void shouldUseFileExtensionForContentType() throws IOException {
        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/example-file.html"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    /*@Test
    void shouldListAllQuestionsWithOptions() throws SQLException, IOException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        OptionDao optionDao = new OptionDao(TestData.testDataSource());

        Question question1 = TestData.exampleQuestion();
        questionDao.insert(question1);
        Question question2 = TestData.exampleQuestion();
        questionDao.insert(question2);

        server.addController(new ListQuestionAndOptionController(questionDao, optionDao));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/question");
        assertThat(client.getMessageBody())
                .contains(question1.getQuestionTitle() + "</h2><p class='descriptionDiv'>" + question1.getQuestionDescription());
    }*/

    @Test
    void shouldListAllQuestions() throws SQLException, IOException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new listQuestionController(questionDao));

        Question question1 = TestData.exampleQuestion();
        questionDao.insert(question1);

        Question question2 = TestData.exampleQuestion();
        questionDao.insert(question2);

        HttpClient client = new HttpClient(
                "localhost",
                server.getPort(),
                "/api/questionOptions"
        );

        assertThat(client.getMessageBody())
                .contains(question1.getQuestionTitle(), question2.getQuestionTitle());

    }

    @Test
    void shouldListAllAnswers() throws SQLException, IOException {
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());
        server.addController(new ListAnswersController(answerDao));

        Answer answer1 = TestData.exampleAnswer();
        answerDao.insert(answer1);

        Answer answer2 = TestData.exampleAnswer();
        answerDao.insert(answer2);

        HttpClient client = new HttpClient(
                "localhost",
                server.getPort(),
                "/api/answer"
        );
        assertEquals(200, client.getStatusCode());
        assertThat(answerDao.listAll())
                .extracting(Answer::getQuestionId)
                .contains(answer1.getQuestionId(), answer2.getQuestionId());

    }
    @Test
    void shouldCreateNewPerson() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new AddPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/setCookie",
                "lastName=Larsen&email=marit@larsen.no",
                "firstName=Marit"
        );
        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(p -> {
                    assertThat(p.getFirstName()).isEqualTo("Marit");
                    assertThat(p.getLastName()).isEqualTo("Larsen");
                    assertThat(p.getMailAddress()).isEqualTo("marit@larsen.no");
                });
    }


    @Test
    void shouldCreateNewQuestion() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new AddQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "questionTitle=Heihei&questionDescription=Lollol"
        );
        assertEquals(303, postclient.getStatusCode());
        assertThat(questionDao.listAll())
                .extracting(Question::getQuestionTitle)
                .contains("Heihei");

    }

    @Test
    void shouldFailCreateNewQuestionWithoutTitle() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new AddQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "questionTitle=&questionDescription=Lollol"
        );
        assertEquals(400, postclient.getStatusCode());
    }

    @Test
    void shouldFailCreateNewQuestionWithoutDescription() throws IOException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new AddQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "questionTitle=Heihei&questionDescription="
        );
        assertEquals(400, postclient.getStatusCode());
    }

    @Test
    void shouldFailCreateNewQuestionWithoutTitleAndDescription() throws IOException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new AddQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "questionTitle=&questionDescription="
        );
        assertEquals(400, postclient.getStatusCode());
    }

    @Test
    void shouldCreateNewOption() throws IOException, SQLException {
        OptionDao optionDao = new OptionDao(TestData.testDataSource());
        server.addController(new AddOptionController(optionDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/alternativeAnswers",
                "questionId=1&optionName=NeiTakk"
        );
        assertEquals(303, postClient.getStatusCode());
        assertThat(optionDao.listAll())
                .extracting(Option::getOptionName)
                .contains("NeiTakk");
    }

    @Test
    void shouldUpdatePerson() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new editPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editPerson",
                "personId=1&newFirstName=Tonje&newLastName=Husvik&newEmail=t@h.no"
        );

        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getFirstName()).isEqualTo("Tonje");
                    assertThat(edit.getLastName()).isEqualTo("Husvik");
                    assertThat(edit.getMailAddress()).isEqualTo("t@h.no");
                });
    }

    @Test
    void shouldUpdateFirstName() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new editPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editPerson",
                "personId=1&newFirstName=Tonje&newLastName=&newEmail="
        );

        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getFirstName()).isEqualTo("Tonje");
                });
    }

    @Test
    void shouldUpdateLastNameAndLastName() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new editPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editPerson",
                "personId=1&newFirstName=Tonje&newLastName=Husvik&newEmail="
        );

        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getFirstName()).isEqualTo("Tonje");
                    assertThat(edit.getLastName()).isEqualTo("Husvik");
                });
    }

    @Test
    void shouldUpdateFirstNameAndEmail() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new editPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editPerson",
                "personId=1&newFirstName=Tonje&newLastName=&newEmail=t@h.no"
        );

        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getFirstName()).isEqualTo("Tonje");
                    assertThat(edit.getMailAddress()).isEqualTo("t@h.no");
                });
    }

    @Test
    void shouldUpdateLastNameAndEmail() throws IOException, SQLException {
        PersonDao personDao = new PersonDao(TestData.testDataSource());
        server.addController(new editPersonController(personDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editPerson",
                "personId=1&newFirstName=&newLastName=Husvik&newEmail=t@h.no"
        );

        assertEquals(303, postClient.getStatusCode());
        assertThat(personDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getLastName()).isEqualTo("Husvik");
                    assertThat(edit.getMailAddress()).isEqualTo("t@h.no");
                });
    }

    @Test
    void shouldUpdateQuestion() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new EditQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editQuestion",
                "questionTitle=1&newTitle=Lol&newDescription=Hehe"
        );

        assertEquals(303, postclient.getStatusCode());
        assertThat(questionDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getQuestionTitle()).isEqualTo("Lol");
                    assertThat(edit.getQuestionDescription()).isEqualTo("Hehe");
                });
    }

    @Test
    void shouldUpdateQuestionTitle() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new EditQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editQuestion",
                "questionTitle=1&newTitle=Lol&newDescription="
        );

        assertEquals(303, postclient.getStatusCode());
        assertThat(questionDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getQuestionTitle()).isEqualTo("Lol");
                });
    }

    @Test
    void shouldUpdateQuestionDescription() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        server.addController(new EditQuestionController(questionDao));

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/editQuestion",
                "questionTitle=1&newTitle=&newDescription=Hehe"
        );

        assertEquals(303, postclient.getStatusCode());
        assertThat(questionDao.listAll())
                .anySatisfy(edit -> {
                    assertThat(edit.getQuestionDescription()).isEqualTo("Hehe");
                });
    }

    @Test
    void shouldRedirectUser() throws IOException {
        server.addController(new RedirectController());

        HttpPostClient postclient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/",
                ""
        );

        assertEquals(303, postclient.getStatusCode());
    }

    @Test
    void shouldCreateNewAnswer() throws IOException, SQLException {
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());
        server.addController(new AddNewAnswerController(answerDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newAnswer",
                "questionId=1&optionId=2",
                "firstName=Siri"
        );
        assertEquals(303, postClient.getStatusCode());
        assertThat(answerDao.listAll())
                .extracting(Answer::getAnswerId)
                .contains(Long.valueOf("1"));
    }
}