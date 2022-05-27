package coco.todo;

import java.util.ArrayList;
import java.util.List;

public class ToDoListService {

    List<ToDo> toDoList = new ArrayList<>();

    public ToDoListService(){
        initToDoList();
    }

    private void initToDoList() {
        toDoList.add(new ToDo("Sport"));
    }

    public List<ToDo> getToDoList() {
        return toDoList;
    }

    public void addToDo(String toDo){
        toDoList.add(new ToDo(toDo));
    }

    public void deleteToDo(String toDoToDelete) {
        int i = 0;
        for(ToDo toDo:toDoList){
            if (toDo.getToDo().equals(toDoToDelete) && i == 0){
                toDoList.remove(toDo);
                i++;
            }
        }
    }
}
