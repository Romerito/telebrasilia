package br.com.telebrasilia.email;


import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.telebrasilia.chamado.Chamado;
import br.com.telebrasilia.dtos.EmailDTO;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.enums.EmailStatus;
import br.com.telebrasilia.protocolo.Protocolo;


/**
 * @author Romerito Alencar
 */

@Service
public class EmailService {

  @Autowired
  EmailRepository emailRepository;

  @Autowired
  JavaMailSender emailSender;


    private static String emailFrom  = "noc@telebrasilia.com";
    private static String www  = "http://pw.telebrasilia.com/portalweb";

    public Email responderChamado(Protocolo protocolo, Empresa empresa, Chamado chamado) {
      Email emailCliente = new Email();
      EmailDTO emailEmpresaSend = EmailDTO.builder()
                .ownerRef("Telebrasília")
                .emailFrom(emailFrom)
                .emailTo(empresa.getE_mail2())
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text("Protocolo de atendimento  "  + protocolo.getNuProtocolo() + " \n\r\nTipo do chamado: " + 
                		chamado.getTpChamado() + "\n\r\nStatus do chamado: " + protocolo.getStProtocolo() + 
                		"\n\r\n Clique no link para acompanhar  " + www)
                .build();
        
      BeanUtils.copyProperties(emailEmpresaSend, emailCliente);
      return  send(emailCliente);
    }

    
    public Email send(Protocolo protocolo, Empresa empresa, Chamado chamado) {
      Email emailTelebrasilia = new Email();
      EmailDTO emailTelebrasiliaSend = EmailDTO.builder()
                .ownerRef(empresa.getDsNoFantas())
                .emailFrom(emailFrom)
                .emailTo(emailFrom)
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text(empresa.getDsNoFantas()  + " \n\r\n" + "Número do protocolo: " + 
                		protocolo.getNuProtocolo() + " \n\r\nTipo do chamado: " + chamado.getTpChamado() + 
                		"\n\r\nStatus do chamado: " + protocolo.getStProtocolo() + 
                		"\n\r\n Clique no link para respnder " + www)
                .build();
        
      BeanUtils.copyProperties(emailTelebrasiliaSend, emailTelebrasilia);
      send(emailTelebrasilia);
    
      Email emailCliente = new Email();
      EmailDTO emailEmpresaSend = EmailDTO.builder()
                .ownerRef("Telebrasília")
                .emailFrom(emailFrom)
                .emailTo(empresa.getE_mail2())
                .subject("PROTOCOLO TELEBRASÍLIA " + protocolo.getNuProtocolo())
                .text("Protocolo de atendimento  "  + protocolo.getNuProtocolo() + 
                		" \n\r\nTipo do chamado: " + chamado.getTpChamado() + "\n\r\nStatus do chamado: " + 
                		protocolo.getStProtocolo() + "\n\r\n Clique no link para acompanhar  " + www)
                .build();
        
      BeanUtils.copyProperties(emailEmpresaSend, emailCliente);
      return  send(emailCliente);
    
    }

    
    public Email send(Empresa empresa) {
      Email email = new Email();
      EmailDTO emailDTOSend = EmailDTO.builder()
                .ownerRef(empresa.getDsNoFantas())
                .emailFrom(emailFrom)
                .emailTo(empresa.getE_mail2())
                .subject("Acesso Telebrasília")
                .text( empresa.getDsNoFantas()  + " \n \r\n" + "Usuário: " + empresa.getCnpj() + 
                		" \n\r\nSenha: " + empresa.getSenha() + " \n\r\nClique no link e faça login " + www)
                .build();
        
      BeanUtils.copyProperties(emailDTOSend, email);
      return  send(email);
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

          email.setStatus(EmailStatus.SENT);
        } catch (Exception e) {
          email.setStatus(EmailStatus.ERROR);
        } 
        return emailRepository.save(email);

  }
}
