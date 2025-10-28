public class Main {

    // variável global para usar como tamanho nos arrays.
    static int tamanho = 20;

    // Esta classe será utilizada para armazenar resultados.
    static class SortStats {
        long trocas = 0;
        long iteracoes = 0;
        String nome;
        int[] resultado;

        SortStats(String nome) {
            this.nome = nome;
        }
    }

    // COMB
    static class CombSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("CombSort");
            double encolher = 1.3;
            int gap = tamanho;
            boolean trocou = true;

            while (gap > 1 || trocou) {
                gap = (int) (gap / encolher);
                if (gap < 1) gap = 1;
                trocou = false;

                for (int i = 0; i + gap < tamanho; i++) {
                    stats.iteracoes++;
                    if (arr[i] > arr[i + gap]) {
                        int temp = arr[i];
                        arr[i] = arr[i + gap];
                        arr[i + gap] = temp;
                        trocou = true;
                        stats.trocas++;
                    }
                }
            }
            stats.resultado = arr;
            return stats;
        }
    }

    // BUCKET SORT
    static class BucketSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("BucketSort");
            if (tamanho <= 0) return stats;

            int max = arr[0], min = arr[0];
            for (int i = 1; i < tamanho; i++) {
                stats.iteracoes++;
                if (arr[i] > max) max = arr[i];
                if (arr[i] < min) min = arr[i];
            }

            int range = max - min + 1;
            int[] count = new int[range];

            for (int i = 0; i < tamanho; i++) {
                stats.iteracoes++;
                count[arr[i] - min]++;
            }

            int index = 0;
            for (int i = 0; i < range; i++) {
                while (count[i] > 0) {
                    stats.iteracoes++;
                    arr[index++] = i + min;
                    count[i]--;
                    stats.trocas++; // contagem de "escritas" como trocas
                }
            }

            stats.resultado = arr;
            return stats;
        }
    }

    // GNOME SORT
    static class GnomeSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("GnomeSort");
            int index = 1;

            while (index < tamanho) {
                stats.iteracoes++;
                if (index == 0 || arr[index] >= arr[index - 1]) {
                    index++;
                } else {
                    int temp = arr[index];
                    arr[index] = arr[index - 1];
                    arr[index - 1] = temp;
                    stats.trocas++;
                    index--;
                }
            }

            stats.resultado = arr;
            return stats;
        }
    }

    // BUBBLE SORT (com flag de parada)
    static class BubbleSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("BubbleSort");
            boolean trocou;

            for (int i = 0; i < tamanho - 1; i++) { 
                trocou = false;
                for (int j = 0; j < tamanho - i - 1; j++) { 
                    stats.iteracoes++;
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        stats.trocas++;
                        trocou = true;
                    }
                }
                if (!trocou) break;
            }
            stats.resultado = arr;
            return stats;
        }
    }

    // SELECTION SORT
    static class SelectionSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("SelectionSort");

            for (int i = 0; i < tamanho - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < tamanho; j++) { 
                    stats.iteracoes++;
                    if (arr[j] < arr[minIdx]) {
                        minIdx = j;
                    }
                }
                if (minIdx != i) {
                    int temp = arr[i];
                    arr[i] = arr[minIdx];
                    arr[minIdx] = temp;
                    stats.trocas++;
                }
            }
            stats.resultado = arr;
            return stats;
        }
    }

    // COCKTAIL SORT
    static class CocktailSort {
        static SortStats sort(int[] arr) {
            SortStats stats = new SortStats("CocktailSort");
            boolean trocou = true;
            int start = 0;
            int end = tamanho - 1;

            while (trocou) {
                trocou = false;

                for (int i = start; i < end; i++) {
                    stats.iteracoes++;
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        stats.trocas++;
                        trocou = true;
                    }
                }

                if (!trocou) break;
                trocou = false;
                end--;

                for (int i = end - 1; i >= start; i--) {
                    stats.iteracoes++;
                    if (arr[i] > arr[i + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = temp;
                        stats.trocas++;
                        trocou = true;
                    }
                }
                start++;
            }

            stats.resultado = arr;
            return stats;
        }
    }

    static void testarAlgoritmos(int[] vetorOriginal, String nomeVetor) {
        System.out.println("\n=== Resultados para " + nomeVetor + " ===");

        SortStats[] resultados = {
            CombSort.sort(vetorOriginal.clone()),
            BucketSort.sort(vetorOriginal.clone()),
            GnomeSort.sort(vetorOriginal.clone()),
            BubbleSort.sort(vetorOriginal.clone()),
            SelectionSort.sort(vetorOriginal.clone()),
            CocktailSort.sort(vetorOriginal.clone())
        };

        System.out.printf("%-15s %-15s %-15s%n", "Algoritmo", "Trocas", "Iterações");
        for (SortStats s : resultados) {
            System.out.printf("%-15s %-15d %-15d%n", s.nome, s.trocas, s.iteracoes);
        }

        // Ranking
        System.out.println("\n->Ranking (menos trocas)");
        ordenarEImprimir(resultados, true);

        System.out.println("\n->Ranking (menos iterações)");
        ordenarEImprimir(resultados, false);
    }

    static void ordenarEImprimir(SortStats[] resultados, boolean porTrocas) {
        // ordenação simples por seleção (em memória local do array de resultados)
        for (int i = 0; i < resultados.length - 1; i++) {
            for (int j = i + 1; j < resultados.length; j++) {
                long a = porTrocas ? resultados[i].trocas : resultados[i].iteracoes;
                long b = porTrocas ? resultados[j].trocas : resultados[j].iteracoes;
                if (a > b) {
                    SortStats temp = resultados[i];
                    resultados[i] = resultados[j];
                    resultados[j] = temp;
                }
            }
        }
        for (int i = 0; i < resultados.length; i++) {
            long valor = porTrocas ? resultados[i].trocas : resultados[i].iteracoes;
            System.out.printf("%dº %-15s -> %d%n", (i + 1), resultados[i].nome, valor);
        }
    }

    public static void main(String[] args) {
        // Arrays usados nos testes — mantive os três que você passou
        int[] vetor1 = {12, 18, 9, 25, 17, 31, 22, 27, 16, 13, 19, 23, 20, 30, 14, 11, 15, 24, 26, 28};
        int[] vetor2 = {5, 7, 9, 10, 12, 14, 15, 17, 19, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31, 32};
        int[] vetor3 = {99, 85, 73, 60, 50, 40, 35, 30, 25, 20, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6};

        testarAlgoritmos(vetor1, "Vetor Aleatorio");
        testarAlgoritmos(vetor2, "Vetor Crescente");
        testarAlgoritmos(vetor3, "Vetor Decrescente");
    }
}
