package service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class Compilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "compilations", cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    private Set<Event> events;
    @Column
    private Boolean pinned;
    @Column
    private String title;
}
