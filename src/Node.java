import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Класс-узел графа файлов.
 */
public final class Node implements Comparable<Node> {

    /**
     * Поле, обозначающее, является ли узел null или же файлом.
     */
    private final Boolean notnull;

    /**
     * Имя файла, являющегося узлом.
     */
    private final String name;

    /**
     * Конструктор от имени файла.
     *
     * @param name имя файла, являющегося узлом.
     */
    public Node(String name) {
        this.name = name;
        notnull = true;
    }

    /**
     * Проверка на то, является ли данный узел файлом.
     *
     * @return true, если узел является файлом. Инае - false.
     */
    public boolean isFile() {
        return notnull;
    }

    /**
     * Перегруженный toString.
     *
     * @return Строковое представление ноды как имя файла - узла.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Сравнение с другим объектом Node.
     *
     * @param o the object to be compared.
     * @return Результат сравнения.
     */
    @Override
    public int compareTo(@NotNull Node o) {
        return (Objects.requireNonNull(name).compareTo(Objects.requireNonNull(o.name)));
    }

    /**
     * Сравнение с другим объектом Object.
     *
     * @param o the object to be compared.
     * @return Результат сравнения.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node other = (Node) o;
        return this.notnull == other.notnull && Objects.equals(this.name, other.name);
    }

    /**
     * Взять хэш-код объекта.
     *
     * @return Хэш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(notnull, name);
    }
}
