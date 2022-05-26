package coco.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoListController {

    private final ToDoListService toDoListService;

    @Autowired
    public ToDoListController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    @GetMapping(value = "/ToDoList")
    public List<ToDo> getToDoList() {
        return toDoListService.getToDoList();
    }

    @PostMapping("/ToDoList/add")
    public void addToDo(@RequestParam("ToDo") String toDo){
        toDoListService.addToDo(toDo);
    }

    @DeleteMapping("/ToDoList/delete")
    public void deleteToDo(@RequestParam("ToDo") String toDo){
        toDoListService.deleteToDo(toDo);    }
}
