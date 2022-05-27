package coco.todo.functions;

import coco.todo.ToDoListController;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.Optional;

public class AllInOne implements HttpFunction {

    private ToDoListController toDoListController;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = response.getWriter();
        if(request.getPath().equals("/ToDoList")){
            toDoListController.getToDoList();
            writer.write("Aloha Dude");
            writer.write(toDoListController.getToDoList().toString());
        }
        else if(request.getPath().equals("/ToDoList/add")){
            Optional<String> todo = request.getFirstQueryParameter("todo");
            if (todo.isPresent()) {
                try {
                    toDoListController.addToDo(todo.get());
                    writer.write("You've added " + todo + "to Your To Do List.");
                }
                catch (IOException e) {
                    writer.write("Sorry, " + todo + "could not been added to Your To Do List!" );
                }
            }
            else{
                writer.write("Please enter a To Do!" );
            }
        }
        else if(request.getPath().equals("/ToDoList/delete")){
            Optional<String> todo = request.getFirstQueryParameter("todo");
            if (todo.isPresent()) {
                try {
                    toDoListController.deleteToDo(todo.get());
                    writer.write("You've deleted " + todo + "from Your To Do List.");
                }
                catch (IOException e) {
                    writer.write("Sorry, " + todo + "could not been deleted from Your To Do List!" );
                }
            }
            else{
                writer.write("Please enter a To Do to delete!" );
            }
        }
        else{
            writer.write("No Valid Input");
        }

    }
}
