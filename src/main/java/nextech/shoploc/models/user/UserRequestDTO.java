package nextech.shoploc.models.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date birthday;

}