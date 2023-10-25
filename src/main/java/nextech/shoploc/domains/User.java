package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column
    private Date birthday;

    @Column
    private String password;



}
