package precio.precios.utils.rabbit;

@FunctionalInterface
public interface EventProcessor {
    void process(RabbitEvent event);
}
