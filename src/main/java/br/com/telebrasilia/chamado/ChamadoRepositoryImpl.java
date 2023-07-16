package br.com.telebrasilia.chamado;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.protocolo.Protocolo;

public class ChamadoRepositoryImpl {

    private EntityManager entityManager;

    public ChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Chamado>  getChamados(String stProtocolo, String nuProtocolo) {
     
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();

        CriteriaQuery query = criteriaBuilder.createQuery(Chamado.class);

        Root<Chamado> chamado = query.from(Chamado.class);
        
        List<Predicate> predicates = new ArrayList<>();

          predicates.add(criteriaBuilder.equal(chamado.join(Empresa.class, chamado));

        if (!stProtocolo.isEmpty()) {
            predicates.add(criteriaBuilder.equal(chamado.get("stProtocolo"), stProtocolo));
        }

        if (!nuProtocolo.isEmpty()) {
            predicates.add(criteriaBuilder.like(chamado.get("nuProtocolo"), nuProtocolo));
        }

        if(!predicates.isEmpty()){
            query.where(predicates.stream().toArray(Predicate[]::new));
        }

        TypedQuery<Chamado> queryResult = this.entityManager.createQuery(query);


        return queryResult.getResultList();
    }

    

}
