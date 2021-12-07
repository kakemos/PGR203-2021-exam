package no.kristiania.http.controllers;

import no.kristiania.db.dao.AnswerDao;
import no.kristiania.db.objects.Answer;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.sql.SQLException;

public class ListAnswersController implements HttpController {

    private AnswerDao answerDao;

    public ListAnswersController(AnswerDao answerDao){
        this.answerDao = answerDao;
    }

    @Override
    public String getPath() {
        return "/api/answer";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        String response = "";

        for(Answer answer : answerDao.listAll()){
           response += answer.getQuestionId() + answer.getOptionId() + answer.getPersonId();
        }
        return new HttpMessage("Http/1.1 200 OK", response);
    }
}
