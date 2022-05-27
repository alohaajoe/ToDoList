package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;

public class RemoveToDo implements HttpFunction {

    private ToDoListService toDoListService;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var todo = request.getFirstQueryParameter("todo");
        var writer = response.getWriter();
        if (todo.isPresent()) {
            String toDoString = todo.get();
            try {
                //toDoListService.deleteToDo(todo.get());
                writer.write("You've deleted " + toDoString + "from Your To Do List.");
            }
            catch (Exception e) {
                writer.write("Sorry, " + toDoString + "could not been deleted from Your To Do List!" );
            }
        }
        else{
            writer.write("Please enter a To Do to delete!" );
        }
    }
}
