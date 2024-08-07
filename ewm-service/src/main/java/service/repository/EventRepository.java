package service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.enumarated.State;
import service.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long id, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("SELECT ev " +
            "FROM Event ev " +
            "WHERE ev.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (ev.initiator.id IN :users) " +
            "AND (ev.category.id IN :categories) " +
            "AND (ev.state IN :state) ")
    Page<Event> findAllEventWithState(@Param("rangeStart") LocalDateTime rangeStart,
                                      @Param("rangeEnd") LocalDateTime rangeEnd,
                                      @Param("users") List<Long> users,
                                      @Param("state") List<State> states,
                                      @Param("categories") List<Long> categories,
                                      Pageable pageable);

    @Query("SELECT ev " +
            "FROM Event ev " +
            "WHERE ev.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "AND (ev.state = :state)" +
            "AND (:paid=null OR ev.paid = :paid) " +
            "AND (ev.category.id IN :categories) " +
            "AND (:text = null OR LOWER(ev.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(ev.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:onlyAvailable = FALSE OR (ev.participantLimit = 0 OR ev.participantLimit > ev.confirmedRequests))")
    Page<Event> findAllEventWithStateForPub(@Param("rangeStart") LocalDateTime rangeStart,
                                            @Param("rangeEnd") LocalDateTime rangeEnd,
                                            @Param("text") String text,
                                            @Param("paid") Boolean paid,
                                            @Param("categories") List<Long> categories,
                                            @Param("onlyAvailable") Boolean onlyAvailable,
                                            @Param("state") State state,
                                            Pageable pageable);

}
