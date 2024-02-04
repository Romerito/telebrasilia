package br.com.telebrasilia.email;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.aberturaChamado.AberturaChamado;
import br.com.telebrasilia.dtos.EmailDTO;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.enums.EmailStatusEnum;
import br.com.telebrasilia.protocoloAtendimento.ProtocoloAtendimento;

/**
 * @author Romerito Alencar
 */

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    JavaMailSender emailSender;


    final private static String EMAIL_FROM = "noc@telebrasilia.com";
    final private static String WWW = "http://pw.telebrasilia.com/portalweb";

    public Email responderChamado(ProtocoloAtendimento protocolo, Empresa empresa, AberturaChamado chamado) {
        Email emailCliente = new Email();
        EmailDTO emailEmpresaSend = EmailDTO.builder()
                .ownerRef("Telebrasília")
                .emailFrom(EMAIL_FROM)
                .emailTo(empresa.getE_mail2())
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text("Protocolo de atendimento  " + protocolo.getNuProtocolo() + " \n\r\nTipo do chamado: " +
                        chamado.getTpChamado() + "\n\r\nStatus do chamado: " + protocolo.getStProtocolo() +
                        "\n\r\n Clique no link para acompanhar  " + WWW)
                .build();

        BeanUtils.copyProperties(emailEmpresaSend, emailCliente);
        return send(emailCliente);
    }


    public Email send(ProtocoloAtendimento protocolo, Empresa empresa, AberturaChamado chamado) {
        Email emailTelebrasilia = new Email();
        EmailDTO emailTelebrasiliaSend = EmailDTO.builder()
                .ownerRef(empresa.getDsNoFantas())
                .emailFrom(EMAIL_FROM)
                .emailTo(EMAIL_FROM)
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text(empresa.getDsNoFantas() + " \n\r\n" + "Número do protocolo: " +
                        protocolo.getNuProtocolo() + " \n\r\nTipo do chamado: " + chamado.getTpChamado() +
                        "\n\r\nStatus do chamado: " + protocolo.getStProtocolo() +
                        "\n\r\n Clique no link para respnder " + WWW)
                .build();

        BeanUtils.copyProperties(emailTelebrasiliaSend, emailTelebrasilia);
        send(emailTelebrasilia);

        Email emailCliente = new Email();
        EmailDTO emailEmpresaSend = EmailDTO.builder()
                .ownerRef("Telebrasília")
                .emailFrom(EMAIL_FROM)
                .emailTo(empresa.getE_mail2())
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text("Protocolo de atendimento  " + protocolo.getNuProtocolo() +
                        " \n\r\nTipo do chamado: " + chamado.getTpChamado() + "\n\r\nStatus do chamado: " +
                        protocolo.getStProtocolo() + "\n\r\n Clique no link para acompanhar  " + WWW)
                .build();

        BeanUtils.copyProperties(emailEmpresaSend, emailCliente);
        return send(emailCliente);

    }


    public Email send(Empresa empresa) {
        Email email = new Email();
        EmailDTO emailDTOSend = EmailDTO.builder()
                .ownerRef(empresa.getDsNoFantas())
                .emailFrom(EMAIL_FROM)
                .emailTo(empresa.getE_mail2())
                .subject("Acesso Telebrasília")
                .text(empresa.getDsNoFantas() + " \n \r\n" + "Usuário: " + empresa.getCnpj() +
                        " \n\r\nSenha: " + empresa.getSenha() + " \n\r\nClique no link e faça login " + WWW)
                .build();

        BeanUtils.copyProperties(emailDTOSend, email);
        return send(email);
    }


    public Email send(Email email) {
        email.setSendDateEmail(LocalDateTime.now());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email.getEmailFrom());
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            emailSender.send(message);

            email.setStatus(EmailStatusEnum.SENT);
        } catch (Exception e) {
            email.setStatus(EmailStatusEnum.ERROR);
        }
        return emailRepository.save(email);

    }
}
