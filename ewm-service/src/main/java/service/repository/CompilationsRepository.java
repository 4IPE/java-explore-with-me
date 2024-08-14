package service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import service.model.Compilations;

import java.util.Optional;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    @EntityGraph(attributePaths = {"events"})
    Optional<Compilations> findById(Long id);

    @EntityGraph(attributePaths = {"events"})
    Page<Compilations> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"events"})
    Page<Compilations> findByPinned(Boolean pinned, Pageable pageable);
}

