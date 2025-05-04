// Implementação dos algoritmos de ordenação e experimento comparativo

import java.util.*;

public class OrdenacaoExperimentos {

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) {
            arr[k++] = L[i++];
        }

        while (j < n2) {
            arr[k++] = R[j++];
        }
    }

    public static int[] gerarLista(int tamanho, String tipo) {
        int[] arr = new int[tamanho];
        Random rand = new Random();

        for (int i = 0; i < tamanho; i++) {
            arr[i] = rand.nextInt(10000);
        }

        if (tipo.equals("crescente")) Arrays.sort(arr);
        else if (tipo.equals("decrescente")) {
            Arrays.sort(arr);
            for (int i = 0; i < tamanho / 2; i++) {
                int temp = arr[i];
                arr[i] = arr[tamanho - i - 1];
                arr[tamanho - i - 1] = temp;
            }
        }

        return arr;
    }

    public static long medirTempo(Runnable sortFunc) {
        long start = System.nanoTime();
        sortFunc.run();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int[] tamanhos = {1000, 10000};
        String[] distribuicoes = {"aleatorio", "crescente", "decrescente"};
        String[] algoritmos = {"Bubble", "Insertion", "Selection", "Quick", "Merge"};

        for (int tam : tamanhos) {
            for (String dist : distribuicoes) {
                System.out.println("Tamanho: " + tam + ", Distribuição: " + dist);

                for (String algoritmo : algoritmos) {
                    long[] tempos = new long[30];

                    for (int i = 0; i < 30; i++) {
                        int[] arr = gerarLista(tam, dist);
                        int[] copia = Arrays.copyOf(arr, arr.length);

                        Runnable sortFunc;
switch (algoritmo) {
    case "Bubble":
        sortFunc = () -> bubbleSort(copia);
        break;
    case "Insertion":
        sortFunc = () -> insertionSort(copia);
        break;
    case "Selection":
        sortFunc = () -> selectionSort(copia);
        break;
    case "Quick":
        sortFunc = () -> quickSort(copia, 0, copia.length - 1);
        break;
    case "Merge":
        sortFunc = () -> mergeSort(copia, 0, copia.length - 1);
        break;
    default:
        sortFunc = () -> {}; // Não faz nada
        break;
}

                        tempos[i] = medirTempo(sortFunc);
                    }

                    double media = Arrays.stream(tempos).average().orElse(0.0) / 1_000_000.0;
                    double desvio = Math.sqrt(Arrays.stream(tempos).mapToDouble(t -> Math.pow(t / 1_000_000.0 - media, 2)).sum() / tempos.length);
                    System.out.printf("%s Sort -> Média: %.2f ms, Desvio: %.2f ms\n", algoritmo, media, desvio);
                }
                System.out.println();
            }
        }
    }
}
