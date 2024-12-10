package precio.precios.utils.expiringMap;

public interface ExpirationListener<E> {
    void expired(E expiredObject);
}
