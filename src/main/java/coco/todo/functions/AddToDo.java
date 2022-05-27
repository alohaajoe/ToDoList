package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.Optional;

public class AddToDo implements HttpFunction {

    private ToDoListService toDoListService;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        response.setStatusCode(200);
        response.setContentType("text/plain");
        Optional<String> todo = request.getFirstQueryParameter("todo");
        var writer = response.getWriter();
        if (todo.isPresent()) {
            try {
                toDoListService.addToDo(todo.get());
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
}
