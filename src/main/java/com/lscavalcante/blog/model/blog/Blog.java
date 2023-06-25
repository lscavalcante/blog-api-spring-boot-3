package com.lscavalcante.blog.model.blog;

import com.lscavalcante.blog.model.comment.Comment;
import com.lscavalcante.blog.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@Entity(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Date createdAt;
    private Date updatedAt;
    @ManyToOne // many blogs can have only one user == (ForeignKey)
    private User user;
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
