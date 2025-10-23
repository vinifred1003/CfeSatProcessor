package br.com.acciolygm.CfeSatProcessor.Repository;


import br.com.acciolygm.CfeSatProcessor.Model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CupomRepository extends JpaRepository<Cupom, Long> {
    boolean existsByAccessKey(String accessKey);
    Optional<Cupom> findByAccessKey(String accessKey);
}
