package tacos.data;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tacos.Taco;
import tacos.TacoOrder;

public interface TacoRepository extends JpaRepository<Taco, Long> {
    List<Taco> findByTacoOrder(TacoOrder tacoOrder);
}

