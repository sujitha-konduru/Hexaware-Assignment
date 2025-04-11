package dao;

import entity.Product;
import entity.User;
import exception.OrderNotFoundException;
import exception.UserNotFoundException;
import utility.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor implements IOrderManagementRepository {

    @Override
    public void createUser(User user) throws SQLException {
        try (Connection conn = DBConnUtil.getConnection()) {
            String query = "INSERT INTO Users (userId, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
        }
    }

    @Override
    public void createProduct(User user, Product product) throws SQLException, UserNotFoundException {
        try (Connection conn = DBConnUtil.getConnection()) {
            String userCheck = "SELECT * FROM Users WHERE userId = ? AND role = 'Admin'";
            PreparedStatement checkStmt = conn.prepareStatement(userCheck);
            checkStmt.setInt(1, user.getUserId());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new UserNotFoundException("Admin user not found");
            }

            String query = "INSERT INTO Products (productId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, product.getProductId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getQuantityInStock());
            ps.setString(6, product.getType());
            ps.executeUpdate();

            if (product.getType().equalsIgnoreCase("Electronics")) {
                entity.Electronics electronics = (entity.Electronics) product;
                String electronicsQuery = "INSERT INTO Electronics (productId, brand, warrantyPeriod) VALUES (?, ?, ?)";
                PreparedStatement elecStmt = conn.prepareStatement(electronicsQuery);
                elecStmt.setInt(1, electronics.getProductId());
                elecStmt.setString(2, electronics.getBrand());
                elecStmt.setInt(3, electronics.getWarrantyPeriod());
                elecStmt.executeUpdate();
            } else if (product.getType().equalsIgnoreCase("Clothing")) {
                entity.Clothing clothing = (entity.Clothing) product;
                String clothingQuery = "INSERT INTO Clothing (productId, size, color) VALUES (?, ?, ?)";
                PreparedStatement clothStmt = conn.prepareStatement(clothingQuery);
                clothStmt.setInt(1, clothing.getProductId());
                clothStmt.setString(2, clothing.getSize());
                clothStmt.setString(3, clothing.getColor());
                clothStmt.executeUpdate();
            }
        }
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection()) {
            String query = "SELECT * FROM Products";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type")
                );
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public void createOrder(User user, List<Product> products) throws SQLException, UserNotFoundException {
        try (Connection conn = DBConnUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String userQuery = "SELECT * FROM Users WHERE userId = ?";
                PreparedStatement userStmt = conn.prepareStatement(userQuery);
                userStmt.setInt(1, user.getUserId());
                ResultSet userRs = userStmt.executeQuery();
                if (!userRs.next()) {
                    throw new UserNotFoundException("User not found");
                }

                String insertOrder = "INSERT INTO Orders (userId) VALUES (?)";
                PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
                orderStmt.setInt(1, user.getUserId());
                orderStmt.executeUpdate();
                ResultSet keys = orderStmt.getGeneratedKeys();
                int orderId = -1;
                if (keys.next()) {
                    orderId = keys.getInt(1);
                }

                String insertItem = "INSERT INTO OrderDetails (orderId, productId) VALUES (?, ?)";
                PreparedStatement itemStmt = conn.prepareStatement(insertItem);
                for (Product p : products) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, p.getProductId());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws SQLException, OrderNotFoundException {
        try (Connection conn = DBConnUtil.getConnection()) {
            String checkQuery = "SELECT * FROM Orders WHERE orderId = ? AND userId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, orderId);
            checkStmt.setInt(2, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new OrderNotFoundException("Order not found");
            }

            String deleteItems = "DELETE FROM OrderDetails WHERE orderId = ?";
            PreparedStatement delItemsStmt = conn.prepareStatement(deleteItems);
            delItemsStmt.setInt(1, orderId);
            delItemsStmt.executeUpdate();

            String deleteOrder = "DELETE FROM Orders WHERE orderId = ?";
            PreparedStatement delOrderStmt = conn.prepareStatement(deleteOrder);
            delOrderStmt.setInt(1, orderId);
            delOrderStmt.executeUpdate();
        }
    }

    @Override
    public List<Product> getOrderByUser(User user) throws SQLException {
        List<Product> productList = new ArrayList<>();
        try (Connection conn = DBConnUtil.getConnection()) {
            String query = "SELECT p.productId, p.productName, p.description, p.price, p.quantityInStock, p.type " +
                    "FROM Products p " +
                    "JOIN OrderDetails od ON p.productId = od.productId " +
                    "JOIN Orders o ON od.orderId = o.orderId " +
                    "WHERE o.userId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        rs.getString("type")
                );
                productList.add(p);
            }
        }
        return productList;
    }
}
