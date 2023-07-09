package br.com.telebrasilia.email;


import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.telebrasilia.dtos.EmailDTO;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.enums.EmailStatus;


/**
 * @author Romerito Alencar
 */

@Service
public class EmailService {

  @Autowired
  EmailRepository emailRepository;

  @Autowired
  JavaMailSender emailSender;


   RestTemplate restTemplate = new RestTemplate();

    private static String emailFrom  = "romerito.alencar@gmail.com";
    
    public Email send(Empresa empresa) {
      Email email = new Email();
      EmailDTO emailDTOSend = EmailDTO.builder()
                .ownerRef(empresa.getDsNoFantas())
                .emailFrom(emailFrom)
                .emailTo(empresa.getEmail2())
                .subject("Senha Telebrasília")
                .text( empresa.getDsNoFantas()  + " \n \r\n" + "Usuário: " + empresa.getCnpj() + " \n\r\n Senha : " + empresa.getSenha() + " \n\r\n " + "http://wwww.telebrasilia.com.br")
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
