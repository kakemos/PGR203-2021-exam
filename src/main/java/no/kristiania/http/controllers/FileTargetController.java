package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.HttpServer.query;

public class FileTargetController implements HttpController{
    @Override
    public String getPath() {
        return "/hello";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String yourName = "world";
        if (query != null) {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);
            yourName = queryMap.get("lastName") + ", " + queryMap.get("firstName");
        }
        String responseText = "<p>Hello " + yourName + "!</p>";

        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
