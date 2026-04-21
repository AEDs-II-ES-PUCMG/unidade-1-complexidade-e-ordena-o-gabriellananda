import java.util.Comparator;

/*Critério C - Ticket Médio por Variedade de Produtos:
A ordenação deve ser baseada no valor médio gasto por tipo de produto distinto presente
no pedido, ou seja, a razão entre o Valor Final do Pedido e a quantidade de posições ocupadas
no vetor do carrinho, independentemente de quantas unidades de cada 
produto foram solicitadas. 
o Desempate: Em caso de empate, utilize o Valor Final do Pedido e, em seguida, o Código 
Identificador.*/

public class ComparadorCriterioC implements Comparator<Pedido> {

    @Override
    public int compare(Pedido o1, Pedido o2) {
        int cmp = Double.compare(o1.getValorMedio(), o2.getValorMedio());
        if (cmp != 0) return cmp;

        return Integer.compare(
            o1.getValorFinalPedido(),
            o2.getIdPrimeiroProduto()
        );
    }
}
