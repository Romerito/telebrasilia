package br.com.telebrasilia.chamado;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.telebrasilia.empresa.Empresa;
import nonapi.io.github.classgraph.utils.Join;

public class ChamadoRepositoryImpl {

    private EntityManager entityManager;

    public ChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Chamado> getChamados(String stProtocolo, String nuProtocolo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Chamado> query = criteriaBuilder.createQuery(Chamado.class);
        Root<Chamado> chamado = query.from(Chamado.class);
   //     Root<Empresa> empresa = query.from(Empresa.class);
      //  Join.join(empresa.get("idEmpresa"), chamado.get("idEmpresa"));
        query.select(chamado).distinct(true);
        TypedQuery<Chamado> typedQuery = entityManager.createQuery(query);
        List<Chamado> resultList = typedQuery.getResultList();
        return resultList;
    }

}
