package ashteam.farm_leftover.user.dto;

import ashteam.farm_leftover.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
public class UserLoginResponseDto {
    public String login;
    public Set<Role> roles;
    public String accessToken;
    public String refreshToken;


}
