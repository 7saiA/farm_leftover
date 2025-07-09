package ashteam.farm_leftover.auth.dto;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String login;
    private String password;
}
