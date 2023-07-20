package br.com.telebrasilia.chamado;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.email.EmailService;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.empresa.EmpresaRepository;
import br.com.telebrasilia.protocolo.Protocolo;
import br.com.telebrasilia.protocolo.ProtocoloRepository;
import br.com.telebrasilia.upload.FilesStorageServiceImpl;

/**
 * @author Romerito Alencar
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
  FilesStorageServiceImpl filesStorageServiceImpl;

  Empresa empresa = new Empresa();

  Protocolo protocolo = new Protocolo();

  private Chamado chamado = new Chamado();

  public Chamado save(MultipartFile[] files, String tpChamado, String dsChamado, Long idEmpresa, String noArquivo) {

    /** consultar empresa */
    empresa = empresaRepository.findEmpresaByIdEmpresa(idEmpresa);
    chamado = new Chamado();
    chamado.setTpChamado(tpChamado);
    chamado.setDsChamado(dsChamado);
    chamado.setIdEmpresa(empresa);
    chamado.setNoSoliccitante(empresa.getDsNoFantas());
    chamado.setNoArquivo(noArquivo);

     

    /** salvar protocolo */
    this.protocolo = new Protocolo();
    this.protocolo = protocoloRepository.save(protocolo);
    protocolo.setCpfCnpj(empresa.getCnpj());
    LocalDateTime date = LocalDateTime.now();
    protocolo.setNuProtocolo("P" + date.getYear() + "" +
                              date.getMonth().getValue() + "" + date.getDayOfMonth() + "" +
                              date.getMinute() + "" + date.getSecond() + "" + protocolo.getIdProtocolo() +
                              "" );
                              protocolo.setNoSolicitante(empresa.getDsNoFantas());
    protocolo.setTpSolicitacao(tpChamado);
    Date data = new Date(System.currentTimeMillis());
    protocolo.setDtAbertura(data);
    protocolo.setStProtocolo("ABERTO");
    protocolo.setCoUsuario("TELEBRASILIA");
    protocolo.setObservacao("PORTAL TELEBRASILIA");
    this.protocolo = protocoloRepository.save(protocolo);
  

   /** salvar arquivo */
    String pathFile =  this.filesStorageServiceImpl.save(files, protocolo.getNuProtocolo());
    chamado.setNoArquivo(pathFile);


    /** Enviar email */
    emailService.send(protocolo, empresa, chamado);
    LOGGER.info("NÚMERO DO PROTOCOLO CRIADO {}..........  ID_PROTOCOLO {}",
    protocolo.getNuProtocolo(), protocolo.getIdProtocolo());


    /** salvar chamado */
    return chamadoRepository.save(chamado);
  }

  public List<Tuple> getChamados(ChamadoDTO chamadoDTO) {
    return chamadoRepositoryImpl.getChamados(chamadoDTO);
  }

}
