package br.com.divMaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.divMaster.dto.ProdutoDTO;
import br.com.divMaster.service.ProdutoService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    // Buscar um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        try {
            ProdutoDTO produto = produtoService.buscarPorId(id);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Criar um novo produto
    @PostMapping
    public ResponseEntity<ProdutoDTO> salvarNovo(@RequestBody ProdutoDTO produtoDTO) {
        try {
            ProdutoDTO produtoSalvo = produtoService.salvarNovo(produtoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Editar um produto existente
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> editar(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {
        try {
            ProdutoDTO produtoAtualizado = produtoService.editar(id, produtoDTO);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Atualizar estoque de um produto específico
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<ProdutoDTO> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        try {
            ProdutoDTO produtoAtualizado = produtoService.atualizarEstoque(id, quantidade);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Deletar um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            produtoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Buscar produtos com base em filtros (nome, código de barras, ID ou código)
    @GetMapping("/filtro")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNomeCodigoOuId(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String codigoBarra,
            @RequestParam(required = false) String codigo) {
        List<ProdutoDTO> produtos = produtoService.buscarPorNomeCodigoOuId(id, nome, codigoBarra, codigo);
        return ResponseEntity.ok(produtos);
    }

    // Listar produtos que estão próximos de vencer
    @GetMapping("/vencendo")
    public ResponseEntity<List<ProdutoDTO>> listarProdutosVencendo() {
        List<ProdutoDTO> produtosVencendo = produtoService.listarProdutosVencendo();
        return ResponseEntity.ok(produtosVencendo);
    }
}
