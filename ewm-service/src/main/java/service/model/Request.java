package service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.enumarated.StatusUpd;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime created;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusUpd status;
}
