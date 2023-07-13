package br.com.telebrasilia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Romerito Alencar
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoDTO {
    
    private Long idChamado;
    private Long coCompania;
    private Long idEmpresa;
    private Long idCliente;
    private String noSolicitante;
    private String tpChamado;
    private String dsChamado;
    private String lcAtendimento;
    private String coUf;
    private String noArquivo;
    
}
