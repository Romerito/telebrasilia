package br.com.telebrasilia.aberturaChamado;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
import br.com.telebrasilia.empresaDadosad.EmpresaDadosad;
import br.com.telebrasilia.enums.ChamadoSituacaoEnum;
import br.com.telebrasilia.enums.ProtocoloStatusEnum;
import br.com.telebrasilia.protocoloAtendimento.ProtocoloAtendimento;
import br.com.telebrasilia.protocoloAtendimento.ProtocoloAtendimentoRepository;
import br.com.telebrasilia.upload.FilesStorageServiceImpl;

/**
 * @author Romerito Alencar
 */

@Service
public class AberturaChamadoService {

    private static final Logger LOGGER = LogManager.getLogger(AberturaChamadoService.class);

    @Autowired
    AberturaChamadoRepositoryImpl chamadoRepositoryImpl;

    @Autowired
    AberturaChamadoRepository chamadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    ProtocoloAtendimentoRepository protocoloRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    FilesStorageServiceImpl filesStorageServiceImpl;

    Empresa empresa = new Empresa();

    ProtocoloAtendimento protocolo = new ProtocoloAtendimento();

    private AberturaChamado chamado = new AberturaChamado();

    private List<Tuple> chamados = new ArrayList<>();

    final static String TELEBRASILIA = "TELEBRASILIA";

    final static String PORTAL_TELEBRASILIA = "PORTAL TELEBRASILIA";

    final static String EM_EXECUCAO = "Em execução";

    public AberturaChamado criar(MultipartFile[] files, String tpChamado, String dsChamado, Long idEmpresa, String noArquivo,
            Long idEmprad) {
        /** consultar empresa */
        empresa = empresaRepository.findEmpresaByIdEmpresa(idEmpresa);
        chamado = new AberturaChamado();
        chamado.setTpChamado(tpChamado);
        chamado.setDsChamado(dsChamado);
        chamado.setIdEmpresa(empresa);
        chamado.setNoSolicitante(empresa.getDsNoFantas());
        chamado.setScChamado(ChamadoSituacaoEnum.NAO_RESOLVIDO);

        EmpresaDadosad empresaDadosad = new EmpresaDadosad();
        empresaDadosad.setIdEmprad(idEmprad);
        chamado.setIdEmprad(empresaDadosad);

        /** salvar protocolo */
        this.protocolo = new ProtocoloAtendimento();
        this.protocolo = protocoloRepository.save(protocolo);
        protocolo.setCpfCnpj(empresa.getCnpj());
        LocalDateTime date = LocalDateTime.now();
        protocolo.setNuProtocolo("P" + date.getYear() + "" +
                date.getMonth().getValue() + "" + date.getDayOfMonth() + "" +
                date.getMinute() + "" + date.getSecond() + "" + protocolo.getIdProtocolo() +
                "");
        protocolo.setNoSolicitante(empresa.getDsNoFantas());
        protocolo.setTpSolicitacao(tpChamado);
        Date dataHora = new Date(System.currentTimeMillis());
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        protocolo.setDtAbertura(dataHora);
        protocolo.setHrExecucao(formatHora.format(dataHora));
        protocolo.setStProtocolo(ProtocoloStatusEnum.ABERTO);
        protocolo.setCoUsuario(TELEBRASILIA);
        protocolo.setObservacao(PORTAL_TELEBRASILIA);
        this.protocolo = protocoloRepository.save(protocolo);
        chamado.setIdProtocolo(protocolo);

        /** salvar arquivo */
        if (!noArquivo.equalsIgnoreCase("noFiles") && !files[0].getOriginalFilename().equalsIgnoreCase(noArquivo)) {
            String pathFile = this.filesStorageServiceImpl.save(files, protocolo.getNuProtocolo(), protocolo.getIdProtocolo());
            chamado.setNoArquivo(pathFile);
        }

        /** Enviar email */
        emailService.send(protocolo, empresa, chamado);
        LOGGER.info("NÚMERO DO PROTOCOLO CRIADO {}..........  ID_PROTOCOLO {}",
                protocolo.getNuProtocolo(), protocolo.getIdProtocolo());

        /** salvar chamado */
        return chamadoRepository.save(chamado);
    }

