package br.com.telebrasilia.protocoloAtendimento;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Romerito Alencar
 */

@Repository
public interface ProtocoloAtendimentoRepository extends JpaRepository<ProtocoloAtendimento, Long> {

    @Query(" select count(p) from ProtocoloAtendimento p where p.nuProtocolo = :nuProtocolo ")
    Integer contAllByNuProtocolo(String nuProtocolo);

    @Query(" select count(*) from ProtocoloAtendimento p join AberturaChamado a ON  a.idProtocolo.idProtocolo = p.idProtocolo WHERE p.stProtocolo = 'FINALIZADO' AND  p.nuProtocolo = :nuProtocolo ")
    Integer verifyProtocoloFinalizado(String nuProtocolo);


    @Query(" select p.dtAbertura from ProtocoloAtendimento p where p.nuProtocolo = :nuProtocolo and p.dtExecucao is null and p.dtSolucao is null ")
    Date findByDtAbertura(String nuProtocolo);
}