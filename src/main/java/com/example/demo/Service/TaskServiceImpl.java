package com.example.demo.Service;

import com.example.demo.Entity.DTO.TaskRequestDto;
import com.example.demo.Entity.DTO.TaskResponseDto;
import com.example.demo.Entity.Task;
import com.example.demo.Repository.TaskRepo;
import com.example.demo.Utils.MappingProfile;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService{
    private final TaskRepo taskRepo;

    public class TaskNotFound extends RuntimeException{
        public TaskNotFound(String message){
            super(message);
        }
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(MappingProfile::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        var task = MappingProfile.mapToEntity(taskDto);
        return MappingProfile.mapToDto(taskRepo.save(task));
    }

    @Override
    public TaskResponseDto getTaskById(Long id) throws TaskNotFound {
        var task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFound("Task not found"));
        return MappingProfile.mapToDto(task);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskDto) throws TaskNotFound {
        var task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFound("Task not found"));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setId(taskDto.getId());
        task.setStatus(taskDto.getStatus());
        task.setDueDate(task.getDueDate());
        return MappingProfile.mapToDto(taskRepo.save(task));
    }

    @Override
    public void deleteTask(Long id) throws TaskNotFound {
        var task = taskRepo.findById(id).orElseThrow(() -> new TaskNotFound("Task not found"));
        taskRepo.delete(task);
    }
}
