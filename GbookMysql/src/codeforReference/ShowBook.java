package codeforReference;

import Util.DbUtil;
import bookDAO.GoogleBooksDao;
import bookDTO.BookDto;
import urlconnection.ConnUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ShowBook {
    static Scanner sc = new Scanner(System.in);
    public static void SearchBook() throws SQLException, ClassNotFoundException {
        Connection con = DbUtil.getConnection();
        // API endpoint and query parameters
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        String query = sc.nextLine();
        //https://www.googleapis.com/books/v1/volumes?q=isbn:9784844336778

        try {
            // Send GET request and retrieve response
            String jsonResponse = ConnUtil.sendGetRequest(apiUrl + query);
            // Create GoogleBooksDao instance
            GoogleBooksDao booksDao = new GoogleBooksDao(con);
            // Parse JSON and retrieve book data
            List<BookDto> books = booksDao.parseJsonResponse(jsonResponse);
            // Print book data
            for (BookDto book : books) {
                System.out.println("ISBN: " + book.getIsbn());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Authors: " + book.getAuthors());
                System.out.println("Published Date: " + book.getPublishedDate());
                System.out.println();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }
}
