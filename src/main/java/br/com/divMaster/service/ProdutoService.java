package br.com.divMaster.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.divMaster.dto.ProdutoDTO;
import br.com.divMaster.entity.Produto;
import br.com.divMaster.entity.User;
import br.com.divMaster.repositorio.ProdutoRepository;
import br.com.divMaster.repositorio.UserRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtém o usuário autenticado do contexto de segurança
     */
    private User getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        } else {
            throw new RuntimeException("Usuário não autenticado");
        }
    }

    /**
     * Lista todos os produtos do usuário autenticado
     */
    public List<ProdutoDTO> listarTodos() {
        User usuario = getUsuarioAutenticado();
        List<Produto> produtos = produtoRepository.findByUsuario(usuario);
        return produtos.stream().map(this::convertToDTO).toList();
    }

    /**
     * Busca um produto pelo ID, verificando se pertence ao usuário autenticado
     */
    public ProdutoDTO buscarPorId(Long id) {
        User usuario = getUsuarioAutenticado();
        Produto produto = produtoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return convertToDTO(produto);
    }

    /**
     * Salva um novo produto vinculado ao usuário autenticado
     */
    public ProdutoDTO salvarNovo(ProdutoDTO produtoDTO) {
        User usuario = getUsuarioAutenticado();
        Produto produto = convertToEntity(produtoDTO);
        produto.setUsuario(usuario);
        produto = produtoRepository.save(produto);
        return convertToDTO(produto);
    }

    /**
     * Edita um produto existente, garantindo que pertence ao usuário autenticado
     */
    public ProdutoDTO editar(Long id, ProdutoDTO produtoDTO) {
        User usuario = getUsuarioAutenticado();
        Produto produtoExistente = produtoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setDataFabricacao(produtoDTO.getDataFabricacao());
        produtoExistente.setDataValidade(produtoDTO.getDataValidade());
        produtoExistente.setPreco(produtoDTO.getPreco());
        produtoExistente.setQuantidade(produtoDTO.getQuantidade());
        produtoExistente.setCodigoBarra(produtoDTO.getCodigoBarra());
        produtoExistente.setCodigo(produtoDTO.getCodigo());
        produtoExistente.setPesavel(produtoDTO.getPesavel());
        
        produtoRepository.save(produtoExistente);
        return convertToDTO(produtoExistente);
    }

    /**
     * Atualiza o estoque de um produto do usuário autenticado
     */
    public ProdutoDTO atualizarEstoque(Long id, Integer quantidade) {
        User usuario = getUsuarioAutenticado();
        Produto produto = produtoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produto.setQuantidade(produto.getQuantidade() + quantidade);
        produto = produtoRepository.save(produto);
        return convertToDTO(produto);
    }

    /**
     * Deleta um produto se ele pertencer ao usuário autenticado
     */
    public void deletar(Long id) {
        User usuario = getUsuarioAutenticado();
        Produto produto = produtoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        produtoRepository.delete(produto);
    }

    /**
     * Busca produtos por nome, código de barra ou ID, pertencentes ao usuário autenticado
     */
    public List<ProdutoDTO> buscarPorNomeCodigoOuId(Long id, String nome, String codigoBarra, String codigo) {
        User usuario = getUsuarioAutenticado();
        List<Produto> produtos = produtoRepository.findByNomeOrCodigoBarraOrIdAndUsuario(id, nome, codigoBarra, codigo, usuario);
        return produtos.stream().map(this::convertToDTO).toList();
    }

    /**
     * Lista produtos que estão próximos da data de vencimento
     */
    public List<ProdutoDTO> listarProdutosVencendo() {
        User usuario = getUsuarioAutenticado();
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(30);
        List<Produto> produtos = produtoRepository.findByDataValidadeBetweenAndUsuario(currentDate, futureDate, usuario);
        return produtos.stream().map(this::convertToDTO).toList();
    }

    /**
     * Converte um Produto para ProdutoDTO
     */
    private ProdutoDTO convertToDTO(Produto produto) {
        return new ProdutoDTO(produto.getId(), produto.getNome(), produto.getDataFabricacao(),
            produto.getDataValidade(), produto.getPreco(), produto.getQuantidade(),
            produto.getCodigoBarra(), produto.getCodigo(), produto.getPesavel());
    }

    /**
     * Converte um ProdutoDTO para Produto
     */
    private Produto convertToEntity(ProdutoDTO produtoDTO) {
        return new Produto(produtoDTO.getId(), produtoDTO.getNome(), produtoDTO.getDataFabricacao(),
            produtoDTO.getDataValidade(), produtoDTO.getPreco(), produtoDTO.getQuantidade(),
            produtoDTO.getCodigoBarra(), produtoDTO.getCodigo(), produtoDTO.getPesavel());
    }
}
