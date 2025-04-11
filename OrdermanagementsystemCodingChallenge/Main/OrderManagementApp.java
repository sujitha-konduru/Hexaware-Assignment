package Main;

import dao.OrderProcessor;
import entity.Clothing;
import entity.Electronics;
import entity.Product;
import entity.User;
import exception.OrderNotFoundException;
import exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderManagementApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderProcessor processor = new OrderProcessor();

        while (true) {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Create User");
            System.out.println("2. Add Product (Admin only)");
            System.out.println("3. Create Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. View All Products");
            System.out.println("6. View Orders by User");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ignored) {}

            try {
                switch (choice) {
                    case 1: {
                        System.out.print("Enter User ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Username: ");
                        String uname = sc.nextLine();
                        System.out.print("Password: ");
                        String pass = sc.nextLine();
                        System.out.print("Role (Admin/Customer): ");
                        String role = sc.nextLine();
                        processor.createUser(new User(id, uname, pass, role));
                        System.out.println("User created successfully.");
                        break;
                    }
                    case 2: {
                        System.out.print("Admin User ID: ");
                        int adminId = Integer.parseInt(sc.nextLine());
                        System.out.print("Product ID: ");
                        int pid = Integer.parseInt(sc.nextLine());
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Description: ");
                        String desc = sc.nextLine();
                        System.out.print("Price: ");
                        double price = Double.parseDouble(sc.nextLine());
                        System.out.print("Stock: ");
                        int stock = Integer.parseInt(sc.nextLine());
                        System.out.print("Type (Electronics/Clothing): ");
                        String type = sc.nextLine();

                        Product product;
                        if (type.equalsIgnoreCase("Electronics")) {
                            System.out.print("Brand: ");
                            String brand = sc.nextLine();
                            System.out.print("Warranty Period (months): ");
                            int warranty = Integer.parseInt(sc.nextLine());
                            product = new Electronics(pid, name, desc, price, stock, brand, warranty);
                        } else if (type.equalsIgnoreCase("Clothing")) {
                            System.out.print("Size: ");
                            String size = sc.nextLine();
                            System.out.print("Color: ");
                            String color = sc.nextLine();
                            product = new Clothing(pid, name, desc, price, stock, size, color);
                        } else {
                            System.out.println("Invalid product type.");
                            break;
                        }

                        processor.createProduct(new User(adminId, null, null, "Admin"), product);
                        System.out.println("Product added.");
                        break; // <-- IMPORTANT FIX
                    }
                    case 3: {
                        System.out.print("User ID: ");
                        int userId = Integer.parseInt(sc.nextLine());
                        User user = new User(userId, null, null, "Customer");

                        List<Product> allProducts = processor.getAllProducts();
                        if (allProducts.isEmpty()) {
                            System.out.println("No products available.");
                            break;
                        }

                        List<Product> selected = new ArrayList<>();
                        System.out.println("Available Products:");
                        for (Product p : allProducts) {
                            System.out.println(p.getProductId() + ": " + p.getProductName() + " - ₹" + p.getPrice());
                        }

                        while (true) {
                            System.out.print("Enter Product ID to add to order (0 to finish): ");
                            int prodId = Integer.parseInt(sc.nextLine());
                            if (prodId == 0) break;
                            for (Product p : allProducts) {
                                if (p.getProductId() == prodId) {
                                    selected.add(p);
                                    break;
                                }
                            }
                        }

                        if (!selected.isEmpty()) {
                            processor.createOrder(user, selected);
                            System.out.println("Order placed successfully.");
                        } else {
                            System.out.println("No products selected. Order not created.");
                        }
                        break;
                    }
                    case 4: {
                        System.out.print("Enter User ID: ");
                        int uid = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter Order ID: ");
                        int oid = Integer.parseInt(sc.nextLine());
                        processor.cancelOrder(uid, oid);
                        System.out.println("Order cancelled.");
                        break;
                    }
                    case 5: {
                        List<Product> products = processor.getAllProducts();
                        System.out.println("All Products:");
                        for (Product p : products) {
                            System.out.println(p.getProductId() + ": " + p.getProductName() + " - ₹" + p.getPrice());
                        }
                        break;
                    }
                    case 6: {
                        System.out.print("Enter User ID: ");
                        int uid = Integer.parseInt(sc.nextLine());
                        List<Product> orders = processor.getOrderByUser(new User(uid, null, null, null));
                        System.out.println("Products ordered by user:");
                        for (Product p : orders) {
                            System.out.println(p.getProductId() + ": " + p.getProductName() + " - ₹" + p.getPrice());
                        }
                        break;
                    }
                    case 7: {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    default: System.out.println("Invalid choice! Try again.");
                }
            } catch (UserNotFoundException | OrderNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Unexpected Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e);
            }
        }
    }
}
