package br.com.divMaster.util;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import br.com.divMaster.entity.ItemVenda;
import br.com.divMaster.entity.Venda;

/**
 * Classe responsável por gerar o cupom não fiscal de uma venda.
 */
public class CupomNaoFiscal {

    /**
     * Gera o cupom não fiscal a partir de uma venda.
     *
     * @param venda Objeto Venda contendo os detalhes da venda realizada.
     * @return String representando o cupom não fiscal formatado.
     */
    public static String gerarCupom(Venda venda) {
        StringBuilder cupom = new StringBuilder();
        // Formata valores monetários para o padrão brasileiro (R$)
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Cabeçalho do cupom
        cupom.append("==================================\n");
        cupom.append("             CUPOM NÃO FISCAL            \n");
        cupom.append("==================================\n");
        cupom.append("Data da Venda: ").append(venda.getDataVenda()).append("\n");
        cupom.append("----------------------------------\n");
        cupom.append("Produtos:\n");

        // Lista os produtos vendidos
        List<ItemVenda> itens = venda.getItens();
        for (ItemVenda item : itens) {
            cupom.append(item.getProduto().getNome()).append(" x")
                .append(item.getQuantidade()).append("  ")
                .append(currencyFormatter.format(item.getPrecoUnitario())).append("\n");
        }

        // Total da venda
        cupom.append("----------------------------------\n");
        cupom.append("Total: ").append(currencyFormatter.format(venda.getTotal())).append("\n");
        cupom.append("==================================\n");
        cupom.append("       Obrigado pela preferência!       \n");
        cupom.append("==================================\n");

        return cupom.toString();
    }
}
