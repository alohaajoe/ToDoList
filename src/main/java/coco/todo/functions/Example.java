package coco.todo.functions;

import com.google.cloud.functions.*;

import java.io.IOException;

public class Example implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        var writer = response.getWriter();
        writer.write("Aloha Dude");
    }
}
