package br.com.telebrasilia.empresaDadosad;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import br.com.telebrasilia.empresa.Empresa;
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
@Table(name = "empresa_dadosad")
public class EmpresaDadosad extends RepresentationModel<EmpresaDadosad> {

    @Id
    @GeneratedValue
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
    private Long idEmpresa;
    private String nuCircuito;
    private Date dtAtivcirc;
    private String cnlCidade;
}