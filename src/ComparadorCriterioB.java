import java.util.Comparator;

/*Critério B - Forma de Pagamento: A ordenação deve listar primeiro os pedidos pagos à vista e,
em seguida, os pedidos pagos de forma parcelada.
Lembre-se da regra de negócio estabelecida: pedidos pagos à vista 
recebem um desconto percentual sobre o valor total, enquanto pedidos parcelados não recebem 
desconto. 
o Desempate: Em caso de empate, utilize o Valor Final do Pedido e, em seguida, o Código 
Identificador.*/
public class ComparadorCriterioB implements Comparator<Pedido> {

    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Double.compare(p1.getformaDePagamento(), p2.getformaDePagamento());
        if (cmp != 0) return cmp;

        return Integer.compare(
            p1.getIdPrimeiroProduto(),
            p2.getIdPrimeiroProduto()
        );
    }
}
