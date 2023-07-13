package br.com.telebrasilia.protocolo;

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

import br.com.telebrasilia.dtos.ProtocoloDTO;
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
public class ProtocoloController {

    private static final Logger LOGGER = LogManager.getLogger(ProtocoloController.class);
    
    @Autowired
    ProtocoloService protocoloService;

    public ProtocoloController(ProtocoloService protocoloService) {
        this.protocoloService = protocoloService;
    }

    Protocolo protocolo = new Protocolo();

    /**
     * @param ProtocoloDTO
     * @return Response
    */
    @ApiOperation(value = "Salva o protocolo")
        @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Protocolo criado", response = Protocolo.class),
        @ApiResponse(code = 400, message = "Erro ao criar protocolo")
    })
    @PostMapping(path = "/protocolo/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> save(@RequestBody @Valid ProtocoloDTO protocoloDTO) {
        try {
            LOGGER.info("Saveing  Protocolo do solicitante... {}",  protocoloDTO.getNoSolicitante());
            BeanUtils.copyProperties(protocoloDTO, protocolo);
            protocolo = protocoloService.save(protocolo);
            protocolo.add(linkTo(methodOn((ProtocoloController.class)).save(protocoloDTO)).withSelfRel());
            LOGGER.info("Saved...  Protocolo {} ",  protocoloDTO.getNuProtocolo());
            return Response.responseBuilder(HttpStatus.CREATED,  protocolo);
        } catch (Exception e) {
            LOGGER.info("Protocolo do solicitante... {}",  protocoloDTO.getNoSolicitante());
            LOGGER.info("Error ... {} " , e.getMessage());
            return Response.responseBuilder(HttpStatus.BAD_REQUEST, protocoloDTO);
        }
    }

}
