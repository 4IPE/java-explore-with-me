package service.model;

import lombok.Getter;
import lombok.Setter;
import service.enumarated.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String annotation;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Categories categories;
    @Column
    private Integer confirmedRequests;
    @Column
    private LocalDateTime createdOn;
    @Column
    private String description;
    @Column(nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column
    private Integer participantLimit;
    @Column
    private LocalDateTime publishedOn;
    @Column
    private Boolean requestModeration;
    @Column
    @Enumerated(value = EnumType.STRING)
    private State state;
    @Column(nullable = false)
    private String title;
    @Column
    private Integer views;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "event_compilation",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id")
    )
    private Set<Compilations> compilations;

    @PrePersist
    public void prePersist() {
        if (this.participantLimit == null) this.participantLimit = 0;
        if (this.requestModeration == null) this.requestModeration = false;
        if (this.paid == null) this.paid = false;
        if (this.views == null) this.views = 0;
    }

}
