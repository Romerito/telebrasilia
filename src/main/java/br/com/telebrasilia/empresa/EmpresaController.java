package br.com.telebrasilia.empresa;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class EmpresaController {

    private static final Logger LOGGER = LogManager.getLogger(EmpresaController.class);
    
    @Autowired
    EmpresaService empresaService;

    @Autowired
    EmailService emailService;

    private Empresa empresa = new  Empresa();

    public EmpresaController(EmpresaService empresaService, EmailService emailService) {
        this.empresaService = empresaService;
        this.emailService = emailService;
    }

    /**
     * @param LoginDTO
     * @return EmpresaResponse
    */
    @ApiOperation(value = "Consultar por cnpj")
        @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Senha consultada", response = Empresa.class),
        @ApiResponse(code = 400, message = "Erro ao consultar senha")
    })
    @PostMapping(path = "/empresa/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> findByCNPJ(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            LOGGER.info("Consulting ... CNPJ {} " ,  loginDTO.getCnpj());
            BeanUtils.copyProperties(loginDTO, empresa);
            this.empresa = empresaService.findByCNPJ(empresa);
            emailService.send(empresa);
            empresa.add(linkTo(methodOn((EmpresaController.class)).findByCNPJ(loginDTO)).withSelfRel());
            LOGGER.info("Consulted ... CNPJ {} " ,  empresa.getCnpj());
            return EmpresaResponse.responseBuilder(HttpStatus.OK,  empresa);
        } catch (Exception e) {
            LOGGER.info("CNPJ ... {} " , loginDTO.getCnpj());
            LOGGER.info("Error ... {} " , e.getMessage());
            return EmpresaResponse.responseBuilder(HttpStatus.BAD_REQUEST, loginDTO);
        }
    }

    /**
     * @param LoginDTO
     * @return EmpresaResponse
     */
    @ApiOperation(value = "Consultar por cnpj e senha")
        @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Login consultado", response = Empresa.class),
        @ApiResponse(code = 400, message = "Erro ao consultar login")
    })
    @PostMapping(path = "/login/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> findEmpresaByCNPJAndSenha(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            LOGGER.info("Consulting ... Login {} " ,  loginDTO.getCnpj());
            this.empresa = empresaService.findByCNPJAndSenha(loginDTO.getCnpj(), loginDTO.getSenha());
            empresa.add(linkTo(methodOn((EmpresaController.class)).findEmpresaByCNPJAndSenha(loginDTO)).withSelfRel());
            LOGGER.info("Consulted ... Login {} " ,  empresa.getCnpj());
            return EmpresaResponse.responseBuilder(HttpStatus.OK,  empresa);
        } catch (Exception e) {
            LOGGER.info("Login ... {} " , loginDTO.getCnpj());
            LOGGER.info("Error ... {} " , e.getMessage());
            return EmpresaResponse.responseBuilder(HttpStatus.BAD_REQUEST, loginDTO);
        }
    }

}
