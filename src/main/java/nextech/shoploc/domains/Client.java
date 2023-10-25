package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "address")
    private String address;

}

