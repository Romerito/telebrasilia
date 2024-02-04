package br.com.telebrasilia.protocoloAtendimento;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.telebrasilia.dtos.ProtocoloDTO;
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
public class ProtocoloAtendimentoController {

    private static final Logger LOGGER = LogManager.getLogger(ProtocoloAtendimentoController.class);

    @Autowired
    ProtocoloAtendimentoService protocoloService;

    public ProtocoloAtendimentoController(ProtocoloAtendimentoService protocoloService) {
        this.protocoloService = protocoloService;
    }

    /**
     * @param ProtocoloDTO
     * @return Response
     */
    @ApiOperation(value = "Salva o protocolo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Protocolo criado", response = ProtocoloAtendimento.class),
            @ApiResponse(code = 400, message = "Erro ao criar protocolo")
    })
    @PostMapping(path = "/protocolo/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody @Valid ProtocoloDTO protocoloDTO) {
        try {
            LOGGER.info("Saveing  Protocolo do solicitante... {}", protocoloDTO.getNoSolicitante());
            ProtocoloAtendimento protocolo = new ProtocoloAtendimento();
            BeanUtils.copyProperties(protocoloDTO, protocolo);
            protocolo = protocoloService.save(protocolo);
            protocolo.add(linkTo(methodOn((ProtocoloAtendimentoController.class)).save(protocoloDTO)).withSelfRel());
            LOGGER.info("Saved...  Protocolo {} ", protocoloDTO.getNuProtocolo());
            return Response.responseBuilder(HttpStatus.CREATED, protocolo);
        } catch (Exception e) {
            LOGGER.info("Protocolo do solicitante... {}", protocoloDTO.getNoSolicitante());
            LOGGER.info("Error ... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, protocoloDTO);
        }
    }

    /**
     * @param ProtocoloDTO
     * @return ChamadoResponse
     */
    @ApiOperation(value = "Verificar protocolo finalizado")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Verificar protocolo finalizado", response = ProtocoloAtendimento.class),
            @ApiResponse(code = 400, message = "Erro ao verificar protocolo finalizado")})
    @GetMapping(path = "/contar/{nuProtocolo}")
    public ResponseEntity<Object> verificarProtocoloFinalizado(@PathVariable String nuProtocolo) {
        try {
            LOGGER.info("Verificar protocolo finalizado");
            Integer finalizado = protocoloService.verificarProtocoloFinalizado(nuProtocolo);
            LOGGER.info("Protocolo {} Finalizado {}", nuProtocolo, finalizado);
            return Response.responseBuilder(HttpStatus.OK, finalizado);
        } catch (Exception e) {
            LOGGER.info("Protocolo .... {}", nuProtocolo);
            LOGGER.info("Error... {} ", e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, nuProtocolo);
        }
    }

}
