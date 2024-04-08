package nextech.shoploc.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "clientAddress1")
    private String lineAddress1;

    @Column(name = "clientAddress2")
    private String lineAddress2;

    @Column(name = "clientCity")
    private String city;

    @Column(name = "clientPostalCode")
    private String postalCode;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_vfp")
    private Boolean vfp;

    @Column
    private LocalDateTime lastDateTimeVFPActivated;


    @OneToOne(mappedBy = "client")
    private LoyaltyCard loyaltyCard;
}

