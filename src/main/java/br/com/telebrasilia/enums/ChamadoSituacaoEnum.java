package br.com.telebrasilia.enums;

import java.util.EnumMap;

/**
 * @author Romerito Alencar
 */
public enum ChamadoSituacaoEnum {
    RESOLVIDO("Resolvido"), NAO_RESOLVIDO("Não resolvido");

    @SuppressWarnings("unused")
    private String situacao;

    ChamadoSituacaoEnum(String situacao) {
        this.situacao = situacao;
    }

    public static String getSituacao(ChamadoSituacaoEnum chamadoSituacao) {
        EnumMap<ChamadoSituacaoEnum, String> situacaoMap = new EnumMap<>(ChamadoSituacaoEnum.class);
        situacaoMap.put(RESOLVIDO, "Resolvido");
        situacaoMap.put(NAO_RESOLVIDO, "Não resolvido");
        return situacaoMap.get(chamadoSituacao);
    }

}