package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.enumarated.StatusUpd;
import service.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEventId(Long eventId);

    List<Request> findByRequesterId(Long userId);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    @Query("SELECT COUNT(r) " +
            "FROM Request r " +
            "WHERE r.event.id =:event_id ")
    Integer countRequest(@Param("event_id") Long eventId);

    List<Request> findAllByEventIdAndStatus(Long eventId, StatusUpd status);
}
