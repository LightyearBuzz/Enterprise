/*Web-приложение Storage было написано 10.11.2019
*это приложение представляет из себя типичное CRUD-приложение.
*Его основная задача - удобное отображение состояния склада с товарами.
*В таблице указано наименование, количество, цена, и место расположения на складе.

*В приложении используются следующие технологии:
*1.язык програмирования Java
*2.система сборки Apache Maven
*3.контейнер сервлетов Apache Tomcat
*4.Servlet API
*5.JSP
*6.JDBC
*7.PostgreSQL
 */


package com.github.lightyear;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//ControllerServlet выполняет функцию контроллера,
//тоесть является посредником между пользователем и системой.
//Контейнер сервлетов вызывает метод init для ввода сервлета в эксплуатацию,
//затем вызывает метод service для передачи ему объектов request и response.
//А метод service вызывает соответствующий метод doGet или doPost.
public class ControllerServlet extends HttpServlet {
    private ItemDAO itemDAO;//создадим ссылочную переменную объекта доступа к данным

    //производит инициализацию переменных которые хранят параметры подключения к базе,
    //а также создает объект доступа к данным
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        itemDAO = new ItemDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    //doPost обрабатывает запросы типа Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/insert"://срабатывает при нажатии Save в AddItem
                    insertItem(request, response);
                    break;

                case "/update"://срабатывает при нажатии Save в EditItem
                    updateItem(request, response);
                    break;
                           }
        } catch (Exception e) {
            processingException(e, response);
        }
    }


    //doGet обрабатывает запросы типа Get
    //Получает часть пути по которому вызвали данный сервлет
    //и в зависимости от него вызывает подходящий метод
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new"://срабатывает при нажатии Add New Item
                    showNewForm(request, response);
                    break;
                case "/delete"://срабатывает при нажатии Delete
                    deleteItem(request, response);
                    break;
                case "/edit"://срабатывает при нажатии Edit
                    showEditForm(request, response);
                    break;
                default://срабатывает при входе
                    showListItems(request, response);
                    break;
            }
        } catch (Exception e) {
            processingException(e, response);
        }
    }

    //Получает List товаров, передает его в request для jsp, выполняет перенаправление на ItemList.jsp
    private void showListItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Item> listItems = itemDAO.getListItems();
        request.setAttribute("listItems", listItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("ItemList.jsp");
        dispatcher.forward(request, response);
    }

    //Выполняет перенаправление на AddItemForm.jsp
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("AddItemForm.jsp");
        dispatcher.forward(request, response);
    }

    //Получает параметр id из request, по id получает из базы объект Item, передает его в request для jsp,
    //выполняет перенаправление на AddItemForm.jsp
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Item existingItem = itemDAO.getItem(id);//получает Item из базы по id
        RequestDispatcher dispatcher = request.getRequestDispatcher("EditItemForm.jsp");//создает диспетчер для перенаправления
        request.setAttribute("item", existingItem);
        dispatcher.forward(request, response);
    }

    //Получает из request параметры title quantity price location,
    //затем создает объект Item и передает его в базу
    private void insertItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String title = request.getParameter("title");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        float price = Float.parseFloat(request.getParameter("price"));
        String location = request.getParameter("location");

        Item newItem = new Item(title, quantity, price, location);
        itemDAO.insertItem(newItem);
        response.sendRedirect("list");
    }

    //Получает из request параметры id, title, quantity, price, location,
    //затем создает объект Item и передает его в базу
    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        float price = Float.parseFloat(request.getParameter("price"));
        String location = request.getParameter("location");

        Item item = new Item(id, title, quantity, price, location);
        itemDAO.updateItem(item);
        response.sendRedirect("list");
    }

    //Получает из request параметр id, передает id в itemDAO для удаления записи
    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        itemDAO.deleteItem(id);
        response.sendRedirect("list");
    }


    //Пытается обработать все исключения в одном месте,
    //обрабатываются 3 типа исключений и выводиться 2 отчета
    //один для пользователя второй для разработчика.
    private void processingException(Exception e, HttpServletResponse response) {
        try {
            PrintWriter pw = response.getWriter();
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                pw.println("Error 001, please contact the support service.");
            }

            if (e instanceof IOException) {
                e.printStackTrace(System.err);
                System.err.println("Failed to redirect or forward");
                System.err.println("Message: " + e.getMessage());
                pw.println("Error 002, please contact the support service.");
            }

            if (e instanceof ServletException) {
                e.printStackTrace(System.err);
                System.err.println("Failed to forward");
                System.err.println("Message: " + e.getMessage());
                pw.println("Error 003, please contact the support service.");
            }

        }catch(IOException IOe){
            IOe.printStackTrace(System.err);
        }
    }
}