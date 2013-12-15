# CollaborativePDFAnnotation

Masterarbeit: Implementierung eines Workflows zur collaborativen PDF-Annotation


## Konfiguration
Es ist nötig einige Konfigurationen vorzunehmen:

### brat konfigurieren
Speichern Sie die Datei ````brat/web/WEB-INF/cgi/brat/config_template.py```` im selben Ordner unter dem Dateinamen ````config.py```` (diese Datei wird von git ignoriert).
In der Datei müssen dann BASE_DIR, DATA_DIR und WORK_DIR gepflegt werden.
Ausserdem muss ein Login angelegt werden, zum Beispiel so:
````
USER_PASSWORD = {
    'editor': 'annotate'
}
````

### java Applikation konfiguieren
Alle Einstellungen werden aus der Datei ````common\src\main\resources\config.conf```` geladen und können dort geändert werden.
Es ist hier darauf zu achten, dass die Property BRAT_WORKING_PATH hier mit der Konfiguration von Brat selbst übereinstimmt.

## Installation
Um das Projekt zu bauen ist maven notwendig.

Bevor es gebaut werden kann ist von OpenNLP der aktuelle Snapshot (Version 1.6.0) notwendig (dieser stellt Konverter zu den Annotationen von brat rapid annotation tool bereit). Der von mir verwendete Build ist als Archiv im Verzeichnis ````/lib/```` zu finden und kann in das lokale Maven-Repository entpackt werden.

Mit dem Befehl ```mvn clean install``` im Root-Verzeichnis des Projektes wird das Web-Archiv ````CollaborativePDFAnnotation-web.war```` gebaut.
Dieses kann in einem Servlet-Container wie Tomcat deployed werden kann.

Die Application läuft dann direkt im root des Containers. 
Das Dispatcher-Servlet selbst behandelt alle Anfragen auf den ````/pages/*```` Ordner.

