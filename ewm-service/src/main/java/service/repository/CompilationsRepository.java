package service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import service.model.Compilations;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    List<Compilations> findByPinned(Boolean pinned, Pageable pageable);
}
