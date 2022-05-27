package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.List;

public class ShowToDoList implements HttpFunction {

    private final ToDoListService toDoListService = new ToDoListService();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = response.getWriter();
        writer.write("Aloha Dude\n");
        List<String> toDoList = toDoListService.getToDoList();
        int size = toDoList.size();
        writer.write("This is the size of the ToDoList " + size + "\n");
        for(String todo:toDoList){
            writer.write(todo + "\n");
        }
    }
}
