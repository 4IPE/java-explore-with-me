package service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.model.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
