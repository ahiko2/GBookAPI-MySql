import Util.DbUtil;
import bookDAO.GoogleBooksDao;
import bookDTO.BookDto;
import bookPrintout.BookPrinter;
import urlconnection.ConnUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("System Begin");
        while (true) {
            try {
                new Main().start();
            } catch (Exception e) {
                System.out.println("Error");
                e.printStackTrace();
            }
        }
    }
    public void start() throws ClassNotFoundException, SQLException {
        System.out.println();
        System.out.println("1:ShowBook 2:Add newBook 3:DeleteBook 0:Finished");
        String ope = sc.nextLine();
        switch (ope) {
            case "1":
                showBooks();
                break;
            case "2":
                SearchBook();
                break;
            case "3":
                break;

            case "0":
                System.out.println("システムを終了します");
                System.exit(0);
            default:
                System.out.println("指定の番号を入力してください");
                break;
        }
    }

    public static void showBooks() throws SQLException {
        Connection con = null;
        GoogleBooksDao booksDao = null;

        try {
             con = DbUtil.getConnection(); //Establish connection
             booksDao = new GoogleBooksDao(con); //Pass connection to Dao

            List<BookDto> books = booksDao.selectAll(); //Call selectAll method
            BookPrinter.printBookData(books);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
    }
    public static void SearchBook() throws SQLException, ClassNotFoundException {
        System.out.println("Enter ISBN code:");
        Connection con = DbUtil.getConnection();
        // API endpoint and query parameters
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

        //https://www.googleapis.com/books/v1/volumes?q=isbn:9784844336778

        try {
            String query = sc.nextLine();
            // Send GET request and retrieve response
            String jsonResponse = ConnUtil.sendGetRequest(apiUrl + query);
            // Create GoogleBooksDao instance
            GoogleBooksDao booksDao = new GoogleBooksDao(con);
            // Parse JSON and retrieve book data
            List<BookDto> books = booksDao.parseJsonResponse(jsonResponse);
            // Print book data
            if (!books.isEmpty()) {
                BookDto book = books.get(0); // Get the first book
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Authors: " + book.getAuthors());
                System.out.println("Published Date: " + book.getPublishedDate());
                System.out.println();

                System.out.println("Do you want to save this book to the database? (yes/no)");
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("y")) {
                    // Save book to database
                    booksDao.saveBookToDatabase(book);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(con); // Close connection
        }
    }




}
