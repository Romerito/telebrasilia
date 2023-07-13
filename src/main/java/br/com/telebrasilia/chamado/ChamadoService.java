package br.com.telebrasilia.chamado;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.empresa.EmpresaRepository;
import br.com.telebrasilia.protocolo.Protocolo;
import br.com.telebrasilia.protocolo.ProtocoloRepository;


/**
 * @author  Romerito Alencar
 */

@Service
public class ChamadoService {
    
    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    ProtocoloRepository protocoloRepository;

    Empresa empresa = new Empresa();

    Protocolo protocolo = new Protocolo();
    
    public Chamado save(Chamado chamado){
        empresa = empresaRepository.findEmpresaByIdEmpresa(chamado.getIdEmpresa());
        chamado.setIdEmpresa(chamado.getIdEmpresa());
        chamado.setNoSoliccitante(empresa.getDsNoFantas());

        LocalDateTime date = LocalDateTime.now();
        protocolo.setCpfCnpj(empresa.getCnpj());
        protocolo.setNuProtocolo("P000" + date.getYear() + "" + date.getMonth().getValue() + "" + date.getMinute() + "" + date.getSecond() + "");
        protocolo.setNoSolicitante(empresa.getDsNoFantas());
        protocolo.setTpSolicitacao(chamado.getTpChamado());
       // protocolo.setDtAbertura(data.getYear().of);
        protocolo.setStProtocolo("ABERTO");
        protocolo.setCoUsuario("TELEBRASILIA");
        protocolo.setObservacao("PORTAL TELEBRASILIA");
        protocolo = protocoloRepository.save(protocolo);
        //salvar protocolo

        //enviar email
        return  chamadoRepository.save(chamado);
    }

}
