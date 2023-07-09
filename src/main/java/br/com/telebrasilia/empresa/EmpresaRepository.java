package br.com.telebrasilia.empresa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author  Romerito Alencar
 */

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query("select e from Empresa e where e.cnpj = :cnpj")
    Empresa findByCNPJ(String cnpj);
 
}