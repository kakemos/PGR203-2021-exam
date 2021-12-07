package no.kristiania.http.controllers;

import no.kristiania.db.objects.Person;
import no.kristiania.db.dao.PersonDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class AddPersonController implements HttpController {
    private PersonDao personDao;

    public AddPersonController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public String getPath() {
        return "/api/setCookie";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Map<String, String> cookieQueryMap = HttpMessage.parseRequestParameters(request.getHeader("Cookie"));

        String firstName = URLDecoder.decode(cookieQueryMap.get("firstName"), StandardCharsets.UTF_8);
        String lastName = URLDecoder.decode(queryMap.get("lastName"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(queryMap.get("email"), StandardCharsets.UTF_8);

        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setMailAddress(email);

        // Using an if to check if the first name-input field is empty or null,
        // since only this field is required for the program. The two others are optional
        if (person.getFirstName() == null || person.getFirstName().isBlank()){
            return new HttpMessage(
                    "HTTP/1.1 400 request error",
                    "First name must be provided"
            );
        }

        personDao.insert(person);

        return new HttpMessage("HTTP/1.1 303 See other", "It is done", "/index.html");
    }
}
