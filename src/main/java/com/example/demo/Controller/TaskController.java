package com.example.demo.Controller;

import com.example.demo.Entity.DTO.TaskRequestDto;
import com.example.demo.Entity.DTO.TaskResponseDto;
import com.example.demo.Service.TaskService;
import com.example.demo.Service.TaskServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    // add CRUD methods
    // add ExceptionHandling
    // create custom exceptions NotFoundException, DataNotValidException, etc.
    // BONUS : Add Swagger documentation (OpenAPI)!
    @GetMapping("/")
    public ResponseEntity<?> getAllTasks() {

        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskRequestDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody TaskRequestDto taskDto){

        try {
            TaskResponseDto taskUpdated = taskService.updateTask(taskId,taskDto);
            return ResponseEntity.ok(taskUpdated);
        } catch (TaskServiceImpl.TaskNotFound e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId){
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (TaskServiceImpl.TaskNotFound e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.
                    status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An error has occurred during deleting the task");
        }
    }

}

