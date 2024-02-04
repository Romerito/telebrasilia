package br.com.telebrasilia.dtos;

import java.sql.Date;

import javax.persistence.ManyToOne;

import br.com.telebrasilia.empresa.Empresa;
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
public class EmpresaDadosadDTO {

    private Long idEmprad;
    private String dsEndereco;
    private String dsCompl;
    private String coCep;
    private String eMail;
    private String stInstal;
    private String noContato;
    private String tlContato;
    private String ufInstal;
    private String dsNoFantas;
    private String dsCidade;
    private Empresa idEmpresa;
    private String nuCircuito;
    private Date dtAtivcirc;
    private String cnlCidade;
}
