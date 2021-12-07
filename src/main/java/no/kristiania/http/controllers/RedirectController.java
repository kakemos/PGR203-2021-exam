package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.sql.SQLException;

public class RedirectController implements HttpController{
    @Override
    public String getPath() {
        return "/";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, IOException {
        return new HttpMessage("HTTP/1.1 303 See other", "Yeeey", "/index.html");
    }
}
