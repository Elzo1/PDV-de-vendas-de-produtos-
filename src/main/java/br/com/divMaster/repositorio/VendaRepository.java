package br.com.divMaster.repositorio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.divMaster.entity.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    // Método para encontrar vendas por data específica
    List<Venda> findByDataVenda(LocalDate dataVenda);

    // Método para encontrar vendas entre duas datas
    List<Venda> findByDataVendaBetween(LocalDate inicio, LocalDate fim);
    
    
}