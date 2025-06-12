package ashteam.farm_leftover.user.dao;

import ashteam.farm_leftover.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
}
