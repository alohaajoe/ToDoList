https://cloud.google.com/functions/docs/create-deploy-http-java?hl=de
https://cloud.google.com/functions/docs/writing?hl=de#http_functions

http://localhost:8080/swagger-ui/index.htm

-------------------- C L O U D - S H E L L ------------------------------------------------

Zunächst müssen wir ein neues Projekt erstellen.
Dazu geben wir in der Cloud Shell fonlgendes ein:

	(gcloud projects list)
	gcloud projects create ToDoList-${CAMPUS_ID}-01

--------------------------------------------------------------------------------------------

Mit

	gcloud config list

lassen wir uns die Konfigurationen des Projekts ausgeben, indem wir uns gerade befinden.
Falls wir dort keine Zeile ausfündig machen können die

	project = ToDoList-${CAMPUS_ID}-01

beinhaltet, wechseln wir zu dem Projekt, indem wir folgendes eingeben:

	gcloud config set project ToDoList-${CAMPUS_ID}-01

wir können danach erneut "gcloud config list" eingeben und sollten dann eine Zeile mit
"project = ToDoList-${CAMPUS_ID}-01" finden.

In der Umgebungsvariable DEVSHELL_PROJECT_ID der Cloud Shell ist außerdem die Projekt-ID unseres Projekts gespeichert.

--------------------------------------------------------------------------------------------

Nun müssen wir noch unseren Billing Account mit dem Projekt verbinden.
Immerhin kostet das ganze Geld und darüber wird dann die Abrechnung erfolgen.

	gcloud beta billing projects link ${DEVSHELL_PROJECT_ID} --billing-account=<AccountID>

	(gcloud beta billing projects link ${DEVSHELL_PROJECT_ID} --billing-account=01CE01-D50165-8527AF)

Es bietet sich an, die Abrechnung zu stoppen:
gcloud beta billing projects unlink ${DEVSHELL_PROJECT_ID}

--------------------------------------------------------------------------------------------

Wir setzen noch die region und Zeitzone

	gcloud config set compute/region europe-west3
	gcloud config set compute/zone europe-west3-c

Außerdem aktivieren noch noch die cloudfunktion und die cloudbuild API.
Diese werden zum erzeugen von CLoud Functions benötigt.

	gcloud services enable cloudfunctions.googleapis.com
	gcloud services enable cloudbuild.googleapis.com


-------------------- A U F  D E M  P C -----------------------------------------

Wir erstellen zunächst einen neuen Ordner lokal auf unserem PC, indem sich das Java Projekt befindet.

	mkdir ~/ToDoList
Mit
cd ~/ToDoList
wechseln wir in den neuen Ordner.

Nun kümmern wir uns um die Programmstruktur und legen dort Zwei Java-Klassen und eine pom.xml an.

	touch pom.xml
	mkdir -p src/main/java/todolist/functions
	touch src/main/java/todolist/functions/ToDoListFunctions.java
	mkdir -p src/main/java/todolist/service
	touch src/main/java/todolist/service/ToDoListService.java

Wir öffnen die noch leeren Dateien im Nano-Text-Editor. Alternativ geht das natürlich auch über beispielsweise VS Code oder IntelliJ.

	nano src/main/java/todolist/service/ToDoListService.java

Dort dann Folgendes rein kopieren:


	package todolist.service;

	import java.util.ArrayList;
	import java.util.List;

	public class ToDoListService {

        List<String> toDoList = new ArrayList<>();

        public ToDoListService(){
            initToDoList();
        }

        private void initToDoList() {
            toDoList.add("sport");
            toDoList.add("backen");
        }

        public List<String> getToDoList() {
            return toDoList;
        }

        public void addToDo(String toDo){
            toDoList.add(toDo);
        }

        public void deleteToDo(String toDoToDelete) {
            int i = 0;
            for(String toDo:toDoList){
                if (toDo.equals(toDoToDelete) && i == 0){
                        toDoList.remove(toDo);
                        i++;
                }
            }
        }
	}


Diese Klasse stellt die wesentlichen Funktionen der To Do List bereit.
Außerdem wird im Konstriktor, die Methode initToDoList aufgerufen, die schon "sport" und "backen" zur Liste hinzufügt.

ToDoListFunctions.java

	nano src/main/java/todolist/functions/ToDoListFunctions.java

