package br.com.telebrasilia.empresa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author  Romerito Alencar
 */

@Service
public class EmpresaService {
    
    @Autowired
    EmpresaRepository empresaRepository;
    
    public Empresa findByCNPJ(Empresa empresa){
        return  empresaRepository.findEmpresaByCNPJ(empresa.getCnpj());
    }

    public Empresa findByCNPJAndSenha(String cnpj, String senha) {
        return empresaRepository.findEmpresaByCNPJAndSenha(cnpj, senha);
    }

}
