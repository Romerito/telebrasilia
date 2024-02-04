package br.com.telebrasilia.empresaDadosad;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.telebrasilia.responses.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Romerito Alencar
 */
@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EmpresaDadosadController {

    private static final Logger LOGGER = LogManager.getLogger(EmpresaDadosadController.class);

    @Autowired
    EmpresaDadosadService empresaDadosadService;

    private List<EmpresaDadosad> listaEmpresaDadosad = new ArrayList<>();

    private EmpresaDadosad empresaDadosad = new EmpresaDadosad();

    /**
     * @param idEmpresa
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Consultar empresa dadosad")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Chamados consultados", response = EmpresaDadosad.class),
            @ApiResponse(code = 400, message = "Erro ao consultar chamados")})
    @GetMapping(path = "/empresadadosad/{idEmpresa}", produces = "application/json")
    public ResponseEntity<Object> consultarChamado(@PathVariable Long idEmpresa) {
        try {
            LOGGER.info("Consultando ... Dadosad....Empresa ID {} ", idEmpresa);

            List<EmpresaDadosad> empresaDadosad = new ArrayList<>();
            listaEmpresaDadosad = empresaDadosadService.consultarEmpresaDadosad(idEmpresa);

            listaEmpresaDadosad.forEach(dadosad -> {
                dadosad.add(linkTo(methodOn((EmpresaDadosadController.class)).consultarChamado(idEmpresa)).withSelfRel());
                empresaDadosad.add(dadosad);
            });

            LOGGER.info("Consultado ... Empresa dadosad .... {} ", empresaDadosad);
            return Response.responseBuilder(HttpStatus.OK, empresaDadosad);
        } catch (Exception e) {
            LOGGER.info("Consultar  ... Dadosad....Empresa ID  {} ...", idEmpresa);
            LOGGER.info("Error ... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, idEmpresa);
        }
    }

}
