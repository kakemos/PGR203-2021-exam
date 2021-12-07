package no.kristiania.http.controllers;

import no.kristiania.db.dao.PersonDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class editPersonController implements HttpController{
    private PersonDao personDao;

    public editPersonController(PersonDao personDao){
        this.personDao = personDao;
    }
    @Override
    public String getPath() {
        return "/api/editPerson";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        long person_id = Long.parseLong(queryMap.get("personId"));
        String newFirstName = URLDecoder.decode(queryMap.get("newFirstName"), StandardCharsets.UTF_8);
        String newLastName = URLDecoder.decode(queryMap.get("newLastName"), StandardCharsets.UTF_8);
        String newEmail = URLDecoder.decode(queryMap.get("newEmail"), StandardCharsets.UTF_8);

        //Using if else to check if some of the input fields are empty or if everyone are !=""
        if(newFirstName == ""){
            personDao.updateLastName(newLastName, person_id);
            personDao.updateEmail(newEmail, person_id);
        }else if(newLastName == ""){
            personDao.updateFirstName(newFirstName, person_id);
            personDao.updateEmail(newEmail, person_id);
        }else if(newEmail == ""){
            personDao.updateFirstName(newFirstName, person_id);
            personDao.updateLastName(newLastName, person_id);
        }else if(newFirstName != "" && newLastName != "" && newEmail != ""){
            personDao.updateFirstName(newFirstName, person_id);
            personDao.updateLastName(newLastName, person_id);
            personDao.updateEmail(newEmail, person_id);
        }

        return new HttpMessage("HTTP/1.1 303 see other", "A person has been edited", "http://localhost:1963/editSurvey.html" );
    }


}
