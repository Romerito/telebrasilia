package br.com.telebrasilia.dtos;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

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
    private Stream<Path> files;

}
