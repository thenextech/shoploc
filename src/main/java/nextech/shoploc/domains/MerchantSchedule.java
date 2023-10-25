package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.*;
import nextech.shoploc.domains.enums.DayOfWeek;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "merchant_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Temporal(TemporalType.TIME)
    @Column(name = "opening_time")
    private Date openingTime;

    @Temporal(TemporalType.TIME)
    @Column(name = "closing_time")
    private Date closingTime;
}

