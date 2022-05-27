package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;

public class AddToDo implements HttpFunction {

    private ToDoListService toDoListService;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatusCode(200);
        response.setContentType("text/plain");
        var writer = response.getWriter();
        var todo = request.getFirstQueryParameter("todo");

        if (todo.isPresent()) {
            String toDoString = todo.get();
            try {
                //toDoListService.addToDo(toDoString);
                writer.write("You've added " + toDoString + " to Your To Do List.");
            }
            catch (Exception e) {
                writer.write("Sorry, " + toDoString + " could not been added to Your To Do List!" );
            }
        }
        else{
            writer.write("Please enter a To Do!" );
        }
    }
}
