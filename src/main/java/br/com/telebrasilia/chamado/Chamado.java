package br.com.telebrasilia.chamado;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.protocolo.Protocolo;
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
    @OneToOne()
    private Empresa idEmpresa;
    private Long idCliente;
    private String noSolicitante;
    private String tpChamado;
    private String dsChamado;
    private String lcAtendimento;
    private String coUf;
    private String noArquivo;
    @OneToOne()
    private Protocolo idProtocolo;
}
