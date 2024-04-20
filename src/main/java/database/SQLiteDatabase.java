package database;

import invoice.BusinessEntity;
import tablerows.CompaniesTableRow;
import tablerows.CustomersTableRow;
import utils.Password;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteDatabase {

    private static final String DATABASE_DIR = System.getProperty("user.dir") + "\\data";
    private static final String DATABASE_URL = String.format("jdbc:sqlite:%s\\SQLiteDatabase.db", DATABASE_DIR);

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT)";
    private static final String CREATE_COMPANIES_TABLE = "CREATE TABLE IF NOT EXISTS companies (name TEXT PRIMARY KEY, street TEXT, city TEXT, state TEXT, zipcode TEXT, user_id int)";
    private static final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS customers (name TEXT PRIMARY KEY, street TEXT, city TEXT, state TEXT, zipcode TEXT, user_id int)";

    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_USER_ID_QUERY = "SELECT id FROM users WHERE username = ?";
    private static final String SELECT_IS_USER_QUERY = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_IS_COMPANY_QUERY = "SELECT * FROM companies WHERE name = ? AND user_id = ?";
    private static final String SELECT_IS_CUSTOMER_QUERY = "SELECT * FROM customers WHERE name = ? AND user_id = ?";

    private static final String INSERT_USER_QUERY = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    private static final String INSERT_COMPANY_QUERY = "INSERT INTO companies (name, street, city, state, zipcode, user_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CUSTOMER_QUERY = "INSERT INTO customers (name, street, city, state, zipcode, user_id) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_COMPANY_QUERY = "UPDATE companies SET name = ?, street = ?, city = ?, state = ?, zipcode = ? WHERE name = ? AND user_id = ?";
    private static final String UPDATE_CUSTOMER_QUERY = "UPDATE customers SET name = ?, street = ?, city = ?, state = ?, zipcode = ? WHERE name = ? AND user_id = ?";

    private static final String DELETE_COMPANY_QUERY = "DELETE FROM companies WHERE name = ? AND user_id = ?";
    private static final String DELETE_CUSTOMER_QUERY = "DELETE FROM customers WHERE name = ? AND user_id = ?";

    private static final String SELECT_COMPANIES_QUERY = "SELECT name, street, city, state, zipcode FROM companies WHERE user_id = ?";
    private static final String SELECT_CUSTOMERS_QUERY = "SELECT name, street, city, state, zipcode FROM customers WHERE user_id = ?";

    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            printSQLException(e);
        }
        return connection;
    }

    public static boolean initDatabase() {
        File dir = new File(DATABASE_DIR);
        if (!dir.exists()) {
            boolean mkdirsSuccess = dir.mkdirs();
            if (!mkdirsSuccess) {
                return false;
            }
        }

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_USER_TABLE);
            statement.executeUpdate(CREATE_COMPANIES_TABLE);
            statement.executeUpdate(CREATE_CUSTOMERS_TABLE);
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean validate(String username, String password) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if (resultSet.next() && Password.validate(password, resultSet.getString("password"))) {
                    connection.close();
                    return true;
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static int getUserId(String username) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_ID_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public static boolean isUser(String username) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IS_USER_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean isCompany(String name, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IS_COMPANY_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, String.valueOf(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean isCustomer(String name, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IS_CUSTOMER_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, String.valueOf(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean insertUser(String username, String password, String email) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY);
            preparedStatement.setString(1, username);
            try {
                preparedStatement.setString(2, Password.hash(password));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                return false;
            }
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean insertCompany(BusinessEntity company, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMPANY_QUERY);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getAddress().getStreet());
            preparedStatement.setString(3, company.getAddress().getCity());
            preparedStatement.setString(4, company.getAddress().getState());
            preparedStatement.setString(5, company.getAddress().getZipcode());
            preparedStatement.setString(6, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean insertCustomer(BusinessEntity customer, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress().getStreet());
            preparedStatement.setString(3, customer.getAddress().getCity());
            preparedStatement.setString(4, customer.getAddress().getState());
            preparedStatement.setString(5, customer.getAddress().getZipcode());
            preparedStatement.setString(6, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean updateCompany(BusinessEntity company, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY_QUERY);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getAddress().getStreet());
            preparedStatement.setString(3, company.getAddress().getCity());
            preparedStatement.setString(4, company.getAddress().getState());
            preparedStatement.setString(5, company.getAddress().getZipcode());
            preparedStatement.setString(6, company.getName());
            preparedStatement.setString(7, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean updateCustomer(BusinessEntity customer, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getAddress().getStreet());
            preparedStatement.setString(3, customer.getAddress().getCity());
            preparedStatement.setString(4, customer.getAddress().getState());
            preparedStatement.setString(5, customer.getAddress().getZipcode());
            preparedStatement.setString(6, customer.getName());
            preparedStatement.setString(7, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean deleteCompany(BusinessEntity company, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY_QUERY);
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean deleteCustomer(BusinessEntity customer, int userId) {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_QUERY);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, String.valueOf(userId));
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static ArrayList<CompaniesTableRow> getCompanies(int userId) {
        ArrayList<CompaniesTableRow> companies = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COMPANIES_QUERY);
            preparedStatement.setString(1, String.valueOf(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companies.add(new CompaniesTableRow(resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zipcode")
                ));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return companies;
    }

    public static ArrayList<CustomersTableRow> getCustomers(int userId) {
        ArrayList<CustomersTableRow> customers = new ArrayList<>();
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMERS_QUERY);
            preparedStatement.setString(1, String.valueOf(userId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customers.add(new CustomersTableRow(resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zipcode")
                ));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return customers;
    }

    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            e.printStackTrace(System.err);
            System.err.println("SQLState: " + ((SQLException) e).getSQLState());
            System.err.println("Error code: " + ((SQLException) e).getErrorCode());
            System.err.println("Message: " + ex.getMessage());

            Throwable t = ex.getCause();
            while (t != null) {
                System.err.println("Cause: " + t);
                t = t.getCause();
            }
        }
    }
}
