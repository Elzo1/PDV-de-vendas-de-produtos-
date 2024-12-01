package br.com.divMaster.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.divMaster.dto.ProdutoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataValidade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataFabricacao;

    private double preco;
    private Boolean pesavel;
    private Integer quantidade;

    private String codigoBarra;
    private String codigo;
    
    // Associação com o usuário criador
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    // Construtor que aceita todos os parâmetros (similar ao ProdutoDTO)
    public Produto(Long id, String nome, LocalDate dataFabricacao, LocalDate dataValidade,
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

    // Construtor que aceita um ProdutoDTO (útil para conversões entre DTO e entidade)
    public Produto(ProdutoDTO produtoDTO) {
        this.id = produtoDTO.getId();
        this.nome = produtoDTO.getNome();
        this.dataFabricacao = produtoDTO.getDataFabricacao();
        this.dataValidade = produtoDTO.getDataValidade();
        this.preco = produtoDTO.getPreco();
        this.quantidade = produtoDTO.getQuantidade();
        this.codigoBarra = produtoDTO.getCodigoBarra();
        this.codigo = produtoDTO.getCodigo();
        this.pesavel = produtoDTO.getPesavel();
    }
}
