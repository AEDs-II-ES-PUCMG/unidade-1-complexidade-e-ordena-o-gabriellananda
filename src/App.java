import java.util.Arrays;
import java.util.Random;

public class App {
    static final int[] tamanhosTesteGrande =  { 31_250_000, 62_500_000, 125_000_000, 250_000_000, 500_000_000 };
    static final int[] tamanhosTesteMedio =   {     12_500,     25_000,      50_000,     100_000,     200_000 };
    static final int[] tamanhosTestePequeno = {          3,          6,          12,          24,          48 };
    static Random aleatorio = new Random();
    static long operacoes;
    static double nanoToMilli = 1.0/1_000_000;
    
    /**
     * Gerador de vetores aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static int[] gerarVetor(int tamanho){
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, tamanho/2);
        }
        return vetor;        
    }

    /**
     * Gerador de vetores de objetos do tipo Integer aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor de Objetos Integer com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static Integer[] gerarVetorObjetos(int tamanho) {
        Integer[] vetor = new Integer[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, 10 * tamanho);
        }
        return vetor;
    }

    public static void main(String[] args) {
        int tam = 20;
        Integer[] vetor = gerarVetorObjetos(tam);
    
        /**==================== BUBBLESORT ====================*/

        BubbleSort<Integer> bolha = new BubbleSort<>();

        Integer[] vetorOrdenadoBolha = bolha.ordenar(vetor);

        System.out.println("\nVetor ordenado método Bolha:");
        System.out.println("Comparações: " + bolha.getComparacoes());
        System.out.println("Movimentações: " + bolha.getMovimentacoes());
        System.out.println("Tempo de ordenação (ms): " + bolha.getTempoOrdenacao());

        /**==================== INSERTIONSORT ====================*/

        InsertionSort<Integer> insertion = new InsertionSort<>();

        Integer[] vetorOrdenadoInsertion = insertion.ordenar(vetor);

        System.out.println("\nVetor ordenado método Insertion:");
        System.out.println("Comparações: " + insertion.getComparacoes());
        System.out.println("Movimentações: " + insertion.getMovimentacoes());
        System.out.println("Tempo de ordenação (ms): " + insertion.getTempoOrdenacao());

        /**==================== SELECTIONSORT ====================*/

        Selectionsort<Integer> selection = new Selectionsort<>();

        Integer[] vetorOrdenadoSelection = selection.ordenar(vetor);

        System.out.println("\nVetor ordenado método Selection:");
        System.out.println("Comparações: " + selection.getComparacoes());
        System.out.println("Movimentações: " + selection.getMovimentacoes());
        System.out.println("Tempo de ordenação (ms): " + selection.getTempoOrdenacao());   
    
        /** Varie o tamanho do vetor a ser ordenado e compare o desempenho dos algoritmos tanto em relação à quantidade de comparações, quanto de movimentações ao ordenar o mesmo array.*/

        int[] tamanhos = {10, 100, 1000};

        for (int t : tamanhos) {
            System.out.println("\n===== TAMANHO: " + t + " =====");

            Integer[] v = gerarVetorObjetos(t);

            BubbleSort<Integer> b = new BubbleSort<>();
            b.ordenar(v);
            System.out.println("\n--- BubbleSort ---");
            System.out.println("Bubble -> Comparações: " + b.getComparacoes());
            System.out.println("Bubble -> Movimentações: " + b.getMovimentacoes());
            System.out.println("Bubble -> Tempo: " + b.getTempoOrdenacao());

            InsertionSort<Integer> i = new InsertionSort<>();
            i.ordenar(v);
            System.out.println("\n--- InsertionSort ---");
            System.out.println("Insertion -> Comparações: " + i.getComparacoes());
            System.out.println("Insertion -> Movimentações: " + i.getMovimentacoes());
            System.out.println("Insertion -> Tempo: " + i.getTempoOrdenacao());

            Selectionsort<Integer> s = new Selectionsort<>();
            s.ordenar(v);
            System.out.println("\n--- Selectionsort ---");
            System.out.println("Selection -> Comparações: " + s.getComparacoes());
            System.out.println("Selection -> Movimentações: " + s.getMovimentacoes());
            System.out.println("Selection -> Tempo: " + s.getTempoOrdenacao());
        }
    }    
}
