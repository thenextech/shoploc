package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.*;
import nextech.shoploc.domains.enums.MerchantStatus;

import java.util.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant extends User {

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "address") 
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MerchantSchedule> schedules = new ArrayList<>();
}
