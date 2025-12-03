package by.paulouskaya.webproject.dao;

import java.util.List;

public abstract class AbstractDao <K, T> {
    protected String id;
    protected String connectionUrl;
    protected String username;
    protected String password;

    public AbstractDao(String primaryKey) {
        this.id = primaryKey;
    }

    public abstract List<T> findAll();
    public abstract T findEntityById(K id);
    public abstract boolean delete(K id);
    public abstract boolean create(T entity);
    public abstract T update(T entity);
}
