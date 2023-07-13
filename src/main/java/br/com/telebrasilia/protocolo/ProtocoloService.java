package br.com.telebrasilia.protocolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author  Romerito Alencar
 */

@Service
public class ProtocoloService {
    
    @Autowired
    ProtocoloRepository protocoloRepository;

    public Protocolo save(Protocolo  protocolo){
        return  protocoloRepository.save(protocolo);
    }
}
