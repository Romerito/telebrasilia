package br.com.telebrasilia.chamado;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.protocolo.Protocolo;

public class ChamadoRepositoryImpl {

    private EntityManager entityManager;

    public ChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Tuple> getChamados(ChamadoDTO chamadoDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        
        Root<Chamado> root = criteriaQuery.from(Chamado.class);

        Join<Empresa, Protocolo> idEmpresa = root.join("idEmpresa"); 
      
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(chamadoDTO.getIdEmpresa());
 
        criteriaQuery
                .multiselect(root, idEmpresa)
                .where(
                        criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa())); 

        List<Tuple> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return  resultList;
    }

}
