package coco.todo.functions;

import coco.todo.ToDoListController;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;

public class ShowToDoList implements HttpFunction {

    private ToDoListController toDoListController;

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        toDoListController.getToDoList();
        var writer = response.getWriter();
        writer.write("Aloha Dude");
        writer.write(toDoListController.getToDoList().toString());
    }
}
