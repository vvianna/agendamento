package br.com.jvmsistemas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PlanoDeSaude.
 */
@Entity
@Table(name = "plano_de_saude")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlanoDeSaude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_plano")
    private String nomePlano;

    @Column(name = "cnpj")
    private Long cnpj;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "plano_de_saude_plano_de_saude",
               joinColumns = @JoinColumn(name = "plano_de_saudes_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "plano_de_saudes_id", referencedColumnName = "id"))
    private Set<Medico> planoDeSaudes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "plano_de_saude_plano_de_saude",
               joinColumns = @JoinColumn(name = "plano_de_saudes_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "plano_de_saudes_id", referencedColumnName = "id"))
    private Set<Especialidade> planoDeSaudes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public PlanoDeSaude nomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
        return this;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public PlanoDeSaude cnpj(Long cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public Set<Medico> getPlanoDeSaudes() {
        return planoDeSaudes;
    }

    public PlanoDeSaude planoDeSaudes(Set<Medico> medicos) {
        this.planoDeSaudes = medicos;
        return this;
    }

    public PlanoDeSaude addPlanoDeSaude(Medico medico) {
        this.planoDeSaudes.add(medico);
        return this;
    }

    public PlanoDeSaude removePlanoDeSaude(Medico medico) {
        this.planoDeSaudes.remove(medico);
        return this;
    }

    public void setPlanoDeSaudes(Set<Medico> medicos) {
        this.planoDeSaudes = medicos;
    }

    public Set<Especialidade> getPlanoDeSaudes() {
        return planoDeSaudes;
    }

    public PlanoDeSaude planoDeSaudes(Set<Especialidade> especialidades) {
        this.planoDeSaudes = especialidades;
        return this;
    }

    public PlanoDeSaude addPlanoDeSaude(Especialidade especialidade) {
        this.planoDeSaudes.add(especialidade);
        return this;
    }

    public PlanoDeSaude removePlanoDeSaude(Especialidade especialidade) {
        this.planoDeSaudes.remove(especialidade);
        return this;
    }

    public void setPlanoDeSaudes(Set<Especialidade> especialidades) {
        this.planoDeSaudes = especialidades;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlanoDeSaude planoDeSaude = (PlanoDeSaude) o;
        if (planoDeSaude.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planoDeSaude.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanoDeSaude{" +
            "id=" + getId() +
            ", nomePlano='" + getNomePlano() + "'" +
            ", cnpj=" + getCnpj() +
            "}";
    }
}
