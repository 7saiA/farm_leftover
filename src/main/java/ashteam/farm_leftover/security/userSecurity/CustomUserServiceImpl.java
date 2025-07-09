package ashteam.farm_leftover.security.userSecurity;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(userAccount);
        //Avoid Null. Constructor: Uses generated record constructor
    }
}
