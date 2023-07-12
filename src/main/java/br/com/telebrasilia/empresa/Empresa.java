package br.com.telebrasilia.empresa;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

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
@Table(name = "empresa")
public class Empresa  extends RepresentationModel<Empresa> {
    
    @Id
    @GeneratedValue
    private Long idEmpresa;
    private String dsNoFantas;
    private String dsEndereco;
    private String dsNoBairro;
    private String coCep;
    private String cnpj;
    private String inscEstadual;
    private String dsRazSocial;
    private Integer coClasse;
    private String email;
    private String noContato;
    private String siteEmpresa;
    private String coNatureza;
    private Integer coCidade;
    private String e_mail2;
    private String inscrMunicipal;
    private String dsComplemento;
    private String stEmpresa;
    private String observacao;
    private String coUf;
    private LocalDateTime dtAdesao;
    private String nvEmpresa;
    private String coordenada;
    private String senha;
    private String email2;
}
