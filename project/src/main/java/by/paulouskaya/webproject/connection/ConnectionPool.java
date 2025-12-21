package by.paulouskaya.webproject.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ConnectionPool {

  private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
  private static final String URL = "jdbc:mysql://localhost:3306/web_project";
  private static final String USER = "root";
  private static final String PASSWORD = "1234";

  private static final int POOL_SIZE = 10;
  private static ConnectionPool instance;
  private final Queue<Connection> freeConnections = new ArrayDeque<>();

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      logger.info("JDBC Driver loaded");
    } catch (ClassNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private ConnectionPool() throws SQLException {
      for (int i = 0; i < POOL_SIZE; i++) {
          freeConnections.add(DriverManager.getConnection(URL, USER, PASSWORD)
        );
      }
      logger.info("ConnectionPool initialized");

  }

  public static synchronized ConnectionPool getInstance() throws SQLException {
    if (instance == null) {
      instance = new ConnectionPool();
    }
    return instance;
  }

  public synchronized Connection getConnection() {
    while (freeConnections.isEmpty()) {
      try {
        wait();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    return freeConnections.poll();
  }

  public synchronized void releaseConnection(Connection connection) {
    freeConnections.offer(connection);
    notifyAll();
  }

  public synchronized void shutdown() throws SQLException {
    for (Connection conn : freeConnections) {
      conn.close();
    }
    freeConnections.clear();
  }
}
