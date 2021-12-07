package no.kristiania;

import no.kristiania.db.objects.Answer;
import no.kristiania.db.objects.Option;
import no.kristiania.db.objects.Person;
import no.kristiania.db.objects.Question;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.Random;

public class TestData<T> {
    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:surveydb;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
    }

    private static Random random = new Random();

    public static String pickOne(String... alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }
    public static Integer pickOneInteger(Integer... alternatives) { return alternatives[random.nextInt(alternatives.length)];
    }


    public static Answer exampleAnswer(){
        Answer answer = new Answer();
        answer.setQuestionId(pickOneInteger(1, 2, 3, 4, 5));
        answer.setPersonId(pickOneInteger(1));
        answer.setOptionId(pickOneInteger(1, 2, 3, 4, 5));

        return answer;
    }

    public static Option exampleOption() {
        Option option = new Option();
        option.setOptionName(pickOne("Bra", "Fint", "Okei"));
        option.setQuestionId(pickOneInteger(1, 2, 3, 4));
        return option;
    }

    public static Person examplePerson() {
        Person person = new Person();
        person.setFirstName(pickOne("Johanne", "Jill", "Jane", "James", "Jacob", "Nora", "Emil", "Noah", "Emma", "Maja", "Oliver", "Filip", "Lukas","Liam", "Henrik", "Sofia", "Emilie"));
        person.setLastName(pickOne("Hansen", "Johansen", "Olsen", "Larsen", "Andersen", "Pedersen", "Nilsen", "Kristiansen", "Jensen", "Karlsen", "Johnsen"));
        person.setMailAddress(pickOne("romantic01@online.no", "crypt@gmail.com", "kramulous@comcast.net", "gomor@icloud.com", "dbrobins@att.net", "lampcht@online.net", "mleary@mac.com", "gward@verizon.net", "dexter@msn.com", "oiyou-47@mail.com", "essi389@mail.com", "brovade5@ymail.com", "tebei41@yopl.com", "pauda9@mail.com", "blomster@yahoo.com", "boot.32@gmail.com"));
        return person;
    }

    public static Question exampleQuestion() {
        Question question = new Question();
        question.setQuestionTitle(pickOne("Hvem er du?", "Har du det bra?", "Hvordan trives du?", "Hei"));
        question.setQuestionDescription(pickOne("Vi vil se hvordan du har det", "Vi vil se din tilstand", "Vi mene"));
        return question;
    }
}