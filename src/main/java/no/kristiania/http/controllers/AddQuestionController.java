package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.db.objects.Question;
import no.kristiania.db.dao.QuestionDao;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class AddQuestionController implements HttpController {
    private QuestionDao questionDao;


    public AddQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public String getPath() {
        return "/api/newQuestion";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Question question = new Question();

        String questionTitle = URLDecoder.decode(queryMap.get("questionTitle"), StandardCharsets.UTF_8);
        String questionDescription = URLDecoder.decode(queryMap.get("questionDescription"), StandardCharsets.UTF_8);

        question.setQuestionTitle(questionTitle);
        question.setQuestionDescription(questionDescription);


        //Using if else to check if some of the input fields are empty or null

        if ((question.getQuestionTitle() == null || question.getQuestionTitle().isBlank()) &&
                (question.getQuestionDescription() == null || question.getQuestionDescription().isBlank())){
            return new HttpMessage(
                    "HTTP/1.1 400 request error",
                    "Both title and description must be provided"
            );
        } else if (question.getQuestionDescription() == null || question.getQuestionDescription().isBlank()){
            return new HttpMessage(
                    "HTTP/1.1 400 request error",
                    "Description must be provided"
            );
        } else if ((question.getQuestionTitle() == null || question.getQuestionTitle().isBlank())){
            return new HttpMessage(
                    "HTTP/1.1 400 request error",
                    "Title must be provided"
            );
        }
        questionDao.insert(question);

        return new HttpMessage("HTTP/1.1 303 See other", "It is done", "http://localhost:1963/index.html" );
    }
}
