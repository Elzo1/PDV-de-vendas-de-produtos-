package br.com.divMaster.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.divMaster.dto.ItemVendaDTO;
import br.com.divMaster.dto.VendaDTO;
import br.com.divMaster.entity.ItemVenda;
import br.com.divMaster.entity.Produto;
import br.com.divMaster.entity.Venda;
import br.com.divMaster.repositorio.ProdutoRepository;
import br.com.divMaster.repositorio.VendaRepository;
import br.com.divMaster.util.CupomNaoFiscal;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public VendaDTO salvarVenda(VendaDTO vendaDTO) {
        // Validate and convert DTO
        Venda venda = convertToEntity(vendaDTO);
        venda.setDataVenda(LocalDateTime.now());
        venda.setTotal(calcularTotal(venda.getItens()));

        // Save venda and perform other operations
        venda = vendaRepository.save(venda);
        atualizarEstoque(venda.getItens(), false); // Regular sale

        // Generate cupom and return DTO
        String cupom = CupomNaoFiscal.gerarCupom(venda);
        System.out.println(cupom);

        return convertToDTO(venda);
    }

    public BigDecimal totalVendasDoDia() {
        // Calculate total sales for the day
        return vendaRepository.findAll().stream()
            .filter(venda -> venda.getDataVenda().toLocalDate().isEqual(LocalDate.now()))
            .map(Venda::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> relatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        // Convert LocalDate to LocalDateTime for comparison
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        return vendaRepository.findAll().stream()
            .filter(venda -> !venda.getDataVenda().isBefore(inicioDateTime) && !venda.getDataVenda().isAfter(fimDateTime))
            .collect(Collectors.groupingBy(venda -> venda.getDataVenda().toLocalDate().toString(),
                    Collectors.reducing(BigDecimal.ZERO, Venda::getTotal, BigDecimal::add)));
    }

    public VendaDTO adicionarProduto(Long vendaId, ItemVendaDTO itemDTO) {
        // Add a product to an existing sale
        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        ItemVenda itemVenda = convertToItemVendaEntity(itemDTO);
        venda.getItens().add(itemVenda);
        venda.setTotal(calcularTotal(venda.getItens()));

        vendaRepository.save(venda);
        return convertToDTO(venda);
    }

    public void cancelarVenda(Long vendaId) {
        // Find the sale by ID
        Venda venda = vendaRepository.findById(vendaId)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        // Update stock for each item in the sale
        atualizarEstoque(venda.getItens(), true); // Pass true for cancellation

        // Delete the sale
        vendaRepository.delete(venda);
    }

    private BigDecimal calcularTotal(List<ItemVenda> itens) {
        return itens.stream()
            .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void atualizarEstoque(List<ItemVenda> itens, boolean isCancellation) {
        for (ItemVenda item : itens) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (isCancellation) {
                // Increase stock if the sale is canceled
                produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            } else {
                // Decrease stock if it's a regular sale
                produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            }
            produtoRepository.save(produto);
        }
    }

    private Venda convertToEntity(VendaDTO vendaDTO) {
        Venda venda = new Venda();
        venda.setItens(vendaDTO.getItens().stream()
            .map(this::convertToItemVendaEntity)
            .collect(Collectors.toList()));
        return venda;
    }

    private ItemVenda convertToItemVendaEntity(ItemVendaDTO itemVendaDTO) {
        ItemVenda itemVenda = new ItemVenda();
        Produto produto = produtoRepository.findById(itemVendaDTO.getProdutoId())
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        itemVenda.setProduto(produto);
        itemVenda.setQuantidade(itemVendaDTO.getQuantidade());
        itemVenda.setPrecoUnitario(itemVendaDTO.getPreco());
        return itemVenda;
    }

    private VendaDTO convertToDTO(Venda venda) {
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setItens(venda.getItens().stream()
            .map(this::convertToItemVendaDTO)
            .collect(Collectors.toList()));
        vendaDTO.setTotal(venda.getTotal());
        vendaDTO.setPaymentMethod(venda.getPaymentMethod());
        vendaDTO.setDataVenda(venda.getDataVenda());
        return vendaDTO;
    }

    private ItemVendaDTO convertToItemVendaDTO(ItemVenda itemVenda) {
        ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
        itemVendaDTO.setProdutoId(itemVenda.getProduto().getId());
        itemVendaDTO.setQuantidade(itemVenda.getQuantidade());
        itemVendaDTO.setPreco(itemVenda.getPrecoUnitario());
        return itemVendaDTO;
    }
}
