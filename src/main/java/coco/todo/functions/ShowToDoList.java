package coco.todo.functions;

import coco.todo.ToDoListService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;

public class ShowToDoList implements HttpFunction {

    private ToDoListService toDoListService;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = response.getWriter();
        writer.write("Aloha Dude");
        writer.write(toDoListService.getToDoList().toString());
    }
}
