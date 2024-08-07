package service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.dto.request.enumerated.StatusUpd;

import javax.persistence.*;
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