    public AberturaChamado reponder(MultipartFile[] files, String tpChamado, String dsChamado, Long idEmpresa,
            String noArquivo, String stProtocolo, String scChamado, String nuProtocolo, Long idChamado, Long idEmprad) {
        /** consultar empresa */
        empresa = empresaRepository.findEmpresaByChamado(idChamado);
        chamado = new AberturaChamado();
        chamado.setTpChamado(tpChamado);
        chamado.setDsChamado(dsChamado);
        chamado.setIdEmpresa(empresa);
        chamado.setNoSolicitante(empresa.getDsNoFantas());

        EmpresaDadosad empresaDadosad = new EmpresaDadosad();
        empresaDadosad.setIdEmprad(idEmprad);
        chamado.setIdEmprad(empresaDadosad);

        if (scChamado != null && scChamado.equalsIgnoreCase(ChamadoSituacaoEnum.RESOLVIDO.name())) {
            chamado.setScChamado(ChamadoSituacaoEnum.RESOLVIDO);
        } else {
            chamado.setScChamado(ChamadoSituacaoEnum.NAO_RESOLVIDO);
        }

        /** salvar protocolo */
        this.protocolo = new ProtocoloAtendimento();
        protocolo.setNuProtocolo(nuProtocolo);
        this.protocolo = protocoloRepository.save(protocolo);
        protocolo.setCpfCnpj(empresa.getCnpj());

        protocolo.setNoSolicitante(empresa.getDsNoFantas());
        protocolo.setTpSolicitacao(tpChamado);



        Date dataHora = new Date(System.currentTimeMillis());
        SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");


        if (stProtocolo.equalsIgnoreCase(EM_EXECUCAO)) {
            protocolo.setStProtocolo(ProtocoloStatusEnum.EM_EXECUCAO);
            protocolo.setDtExecucao(dataHora);
        } else {
            protocolo.setStProtocolo(ProtocoloStatusEnum.FINALIZADO);
            protocolo.setDtSolucao(formatData.format(dataHora));
        }

        protocolo.setHrExecucao(formatHora.format(dataHora));

        protocolo.setCoUsuario("TELEBRASILIA");
        protocolo.setObservacao("PORTAL TELEBRASILIA");
        this.protocolo = protocoloRepository.save(protocolo);

        /** salvar arquivo */
        if (!noArquivo.equalsIgnoreCase("noFiles") && !files[0].getOriginalFilename().equalsIgnoreCase(noArquivo)) {
            String pathFile = this.filesStorageServiceImpl.save(files, protocolo.getNuProtocolo(), protocolo.getIdProtocolo());
            chamado.setNoArquivo(pathFile);
        }

        /** Enviar email */
        emailService.send(protocolo, empresa, chamado);
        LOGGER.info("NÚMERO DO PROTOCOLO CRIADO {}..........  ID_PROTOCOLO {}",
                protocolo.getNuProtocolo(), protocolo.getIdProtocolo());

        /** salvar chamado */
        chamado.setIdProtocolo(this.protocolo);
        return chamadoRepository.save(chamado);
    }

