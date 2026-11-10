package patterns.command;

public interface Comando {

    void ejecutar();

    default boolean puedeDeshacer() {
        return false;
    }

    default void deshacer() {
        throw new UnsupportedOperationException(
                "This command does not support undo."
        );
    }

    default String getDescription() {
        return getClass().getSimpleName();
    }
}