package br.com.jvmsistemas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "crm")
    private String crm;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "medico_medico",
               joinColumns = @JoinColumn(name = "medicos_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "medicos_id", referencedColumnName = "id"))
    private Set<Especialidade> medicos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "medico_medico",
               joinColumns = @JoinColumn(name = "medicos_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "medicos_id", referencedColumnName = "id"))
    private Set<PlanoDeSaude> medicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Medico nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public Medico crm(String crm) {
        this.crm = crm;
        return this;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Set<Especialidade> getMedicos() {
        return medicos;
    }

    public Medico medicos(Set<Especialidade> especialidades) {
        this.medicos = especialidades;
        return this;
    }

    public Medico addMedico(Especialidade especialidade) {
        this.medicos.add(especialidade);
        return this;
    }

    public Medico removeMedico(Especialidade especialidade) {
        this.medicos.remove(especialidade);
        return this;
    }

    public void setMedicos(Set<Especialidade> especialidades) {
        this.medicos = especialidades;
    }

    public Set<PlanoDeSaude> getMedicos() {
        return medicos;
    }

    public Medico medicos(Set<PlanoDeSaude> planoDeSaudes) {
        this.medicos = planoDeSaudes;
        return this;
    }

    public Medico addMedico(PlanoDeSaude planoDeSaude) {
        this.medicos.add(planoDeSaude);
        return this;
    }

    public Medico removeMedico(PlanoDeSaude planoDeSaude) {
        this.medicos.remove(planoDeSaude);
        return this;
    }

    public void setMedicos(Set<PlanoDeSaude> planoDeSaudes) {
        this.medicos = planoDeSaudes;
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
        Medico medico = (Medico) o;
        if (medico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", crm='" + getCrm() + "'" +
            "}";
    }
}
