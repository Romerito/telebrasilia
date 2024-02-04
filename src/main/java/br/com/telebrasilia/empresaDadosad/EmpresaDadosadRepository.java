package br.com.telebrasilia.empresaDadosad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.telebrasilia.empresa.Empresa;

/**
 * @author Romerito Alencar
 */
@Repository
public interface EmpresaDadosadRepository extends JpaRepository<EmpresaDadosad, Long> {

    @Query("select e from EmpresaDadosad e where e.idEmpresa = :idEmpresa")
    List<EmpresaDadosad> findByIdEmpresaDadosad(Long idEmpresa);
}
