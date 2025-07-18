package ashteam.farm_leftover.user.dao;

import ashteam.farm_leftover.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    UserAccount findByFarmNameIgnoreCase(String farmName);
}

//TODO speak with Natan, maybe we should to split UserAccount to User and Farm