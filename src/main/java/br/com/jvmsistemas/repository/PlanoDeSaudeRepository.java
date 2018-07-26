package br.com.jvmsistemas.repository;

import br.com.jvmsistemas.domain.PlanoDeSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PlanoDeSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoDeSaudeRepository extends JpaRepository<PlanoDeSaude, Long> {

    @Query(value = "select distinct plano_de_saude from PlanoDeSaude plano_de_saude left join fetch plano_de_saude.planoDeSaudes left join fetch plano_de_saude.planoDeSaudes",
        countQuery = "select count(distinct plano_de_saude) from PlanoDeSaude plano_de_saude")
    Page<PlanoDeSaude> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct plano_de_saude from PlanoDeSaude plano_de_saude left join fetch plano_de_saude.planoDeSaudes left join fetch plano_de_saude.planoDeSaudes")
    List<PlanoDeSaude> findAllWithEagerRelationships();

    @Query("select plano_de_saude from PlanoDeSaude plano_de_saude left join fetch plano_de_saude.planoDeSaudes left join fetch plano_de_saude.planoDeSaudes where plano_de_saude.id =:id")
    Optional<PlanoDeSaude> findOneWithEagerRelationships(@Param("id") Long id);

}
