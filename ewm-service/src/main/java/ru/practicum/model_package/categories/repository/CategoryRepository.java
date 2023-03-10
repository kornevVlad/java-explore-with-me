package ru.practicum.model_package.categories.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model_package.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAll(Pageable pageable);
}
