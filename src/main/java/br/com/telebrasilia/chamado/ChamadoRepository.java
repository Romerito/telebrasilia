package br.com.telebrasilia.chamado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Romerito Alencar
 */

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long>{

}