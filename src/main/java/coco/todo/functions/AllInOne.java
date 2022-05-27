package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;

public class AllInOne implements HttpFunction {

    private ToDoListService toDoListService;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = response.getWriter();
        if(request.getPath().equals("/ToDoList")){
            writer.write("Aloha Dude");
            //writer.write(toDoListService.getToDoList().toString());
        }
        else if(request.getPath().equals("/ToDoList/add")){
            var todo = request.getFirstQueryParameter("todo");
            if (todo.isPresent() && !todo.get().isBlank()) {
                String todoString = todo.get();
                try {
                    //toDoListService.addToDo(todoString);
                    writer.write("You've added " + todoString + " to Your To Do List.");
                }
                catch (IOException e) {
                    writer.write("Sorry, " + todoString + " could not been added to Your To Do List!" );
                }
            }
            else{
                writer.write("Please enter a To Do!" );
            }
        }
        else if(request.getPath().equals("/ToDoList/delete")){
            var todo = request.getFirstQueryParameter("todo");
            if (todo.isPresent() && !todo.get().isBlank()) {
                String toDoString = todo.get();
                try {
                    //toDoListService.deleteToDo(toDoString);
                    writer.write("You've deleted " + toDoString + " from Your To Do List.");
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
            writer.write("No Valid Input");
        }

    }
}
