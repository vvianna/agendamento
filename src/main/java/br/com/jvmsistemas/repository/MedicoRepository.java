package br.com.jvmsistemas.repository;

import br.com.jvmsistemas.domain.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Medico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Query(value = "select distinct medico from Medico medico left join fetch medico.medicos left join fetch medico.medicos",
        countQuery = "select count(distinct medico) from Medico medico")
    Page<Medico> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct medico from Medico medico left join fetch medico.medicos left join fetch medico.medicos")
    List<Medico> findAllWithEagerRelationships();

    @Query("select medico from Medico medico left join fetch medico.medicos left join fetch medico.medicos where medico.id =:id")
    Optional<Medico> findOneWithEagerRelationships(@Param("id") Long id);

}
