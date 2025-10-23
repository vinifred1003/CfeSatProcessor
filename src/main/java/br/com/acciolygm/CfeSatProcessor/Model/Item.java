// Item.java
package br.com.acciolygm.CfeSatProcessor.Model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="cupom_id")
    private Cupom cupom;

    @Column(name="code", length=80)
    private String code;

    @Column(name="description", length=255)
    private String description;

    @Column(name="quantity", precision=15, scale=4)
    private BigDecimal quantity;

    @Column(name="cfop", nullable=false, length=10)
    private String cfop;

    @Column(name="grossTotal", precision=15, scale=2)
    private BigDecimal grossTotal;
}
