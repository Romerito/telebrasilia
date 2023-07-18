package br.com.telebrasilia.chamado;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.email.EmailService;
import br.com.telebrasilia.responses.Response;
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

    private List<Chamado> chamados = new ArrayList<>();

    public ChamadoController(ChamadoService chamadoService, EmailService emailService) {
        this.chamadoService = chamadoService;
        this.emailService = emailService;
    }

    /**
     * @param ChamadoDTO
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Criar chamado")
        @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Chamado criado", response = Chamado.class),
        @ApiResponse(code = 400, message = "Erro ao criar chamado")
    })
    @PostMapping(path = "/chamado/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody @Valid ChamadoDTO chamadoDTO) {
        try {
            LOGGER.info("Saveing ... Chamado {} " ,  chamadoDTO);
            BeanUtils.copyProperties(chamadoDTO, chamado);
            chamado = chamadoService.save(chamado);
            chamado.add(linkTo(methodOn((ChamadoController.class)).save(chamadoDTO)).withSelfRel());
            LOGGER.info("Saved ... Chamado {} " ,  chamado);
            return Response.responseBuilder(HttpStatus.OK,  chamado);
        } catch (Exception e) {
            LOGGER.info("Chamado ... {} " , chamadoDTO);
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, chamadoDTO);
        }
    }

     /**
     * @param ChamadoDTO
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Consultar chamados")
        @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Chamados consultados", response = Chamado.class),
        @ApiResponse(code = 400, message = "Erro ao consultar chamados")
    })
    @GetMapping(path = "/chamado/")
    public ResponseEntity<Object> getChamados(@RequestParam(name = "stProtocolo") String stProtocolo, @RequestParam(name = "nuProtocolo") String nuProtocolo) {
        try {
            LOGGER.info("Consultando ... Chamados {} " ,  nuProtocolo);
            chamados = chamadoService.getChamados(stProtocolo, nuProtocolo);
            for (Chamado chamado : chamados) {
                System.out.println("Tipo do chamado: " + chamado.getTpChamado());
            }
         //   chamado.add(linkTo(methodOn((ChamadoController.class)).getChamados(stProtocolo, nuProtocolo)).withSelfRel());
           // LOGGER.info("Consultado ... Chamados {} " ,  chamados.toString());
            return Response.responseBuilder(HttpStatus.OK,  nuProtocolo);
        } catch (Exception e) {
            LOGGER.info("Consultar chamados ... {} " , nuProtocolo);
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, nuProtocolo);
        }
    }
    
}
