package com.example.Task_Management.services.employee;

import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.dto.TaskDTO;

import java.util.List;

public interface EmployeeService {
   List<TaskDTO> getTasksByUserId();
  TaskDTO updateTask(long id,String status);
   TaskDTO getTaskById(Long id);
    CommentDTO createComment(Long taskId, String content);
    List<CommentDTO> getCommentsByTaskId(Long taskId);
}
