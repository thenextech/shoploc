package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextech.shoploc.domains.enums.TypeBenefits;

import java.time.LocalDateTime;

@Entity
@Table(name = "benefits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitsId;

    @Column(name = "benefits_description")
    private String description;

    @Column
    private double cost;

    @Column
    private TypeBenefits typeBenefits;

    @Column
    private LocalDateTime dateCreation;

    @Column
    private LocalDateTime dateStart;

    @Column
    private LocalDateTime dateEnd;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
