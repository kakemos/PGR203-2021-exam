package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.sql.SQLException;

public interface HttpController {

    //Creating an interface since every controller uses both getPath() and handle()

    String getPath();

    HttpMessage handle(HttpMessage request) throws SQLException, IOException;
}
