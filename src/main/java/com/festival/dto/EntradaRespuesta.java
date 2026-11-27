package com.festival.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.festival.modelo.*;

public record EntradaRespuesta(Long id, TipoEntrada tipo, Long asistenteId, Long organizadorId,
                              Long eventoId, BigDecimal precio, String codigo,
                              LocalDateTime fechaCompra, LocalDateTime fechaUso) {
    public static EntradaRespuesta desde(Entrada entrada) {
        TipoEntrada tipo;
        String codigo;
        LocalDateTime fechaUso = null;
        if (entrada instanceof General general) {
            tipo = TipoEntrada.GENERAL;
            codigo = general.getCodigoAlfanumerico();
        } else if (entrada instanceof Vip vip) {
            tipo = TipoEntrada.VIP;
            codigo = vip.getCodigoTarjeta();
        } else if (entrada instanceof QR qr) {
            tipo = TipoEntrada.QR;
            codigo = qr.getCodigoQr();
            fechaUso = qr.getFechaUso();
        } else {
            throw new IllegalStateException("Tipo de entrada no reconocido");
        }
        return new EntradaRespuesta(entrada.getId(), tipo, entrada.getAsistente().getId(),
                entrada.getOrganizador().getId(), entrada.getEvento().getId(), entrada.getPrecio(),
                codigo, entrada.getFechaCompra(), fechaUso);
    }
}
