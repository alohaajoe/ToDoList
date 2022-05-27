package coco.todo;

import java.util.ArrayList;
import java.util.List;

public class ToDoListService {

    List<String> toDoList = new ArrayList<>();

    public ToDoListService(){
        initToDoList();
    }

    private void initToDoList() {
        toDoList.add("sport");
        toDoList.add("backen");
    }

    public List<String> getToDoList() {
        return toDoList;
    }

    public void addToDo(String toDo){
        toDoList.add(toDo);
    }

    public void deleteToDo(String toDoToDelete) {
        int i = 0;
        for(String toDo:toDoList){
            if (toDo.equals(toDoToDelete) && i == 0){
                toDoList.remove(toDo);
                i++;
            }
        }
    }
}
