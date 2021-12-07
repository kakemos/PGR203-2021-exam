# PGR203 Avansert Java eksamen

[![.github/workflows/maven.yml](https://github.com/kristiania-pgr203-2021/pgr203-exam-mgrinaker/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/kristiania-pgr203-2021/pgr203-exam-mgrinaker/actions/workflows/maven.yml)

## Github repository
https://github.com/kristiania-pgr203-2021/pgr203-exam-mgrinaker

## Hvordan man kjører programmet
Først må man bygge en executable jar-fil:
* Kjør en mvn clean
* Kjør så en mvn package
* Så er det viktig å lage en pgr203.properties" med: 
  * dataSource.password=passordet til database eieren
  * dataSource.url=jdbc:postgresql://localhost:5432/'navnet på databasen'
  * dataSource.username=brukernavnet til eieren
* Skriv deretter i terminalen: java -Dfile.encoding="UTF-8" -jar target/pgr203-exam-mgrinaker-1.0-SNAPSHOT.jar

Programmet skal brukes via en browser, der vi selv kun har brukt browseren Google Chrome. 
Man starter med å gå til "http://localhost:1963/index.html".
Der vil aller først få opp en form der man skal skrive inn fornavn, etternavn og epost. Gjør man ikke dette,
vil det ikke være mulig å svare på spørsmål, da man trenger å registrere brukeren før man kan registrere et svar.
Serveren vil altså krasje hvis den ikke får tak i en bruker.
Etter at brukeren er registrert, kommer man til en rekke spørsmål som skal besvares med svaralternativer. Man trykker så 
på Submit answer-knappen for hvert spørsmål for å registrere sitt svar.
Hvis man ønsker å redigere et spørsmål er det mulighet for det i navigationbaren rett under overskriften. 
Der ser man også at det er mulighet for å legge til flere svaralternativer på et spørsmål hvis man ønsker det.


## Datamodell
![](docs/datamodell.png)
![](docs/plantUML.png)

## Ekstra leveranse utover minimum
Første gang man besøker undersøkelsen får man opp tre inputfelt der man må skrive inn og lagre opplysnigner om seg selv.

Hver gang brukeren har lagt inn ett nytt spørsmål eller ett nytt option blir brukeren sendt tilbake til index.html.
Dette ble gjort ved at vi gjorde startLine til "HTTP/1.1 303 See other" og location til "http://localhost:1963/index.html".

Vi har lagt inn UTF-8-decoding på alle inputfelt, sånn at både Æ, Ø, Å og @ skal bli lest inn og tolka riktig. Likevel vil
ikke ÆØÅ funke når man kjører jar-fila, så derfor skal man kjøre java -Dfile.encoding="UTF-8" -jar target/pgr203-exam-mgrinaker-1.0-SNAPSHOT.jar
når man kjører jar-fila for å få de med.

Der vi legger til brukerinformasjon i starten av surveyen har vi gjort sånn at navnet til brukeren lagres som en cookie.
Senere bruker vi denne cookien når brukeren registrerer svarene sine i surveyen, og kobler den opp mot personId i person-databasen.

I HttpServer har vi kode som leser filer fra disc, der den sjekker fil-endingene for å avgjøre hvilken content-type som
skal brukes. Det gjør det mulig å laste inn både .txt, .html og .css-filer riktig.

En abstractDao ble laget for å ikke få duplisert kode. Der har vi brukt generics for at de andre klassene kan få bruke 
metodene. Der har vi insert(), retrieve(), listAll() og update(). Vi har med update() i PersonDao, men de brukes ikke i
løsningen. Vi valge likevel å ta de med så vi skulle ha mulighet til å bruke de om det trengtes.

Hvis handleClient får "HTTP/1.1 500 Internal Server Error" så vil den skrive ut "Statuscode 500" til brukeren for å 
fortelle at serveren har kræsjet.
I tillegg har det blitt lagt inn at hvis brukeren skriver inn kun "/" eller har det blankt rett bak "localhost:1963" 
så vil de ble sendt rett til index.html.

I tab-vinduet har vi lagt inn ett lite favicon. Dette favicon ble kodet inn i alle html-filer i headeren.

Under datamodell har vi en illustrasjon over hvordan databasen er satt opp, og noen diagrammer over hvordan programmet vårt fungerer.
Vi føler vi har fått med oss de aller viktigste tabellene som trengs for dette prosjektet. Vi prøvde oss i starten med to
ekstra tabeller, workplace og profession, men syntes disse ble overflødige og vi valgte heller å fokusere på å forbedre
andre aspekter i prosjektet enn å ha en unødvendig komplisert datamodell.

## Sjekkliste

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lest eksamensteksten
* [x] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/pgr203-2021-repository
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [x] `README.md` inneholder en korrekt link til Github Actions
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [x] `README.md` inneholder et diagram som viser datamodellen

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Serveren leser HTML-filer fra JAR-filen slik at den ikke er avhengig av å kjøre i samme directory som kildekoden
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet kan opprette spørsmål og lagrer disse i databasen (påkrevd for bestått)
* [x] Programmet kan vise spørsmål (påkrevd for D)
* [x] Programmet kan legge til alternativ for spørsmål (påkrevd for D)
* [x] Programmet kan registrere svar på spørsmål (påkrevd for C)
* [x] Programmet kan endre tittel og tekst på et spørsmål (påkrevd for B)

### Ekstraspørsmål (dere må løse mange/noen av disse for å oppnå A/B)

* [x] Før en bruker svarer på et spørsmål hadde det vært fint å la brukeren registrere navnet sitt. Klarer dere å implementere dette med cookies? Lag en form med en POST request der serveren sender tilbake Set-Cookie headeren. Browseren vil sende en Cookie header tilbake i alle requester. Bruk denne til å legge inn navnet på brukerens svar
* [x] Når brukeren utfører en POST hadde det vært fint å sende brukeren tilbake til dit de var før. Det krever at man svarer med response code 303 See other og headeren Location
* [x] Når brukeren skriver inn en tekst på norsk må man passe på å få encoding riktig. Klarer dere å lage en <form> med action=POST og encoding=UTF-8 som fungerer med norske tegn? Klarer dere å få æøå til å fungere i tester som gjør både POST og GET?
* [ ] Å opprette og liste spørsmål hadde vært logisk og REST-fult å gjøre med GET /api/questions og POST /api/questions. Klarer dere å endre måten dere hånderer controllers på slik at en GET og en POST request kan ha samme request target?
* [x] Vi har sett på hvordan å bruke AbstractDao for å få felles kode for retrieve og list. Kan dere bruke felles kode i AbstractDao for å unngå duplisering av inserts og updates?
* [x] Dersom noe alvorlig galt skjer vil serveren krasje. Serveren burde i stedet logge dette og returnere en status code 500 til brukeren
* [x] Dersom brukeren går til http://localhost:8080 får man 404. Serveren burde i stedet returnere innholdet av index.html
* [x] Et favorittikon er et lite ikon som nettleseren viser i tab-vinduer for en webapplikasjon. Kan dere lage et favorittikon for deres server? Tips: ikonet er en binærfil og ikke en tekst og det går derfor ikke an å laste den inn i en StringBuilder
* [ ] I forelesningen har vi sett på å innføre begrepet Controllers for å organisere logikken i serveren. Unntaket fra det som håndteres med controllers er håndtering av filer på disk. Kan dere skrive om HttpServer til å bruke en FileController for å lese filer fra disk?
* [x] Kan dere lage noen diagrammer som illustrerer hvordan programmet deres virker?
* [ ] JDBC koden fra forelesningen har en feil ved retrieve dersom id ikke finnes. Kan dere rette denne?
* [x] I forelesningen fikk vi en rar feil med CSS når vi hadde `<!DOCTYPE html>`. Grunnen til det er feil content-type. Klarer dere å fikse det slik at det fungerer å ha `<!DOCTYPE html>` på starten av alle HTML-filer?
* [ ] Klarer dere å lage en Coverage-rapport med GitHub Actions med Coveralls? (Advarsel: Foreleser har nylig opplevd feil med Coveralls så det er ikke sikkert dere får det til å virke)
* [ ] FARLIG: I løpet av kurset har HttpServer og tester fått funksjonalitet som ikke lenger er nødvendig. Klarer dere å fjerne alt som er overflødig nå uten å også fjerne kode som fortsatt har verdi? (Advarsel: Denne kan trekke ned dersom dere gjør det feil!)

Laget av Martine Grinaker,Tonje Husvik, Cathrine Jakobsen