Dort dann Folgendes rein kopieren:


	package todolist.functions;

	import todolist.service.ToDoListService;

	import com.google.cloud.functions.HttpFunction;
	import com.google.cloud.functions.HttpRequest;
	import com.google.cloud.functions.HttpResponse;

	import java.io.IOException;
	import java.util.List;

	public class ToDoListFunctions implements HttpFunction {

        private final ToDoListService toDoListService = new ToDoListService();

        @Override
        public void service(HttpRequest request, HttpResponse response) throws IOException {
            var writer = response.getWriter();
            List<String> toDoList = toDoListService.getToDoList();

            if(request.getPath().equals("/add")){

                var todo = request.getFirstQueryParameter("todo");

                if (todo.isPresent() && !todo.get().isBlank()) {
                        String toDoString = todo.get();
                    try {
                            int c = 0;
                            for (String toDo : toDoList) {
                                c++;
                                if (toDo.equals(toDoString)) {
                                        writer.write("You've got " + toDoString + " already in Your To Do List.");
                                        break;
                                }
                                else if(c==toDoList.size()){
                                        toDoListService.addToDo(toDoString);
                                        writer.write("You've added " + toDoString + " to Your To Do List.");
                                }
                            }
                        }
                        catch (IOException e) {
                            writer.write("Sorry, " + toDoString + " could not been added to Your To Do List!" );
                        }
                }
                else{
                        writer.write("Please enter a To Do to add!" );
                }
            }

            else if(request.getPath().equals("/delete")){
                var todo = request.getFirstQueryParameter("todo");
                if (todo.isPresent() && !todo.get().isBlank()) {
                        String toDoString = todo.get();
                        try {
                            int c = 0;
                            for (String toDo : toDoList) {
                                c++;
                                if (toDo.equals(toDoString)) {
                                        toDoListService.deleteToDo(toDoString);
                                        writer.write("You've deleted " + toDoString + " from Your To Do List.");
                                        break;
                                }
                                else if(c==toDoList.size()){
                                        writer.write("You've got no " + toDoString + " in Your To Do List.");
                                }
                            }
                        }
                        catch (IOException e) {
                            writer.write("Sorry, " + toDoString + " could not been deleted from Your To Do List!" );
                    }
                }
                else{
                        writer.write("Please enter a To Do to delete!" );
                }
            }

            else{
                writer.write("Your To Do List: \n");
                for(String todo:toDoList){
                        writer.write(todo + "\n");
                }
            }
        }
	}



Hier passiert die Zuordnung der einzelnen Funktionen der ToDoListService Klasse.

pom.xml

	nano pom.xml

Dort dann Folgendes rein kopieren:

	<project xmlns="http://maven.apache.org/POM/4.0.0"
    	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    		   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  		<modelVersion>4.0.0</modelVersion>

  		<groupId>todolist</groupId>
  		<artifactId>ToDoList</artifactId>
  		<version>1.0.0-SNAPSHOT</version>

  		<properties>
            <maven.compiler.target>11</maven.compiler.target>
            <maven.compiler.source>11</maven.compiler.source>
  		</properties>

  		<dependencies>
            <!-- Required for Function primitives -->
            <dependency>
            <groupId>com.google.cloud.functions</groupId>
            <artifactId>functions-framework-api</artifactId>
            <version>1.0.4</version>
            <scope>provided</scope>
            </dependency>
  		</dependencies>

  		<build>
            <plugins>
      			<plugin>
                    <!--
                    Google Cloud Functions Framework Maven plugin

                    This plugin allows you to run Cloud Functions Java code
                    locally. Use the following terminal command to run a
                    given function locally:

                    mvn function:run -Drun.functionTarget=your.package.yourFunction
                    -->
                    <groupId>com.google.cloud.functions</groupId>
                    <artifactId>function-maven-plugin</artifactId>
                    <version>0.10.0</version>
                    <configuration>
                        <functionTarget>todolist.functions.ToDoListFunctions</functionTarget>
                    </configuration>
      			</plugin>
            </plugins>
  		</build>
	</project>


Mit dem Google Cloud Functions Framework Maven Plugin, kann man das Programm loacal testen

	mvn function:run

In einem neuen Terminal Fenster

	curl localhost:8080

Oder im browser localhost:8080 aufrufen. Es sollte eine To Do Liste mit sport und backen zu sehen sein.

Mit Strg/Control + C kann das Programm angehalten werden.

	mvn clean 

Dann kann es hochgeladen werden.

-------------------------------------------------------------------------------------------------------------------

	cd todolist/

	gcloud functions deploy todolist-function --entry-point todolist.functions.ToDoListFunctions --runtime java11 --trigger-http --memory 512MB --allow-unauthenticated --region europe-west3

Wenn alles erfolgreich war, sehen wir auch die URL die wir aufrufen können um die Function zu triggern

	httpsTrigger:
  		securityLevel: SECURE_ALWAYS
  		url: https://europe-west3-ToDoList-jhaseloh-01.cloudfunctions.net/todolist-function

Im Browser: https://us-central1-function-jhaseloh-01.cloudfunctions.net/todolist-function

Oder auf dem PC auf der Konsole:

	curl https://us-central1-function-jhaseloh-01.cloudfunctions.net/todolist-function

	gcloud functions logs read tolist-function
