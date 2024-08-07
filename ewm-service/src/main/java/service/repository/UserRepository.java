package service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u  " +
            "FROM User u " +
            "WHERE (u.id IN :ids) ")
    List<User> findAllUsersWithIds(@Param("ids") List<Long> ids,
                                   Pageable pageable);
}
