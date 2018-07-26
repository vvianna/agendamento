package br.com.jvmsistemas.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Especialidade.
 */
@Entity
@Table(name = "especialidade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Especialidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_especialidade")
    private String nomeEspecialidade;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "especialidade_especialidade",
               joinColumns = @JoinColumn(name = "especialidades_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "especialidades_id", referencedColumnName = "id"))
    private Set<Medico> especialidades = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "especialidade_especialidade",
               joinColumns = @JoinColumn(name = "especialidades_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "especialidades_id", referencedColumnName = "id"))
    private Set<PlanoDeSaude> especialidades = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    public Especialidade nomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
        return this;
    }

    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }

    public Set<Medico> getEspecialidades() {
        return especialidades;
    }

    public Especialidade especialidades(Set<Medico> medicos) {
        this.especialidades = medicos;
        return this;
    }

    public Especialidade addEspecialidade(Medico medico) {
        this.especialidades.add(medico);
        return this;
    }

    public Especialidade removeEspecialidade(Medico medico) {
        this.especialidades.remove(medico);
        return this;
    }

    public void setEspecialidades(Set<Medico> medicos) {
        this.especialidades = medicos;
    }

    public Set<PlanoDeSaude> getEspecialidades() {
        return especialidades;
    }

    public Especialidade especialidades(Set<PlanoDeSaude> planoDeSaudes) {
        this.especialidades = planoDeSaudes;
        return this;
    }

    public Especialidade addEspecialidade(PlanoDeSaude planoDeSaude) {
        this.especialidades.add(planoDeSaude);
        return this;
    }

    public Especialidade removeEspecialidade(PlanoDeSaude planoDeSaude) {
        this.especialidades.remove(planoDeSaude);
        return this;
    }

    public void setEspecialidades(Set<PlanoDeSaude> planoDeSaudes) {
        this.especialidades = planoDeSaudes;
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
        Especialidade especialidade = (Especialidade) o;
        if (especialidade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), especialidade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Especialidade{" +
            "id=" + getId() +
            ", nomeEspecialidade='" + getNomeEspecialidade() + "'" +
            "}";
    }
}
