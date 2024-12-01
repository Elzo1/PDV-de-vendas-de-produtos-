package br.com.divMaster.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.divMaster.dto.ItemVendaDTO;
import br.com.divMaster.dto.VendaDTO;
import br.com.divMaster.service.VendaService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    /**
     * Salva uma nova venda e gera um cupom não fiscal.
     * 
     * @param vendaDTO Objeto DTO contendo os detalhes da venda.
     * @return Resposta com o DTO da venda salva.
     */
    @PostMapping
    public ResponseEntity<VendaDTO> salvarVenda( @PathVariable  @RequestBody VendaDTO vendaDTO) {
        VendaDTO vendaSalva = vendaService.salvarVenda(vendaDTO); // No Long parameter needed
        return ResponseEntity.ok(vendaSalva);
    }

    /**
     * Retorna o total das vendas realizadas no dia atual.
     * 
     * @return Resposta com o total das vendas do dia.
     */
    @GetMapping("/total-dia")
    public ResponseEntity<BigDecimal> totalVendasDoDia() {
        BigDecimal total = vendaService.totalVendasDoDia();
        return ResponseEntity.ok(total);
    }

    /**
     * Gera um relatório das vendas por período.
     * 
     * @param inicio Data de início do período.
     * @param fim    Data de fim do período.
     * @return Resposta com o relatório das vendas por período.
     */
    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, BigDecimal>> relatorioPorPeriodo(
            @RequestParam("inicio") LocalDate inicio,
            @RequestParam("fim") LocalDate fim) {
        Map<String, BigDecimal> relatorio = vendaService.relatorioPorPeriodo(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    /**
     * Cancela uma venda e reverte o estoque dos produtos.
     * 
     * @param vendaId ID da venda a ser cancelada.
     * @return Resposta com status de operação.
     */
    @DeleteMapping("/{vendaId}")
    public ResponseEntity<Void> cancelarVenda(@PathVariable Long vendaId) {
        vendaService.cancelarVenda(vendaId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adiciona um produto a uma venda existente.
     * 
     * @param vendaId ID da venda.
     * @param itemDTO DTO do item de venda a ser adicionado.
     * @return Resposta com o DTO da venda atualizada.
     */
    @PostMapping("/{vendaId}/adicionar-produto")
    public ResponseEntity<VendaDTO> adicionarProduto(
            @PathVariable Long vendaId,
            @RequestBody ItemVendaDTO itemDTO) {
        VendaDTO vendaAtualizada = vendaService.adicionarProduto(vendaId, itemDTO);
        return ResponseEntity.ok(vendaAtualizada);
    }
}
