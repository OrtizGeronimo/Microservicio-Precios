package precio.precios.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import precio.precios.model.Price;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepo extends JpaRepository<Price, Long> {

    @Query(value = "SELECT * FROM price " +
            "WHERE articleId = :articleId AND effectiveDate IS NULL",nativeQuery = true)
    Optional<Price> findByArticleId(@Param("articleId") Long articleId);

    @Query(value = "SELECT * FROM price " +
            "WHERE articleId = :articleId",nativeQuery = true)
    List<Price> findAllByArticleId(@Param("articleId") Long articleId);


}
