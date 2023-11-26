package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.*;
import nextech.shoploc.domains.enums.AccountStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private AccountStatus status;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MerchantSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MerchantsCategoriesProducts> merchantsCategoriesProducts = new ArrayList<>();
}
