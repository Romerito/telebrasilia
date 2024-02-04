package br.com.telebrasilia.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Romerito Alencar
 */
@Service
public class EmpresaService {

    @Autowired
    EmpresaRepository empresaRepository;

    public Empresa findByCNPJ(String cnpj) {
        return empresaRepository.findEmpresaByCNPJ(cnpj);
    }

    public Empresa findByCNPJAndSenha(String cnpj, String senha) {
        return empresaRepository.findEmpresaByCNPJAndSenha(cnpj, senha);
    }

}
