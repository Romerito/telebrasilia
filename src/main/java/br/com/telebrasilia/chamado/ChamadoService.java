package br.com.telebrasilia.chamado;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

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
        /**consultar empresa */
        empresa = empresaRepository.findEmpresaByIdEmpresa(chamado.getIdEmpresa());
        chamado.setIdEmpresa(chamado.getIdEmpresa());
        chamado.setNoSoliccitante(empresa.getDsNoFantas());

        /**salvar protocolo */
        protocolo.setCpfCnpj(empresa.getCnpj());
        LocalDateTime date = LocalDateTime.now();
        protocolo.setNuProtocolo("P000" + date.getYear() + "" + date.getMonth().getValue() + "" + date.getDayOfMonth() + "" + date.getMinute() + "" + date.getSecond() + "");
        protocolo.setNoSolicitante(empresa.getDsNoFantas());
        protocolo.setTpSolicitacao(chamado.getTpChamado());
        Date data = new Date(System.currentTimeMillis());
        protocolo.setDtAbertura(data);
        protocolo.setStProtocolo("ABERTO");
        protocolo.setCoUsuario("TELEBRASILIA");
        protocolo.setObservacao("PORTAL TELEBRASILIA");
        protocolo = protocoloRepository.save(protocolo);
      

        System.out.println("PROTOCOLO CRIADO................ " + protocolo.getIdProtocolo());
        //enviar email
        return  chamadoRepository.save(chamado);
    }

}
