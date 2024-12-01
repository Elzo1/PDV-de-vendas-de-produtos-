package br.com.divMaster.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VendaDTO {
    private List<ItemVendaDTO> itens; // List of items in the sale
    private BigDecimal total;           // Total amount for the sale
    private String paymentMethod;       // Payment method used
    private LocalDateTime dataVenda;    // Date of the sale
}
