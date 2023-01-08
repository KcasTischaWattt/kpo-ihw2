import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


/**
 * Класс для проверки файлов на наличие директив require.
 */
public final class FileScanner {

    /**
     * Проверка файла на директивы require.
     *
     * @param file      Проверяемый файл.
     * @param directory Директория, в которой находится файл.
     * @return Список директив require, встретившихся в этом файле.
     */
    public static List<Node> searchForRequires(@NotNull Node file, String directory) {
        // Список встретившихся директив require.
        List<Node> requires = new ArrayList<>();
        BufferedReader br;
        // Считываемая строка файла.
        String line;

        // Открытие файла и чтение первой строки.
        try {
            br = new BufferedReader(new FileReader(file.toString()));
            line = br.readLine();
        } catch (FileNotFoundException exception) {
            System.out.println("Ошибка при поиске файла " + file);
            return requires;
        } catch (IOException exception) {
            System.out.println("Произошла ошибка ввода-вывода: не удаётся прочитать файл " + file);
            return requires;
        }

        // Чтение всех строк файла поочерёдно.
        while (line != null) {
            // Добавление найденной директивы в список.
            if (line.startsWith("require")) {
                String requireString = directory + File.separator + line.substring(9, line.length() - 1);
                requires.add(new Node(requireString));
            }
            try {
                // Чтение новой строки.
                line = br.readLine();
            } catch (IOException e) {
                System.out.println("Произошла ошибка ввода-вывода: не удаётся прочитать файл " + file);
                break;
            }
        }

        return requires;
    }
}
