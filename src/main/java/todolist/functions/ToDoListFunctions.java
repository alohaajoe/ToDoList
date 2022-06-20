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
