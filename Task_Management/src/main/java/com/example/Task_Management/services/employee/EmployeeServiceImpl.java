package com.example.Task_Management.services.employee;

import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.dto.TaskDTO;
import com.example.Task_Management.entities.Comment;
import com.example.Task_Management.entities.Task;
import com.example.Task_Management.entities.User;
import com.example.Task_Management.enums.TaskStatus;
import com.example.Task_Management.repositories.CommentRepository;
import com.example.Task_Management.repositories.TaskRepository;
import com.example.Task_Management.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    @Override
    public List<TaskDTO> getTasksByUserId() {
        User user = jwtUtil.getLoggedInUser();
        if (user == null) throw new EntityNotFoundException("User not found");

        return taskRepository.findAllByUserId(user.getId()).stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(setTaskStatus(status));
            return taskRepository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    // ===============================
    // NEW METHODS LIKE ADMIN SIDE
    // ===============================

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();

        if (optionalTask.isPresent() && user != null) {
            Comment comment = new Comment();
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            comment.setContent(content);
            comment.setCreatedAt(new Date());
            return commentRepository.save(comment).getCommentDTo();
        }

        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream()
                .map(Comment::getCommentDTo)
                .collect(Collectors.toList());
    }

    // ===============================
    // Helper
    // ===============================
    private TaskStatus setTaskStatus(String status) {
        return switch (status) {
            case "Pending" -> TaskStatus.Pending;
            case "Inprogress" -> TaskStatus.Inprogress;
            case "Completed" -> TaskStatus.Completed;
            case "Deferred" -> TaskStatus.Deferred;
            default -> TaskStatus.Cancelled;
        };
    }
}
