package br.com.telebrasilia.chamado;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import br.com.telebrasilia.empresa.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author  Romerito Alencar
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "abertura_chamado")
public class Chamado  extends RepresentationModel<Chamado> {
    
    @Id
    @GeneratedValue
    private Long idChamado;
    private Long coCompania;
    @ManyToOne()
    private Empresa idEmpresa;
    private Long idCliente;
    private String noSoliccitante;
    private String tpChamado;
    private String dsChamado;
    private String lcAtendimento;
    private String coUf;
    private String noArquivo;
    
}
