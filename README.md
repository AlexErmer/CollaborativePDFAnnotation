# CollaborativePDFAnnotation

Masterarbeit: Implementierung eines Workflows zur collaborativen PDF-Annotation


## Installation
Um das Projekt zu bauen ist maven notwendig.

Mit dem Befehl ```mvn clean install``` im Root-Verzeichnis des Projektes wird das Web-Archiv ````CollaborativePDFAnnotation-web.war```` gebaut.
Dieses kann in einem Servlet-Container wie Tomcat deployed werden kann.

Die Application läuft dann direkt im root des Containers. 
Das Dispatcher-Servlet selbst behandelt alle Anfragen auf den ````/pages/*```` Ordner.

