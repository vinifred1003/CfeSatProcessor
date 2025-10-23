package br.com.acciolygm.CfeSatProcessor.Repository;

import br.com.acciolygm.CfeSatProcessor.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {}
