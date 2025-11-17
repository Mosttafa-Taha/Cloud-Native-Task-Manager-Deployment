package com.example.Task_Management.entities;

import com.example.Task_Management.dto.CommentDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="task_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    public CommentDTO getCommentDTo(){
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setId(id);
    commentDTO.setContent(content);
    commentDTO.setCreatedAt(createdAt);
    commentDTO.setTaskId(task.getId());
    commentDTO.setUserId(user.getId());

    // Set the username to show in frontend
    if(user != null){
        commentDTO.setPostedBy(user.getName()); 
    }

    return commentDTO;
}

}
