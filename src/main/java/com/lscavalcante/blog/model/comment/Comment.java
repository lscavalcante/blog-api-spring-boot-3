package com.lscavalcante.blog.model.comment;

import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/*
    @ManyToOne: (ForeignKey)
    A anotação @ManyToOne é usada para indicar um relacionamento muitos-para-um entre duas entidades.
    Ela é colocada no campo/propriedade da entidade que representa o lado "muitos" do relacionamento.
    É comumente usado quando uma entidade possui uma referência para outra entidade.
    Por exemplo, se tivermos uma classe Comment que pertence a um único Blog, podemos usar @ManyToOne na classe Comment para mapear essa relação.
*/


/*
    @OneToMany: (ForeignKey)
    A anotação @OneToMany é usada para indicar um relacionamento um-para-muitos entre duas entidades.
    Ela é colocada no campo/propriedade da entidade que representa o lado "um" do relacionamento.
    É comumente usado quando uma entidade possui uma coleção de outras entidades.
    Por exemplo, se tivermos uma classe Blog que possui vários Comment, podemos usar @OneToMany na classe Blog para mapear essa relação.
*/


@Data
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @ManyToOne // many comment can have only one user == ForeignKey
    private User author;
    @ManyToOne // many comment can have only one blog == ForeignKey
    private Blog blog;
}
