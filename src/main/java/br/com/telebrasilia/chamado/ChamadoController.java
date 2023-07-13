package br.com.telebrasilia.chamado;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.dtos.LoginDTO;
import br.com.telebrasilia.email.EmailService;
import br.com.telebrasilia.responses.EmpresaResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author  Romerito Alencar
 */

@Validated
@RestController
@RequestMapping("/api") 
@CrossOrigin(origins = "*")
public class ChamadoController {

    private static final Logger LOGGER = LogManager.getLogger(ChamadoController.class);
    
    @Autowired
    ChamadoService chamadoService;

    @Autowired
    EmailService emailService;

    private Chamado chamado = new  Chamado();

    public ChamadoController(ChamadoService chamadoService, EmailService emailService) {
        this.chamadoService = chamadoService;
        this.emailService = emailService;
    }

    /**
     * @param LoginDTO
     * @return EmpresaResponse
     */
    @ApiOperation(value = "Consultar por CNPJ e Senha")
        @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Login consultado", response = Chamado.class),
        @ApiResponse(code = 400, message = "Erro ao consultar login")
    })
    @PostMapping(path = "/login/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody @Valid ChamadoDTO chamadoDTO) {
        try {
            LOGGER.info("Consulting ... Login {} " ,  chamadoDTO);
      //      this.chamado = chamadoService.save(chamadoDTO);
            chamado.add(linkTo(methodOn((ChamadoController.class)).save(chamadoDTO)).withSelfRel());
         //   LOGGER.info("Consulted ... Login {} " ,  empresa.getCnpj());
            return EmpresaResponse.responseBuilder(HttpStatus.OK,  chamadoDTO);
        } catch (Exception e) {
           // LOGGER.info("Login ... {} " , loginDTO.getCnpj());
            LOGGER.info("Error ... {} " , e.getMessage());
            return EmpresaResponse.responseBuilder(HttpStatus.BAD_REQUEST, chamadoDTO);
        }
    }

}
