package br.com.telebrasilia.chamado;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.empresa.Empresa;

public class ChamadoRepositoryImpl {

    private EntityManager entityManager;

    public ChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Chamado> getChamados(ChamadoDTO chamadoDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Chamado> root = criteriaQuery.from(Chamado.class);
        Join<Chamado, Empresa> empresa = root.join("idEmpresa"); 


        criteriaQuery
                .multiselect(root, empresa)
                .where(
                        criteriaBuilder.equal(empresa.get("idEmpresa"), chamadoDTO.getIdEmpresa()));
        
        Query typedQuery = entityManager.createQuery(criteriaQuery);
        List<Chamado> resultList = typedQuery.getResultList();
        return resultList;
    }

}
