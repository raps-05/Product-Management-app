import java.sql.*;
import java.util.*;
//import java.io.*;

// Product class
class Product {
  private int productId;
  private String productName;
  private List<CustomerReview> reviews;
  private Features features;

  public Product(int productId, String productName) {
    this.productId = productId;
    this.productName = productName;
    this.reviews = new ArrayList<>();
    this.features = new Features();
  }

  // Getters for productId and productName
  public int getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void addReview(CustomerReview review) {
    reviews.add(review);
  }

  public List<CustomerReview> getReviews() {
    return reviews;
  }

  public Features getFeatures() {
    return features;
  }

  public double calculateAverageRatingOfProduct() {
    if (reviews.isEmpty()) {
      return 0.0;
    }

    int totalRating = 0;
    for (CustomerReview review : reviews) {
      totalRating += review.getRating();
    }

    return (double) totalRating / reviews.size();
  }
}

// CustomerReview class
class CustomerReview {
  private String reviewText;
  private int rating;

  public CustomerReview(String reviewText, int rating) {
    this.reviewText = reviewText;
    this.rating = rating;
  }

  public String getReviewText() {
    return reviewText;

  }

  public void setReviewText(String reviewText) {
    this.reviewText = reviewText;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }
}

class Features {
  private Map<String, Integer> featureRatings;

  public Features() {
    featureRatings = new HashMap<>();
  }

  public void addFeatureRating(String featureName, int rating) {
    featureRatings.put(featureName, rating);
  }

  public int getFeatureRating(String featureName) {
    return featureRatings.getOrDefault(featureName, 0);
  }

  public Map<String, Integer> getFeatureRatings() {
    return featureRatings;
  }
}

class MySQLConnector {
  private Connection connection;

  public MySQLConnector(String url, String username,
      String password) throws SQLException {
    connection = DriverManager.getConnection(url, username, password);
  }

  public void insertFeatureRating(int productId, String featureName,
      int rating,
      double averageRating) throws SQLException {
    String query = "INSERT INTO feature_ratings (product_id, feature_name, rating, average_rating) VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, productId);
      preparedStatement.setString(2, featureName);
      preparedStatement.setInt(3, rating);
      preparedStatement.setDouble(4, averageRating);
      preparedStatement.executeUpdate();
    }
  }

  public void insertProduct(Product product) throws SQLException {
    String query = "INSERT INTO products (product_id, product_name) VALUES (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, product.getProductId());
      preparedStatement.setString(2, product.getProductName());
      preparedStatement.executeUpdate();
    }
  }

  public void insertCustomerReview(int productId,
      CustomerReview review) throws SQLException {
    String query = "INSERT INTO reviews (product_id, review_text, rating) VALUES (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, productId);
      preparedStatement.setString(2, review.getReviewText());
      preparedStatement.setInt(3, review.getRating());
      preparedStatement.executeUpdate();
    }
  }

  public Map<String, Integer> getFeatureRatings(int productId) throws SQLException {
    Map<String, Integer> featureRatings = new HashMap<>();
    String query = "SELECT feature_name, rating FROM feature_ratings WHERE product_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, productId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          String featureName = resultSet.getString("feature_name");
          int rating = resultSet.getInt("rating");
          featureRatings.put(featureName, rating);
        }
      }
    }
    return featureRatings;
  }

  public int getFeatureRating(int productId,
      String featureName) throws SQLException {
    String query = "SELECT rating FROM feature_ratings WHERE product_id = ? AND feature_name = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, productId);
      preparedStatement.setString(2, featureName);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt("rating");
        }
      }
    }
    return 0;
  }

  public void insertAverageRating(int productId,
      double averageRating) throws SQLException {
    String query = "UPDATE products SET average_rating = ? WHERE product_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setDouble(1, averageRating);
      preparedStatement.setInt(2, productId);
      preparedStatement.executeUpdate();
    }
  }

  public void updateProductName(int productId,
      String newProductName) throws SQLException {
    String query = "UPDATE products SET product_name = ? WHERE product_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, newProductName);
      preparedStatement.setInt(2, productId);
      preparedStatement.executeUpdate();
    }
  }

  public void updateFeatureRating(int productId, String featureName,
      int newRating) throws SQLException {
    String query = "UPDATE feature_ratings SET rating = ? WHERE product_id = ? AND feature_name = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, newRating);
      preparedStatement.setInt(2, productId);
      preparedStatement.setString(3, featureName);
      preparedStatement.executeUpdate();
    }
  }

  public void updateCustomerReview(int productId,
      CustomerReview review) throws SQLException {
    String query = "UPDATE reviews SET review_text = ?, rating = ? WHERE product_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, review.getReviewText());
      preparedStatement.setInt(2, review.getRating());
      preparedStatement.setInt(3, productId);
      preparedStatement.executeUpdate();
    }
  }

  public void deleteProduct(int productId) throws SQLException {
    String query = "DELETE FROM products WHERE product_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, productId);
      preparedStatement.executeUpdate();
    }
  }

  // public void updateProduct(int productId, String newProductName) throws
  // SQLException {
  // String query = "{CALL UpdateProductName(?, ?)}";
  // try (CallableStatement callableStatement = connection.prepareCall(query)) {
  // callableStatement.setInt(1, productId);
  // callableStatement.setString(2, newProductName);
  // callableStatement.executeUpdate();
  // }
  // }

  public void closeConnection() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
}

