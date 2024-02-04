package br.com.telebrasilia.enums;

import java.util.EnumMap;

/**
 * @author Romerito Alencar
 */
public enum ProtocoloStatusEnum {
    ABERTO("Aberto"), EM_EXECUCAO("Em execução"), FINALIZADO("Finalizado");

    private String stProtocolo;

    ProtocoloStatusEnum(String stProtocolo) {
        this.stProtocolo = stProtocolo;
    }

    public static String getStProtocolo(ProtocoloStatusEnum stProtocolo) {
        EnumMap<ProtocoloStatusEnum, String> stProtocoloMap = new EnumMap<>(ProtocoloStatusEnum.class);
        stProtocoloMap.put(ABERTO, "Aberto");
        stProtocoloMap.put(EM_EXECUCAO, "Em execução");
        stProtocoloMap.put(FINALIZADO, "Finalizado");
        return stProtocoloMap.get(stProtocolo);
    }

}
