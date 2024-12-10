package precio.precios.security;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import precio.precios.utils.errors.UserUnauthorizedException;
import precio.precios.utils.expiringMap.ExpiringMap;

@Service
public class TokenService {

    @Autowired
    TokenDao tokenDao;

    static final ExpiringMap<String, User> map = new ExpiringMap<>(60 * 60, 60 * 5);

    public void validateAdmin(String token) {
        validateLoggedIn(token);
        User cachedUser = map.get(token);
        if (cachedUser == null) {
            throw new UserUnauthorizedException("Usuario no autorizado");
        }
        if (!contains(cachedUser.permissions, "admin")) {
            throw new UserUnauthorizedException("Usuario sin permisos de admin");
        }
    }

    public void validateLoggedIn(String token) {
        if (StringUtils.isBlank(token)) {
            throw new UserUnauthorizedException("Usuario no autorizado");
        }

        User cachedUser = map.get(token);
        if (cachedUser != null) {
            return;
        }

        User user = tokenDao.retrieveUser(token);
        if (user == null) {
            throw new UserUnauthorizedException("Usuario no autorizado");
        }
        map.put(token, user);
    }

    public User getUser(String token) {
        if (token.isEmpty()) {
            throw new UserUnauthorizedException("Usuario no autorizado");
        }

        User cachedUser = map.get(token);
        if (cachedUser != null) {
            return cachedUser;
        }

        User user = tokenDao.retrieveUser(token);
        if (user == null) {
            throw new UserUnauthorizedException("Usuario no autorizado");
        }
        map.put(token, user);
        return user;
    }

    public void invalidate(String token) {
        map.remove(token);
    }

    private boolean contains(String[] permissions, String permission) {
        for (String s : permissions) {
            if (s.equals(permission)) {
                return true;
            }
        }
        return false;
    }
}
