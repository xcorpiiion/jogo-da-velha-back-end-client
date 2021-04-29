package br.com.unip.jogodavelha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@MappedSuperclass
@Inheritance(strategy = SINGLE_TABLE)
public abstract class AbstractId implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;
}
