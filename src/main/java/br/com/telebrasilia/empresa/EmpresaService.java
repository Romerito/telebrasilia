package br.com.telebrasilia.empresa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.dtos.EmailDTO;


/**
 * @author  Romerito Alencar
 */

@Service
public class EmpresaService {
    
    @Autowired
    EmpresaRepository empresaRepository;
    
    public Empresa findByCNPJ(Empresa empresa){
        return  empresaRepository.findByCNPJ(empresa.getCnpj());
     }

}
