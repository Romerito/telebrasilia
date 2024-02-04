package br.com.telebrasilia.aberturaChamado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Romerito Alencar
 */
@Repository
public interface AberturaChamadoRepository extends JpaRepository<AberturaChamado, Long> {

}