    public List<ChamadoDTO> consultar(ChamadoDTO chamadoDTO) {
        chamados = chamadoRepositoryImpl.getChamados(chamadoDTO);

        List<ChamadoDTO> listaChamadoDTO = new ArrayList<>();

        AberturaChamado aberturaChamado;
        ProtocoloAtendimento protocoloAtendimento;

        for (Tuple tuple : chamados) {
            chamadoDTO = new ChamadoDTO();

            aberturaChamado = tuple.get(0, AberturaChamado.class);
            chamadoDTO.setIdChamado(aberturaChamado.getIdChamado());
            chamadoDTO.setTpChamado(aberturaChamado.getTpChamado());
            chamadoDTO.setDsChamado(aberturaChamado.getDsChamado());
            chamadoDTO.setNoArquivo(aberturaChamado.getNoArquivo());
            chamadoDTO.setNoSoliccitante(aberturaChamado.getNoSolicitante());
            chamadoDTO.setIdEmpresa(aberturaChamado.getIdEmpresa().getIdEmpresa());
            chamadoDTO.setIdProtocolo(aberturaChamado.getIdProtocolo().getIdProtocolo());
            chamadoDTO.setScChamado(ChamadoSituacaoEnum.getSituacao(aberturaChamado.getScChamado()));
            chamadoDTO.setIdEmprad(aberturaChamado.getIdEmprad().getIdEmprad());

            if (aberturaChamado.getNoArquivo() != null) {
                chamadoDTO.setFiles(this.filesStorageServiceImpl.loadAll(chamadoDTO.getNuProtocolo(), chamadoDTO.getIdProtocolo()));
            }

            protocoloAtendimento = tuple.get(2, ProtocoloAtendimento.class);
            chamadoDTO.setNuProtocolo(protocoloAtendimento.getNuProtocolo());
            chamadoDTO.setStProtocolo(ProtocoloStatusEnum.getStProtocolo(protocoloAtendimento.getStProtocolo()));

            chamadoDTO.setHrExecucao(protocoloAtendimento.getHrExecucao());

            SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");

            if (protocoloAtendimento.getDtAbertura() != null) {
                chamadoDTO.setDtAbertura(formatData.format(protocoloAtendimento.getDtAbertura()));
            }

            if (protocoloAtendimento.getDtExecucao() != null) {
                chamadoDTO.setDtExecucao(formatData.format(protocoloAtendimento.getDtExecucao()));
            }
            if (protocoloAtendimento.getDtSolucao() != null) {
                chamadoDTO.setDtSolucao(protocoloAtendimento.getDtSolucao());
            }

            chamadoDTO.setCnpj(protocoloAtendimento.getCpfCnpj());


            chamadoDTO.setTotalProtocolos(protocoloRepository.contAllByNuProtocolo(protocoloAtendimento.getNuProtocolo()));

            chamadoDTO.setFinalizado(protocoloRepository.verifyProtocoloFinalizado(protocoloAtendimento.getNuProtocolo()));

            if (chamadoDTO.getStProtocolo().equalsIgnoreCase("Em execução") || chamadoDTO.getStProtocolo().equalsIgnoreCase("Finalizado") && protocoloAtendimento.getDtExecucao() != null) {
                SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date dataAbertura = protocoloRepository.findByDtAbertura(protocoloAtendimento.getNuProtocolo());
                chamadoDTO.setDataAbertura(formatDataHora.format(dataAbertura));

                SimpleDateFormat formatYear = new SimpleDateFormat("yyyy-MM-dd");

                LocalDate dtAbertura = LocalDate.parse(formatYear.format(dataAbertura), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate dtDuracao = LocalDate.parse(formatYear.format(protocoloAtendimento.getDtExecucao()), DateTimeFormatter.ISO_LOCAL_DATE);
                Period period = Period.between(dtAbertura, dtDuracao);

                SimpleDateFormat formatHors = new SimpleDateFormat("hh:mm:ss");
                LocalTime hrAbertura =  LocalTime.parse(formatHors.format(dataAbertura), DateTimeFormatter.ISO_LOCAL_TIME);
                LocalTime hrDuracao =  LocalTime.parse(formatHors.format(protocoloAtendimento.getDtExecucao()), DateTimeFormatter.ISO_LOCAL_TIME);

                int years = Math.abs(period.getYears());
                int months = Math.abs(period.getMonths());
                int days = Math.abs(period.getDays());

                int hours =  Math.abs(hrDuracao.getHour() - hrAbertura.getHour());
                int minute =  Math.abs(hrDuracao.getMinute() - hrAbertura.getMinute());
                int second =  Math.abs(hrDuracao.getSecond() - hrAbertura.getSecond());

                StringBuilder durationTotal = new StringBuilder();
                if (years > 0 && years < 2) {
                    durationTotal.append(years + " Ano ");
                }
                if (years > 1) {
                    durationTotal.append(years + " Anos ");
                }
                if (months > 0 && months < 2) {
                    durationTotal.append(months + " Mês ");
                }
                if (months > 1) {
                    durationTotal.append(months + " Meses ");
                }
                if (days > 0 && days < 2) {
                    durationTotal.append(days + " Dia ");
                }
                if (days > 1) {
                    durationTotal.append(days + " Dias ");
                }
                if (hours > 0 && hours < 2) {
                    durationTotal.append(hours + " Hora ");
                }
                if (hours > 1) {
                    durationTotal.append(hours + " Horas ");
                }
                if (minute > 0 && minute < 2) {
                    durationTotal.append(minute + " Minuto ");
                }
                if (minute > 1) {
                    durationTotal.append(minute + " Minutos ");
                }
                if (second > 0 && second < 2) {
                    durationTotal.append(second + " Segundo ");
                }
                if (second > 1) {
                    durationTotal.append(second + " Segundos ");
                }
                chamadoDTO.setDuracaoChamado(durationTotal);

            }
            listaChamadoDTO.add(chamadoDTO);
        }

        return listaChamadoDTO;
    }

    public Resource carregarArquivo(ChamadoDTO chamadoDTO) {
        return this.filesStorageServiceImpl.load(chamadoDTO);
    }


}