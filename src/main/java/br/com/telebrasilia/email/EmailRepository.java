package br.com.telebrasilia.email;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author  Romerito Alencar
 */

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

 
}