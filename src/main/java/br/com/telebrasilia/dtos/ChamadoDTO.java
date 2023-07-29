package br.com.telebrasilia.dtos;

import java.util.List;

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
    private Long idProtocolo;
    private Long idCliente;
    private String noSoliccitante;
    private String tpChamado;
    private String dsChamado;
    private String lcAtendimento;
    private String coUf;
    private String noArquivo;
    private String nuProtocolo;
    private String stProtocolo;
    private String dsProtocolo;
    private List<String> files;
    private String file;
    private Integer pageNumber;
    private Integer pageSize;

}
