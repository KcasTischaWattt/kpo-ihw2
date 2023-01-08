import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Основной класс программы, который строит граф и отвечает за сортировку.
 */
public final class Graph {
    /**
     * Хранение графа как список смежности.
     */
    private Map<Node, List<Node>> adjacencyList;
    /**
     * Текущая директория.
     */
    private final String directory;

    /**
     * Конструктор графа от директории.
     *
     * @param directory Укаазанная директория.
     */
    public Graph(String directory) {
        this.directory = directory;
        run();
    }

    /**
     * Запуск работы графа.
     */
    private void run() {
        // Список файлов, в который добавлены все файлы из директории.
        List<Node> files;
        try {
            // Добавление файлов из директории.
            files = DirectoryChecker.checkDirectory(new File(directory));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        // Постройка графа.
        buildGraph(files);
        // Сортировка файлов.
        List<Node> sortedFiles = getFilesInOrder();
        System.out.println("Сканирование завершено.");

        // Вывод результата в файл.
        StringBuilder result = new StringBuilder();
        for (var file : sortedFiles) {
            if (file.isFile()) {
                try {
                    result.append(new String(Files.readAllBytes(Paths.get(file.toString()))));
                    result.append('\n');
                } catch (IOException exception) {
                    System.out.print(exception.getMessage());
                }
            }
        }
        try (FileWriter writer = new FileWriter("result.txt", false)) {
            writer.write(result.toString());
            System.out.println("Результат записан в файл.");
            writer.flush();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Постройка графа
     *
     * @param files Файлы, из которых и будет строится граф
     */
    @Contract(pure = true)
    private void buildGraph(@NotNull List<Node> files) {
        adjacencyList = new TreeMap<>();
        for (var file : files) {
            adjacencyList.put(file, FileScanner.searchForRequires(file, directory));
        }
    }

    /**
     * Сортировка файлов топологической сортировкой
     *
     * @return Список отсортированных файлов
     */
    private @NotNull List<Node> getFilesInOrder() {
        List<Node> topologicalSortResult = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        Map<Node, Boolean> visited = new HashMap<>();
        // Использовал материалы с сайтов GeeksForGeeks и Википедиии для того, чтобы разобраться в сортировке
        for (Node key : adjacencyList.keySet()) {
            visited.put(key, false);
            }
        }

        for (Node node : adjacencyList.keySet()) {
            if (!visited.get(node)) {
                topologicalSort(node, visited, stack);
            }
        }

        Map<Node, Integer> pos = new HashMap<>();
        int i = 0;
        while (!stack.isEmpty()) {
            pos.put(stack.peek(), i++);
            topologicalSortResult.add(stack.pop());
        }

        // Почему-то сортировка работает криво и возвращает список файлов в обратном порядке,
        // поэтому я разворачиваю итоговый список.
        Collections.reverse(topologicalSortResult);

        // Проверка на наличие циклов
        return checkForCycles(pos) ? topologicalSortResult : new ArrayList<>();
    }

    /**
     * Метод для проверки циклов
     *
     * @param pos HashMap позиций
     * @return true, если есть циклы. Иначе - false
     */
    private boolean checkForCycles(Map<Node, Integer> pos) {
        for (Node par : adjacencyList.keySet()) {
            for (Node child : adjacencyList.get(par)) {
                if (pos.get(par) > pos.get(child) || Objects.equals(par, child)) {
                    System.out.println("Обнаружен цикл! Сортировка невозможна");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Топологическая сортировка от ноды через dfs
     *
     * @param node    Саама нода
     * @param visited Уже посещённые вершины
     * @param stack   Стэк нод
     */
    private void topologicalSort(Node node, @NotNull Map<Node, Boolean> visited, Stack<Node> stack) {
        visited.put(node, true);
        List<Node> chads = adjacencyList.get(node);
        if (chads != null) {
            for (Node chad : chads) {
                if (!visited.get(chad)) {
                    topologicalSort(chad, visited, stack);
                }
            }
        }
        stack.push(node);
    }
}
