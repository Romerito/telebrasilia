package br.com.telebrasilia.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Romerito Alencar
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query("select e from Empresa e where e.cnpj = :cnpj")
    Empresa findEmpresaByCNPJ(String cnpj);

    @Query("select e from Empresa e where e.cnpj = :cnpj and e.senha = :senha")
    Empresa findEmpresaByCNPJAndSenha(String cnpj, String senha);

    @Query("select e from Empresa e where e.idEmpresa = :idEmpresa")
    Empresa findEmpresaByIdEmpresa(Long idEmpresa);

    @Query("select e from Empresa e join AberturaChamado c on c.idEmpresa = e where c.idChamado = :idChamado")
    Empresa findEmpresaByChamado(Long idChamado);

    @Query("select e from Empresa e where e.cnpj = :cnpj")
    Empresa findEmpresaByCnpj(String cnpj);


}