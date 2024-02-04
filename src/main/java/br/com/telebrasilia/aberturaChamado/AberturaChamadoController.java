package br.com.telebrasilia.aberturaChamado;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @author Romerito Alencar
 */

@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AberturaChamadoController {

    private static final Logger LOGGER = LogManager.getLogger(AberturaChamadoController.class);

    @Autowired
    AberturaChamadoService chamadoService;

    @Autowired
    EmailService emailService;

    private AberturaChamado chamado = new AberturaChamado();

    private List<ChamadoDTO> listChamadoDTO = new ArrayList<>();

    private ChamadoDTO chamadoDTO;
    private Integer totalProtocolo;

    public AberturaChamadoController(AberturaChamadoService chamadoService, EmailService emailService) {
        this.chamadoService = chamadoService;
        this.emailService = emailService;
    }

    /**
     * @param files,tpChamado,dsChamado,idEmpresa,noArquivo
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Criar chamado")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Chamado criado com anexo", response = AberturaChamado.class),
            @ApiResponse(code = 400, message = "Erro ao criar chamado")})
    @PostMapping(path = "/chamado-anexo/")
    public ResponseEntity<Object> criar(@RequestParam("files") MultipartFile[] files,
            @RequestParam("tpChamado") String tpChamado, @RequestParam("dsChamado") String dsChamado,
            @RequestParam("idEmpresa") Long idEmpresa, @RequestParam("noArquivo") String noArquivo,
            @RequestParam("idEmprad") Long idEmprad) {
        try {
            LOGGER.info("Saveing ... Chamado {} ... Empresa {} ", dsChamado, idEmpresa);
            chamado = chamadoService.criar(files, tpChamado, dsChamado, idEmpresa, noArquivo, idEmprad);
            chamado.add(linkTo(
                    methodOn((AberturaChamadoController.class)).criar(files, tpChamado, dsChamado, idEmpresa, noArquivo,
                            idEmprad))
                    .withSelfRel());
            LOGGER.info("Saved ... Chamado Idchamado... {} ", chamado.getIdChamado());
            return Response.responseBuilder(HttpStatus.OK, chamado);
        } catch (Exception e) {
            LOGGER.info("Chamado ... {} ", tpChamado);
            LOGGER.info("Error ... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, tpChamado);
        }
    }

    /**
     * @param files,tpChamado,idEmpresa,stProtocolo,scChamado,dsChamado,noArquivo,nuProtocolo,idChamado
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Reponder chamado")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Chamado criado", response = AberturaChamado.class),
            @ApiResponse(code = 400, message = "Erro ao criar chamado")})
    @PostMapping(path = "/responder-anexo/")
    public ResponseEntity<Object> reponder(@RequestParam("files") MultipartFile[] files,
            @RequestParam("tpChamado") String tpChamado, @RequestParam("dsChamado") String dsChamado,
            @RequestParam("idEmpresa") Long idEmpresa, @RequestParam("noArquivo") String noArquivo,
            @RequestParam("stProtocolo") String stProtocolo, @RequestParam("nuProtocolo") String nuProtocolo,
            @RequestParam("scChamado") String scChamado, @RequestParam("idChamado") Long idChamado,
            @RequestParam("idEmprad") Long idEmprad) {
        try {
            LOGGER.info("Respondendo ... Chamado {} ... Empresa {} ", dsChamado, idEmpresa);
            chamado = chamadoService.reponder(files, tpChamado, dsChamado, idEmpresa,
                    noArquivo, stProtocolo, scChamado, nuProtocolo, idChamado, idEmprad);
            chamado.add(linkTo(methodOn((AberturaChamadoController.class)).reponder(files, tpChamado, dsChamado,
                    idEmpresa, noArquivo, stProtocolo, scChamado, nuProtocolo, idChamado, idEmprad)).withSelfRel());
            LOGGER.info("Saved ... Chamado Idchamado... {} ", chamado.getIdChamado());
            return Response.responseBuilder(HttpStatus.OK, chamado);
        } catch (Exception e) {
            LOGGER.info("Chamado ... {} ", tpChamado);
            LOGGER.info("Error ... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, tpChamado);
        }
    }

    /**
     * @param chamadoDTO
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Consultar chamados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Chamados consultados", response = AberturaChamado.class),
            @ApiResponse(code = 400, message = "Erro ao consultar chamados")})
    @PostMapping(path = "/chamados/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> consultar(@RequestBody @Valid ChamadoDTO chamadoDTO) {
        try {
            LOGGER.info("Consultando ... Chamados .... Empresa {} ", chamadoDTO.getIdEmpresa());
            listChamadoDTO = chamadoService.consultar(chamadoDTO);
            chamado.add(linkTo(methodOn((AberturaChamadoController.class)).consultar(chamadoDTO)).withSelfRel());
            LOGGER.info("Consultado ... Chamados {} ", listChamadoDTO);
            return Response.responseBuilder(HttpStatus.OK, listChamadoDTO);
        } catch (Exception e) {
            LOGGER.info("Consultar chamados ... {} ... Empresa ", chamadoDTO.getIdEmpresa());
            LOGGER.info("Error ... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, chamadoDTO);
        }
    }

    /**
     * @param idProtocolo,noArquivo
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Carregar arquivo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Chamados consultados", response = AberturaChamado.class),
            @ApiResponse(code = 400, message = "Erro ao carregar arquivo")})
    @GetMapping(path = "/file/{idProtocolo}/{noArquivo}")
    public ResponseEntity<Resource> carregarArquivo(@PathVariable Long idProtocolo, @PathVariable String noArquivo) {
        try {
            LOGGER.info("Carregando arquivo ... {} ", noArquivo);
            chamadoDTO = new ChamadoDTO();
            chamadoDTO.setIdProtocolo(idProtocolo);
            chamadoDTO.setNoArquivo(noArquivo);
            Resource file = this.chamadoService.carregarArquivo(chamadoDTO);
            Path path = file.getFile().toPath();

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);

        } catch (Exception e) {
            LOGGER.info("Carregando arquivo ... {} ", noArquivo);
            LOGGER.info("Error ... {} ", e.getMessage());
            return null;
        }
    }

}
