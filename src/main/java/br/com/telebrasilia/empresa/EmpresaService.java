package br.com.telebrasilia.empresa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author  Romerito Alencar
 */

@Service
public class EmpresaService {
    
    @Autowired
    EmpresaRepository empresaRepository;
    
    RestTemplate restTemplate = new RestTemplate();

/*     private static String url = "http://localhost:8082/api/email/";
    private static String emailFrom  = "romerito.alencar@gmail.com"; */
    
    public Empresa findByCNPJ(Empresa empresa){
      
      /*   restTemplate = new RestTemplate();
        
        EmailDTO emailDTOGet = EmailDTO.builder()
              //  .ownerRef(empresa.getName())
                .emailFrom(emailFrom)
                .emailTo(emailFrom)
              //  .subject(empresa.getSubject())
               // .text(empresa.getName()  +" - "+ empresa.getPhone()  +" - "+ empresa.getEmail() +" \n \r\n" + empresa.getMessage())
                .build();

        restTemplate.postForEntity(url, emailDTOGet, String.class);
        EmailDTO emailDTOSend = EmailDTO.builder()
         //       .ownerRef(empresa.getName())
                .emailFrom(emailFrom)
           //     .emailTo(empresa.getEmail())
                .subject("Alencar Web")
                .text("Olá sou Romerito. Já recebi seu email e estou analisando. Em breve entrarei enempresa. \n \r\n" + "Obrigado pela preferência. \n \r\n" + "Atenciosamente,")
                .build();
        
        restTemplate.postForEntity(url,emailDTOSend, String.class);
         */
        return  empresaRepository.findByCNPJ(empresa.getCnpj());
     }

}
