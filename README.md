# Allgemeines

Der Server läuft auf Port 8080.
Benutzung:

``` 
    java -jar server.jar <Ordner, in dem die hochgeladenen Bilder gespeichert werden>
```

# Datenbank

Damit der Server funktioniert, muss auf Port 3306 eine MySQL-Datenbank
mit dem Namen "cirrus" laufen.
Die Datenbankstruktur wird beim ersten Start angelegt.

Username: cirrus

Passwort: cirrusPasswort

# Datentypen
Valide Datentypen sind: 
  - height
  - temperature
  - latitude
  - longitude
  - pressure
  - dust
  - voltage
  - internal_temperature

Der Wert muss jeweils als Double vorliegen.

# REST

## GET-Requests

Liste aller URLs:

  - [/picture](/picture) sendet das neueste Bild zurück

  - [/pictures/:id](/pictures/:id) sendet das Bild mit der ID :id zurück

  - [/pictures](/pictures) sendet eine Liste aller Bilder als JSON
    zurück

  - [/data?time=:time\&type=:type](/data?time=:time&type=:type) sendet
    eine Liste aller Messdaten vom Typ :type, die nach :time erstellt
    wurden, als JSON zurück. Werden einer oder mehrere der Parameter
    nicht gesetzt, werden sie nicht beachtet. :time hat das Format
    "yyyy-mm-dd hh:mm:ss".

## POST-Requests

Für POST-Requests muss der Request-Parameter "token=authToken" mitgesendet werden. Liste aller URLs:

  - [/data?type=:type\&value=:value](/data?type=:type&value=:value) fügt
    ein Messdatum vom Typ :type mit Wert :value mit der aktuellen Zeit
    in die Datenbank ein

  - [/picture](/picture) lädt ein Bild hoch. Eine HTML-form, die das
    macht, muss enctype="multipart/form-data" als Attribut haben. Der
    Name des Dateiparameters in der Request muss "picture" lauten.

# WebSockets

## Senden

### Messdaten

Der Websocket zum Senden von Messdaten ist unter
[/sendData](/sendData) zu erreichen. Das Protokoll ist nicht
[http(s)://](http\(s\)://), sondern <ws://>. Eine Nachricht muss von
folgendem Format sein:

``` 
    <Datentyp> <Wert>
```

### Bilder

Der Websocket zum Senden von Bildern ist unter
[/sendPictures](/sendPictures) zu erreichen. Das Protokoll ist nicht
[http(s)://](http\(s\)://), sondern <ws://>. Das Bild kann entweder binär oder als Base64-dekodierte Textnachricht versendet werden.

## Empfangen

Die Websockets zum Empfangen werden sofort nach Upload neuer Daten auf
den Server über die Daten benachrichtigt.

### Messdaten

Der Websocket zum Empfangen von Messdaten ist unter
[/receiveData](/receiveData) zu erreichen. Das Protokoll ist nicht
[http(s)://](http\(s\)://), sondern <ws://>. Der Server sendet hierbei die Daten im JSON-Format. Eine Nachricht enthält ID, Typ, Wert, Zeit, Zeit als Millisekunden.

### Bilder

Der Websocket zum Empfangen von Bildern ist unter
[/receivePictures](/receivePictures) zu
erreichen. Das Protokoll ist nicht [http(s)://](http\(s\)://), sondern <ws://>. Eine Nachricht im WebSocket enthält Bild-ID, Datum und Dateityp als JSON.

# Web-Client
Der WebClient ist direkt unter [localhost:8080](http://localhost:8080) erreichbar.
