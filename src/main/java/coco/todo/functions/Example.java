package coco.todo.functions;

import com.google.cloud.functions.*;

import java.io.IOException;

public class Example implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
            response.setStatusCode(200);
            response.setContentType("text/plain");

            var writer = response.getWriter();

            var todo = request.getFirstQueryParameter("todo");
            if(todo.isPresent()){
                try{
                    writer.write("ToDo: " + todo.get());
                }
                catch (Exception e){
                    writer.write("Please provide a valid number.");
                }
            }
            else {
                writer.write("Please provide a number");
            }
    }
}
