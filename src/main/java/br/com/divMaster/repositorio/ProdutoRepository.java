package br.com.divMaster.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.divMaster.entity.Produto;
import br.com.divMaster.entity.User;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca produtos por nome e usuário
    List<Produto> findByNomeContainingIgnoreCaseAndUsuario(String nome, User usuario);

    // Busca produtos próximos ao vencimento e usuário
    List<Produto> findByDataValidadeBetweenAndUsuario(LocalDate startDate, LocalDate endDate, User usuario);

    // Busca produto por código de barras e usuário
    List<Produto> findByCodigoBarraAndUsuario(String codigoBarra, User usuario);

    // Busca produto por código e usuário
    List<Produto> findByCodigoAndUsuario(String codigo, User usuario);

    // Busca produto pelo nome, código de barras, ID ou código e usuário
    @Query("SELECT p FROM Produto p WHERE (:id IS NULL OR p.id = :id) " +
           "AND (:nome IS NULL OR p.nome LIKE %:nome%) " +
           "AND (:codigoBarra IS NULL OR p.codigoBarra = :codigoBarra) " +
           "AND (:codigo IS NULL OR p.codigo = :codigo) " +
           "AND p.usuario = :usuario")
    List<Produto> findByNomeOrCodigoBarraOrIdAndUsuario(@Param("id") Long id,
                                                        @Param("nome") String nome,
                                                        @Param("codigoBarra") String codigoBarra,
                                                        @Param("codigo") String codigo,
                                                        @Param("usuario") User usuario);

    // Busca produto por ID e usuário
    Optional<Produto> findByIdAndUsuario(Long id, User usuario);

    // Lista todos os produtos de um usuário específico
    List<Produto> findByUsuario(User usuario);
}
