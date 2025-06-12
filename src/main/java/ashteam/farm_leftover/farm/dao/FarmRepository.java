package ashteam.farm_leftover.farm.dao;

import ashteam.farm_leftover.farm.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Integer> {
}
