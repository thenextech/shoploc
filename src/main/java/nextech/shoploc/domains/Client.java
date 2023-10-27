package nextech.shoploc.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

