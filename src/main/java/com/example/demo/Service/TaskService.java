package com.example.demo.Service;

import com.example.demo.Entity.DTO.TaskRequestDto;
import com.example.demo.Entity.DTO.TaskResponseDto;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> getAllTasks() throws UserService.UserRetrievalException;
    TaskResponseDto createTask(TaskRequestDto taskDto) throws TaskServiceImpl.TaskNotFound;
    TaskResponseDto getTaskById(Long id) throws TaskServiceImpl.TaskNotFound;
    TaskResponseDto updateTask(Long id, TaskRequestDto taskDto) throws UserService.UserUpdateException;
    void deleteTask(Long id) throws UserService.UserDeleteException;
}