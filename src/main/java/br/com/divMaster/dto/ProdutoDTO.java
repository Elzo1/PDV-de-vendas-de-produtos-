package br.com.divMaster.dto;

import java.time.LocalDate;

import br.com.divMaster.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private double preco;
    private Integer quantidade;
    private Boolean pesavel;
    private String codigoBarra;
    private String codigo;

    // Construtor que aceita uma entidade Produto
    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.dataFabricacao = produto.getDataFabricacao();
        this.dataValidade = produto.getDataValidade();
        this.preco = produto.getPreco();
        this.quantidade = produto.getQuantidade();
        this.codigoBarra = produto.getCodigoBarra();
        this.codigo = produto.getCodigo();
        this.pesavel = produto.getPesavel();
    }

    // Construtor com parâmetros específicos (para uso no ProdutoService)
    public ProdutoDTO(Long id, String nome, LocalDate dataFabricacao, LocalDate dataValidade,
                      double preco, Integer quantidade, String codigoBarra, String codigo, Boolean pesavel) {
        this.id = id;
        this.nome = nome;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.preco = preco;
        this.quantidade = quantidade;
        this.codigoBarra = codigoBarra;
        this.codigo = codigo;
        this.pesavel = pesavel;
    }

    // Construtor padrão
    public ProdutoDTO() {
        super();
    }
}
