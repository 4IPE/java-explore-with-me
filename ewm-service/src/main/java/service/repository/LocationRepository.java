package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
