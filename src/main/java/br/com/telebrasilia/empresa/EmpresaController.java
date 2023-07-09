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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.telebrasilia.dtos.EmpresaDTO;
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

    public EmpresaController(EmpresaService empresaService, EmailService emailService) {
        this.empresaService = empresaService;
        this.emailService = emailService;
    }

    /**
     * @param EmpresaDTO
     * @return EmpresaResponse
    */
    @ApiOperation(value = "Consultar por CNPJ")
        @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Senha consultada", response = Empresa.class),
        @ApiResponse(code = 400, message = "Erro ao consultar senha")
    })
    @GetMapping(path = "/empresa/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> findByCNPJ(@RequestBody @Valid EmpresaDTO empresaDTO) {
        try {
            LOGGER.info("Consulting ... CNPJ {} " ,  empresaDTO.getCnpj());
            Empresa empresa = new Empresa();
            BeanUtils.copyProperties(empresaDTO, empresa);
            empresa = empresaService.findByCNPJ(empresa);
            emailService.send(empresa);
            empresa.add(linkTo(methodOn((EmpresaController.class)).findByCNPJ(empresaDTO)).withSelfRel());
            LOGGER.info("Consulted ... CNPJ {} " ,  empresaDTO.getCnpj());
            return EmpresaResponse.responseBuilder(HttpStatus.OK,  empresa);
        } catch (Exception e) {
            LOGGER.info("CNPJ ... {} " , empresaDTO.getCnpj());
            LOGGER.info("Error ... {} " , e.getMessage());
            return EmpresaResponse.responseBuilder(HttpStatus.BAD_REQUEST, empresaDTO);
        }
    }

}
