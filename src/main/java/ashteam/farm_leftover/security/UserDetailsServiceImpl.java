package ashteam.farm_leftover.security;

import ashteam.farm_leftover.user.dao.UserRepository;
import ashteam.farm_leftover.user.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Collection<String> authorities = userAccount.getRoles().stream().map(r -> "ROLE_" + r.name())
                .toList();
        return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(authorities));
    }
}
