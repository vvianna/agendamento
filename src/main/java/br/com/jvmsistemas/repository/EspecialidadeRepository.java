package br.com.jvmsistemas.repository;

import br.com.jvmsistemas.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Especialidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    @Query(value = "select distinct especialidade from Especialidade especialidade left join fetch especialidade.especialidades left join fetch especialidade.especialidades",
        countQuery = "select count(distinct especialidade) from Especialidade especialidade")
    Page<Especialidade> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct especialidade from Especialidade especialidade left join fetch especialidade.especialidades left join fetch especialidade.especialidades")
    List<Especialidade> findAllWithEagerRelationships();

    @Query("select especialidade from Especialidade especialidade left join fetch especialidade.especialidades left join fetch especialidade.especialidades where especialidade.id =:id")
    Optional<Especialidade> findOneWithEagerRelationships(@Param("id") Long id);

}
