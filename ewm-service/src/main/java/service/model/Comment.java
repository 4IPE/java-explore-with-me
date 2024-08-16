package service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Table(name = "comments")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private User commentator;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column
    private LocalDateTime created;
    @Column
    private String text;
}
