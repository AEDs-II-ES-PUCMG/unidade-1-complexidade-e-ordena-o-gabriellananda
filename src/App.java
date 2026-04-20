import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class App {

    static final int MAX_PEDIDOS = 100;

    /** Vetor principal de produtos */
    static Produto[] produtos;

    /** Cópia dos produtos ordenada por código */
    static Produto[] produtosOrdenadosPorCodigo;

    /** Cópia dos produtos ordenada por descrição */
    static Produto[] produtosOrdenadosPorDescricao;

    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;

    // #region utilidades
    static Scanner teclado;

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static String lerTexto(String mensagem) {
        System.out.print(mensagem + ": ");
        return teclado.nextLine();
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("0 - Finalizar");

        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Por descrição");
        System.out.println("2 - Por código");

        return lerNumero("Digite sua opção", Integer.class);
    }

    /**
     * Exibe o menu de critério de busca.
     * @return opção digitada pelo usuário
     */
    static int exibirMenuBusca() {
        cabecalho();
        System.out.println("Buscar produto por:");
        System.out.println("1 - Identificador");
        System.out.println("2 - Descrição");

        return lerNumero("Digite sua opção", Integer.class);
    }

    // #endregion

    /**
     * Carrega os produtos de um arquivo texto.
     * A primeira linha deve conter a quantidade de registros.
     * @param nomeArquivo nome do arquivo
     * @return vetor de produtos carregados
     */
    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        quantProdutos = 0;

        try {
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine());

            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                Produto novoProduto = Produto.criarDoTexto(dados.nextLine());
                dadosCarregados[quantProdutos] = novoProduto;
                quantProdutos++;
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado. Produtos não carregados");
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    /**
     * Cria as duas cópias ordenadas exigidas pelo enunciado:
     * uma por identificador e outra por descrição.
     */
    static void prepararCopiasOrdenadas() {
        IOrdenador<Produto> ordenadorPadrao = new MergeSort<>();
        produtosOrdenadosPorCodigo = ordenadorPadrao.ordenar(produtos, new ComparadorPorCodigo());
        produtosOrdenadosPorDescricao = ordenadorPadrao.ordenar(produtos, new ComparadorPorDescricao());
    }

    /**
     * Pesquisa binária por código no vetor previamente ordenado por código.
     * @param codigo identificador procurado
     * @return produto encontrado ou null
     */
    static Produto buscaBinariaPorCodigo(int codigo) {
        int ini = 0;
        int fim = quantProdutos - 1;

        while (ini <= fim) {
            int meio = (ini + fim) / 2;
            int codigoMeio = produtosOrdenadosPorCodigo[meio].getIdProduto();

            if (codigoMeio == codigo) {
                return produtosOrdenadosPorCodigo[meio];
            } else if (codigo < codigoMeio) {
                fim = meio - 1;
            } else {
                ini = meio + 1;
            }
        }

        return null;
    }

    /**
     * Pesquisa binária por descrição no vetor previamente ordenado por descrição.
     * @param descricao descrição procurada
     * @return produto encontrado ou null
     */
    static Produto buscaBinariaPorDescricao(String descricao) {
        int ini = 0;
        int fim = quantProdutos - 1;
        String chave = descricao.trim();

        while (ini <= fim) {
            int meio = (ini + fim) / 2;
            Produto produtoMeio = produtosOrdenadosPorDescricao[meio];
            int comparacao = chave.compareToIgnoreCase(produtoMeio.getDescricao());

            if (comparacao == 0) {
                return produtoMeio;
            } else if (comparacao < 0) {
                fim = meio - 1;
            } else {
                ini = meio + 1;
            }
        }

        return null;
    }

    /**
     * Localiza um produto usando pesquisa binária, podendo buscar
     * por identificador ou por descrição.
     * @return produto localizado ou null
     */
    static Produto localizarProduto() {
        int criterio = exibirMenuBusca();

        if (criterio == 1) {
            Integer numero = lerNumero("Digite o identificador do produto", Integer.class);
            if (numero == null)
                return null;
            return buscaBinariaPorCodigo(numero);
        } else if (criterio == 2) {
            String descricao = lerTexto("Digite a descrição do produto");
            return buscaBinariaPorDescricao(descricao);
        }

        return null;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        String mensagem = "Produto não encontrado.";

        if (produto != null) {
            mensagem = String.format("Dados do produto:\n%s", produto);
        }

        System.out.println(mensagem);
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        System.out.println("Filtrando por valor máximo:");
        Double valor = lerNumero("valor", Double.class);

        if (valor == null) {
            System.out.println("Valor inválido.");
            return;
        }

        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() < valor)
                relatorio.append(produtos[i]).append("\n");
        }
        System.out.println(relatorio.toString());
    }

    /**
     * Ordena o vetor principal de produtos de acordo com o algoritmo e
     * comparador escolhidos pelo usuário.
     */
    static void ordenarProdutos() {
        cabecalho();

        int opcao = exibirMenuOrdenadores();
        switch (opcao) {
            case 1 -> ordenador = new BubbleSort<>();
            case 2 -> ordenador = new InsertionSort<>();
            case 3 -> ordenador = new SelectionSort<>();
            case 4 -> ordenador = new MergeSort<>();
            default -> ordenador = null;
        }

        if (ordenador != null) {
            opcao = exibirMenuComparadores();
            ComparadorPorCodigo comparadorCodigo = new ComparadorPorCodigo();
            ComparadorPorDescricao comparadorDescricao = new ComparadorPorDescricao();

            switch (opcao) {
                case 2 -> produtos = ordenador.ordenar(produtos, comparadorCodigo);
                default -> produtos = ordenador.ordenar(produtos, comparadorDescricao);
            }

            System.out.println("Tempo gasto: " + ordenador.getTempoOrdenacao() + "ms");
        }
        ordenador = null;
    }

    static void embaralharProdutos() {
        Collections.shuffle(Arrays.asList(produtos));
    }

    static void verificarSubstituicao(Produto[] dadosOriginais, Produto[] copiaDados) {
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)?");
        String resposta = teclado.nextLine().toUpperCase();
        if (resposta.equals("S"))
            dadosOriginais = Arrays.copyOf(copiaDados, copiaDados.length);
    }

    static void listarProdutos() {
        cabecalho();
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);

        if (produtos != null) {
            /*
             * Após o carregamento, já são criadas as duas cópias ordenadas
             * pedidas pelo enunciado.
             */
            prepararCopiasOrdenadas();
            embaralharProdutos();
        }

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> filtrarPorPrecoMaximo();
                case 3 -> ordenarProdutos();
                case 4 -> embaralharProdutos();
                case 5 -> listarProdutos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);
        teclado.close();
    }
}