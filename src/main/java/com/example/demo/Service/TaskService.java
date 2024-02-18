package com.example.demo.Service;

import com.example.demo.Entity.DTO.TaskRequestDto;
import com.example.demo.Entity.DTO.TaskResponseDto;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> getAllTasks();
    TaskResponseDto createTask(TaskRequestDto taskDto);
    TaskResponseDto getTaskById(Long id) throws TaskServiceImpl.TaskNotFound;
    TaskResponseDto updateTask(Long id, TaskRequestDto taskDto) throws Exception;
    void deleteTask(Long id) throws Exception;
}