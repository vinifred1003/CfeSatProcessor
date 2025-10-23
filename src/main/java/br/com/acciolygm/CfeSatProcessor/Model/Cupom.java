// Cupom.java
package br.com.acciolygm.CfeSatProcessor.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity 
@Table(name="cupons", uniqueConstraints = @UniqueConstraint(name="uk_cupons_access_key", columnNames = "access_key"),
        indexes = {
            @Index(name="idx_number_cupom", columnList="cfe"),
            @Index(name="idx_total_cupom", columnList="total")

})

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Cupom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="access_key", nullable=false, length=60)
    @NotBlank
    private String accessKey;

    @Column(name="cfe", nullable=false)
    private String cfe;

    @Column(name="issuedAt", nullable=false)
    private LocalDateTime issuedAt;

    @Column(name="total", precision=15, scale=2, nullable=false)
    private BigDecimal total;

    @Column(name="canceled", nullable=false)
    private boolean canceled;

    @OneToMany(mappedBy = "cupom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
}
