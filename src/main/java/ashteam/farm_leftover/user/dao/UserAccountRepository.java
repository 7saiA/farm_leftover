package ashteam.farm_leftover.user.dao;

import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByFarmName(String farmName);

    List<UserAccount> findUserAccountByRoleAndFarmNameContainsIgnoreCase(Role role, String query);
}