public class App {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Product product = null;
    int productId = 0; // Declare and initialize productId
    try {
      // Connect to MySQL database
      String url = "jdbc:mysql://localhost:3306/customer_data"; // Replace with your database URL
      String username = "users"; // Replace with your MySQL username
      String password = ""; // Replace with your MySQL password
      MySQLConnector connector = new MySQLConnector(url, username, password);

      int choice;
      do {
        System.out.println("\nMENU");
        System.out.println("1. Add Product");
        System.out.println("2. Add Customer Review");
        System.out.println("3. Add Feature Rating");
        System.out.println("4. Calculate Average Rating");
        System.out.println("5. Display Feature Ratings");
        System.out.println("6. Update Product_Name");
        //System.out.println("7. Update Feature Ratings");
        System.out.println("8. Update Customer Review");
        System.out.println("9. Delete product information");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left by nextInt()

        switch (choice) {
          case 1:
            // Moved product creation outside the loop
            if (product == null) {
              System.out.print("Enter Product ID: ");
              productId = scanner.nextInt();
              scanner.nextLine(); // Consume the newline character left by nextInt()

              System.out.print("Enter Product Name: ");
              String productName = scanner.nextLine();
              System.out.println("Product is added!");

              product = new Product(productId, productName);
              connector.insertProduct(product);
            } else {
              System.out.println("Product already added!");
            }
            break;

          case 2:
            if (product != null) {

              System.out.print("Enter Product ID: ");
              int productIdForReview = scanner.nextInt();
              scanner.nextLine(); // Consume the newline character left by nextInt()

              System.out.print("Enter Review Text: ");
              String reviewText = scanner.nextLine();

              System.out.print("Enter Rating (1-5): ");
              int rating = scanner.nextInt();
              scanner.nextLine();

              CustomerReview review = new CustomerReview(reviewText, rating);
              product.addReview(review);
              connector.insertCustomerReview(productIdForReview, review);
            }
            break;

          case 3:
            if (product != null) {
              System.out.print("Enter Product ID: ");
              int productIdForFeature = scanner.nextInt();
              scanner.nextLine(); // Consume the newline character left by nextInt()

              Features features = new Features();

              System.out.print("How many features do you want to rate? ");
              int numFeatures = scanner.nextInt();
              scanner.nextLine();

              for (int i = 0; i < numFeatures; i++) {
                System.out.print("Enter Feature Name: ");
                String featureName = scanner.nextLine();

                System.out.print("Enter Rating (1-5): ");
                int featureRating = scanner.nextInt();
                scanner.nextLine();

                features.addFeatureRating(featureName, featureRating);
                connector.insertFeatureRating(productIdForFeature,
                    featureName,
                    featureRating, 0);
              }
            } else {
              System.out.println("Please add a product first!");
            }
            break;

          case 4:
            System.out.print("Enter Product ID: ");
            int productIdForAvgRating = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            // double avgRating = product.calculateAverageRatingOfProduct ();
            // connector.insertAverageRating (productIdForAvgRating,
            // avgRating);
            // System.out.println ("Average Rating of the Product: " +
            // avgRating);
            // break;

            if (product != null) {
              double averageRating = product.calculateAverageRatingOfProduct();
              System.out.println("Average Rating of the Product: " +
                  averageRating);
            } else {
              System.out.println("Please add a product first!");
            }
            break;

          case 5:
            System.out.print("Enter Product ID: ");
            int productIdForFeatureRatings = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            System.out.println("Feature Ratings:");
            for (String featureName : connector.getFeatureRatings(productIdForFeatureRatings).keySet()) {
              int featureRating = connector.getFeatureRating(productIdForFeatureRatings,
                  featureName);
              System.out.println(featureName + ": " + featureRating);
            }
            break;

          case 6:
            if (connector != null) {
              System.out.print("Enter new Product Name: ");
              String newProductName = scanner.nextLine();
              System.out.print("Enter Product ID: ");
              productId = scanner.nextInt();
              scanner.nextLine(); // Consume the newline character left by nextInt()
              connector.updateProductName(productId, newProductName);
              System.out.println("Product Name updated successfully!");
            } else {
              System.out.println("No product found. Please add a product first!");
            }
            break;

          case 7: // Update Feature Rating
            if (product != null) {
              Map<String, Integer> featureRatings = product.getFeatures().getFeatureRatings();
              if (!featureRatings.isEmpty()) {
                int index = 1;
                for (String featureName : featureRatings.keySet()) {
                  System.out.println(index + ". " + featureName +
                      ": " +
                      featureRatings.get(featureName));
                  index++;
                }
                System.out.print("Select the index of the feature to update: ");
                int selectedFeatureIndex = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                List<String> featureNames = new ArrayList<>(featureRatings.keySet());
                String selectedFeatureName = featureNames.get(selectedFeatureIndex - 1);

                System.out.print("Enter new Feature Rating (1-5): ");
                int newFeatureRating = scanner.nextInt();
                scanner.nextLine();

                product.getFeatures().addFeatureRating(selectedFeatureName,
                    newFeatureRating);
                connector.updateFeatureRating(product.getProductId(),
                    selectedFeatureName,
                    newFeatureRating);
                System.out.println("Feature Rating updated successfully!");
              } else {
                System.out.println("No features found. Please add feature ratings first!");
              }
            } else {
              System.out.println("No product found. Please add a product first!");
            }
            break;

          case 8: // Update Customer Review
            if (product != null) {
              List<CustomerReview> reviews = product.getReviews();
              if (!reviews.isEmpty()) {
                int index = 1;
                for (CustomerReview review : reviews) {
                  System.out.println(index + ". " +
                      review.getReviewText() + ": " +
                      review.getRating());
                  index++;
                }
                System.out.print("Select the index of the review to update: ");
                int selectedReviewIndex = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                CustomerReview selectedReview = reviews.get(selectedReviewIndex - 1);

                System.out.print("Enter new Review Text: ");
                String newReviewText = scanner.nextLine();

                System.out.print("Enter new Rating (1-5): ");
                int newRating = scanner.nextInt();
                scanner.nextLine();

                selectedReview.setReviewText(newReviewText);
                selectedReview.setRating(newRating);
                connector.updateCustomerReview(product.getProductId(),
                    selectedReview);
                System.out.println("Customer Review updated successfully!");
              } else {
                System.out.println("No customer reviews found. Please add reviews first!");
              }
            } else {
              System.out.println("No product found. Please add a product first!");
            }
            break;

          case 9:
            // Delete Product
            System.out.print("Enter Product ID to delete: ");
            int productIdToDelete = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character left by nextInt()

            connector.deleteProduct(productIdToDelete);
            System.out.println("Product with ID " + productIdToDelete +
                " deleted successfully!");
            break;
          case 10:
            System.out.println("Exiting...");
            break;

          default:
            System.out.println("Invalid choice. Please enter a valid option.");
            break;
        }

      } while (choice != 10);
      if (product != null) {
        double averageRating = product.calculateAverageRatingOfProduct();
        System.out.println("Average Rating of the Product: " +
            averageRating);
        connector.insertAverageRating(productId, averageRating);
      } else {
        System.out.println("No product found. Average rating calculation skipped.");
      }

      connector.closeConnection();

    }

    catch (InputMismatchException e) {
      System.out.println("Invalid input. Please enter valid values.");
    } catch (SQLException e) {
      System.out.println("Database error: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("An error occurred: " + e.getMessage());
    } finally {
      scanner.close();
    }

  }
}
