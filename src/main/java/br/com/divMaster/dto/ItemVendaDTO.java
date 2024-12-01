package br.com.divMaster.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemVendaDTO {
    private Long produtoId;
    private int quantidade;
    private BigDecimal preco; // Adicionar o campo "preco"

    // Getters e Setters
}
