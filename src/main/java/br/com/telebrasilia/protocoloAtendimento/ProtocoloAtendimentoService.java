package br.com.telebrasilia.protocoloAtendimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Romerito Alencar
 */

@Service
public class ProtocoloAtendimentoService {

    @Autowired
    ProtocoloAtendimentoRepository protocoloRepository;

    public ProtocoloAtendimento save(ProtocoloAtendimento protocolo) {
        return protocoloRepository.save(protocolo);
    }

    public Integer verificarProtocoloFinalizado(String nuProtocolo) {
        return protocoloRepository.verifyProtocoloFinalizado(nuProtocolo);
    }
}
