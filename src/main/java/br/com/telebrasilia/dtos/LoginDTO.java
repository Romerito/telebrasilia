package br.com.telebrasilia.dtos;


import javax.validation.constraints.NotBlank;

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
public class LoginDTO {
    
    private String cnpj;
    private String senha;
}
