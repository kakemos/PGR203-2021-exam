package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.db.dao.QuestionDao;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class EditQuestionController implements HttpController{
    private QuestionDao questionDao;

    public EditQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public String getPath() {
        return "/api/editQuestion";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        long questionTitle = Long.parseLong(queryMap.get("questionTitle"));
        String newTitle = URLDecoder.decode(queryMap.get("newTitle"), StandardCharsets.UTF_8);
        String newDescription = URLDecoder.decode(queryMap.get("newDescription"), StandardCharsets.UTF_8);

        //Using if else to check if som of the input fields are empty.
        if(newTitle == ""){
            questionDao.updateQuestionDescription(newDescription, questionTitle);
        }else if(newDescription == ""){
            questionDao.updateQuestionTitle(newTitle, questionTitle);
        }else if(newTitle != "" && newDescription != "") {
            questionDao.updateQuestionTitle(newTitle, questionTitle);
            questionDao.updateQuestionDescription(newDescription, questionTitle);
        }

        return new HttpMessage("HTTP/1.1 303 see other", "A question has been edited to: " + newTitle + ": " + newDescription, "http://localhost:1963/editSurvey.html");
    }
}
