package br.com.telebrasilia.protocolo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Romerito Alencar
 */

@Repository
public interface ProtocoloRepository extends JpaRepository<Protocolo, Long> {
 
}