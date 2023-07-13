package br.com.telebrasilia.chamado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.empresa.EmpresaRepository;


/**
 * @author  Romerito Alencar
 */

@Service
public class ChamadoService {
    
    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    Empresa empresa = new Empresa();
    
    public Chamado save(Chamado chamado){
        empresa = empresaRepository.findEmpresaByIdEmpresa(chamado.getIdEmpresa());
        chamado.setIdEmpresa(chamado.getIdEmpresa());
        chamado.setNoSoliccitante(empresa.getDsNoFantas());
        //salvar protocolo
        //enviar email
        return  chamadoRepository.save(chamado);
    }

}
