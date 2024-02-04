package br.com.telebrasilia.empresaDadosad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.empresa.Empresa;

/**
 * @author Romerito Alencar
 */
@Service
public class EmpresaDadosadService {

    @Autowired
    EmpresaDadosadRepository empresaDadosadRepository;

    public List<EmpresaDadosad> consultarEmpresaDadosad(Long idEmpresa) {
        return empresaDadosadRepository.findByIdEmpresaDadosad(idEmpresa);
    }
}
