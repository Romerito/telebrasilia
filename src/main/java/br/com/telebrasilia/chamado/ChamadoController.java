package br.com.telebrasilia.chamado;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    private List<ChamadoDTO> listaChamadoDTOs = new ArrayList<>();

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
    @PostMapping(path = "/chamado/")
    public ResponseEntity<Object> criarChamado(@RequestParam("files") MultipartFile[] files, 
    @RequestParam("tpChamado") String tpChamado, @RequestParam("dsChamado") String dsChamado,
    @RequestParam("idEmpresa") Long idEmpresa, @RequestParam("noArquivo") String noArquivo) {
        try {
            LOGGER.info("Saveing ... Chamado {} ... Empresa {} " ,  dsChamado, idEmpresa);
            chamado = chamadoService.criarChamado(files, tpChamado, dsChamado, idEmpresa, noArquivo);
            chamado.add(linkTo(methodOn((ChamadoController.class)).criarChamado(files, tpChamado, dsChamado, idEmpresa, noArquivo)).withSelfRel());
            LOGGER.info("Saved ... Chamado Idchamado... {} " ,  chamado.getIdChamado());
            return Response.responseBuilder(HttpStatus.OK,  chamado);
        } catch (Exception e) {
            LOGGER.info("Chamado ... {} " , tpChamado);
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, tpChamado);
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
    @PostMapping(path = "/chamados/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> consultarChamado(@RequestBody @Valid ChamadoDTO chamadoDTO) {
        try {
            LOGGER.info("Consultando ... Chamados .... Empresa {} " ,  chamadoDTO.getIdEmpresa());
            listaChamadoDTOs = chamadoService.consultarChamado(chamadoDTO);
            chamado.add(linkTo(methodOn((ChamadoController.class)).consultarChamado(chamadoDTO)).withSelfRel());
            LOGGER.info("Consultado ... Chamados {} " ,  listaChamadoDTOs);
            return Response.responseBuilder(HttpStatus.OK,  listaChamadoDTOs);
        } catch (Exception e) {
            LOGGER.info("Consultar chamados ... {} ... Empresa " , chamadoDTO.getIdEmpresa());
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, chamadoDTO);
        }
    }

    
     /**
     * @param ChamadoDTO
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Responder chamado")
        @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Responder chamado", response = Chamado.class),
        @ApiResponse(code = 400, message = "Erro ao consultar chamados")
    })
    @PostMapping(path = "/responder/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> responderChamado(@RequestBody @Valid ChamadoDTO chamadoDTO) {
        try {
            LOGGER.info("Responder chamado  .... Empresa {} " ,  chamadoDTO.getIdEmpresa());
            chamado = chamadoService.responderChamado(chamadoDTO);
            chamado.add(linkTo(methodOn((ChamadoController.class)).responderChamado(chamadoDTO)).withSelfRel());
            LOGGER.info("Consultado ... Chamados {} " ,  chamado);
            return Response.responseBuilder(HttpStatus.OK,  chamado);
        } catch (Exception e) {
            LOGGER.info("Consultar chamados ... {} ... Empresa " , chamadoDTO.getIdEmpresa());
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, chamadoDTO);
        }
    }
    
}
