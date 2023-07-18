package br.com.telebrasilia.chamado;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.plaf.multi.MultiButtonUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.email.EmailService;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.empresa.EmpresaRepository;
import br.com.telebrasilia.protocolo.Protocolo;
import br.com.telebrasilia.protocolo.ProtocoloRepository;
import br.com.telebrasilia.upload.FilesStorageService;
import br.com.telebrasilia.upload.FilesStorageServiceImpl;


/**
 * @author  Romerito Alencar
 */

@Service
public class ChamadoService {

        private static final Logger LOGGER = LogManager.getLogger(ChamadoService.class);
    
    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    ProtocoloRepository protocoloRepository;

    @Autowired
    ChamadoRepositoryImpl chamadoRepositoryImpl;

    @Autowired
    EmailService emailService;

    @Autowired
    FilesStorageServiceImpl FilesStorageServiceImpl;

    Empresa empresa = new Empresa();

    Protocolo protocolo = new Protocolo();

    private Chamado chamado = new  Chamado();

    public Chamado save(ChamadoDTO chamadoDTO){
        /**consultar empresa */
        empresa = empresaRepository.findEmpresaByIdEmpresa(chamadoDTO.getIdEmpresa());
        chamadoDTO.setIdEmpresa(chamadoDTO.getIdEmpresa());
        chamadoDTO.setNoSoliccitante(empresa.getDsNoFantas());

        /**salvar protocolo */
        this.protocolo = new Protocolo();
        this.protocolo = protocoloRepository.save(protocolo);
        protocolo.setCpfCnpj(empresa.getCnpj());
        LocalDateTime date = LocalDateTime.now();
        protocolo.setNuProtocolo("P000" + date.getYear() + "" + date.getMonth().getValue() + "" + date.getDayOfMonth() + "" + date.getMinute() + "" + date.getSecond() + "" + protocolo.getIdProtocolo() + "" );
        protocolo.setNoSolicitante(empresa.getDsNoFantas());
        protocolo.setTpSolicitacao(chamadoDTO.getTpChamado());
        Date data = new Date(System.currentTimeMillis());
        protocolo.setDtAbertura(data);
        protocolo.setStProtocolo("ABERTO");
        protocolo.setCoUsuario("TELEBRASILIA");
        protocolo.setObservacao("PORTAL TELEBRASILIA");
        this.protocolo = protocoloRepository.save(protocolo);
      
        /**salvar protocolo */
        
        BeanUtils.copyProperties(chamadoDTO, chamado);
        emailService.send(protocolo, empresa, chamado);
        LOGGER.info("NÚMERO DO PROTOCOLO: {}......  ID_PROTOCOLO {}",  protocolo.getNuProtocolo(), protocolo.getIdProtocolo());
        

        /**salvar arquivo */
        this.FilesStorageServiceImpl.save(chamadoDTO.getNoArquivos());

        /**salvar chamado */
        return  chamadoRepository.save(chamado);
    }

    public List<Chamado>  getChamados(String stProtocolo, String nuProtocolo) {
      return chamadoRepositoryImpl.getChamados(stProtocolo, nuProtocolo);
    }

}
