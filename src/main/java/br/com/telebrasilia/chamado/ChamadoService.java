package br.com.telebrasilia.chamado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author  Romerito Alencar
 */

@Service
public class ChamadoService {
    
    @Autowired
    ChamadoRepository chamadoRepository;
    
    public Chamado save(Chamado chamado){
        return  chamadoRepository.save(chamado);
    }

}
