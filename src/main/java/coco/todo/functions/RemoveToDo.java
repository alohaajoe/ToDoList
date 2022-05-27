package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.List;

public class RemoveToDo implements HttpFunction {

    private final ToDoListService toDoListService = new ToDoListService();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var todo = request.getFirstQueryParameter("todo");
        var writer = response.getWriter();
        if (todo.isPresent()) {
            String toDoString = todo.get();
            try {
                List<String> toDoList = toDoListService.getToDoList();
                int c = 0;

                for (String toDo : toDoList) {
                    c++;
                    if(c==toDoList.size()){
                        writer.write("You've got no " + toDoString + " in Your To Do List.");
                    }
                    else if (toDo.equals(toDoString)) {
                        toDoListService.deleteToDo(toDoString);
                        writer.write("You've deleted " + toDoString + " from Your To Do List.");
                        break;
                    }
                }
            }
            catch (Exception e) {
                writer.write("Sorry, " + toDoString + " could not been deleted from Your To Do List!" );
            }
        }
        else{
            writer.write("Please enter a To Do to delete!" );
        }
    }
}
