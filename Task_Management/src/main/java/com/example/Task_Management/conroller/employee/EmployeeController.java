package com.example.Task_Management.conroller.employee;


import com.example.Task_Management.dto.CommentDTO;
import com.example.Task_Management.dto.TaskDTO;
import com.example.Task_Management.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;
    
    
    @GetMapping("/dashboard")
    public ResponseEntity<List<TaskDTO>> getTaskByUserId(){
       return ResponseEntity.ok(employeeService.getTasksByUserId());
    }

    @GetMapping("/updateTask/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,@PathVariable String status){
        TaskDTO updatedTaskDTO = employeeService.updateTask(id,status);
        if(updatedTaskDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updatedTaskDTO);
    }

     @GetMapping("/oneTask/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    // 2️⃣ Create Comment on Task
    @PostMapping("/createComment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long taskId,
            @RequestParam String content
    ) {
        CommentDTO createdComment = employeeService.createComment(taskId, content);
        if (createdComment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // 3️⃣ Get All Comments for Task
    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
    }

}
