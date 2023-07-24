package br.com.telebrasilia.chamado;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

  private List<Tuple> chamados = new ArrayList<>();

 
  public Chamado criarChamado(MultipartFile[] files, String tpChamado, String dsChamado, Long idEmpresa, String noArquivo) {
      /** consultar empresa */
      empresa = empresaRepository.findEmpresaByIdEmpresa(idEmpresa);
      chamado = new Chamado();
      chamado.setTpChamado(tpChamado);
      chamado.setDsChamado(dsChamado);
      chamado.setIdEmpresa(empresa);
      chamado.setNoSolicitante(empresa.getDsNoFantas());
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
      protocolo.setStProtocolo("Aberto");
      protocolo.setCoUsuario("TELEBRASILIA");
      protocolo.setObservacao("PORTAL TELEBRASILIA");
      this.protocolo = protocoloRepository.save(protocolo);
      chamado.setIdProtocolo(protocolo);

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

  public List<ChamadoDTO> consultarChamado(ChamadoDTO chamadoDTO) {
    chamados = chamadoRepositoryImpl.getChamados(chamadoDTO);
          
      List<ChamadoDTO> listaChamadoDTOs = new ArrayList<>();
        
      Chamado filterChamdoDTO;
      Protocolo filterProtocoloDTO;

        for (Tuple tuple : chamados) {
                chamadoDTO = new ChamadoDTO();

                filterChamdoDTO = tuple.get(0, Chamado.class);
                chamadoDTO.setIdChamado(filterChamdoDTO.getIdChamado());
                chamadoDTO.setTpChamado(filterChamdoDTO.getTpChamado());
                chamadoDTO.setDsChamado(filterChamdoDTO.getDsChamado());
                chamadoDTO.setNoArquivo(filterChamdoDTO.getNoArquivo());
                chamadoDTO.setNoSoliccitante(filterChamdoDTO.getNoSolicitante());    
                chamadoDTO.setIdEmpresa(filterChamdoDTO.getIdEmpresa().getIdEmpresa());
                chamadoDTO.setIdProtocolo(filterChamdoDTO.getIdProtocolo().getIdProtocolo());

                filterProtocoloDTO = tuple.get(2, Protocolo.class);
                chamadoDTO.setNuProtocolo(filterProtocoloDTO.getNuProtocolo());
                chamadoDTO.setStProtocolo(filterProtocoloDTO.getStProtocolo());

                chamadoDTO.setFiles(this.filesStorageServiceImpl.loadAll(chamadoDTO.getNuProtocolo()));
           
                listaChamadoDTOs.add(chamadoDTO);
            }
           
   return listaChamadoDTOs;
  }


  public Chamado responderChamado(ChamadoDTO chamadoDTO) {
    /** consultar empresa */
      empresa = empresaRepository.findEmpresaByIdEmpresa(chamadoDTO.getIdEmpresa());
      chamado = new Chamado();
      chamado.setTpChamado(chamadoDTO.getTpChamado());
      chamado.setDsChamado(chamadoDTO.getDsProtocolo());
      chamado.setIdEmpresa(empresa);
      chamado.setNoSolicitante(empresa.getDsNoFantas());


      /** salvar protocolo */
      this.protocolo = new Protocolo();
      this.protocolo = protocoloRepository.save(protocolo);
      protocolo.setCpfCnpj(empresa.getCnpj());
      protocolo.setNuProtocolo(chamadoDTO.getNuProtocolo());
                                protocolo.setNoSolicitante(empresa.getDsNoFantas());
      protocolo.setTpSolicitacao(chamadoDTO.getTpChamado());
      Date data = new Date(System.currentTimeMillis());
      
      protocolo.setStProtocolo(chamadoDTO.getStProtocolo());
      
      
      if(chamadoDTO.getStProtocolo().equals("Em execução")){
        protocolo.setDtExecucao(data);
      } else {
                protocolo.setDtSolucao(data.toString());
      }
      
      protocolo.setCoUsuario("TELEBRASILIA");
      protocolo.setObservacao("PORTAL TELEBRASILIA");
      this.protocolo = protocoloRepository.save(protocolo);
     
      
      
      /** Enviar email */
      emailService.responderChamado(protocolo, empresa, chamado);
      LOGGER.info("NÚMERO DO PROTOCOLO CRIADO {}..........  ID_PROTOCOLO {}",
      protocolo.getNuProtocolo(), protocolo.getIdProtocolo());
      
      
      /** salvar chamado */
      chamado.setIdProtocolo(protocolo);
      return chamadoRepository.save(chamado);
  }

  public Resource charregarArquivo(ChamadoDTO chamadoDTO) {
    return this.filesStorageServiceImpl.load(chamadoDTO);
  }

  



}