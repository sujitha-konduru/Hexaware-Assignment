package dao;

import entity.Product;
import entity.User;
import exception.OrderNotFoundException;
import exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface IOrderManagementRepository {

    void createUser(User user) throws SQLException;

    void createProduct(User user, Product product) throws SQLException, UserNotFoundException, Exception;

    List<Product> getAllProducts() throws SQLException;

    void createOrder(User user, List<Product> products) throws SQLException, UserNotFoundException;

    void cancelOrder(int userId, int orderId) throws SQLException, UserNotFoundException, OrderNotFoundException, Exception;

    List<Product> getOrderByUser(User user) throws SQLException, UserNotFoundException;
}
