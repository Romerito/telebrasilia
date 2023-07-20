package br.com.telebrasilia.chamado;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ChamadoRepositoryImpl {

    private EntityManager entityManager;

    public ChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Chamado> getChamados(String stProtocolo, String nuProtocolo) {
      /*   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Chamado> query = criteriaBuilder.createQuery(Chamado.class);
        Root<Chamado> root = query.from(Chamado.class);
   
        Join<Chamado, Empresa> idEmpresa = root.join("id_empresa", JoinType.INNER); */
       // Join<Book, Genre> genre = root.join("genres");
        
      
       String query = " select * ( select distinct(p.id_protocolo), p.* from abertura_chamado c join empresa e on  c.id_empresa = e.id_empresa join protocolo_atendimento p on  p.cpf_cnpj = e.cnpj ) ";
      
        Query typedQuery = entityManager.createQuery(query);
        List<Chamado> resultList = typedQuery.getResultList();
        return resultList;
    }

}
