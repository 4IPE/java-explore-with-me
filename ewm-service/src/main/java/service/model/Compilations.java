package service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Compilations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "compilations", cascade = {CascadeType.MERGE})
    private List<Event> events;
    @Column
    private Boolean pinned;
    @Column
    private String title;
}
