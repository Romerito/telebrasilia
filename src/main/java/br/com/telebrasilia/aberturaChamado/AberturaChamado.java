package br.com.telebrasilia.aberturaChamado;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.empresaDadosad.EmpresaDadosad;
import br.com.telebrasilia.enums.ChamadoSituacaoEnum;
import br.com.telebrasilia.protocoloAtendimento.ProtocoloAtendimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Romerito Alencar
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "abertura_chamado")
public class AberturaChamado extends RepresentationModel<AberturaChamado> {

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
    private ProtocoloAtendimento idProtocolo;
    @Enumerated(EnumType.STRING)
    private ChamadoSituacaoEnum scChamado;
    @OneToOne()
    private EmpresaDadosad idEmprad;
}
