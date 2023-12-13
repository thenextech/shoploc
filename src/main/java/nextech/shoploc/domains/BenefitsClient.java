package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "benefits_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenefitsClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitsClientId;

    @Column
    private LocalDateTime dateCreation;

    @Column
    private Status status;

    @ManyToOne
    @JoinColumn(name = "benefits_id", nullable = false)
    private Benefits benefits;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
