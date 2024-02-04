package br.com.telebrasilia.aberturaChamado;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import br.com.telebrasilia.dtos.ChamadoDTO;
import br.com.telebrasilia.empresa.Empresa;
import br.com.telebrasilia.protocoloAtendimento.ProtocoloAtendimento;

/**
 * @author Romerito Alencar
 */
public class AberturaChamadoRepositoryImpl {

    private EntityManager entityManager;

    public AberturaChamadoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Tuple> getChamados(ChamadoDTO chamadoDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

        Root<AberturaChamado> root = criteriaQuery.from(AberturaChamado.class);

        Join<AberturaChamado, Empresa> idEmpresa = root.join("idEmpresa");
        Join<AberturaChamado, ProtocoloAtendimento> idProtocolo = root.join("idProtocolo");

        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(chamadoDTO.getIdEmpresa());

        if (chamadoDTO.getNuProtocolo() == null && chamadoDTO.getIdEmpresa() == 3 && chamadoDTO.getStProtocolo() == null) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getNuProtocolo() == null && chamadoDTO.getIdEmpresa() == 3 && chamadoDTO.getStProtocolo() != null
                && chamadoDTO.getStProtocolo().equalsIgnoreCase("Selecione")) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getNuProtocolo() == null && chamadoDTO.getIdEmpresa() == 3 && chamadoDTO.getStProtocolo() != null
                && !chamadoDTO.getStProtocolo().equalsIgnoreCase("Selecione")) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .where(criteriaBuilder.equal(idProtocolo.get("stProtocolo"), chamadoDTO.getStProtocolo()))
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getNuProtocolo() == null && chamadoDTO.getStProtocolo() == null && chamadoDTO.getIdEmpresa() != 3) {
            criteriaQuery.multiselect(root, idEmpresa, idProtocolo)
                    .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()))
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getStProtocolo() != null && chamadoDTO.getNuProtocolo() == null) {
            if (chamadoDTO.getStProtocolo().equalsIgnoreCase("Selecione") && chamadoDTO.getIdEmpresa() != 3) {
                criteriaQuery.multiselect(root, idEmpresa, idProtocolo)
                        .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()))
                        .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
            }

        }

        if (chamadoDTO.getStProtocolo() != null && chamadoDTO.getNuProtocolo() != null) {
            if (chamadoDTO.getStProtocolo().equalsIgnoreCase("Selecione") && chamadoDTO.getNuProtocolo() != null) {
                criteriaQuery
                        .multiselect(root, idEmpresa, idProtocolo)
                        .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                                criteriaBuilder.like((idProtocolo.get("nuProtocolo").as(String.class)),
                                        "%" + chamadoDTO.getNuProtocolo() + "%"))
                        .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
            }

        }

        if (chamadoDTO.getNuProtocolo() != null && chamadoDTO.getStProtocolo() == null) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                            criteriaBuilder.like((idProtocolo.get("nuProtocolo").as(String.class)),
                                    "%" + chamadoDTO.getNuProtocolo() + "%"))
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getNuProtocolo() == null && chamadoDTO.getStProtocolo() != null && !chamadoDTO.getStProtocolo()
                .equalsIgnoreCase("Selecione") && chamadoDTO.getIdEmpresa() != 3) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                            criteriaBuilder.equal(idProtocolo.get("stProtocolo"), chamadoDTO.getStProtocolo()))
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        if (chamadoDTO.getNuProtocolo() != null && chamadoDTO.getStProtocolo() != null && !chamadoDTO.getStProtocolo()
                .equalsIgnoreCase("Selecione")) {
            criteriaQuery
                    .multiselect(root, idEmpresa, idProtocolo)
                    .where(criteriaBuilder.equal(idEmpresa.get("idEmpresa"), empresa.getIdEmpresa()),
                            criteriaBuilder.like((idProtocolo.get("nuProtocolo").as(String.class)),
                                    "%" + chamadoDTO.getNuProtocolo() + "%"),
                            criteriaBuilder.equal(idProtocolo.get("stProtocolo"), chamadoDTO.getStProtocolo()))
                    .orderBy(criteriaBuilder.desc(root.get("idProtocolo")));
        }

        return entityManager.createQuery(criteriaQuery).setFirstResult(chamadoDTO.getPageNumber() - 1).setMaxResults(10).getResultList();
    }

}
