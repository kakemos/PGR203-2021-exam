package no.kristiania.http.controllers;

import no.kristiania.db.objects.Answer;
import no.kristiania.db.dao.AnswerDao;
import no.kristiania.db.objects.Person;
import no.kristiania.db.dao.PersonDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class AddNewAnswerController implements HttpController{
    private AnswerDao answerDao;

    public AddNewAnswerController(AnswerDao answerDao){
        this.answerDao = answerDao;
    }

    @Override
    public String getPath() {
        return "/api/newAnswer";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Map<String, String> cookieQueryMap = HttpMessage.parseRequestParameters(request.getHeader("Cookie"));

        String cookieName = URLDecoder.decode(cookieQueryMap.get("firstName"), StandardCharsets.UTF_8);
        Person name = PersonDao.listOutPersonFromCookieName(cookieName);

        long firstName = name.getPersonId();
        String question = queryMap.get("questionId");
        String option = queryMap.get("optionId");

        Answer answer = new Answer();
        answer.setPersonId(firstName);
        answer.setQuestionId(Long.parseLong(question));
        answer.setOptionId(Long.parseLong(option));

        answerDao.insert(answer);
        return new HttpMessage("HTTP/1.1 303 See other", "It is done", "http://localhost:1963/index.html");
    }
}
