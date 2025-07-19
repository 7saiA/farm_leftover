package ashteam.farm_leftover.auth.dto.response;

import ashteam.farm_leftover.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String accessToken;
    String refreshToken;
    UserDto userDto;

    public AuthResponse withoutRefreshToken() {
        return new AuthResponse(this.accessToken, null, this.userDto);
    }
}
