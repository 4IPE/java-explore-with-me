package service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.model.Compilations;

import java.util.List;
import java.util.Optional;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    @Query("SELECT c FROM Compilations c LEFT JOIN FETCH c.events WHERE c.id = :id")
    Optional<Compilations> findByIdWithEvents(@Param("id") Long id);

    @Query("SELECT c FROM Compilations c LEFT JOIN FETCH c.events")
    List<Compilations> findAllWithEvents(Pageable pageable);

    @Query("SELECT c FROM Compilations c LEFT JOIN FETCH c.events WHERE c.pinned = :pinned")
    List<Compilations> findByPinnedWithEvents(@Param("pinned") Boolean pinned, Pageable pageable);
}

