package by.paulouskaya.webproject.dao;

public abstract class AbstractDao {
    protected String id;
    protected String connectionUrl;
    protected String username;
    protected String password;

    public AbstractDao(String primaryKey) {
        this.id = primaryKey;
    }

    public abstract void insert(Object entity);
    public abstract void update(Object entity);
    public abstract void delete(Object entity);
    public abstract Object findById(int id);
}
