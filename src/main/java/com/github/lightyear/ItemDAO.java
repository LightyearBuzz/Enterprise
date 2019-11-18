package com.github.lightyear;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public ItemDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    //создает соединение с базой если его нет, или оно закрыто
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    //разрывает соединение если оно есть и не закрыто
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    //создает PreparedStatement настраивает его на вставку объекта в базу и выполняет.
    //закрывает statement, закрывает connection
    public boolean insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO storagetable (title, quantity, price, location) VALUES (?, ?, ?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, item.getTitle());
        statement.setInt(2, item.getQuantity());
        statement.setFloat(3, item.getPrice());
        statement.setString(4, item.getLocation());
        boolean rowInserted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();

        return rowInserted;
    }

    //возвращает список всех товаров из таблицы.
    //закрывает resultSet, закрывает statement, закрывает connection
    public List<Item> getListItems() throws SQLException {
        List<Item> listItems = new ArrayList<Item>();

        String sql = "SELECT * FROM storagetable";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            int quantity = resultSet.getInt("quantity");
            float price = resultSet.getFloat("price");
            String location = resultSet.getString("location");

            Item item = new Item(id, title, quantity, price, location);
            listItems.add(item);
        }

        resultSet.close();
        statement.close();
        disconnect();

        return listItems;
    }

    //удаляет указанный элемент из таблицы с помощью PreparedStatement,
    //затем закрывает statement, закрывает connection
    public boolean deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM storagetable where id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();

        return rowDeleted;
    }

    //редактирует указанную запись в таблице с помощью PreparedStatement,
    //затем закрывает statement, закрывает connection
    public boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE storagetable SET title = ?, quantity = ?, price = ?, location = ?"
                        + " WHERE id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);

        statement.setString(1, item.getTitle());
        statement.setInt(2, item.getQuantity());
        statement.setFloat(3, item.getPrice());
        statement.setString(4, item.getLocation());
        statement.setInt(5, item.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;

        statement.close();
        disconnect();

        return rowUpdated;
    }

    //возвращает указанную запись в таблице с помощью PreparedStatement,
    //затем закрывает statement, закрывает connection
    public Item getItem(int id) throws SQLException {
        Item item = null;
        String sql = "SELECT * FROM storagetable WHERE id = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {//передвнем указатель тк он указывает перед первым элементом
            String title = resultSet.getString("title");
            int quantity = resultSet.getInt("quantity");
            float price = resultSet.getFloat("price");
            String location = resultSet.getString("location");

            item = new Item(id, title, quantity, price, location);
        }

        resultSet.close();
        statement.close();

        return item;
    }
}