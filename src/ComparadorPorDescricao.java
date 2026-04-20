import java.util.Comparator;

/**
 * Comparador de produtos por descrição.
 */
public class ComparadorPorDescricao implements Comparator<Produto> {

    @Override
    public int compare(Produto o1, Produto o2) {
        return o1.getDescricao().compareToIgnoreCase(o2.getDescricao());
    }
}