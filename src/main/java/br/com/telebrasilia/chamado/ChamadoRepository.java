package br.com.telebrasilia.chamado;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Romerito Alencar
 */

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
   
    // @Query(" select  p.*, c.* from abertura_chamado c join empresa e on  c.id_empresa = e.id_empresa join protocolo_atendimento p on e.cnpj = p.cpf_cnpj where p.st_protocolo = 'ABERTO' and p.nu_protocolo = 'P00020237162627';select e from Empresa e where e.cnpj = :cnpj and e.senha = :senha ")
    // Chamado findChamadoByCNPJAndnuProtocoloAndStChamado(String nuProtocolo, String stProtocolo);
}