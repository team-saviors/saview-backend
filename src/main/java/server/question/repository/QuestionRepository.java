package server.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import server.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByMainCategory(String mainCategory, Pageable pageable);
    Page<Question> findAllBySubCategory(String subCategory, Pageable pageable);
    Page<Question> findAllByMainCategoryAndSubCategory(String mainCategory, String subCategory, Pageable pageable);

    Page<Question> findByContentContaining(String keyword, Pageable pageable);
}
