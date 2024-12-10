package precio.precios.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import precio.precios.model.Deal;

import java.util.Optional;

@Repository
public interface DealRepo extends JpaRepository<Deal, Long> {

    Optional<Deal> findDealByCode(String code);

}
