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
        
        Join<Chamado, Empresa> idEmpresa = root.join("idEmpresa");
        Join<Chamado, Protocolo> idProtocolo = root.join("idProtocolo");
      
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(chamadoDTO.getIdEmpresa());
 
        if(chamadoDTO.getNuProtocolo() == null && chamadoDTO.getStProtocolo() == null){
         criteriaQuery
                .multiselect(root, idEmpresa, idProtocolo)
                .where(
                        criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()));
        }
        
        if(chamadoDTO.getNuProtocolo() != null && chamadoDTO.getStProtocolo() == null){
           criteriaQuery
                .multiselect(root, idEmpresa, idProtocolo)
                .where(
                        criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                        criteriaBuilder.like((idProtocolo.get("nuProtocolo").as(String.class)), "%" +  chamadoDTO.getNuProtocolo() + "%" ));
        }

          if(chamadoDTO.getNuProtocolo() == null && chamadoDTO.getStProtocolo() != null){
           criteriaQuery
                .multiselect(root, idEmpresa, idProtocolo)
                .where(
                        criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                         criteriaBuilder.equal(idProtocolo.get("stProtocolo"), chamadoDTO.getStProtocolo()));
        }
        
        if(chamadoDTO.getNuProtocolo() != null && chamadoDTO.getStProtocolo() != null){
            criteriaQuery
                .multiselect(root, idEmpresa, idProtocolo)
                .where(
                        criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                        criteriaBuilder.like((idProtocolo.get("nuProtocolo").as(String.class)), "%" +  chamadoDTO.getNuProtocolo() + "%" ),
                        criteriaBuilder.equal(idProtocolo.get("stProtocolo"), chamadoDTO.getStProtocolo()));
        }
      
         return  entityManager.createQuery(criteriaQuery).getResultList();
    }

}
