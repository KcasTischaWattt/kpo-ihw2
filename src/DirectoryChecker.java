import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для рекурсивного поиска файлов в директории.
 */
public final class DirectoryChecker {

    /**
     * Коллекция для хранения найденных в директории файлов.
     */
    static final List<Node> res = new ArrayList<>();

    /**
     * Рекурсивный поиск файлов в директории.
     * @param file Директрия, в которой ищутся файлы.
     * @return Список файлов в директрии.
     * @throws IOException Вызывается при некорректном файле.
     */
    public static List<Node> checkDirectory(@NotNull File file) throws IOException {
        // Если директория есть файл - добавляем в список.
        if (!file.isDirectory()) {
            res.add(new Node(file.getPath()));
        // Иначе - рекурсивно ищем файлы в директрии.
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                throw new IOException("Произошла ошибка ввода-вывода в директории " + file.getName());
            }
            for (File f : files) {
                checkDirectory(f);
            }
        }
        return res;
    }
}
