package service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import service.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(Long eventId, Pageable pageable);

    List<Comment> findByCommentatorId(Long userId, Pageable pageable);

    Optional<Comment> findByIdAndCommentatorIdAndEventId(Long comId, Long userId, Long eventId);
}
