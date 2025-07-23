package ashteam.farm_leftover.user.dao;

import ashteam.farm_leftover.user.model.Role;
import ashteam.farm_leftover.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findByFarmNameIgnoreCase(String farmName);

    List<UserAccount> findUserAccountByRoleAndFarmNameContainsIgnoreCase(Role role, String query);
}
