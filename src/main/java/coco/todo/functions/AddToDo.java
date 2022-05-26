package coco.todo.functions;

import coco.todo.ToDoListController;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.Optional;

public class AddToDo implements HttpFunction {

    private ToDoListController toDoListController;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Optional<String> todo = request.getFirstQueryParameter("todo");
        var writer = response.getWriter();
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
}
