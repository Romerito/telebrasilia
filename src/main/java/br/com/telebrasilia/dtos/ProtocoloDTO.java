package br.com.telebrasilia.dtos;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Romerito Alencar
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocoloDTO {

    private String cpfCnpj;
    private Long idCliente;
    private String nuProtocolo;
    private String noSolicitante;
    private String tpSolicitacao;
    private Date dtAbertura;
    private Date dtExecucao;
    private String hrExecucao;
    private String dtSolucao;
    private String nuTelefone;
    private String stProtocolo;
    private String coUsuario;
    private String observacao;

}
