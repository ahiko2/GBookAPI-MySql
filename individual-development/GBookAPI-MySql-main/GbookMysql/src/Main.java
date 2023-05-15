import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import Util.DbUtil;
import bookDAO.GoogleBooksDao;
import bookDTO.BookDto;
import bookPrintout.BookPrinter;
import urlconnection.ConnUtil;


public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("システム開始");
        while (true) {
            try {
                new Main().start();
            } catch (Exception e) {
                System.out.println("どこかでエラー");
                e.printStackTrace();
            }
        }
    }
    public static boolean isISBNValid(String isbn) {
        if (isbn.length() != 13) {
        	String flg = "ISBNのコードは数字の１３桁です";
        	System.out.println(flg);
            return false;
        }
        if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
        	
        	String flg = "ISBNコードではありません、数字であるか確認してください";
        	System.out.println(flg);
            return false;
        }
        return true;
    }
    
    public void start() throws ClassNotFoundException, SQLException {
        System.out.println();
        System.out.println("1:本の一覧 2:追加  3:本の削除 0:終了");
        String ope = sc.nextLine();
        switch (ope) {
            case "1":
                showBooks();
                break;
            case "2":
                SearchBook();
                break;
            case "3":
            	DeleteBook();
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

            List<BookDto> books = booksDao.selectAll(); //Call selectAll method from DAO
            BookPrinter.printBookData(books);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
    }
    public static void SearchBook() throws SQLException, ClassNotFoundException {
        System.out.println("ISBNコードを入力してください");
        Connection con = DbUtil.getConnection();
       
        //API link from google
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        
        //example
        //https://www.googleapis.com/books/v1/volumes?q=isbn:9784844336778

        try {
            String query = sc.nextLine();
            if (!isISBNValid(query)) {
                return;
            }
            
            // Send GET request and retrieve response
            String jsonResponse = ConnUtil.sendGetRequest(apiUrl + query);
            GoogleBooksDao booksDao = new GoogleBooksDao(con);
            // Parse JSON and retrieve book data
            List<BookDto> books = booksDao.parseJsonResponse(jsonResponse);
            // Print book data
            if (!books.isEmpty()) {
                BookDto book = books.get(0); // Get the first book
                System.out.println("ISBNコード		: " + book.getIsbn());
                System.out.println("タイトル		: " + book.getTitle());
                System.out.println("作家名			: " + book.getAuthors());
                System.out.println("出版日			: " + book.getPublishedDate());
                System.out.println();

                
                //since book.getisbn is string, need to parse this into long, cannot int coz too long
                long isbnLong = Long.parseLong(book.getIsbn());
                List<BookDto> existingBooks = booksDao.selectbyISBN(isbnLong);
                if (!existingBooks.isEmpty()) {
                    System.out.println("もうすでに保存済みです");
                } else {
                    System.out.println("こちらの本を登録しますか (Y/N)");
                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("y")) {
                        // Save book to database
                        booksDao.saveBookToDatabase(book);
                    }else {
                    	System.out.println("キャンセルしました");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(con); // Close connection
        }
    }
    
    
    
    public static void DeleteBook() throws ClassNotFoundException, SQLException {
    	 Connection con = null;
        try {
        	con = DbUtil.getConnection();

        	 System.out.println("ISBNコードを入力してください");
            String isbncode = sc.nextLine();
            if (!isISBNValid(isbncode)) {
                return;
            }
            
            GoogleBooksDao booksDao = new GoogleBooksDao(con);

            // String to Long
            long isbnLong = Long.parseLong(isbncode);

            // Check if the book exists in the database before deletion
            List<BookDto> existingBooks = booksDao.selectbyISBN(isbnLong);
            
            if (existingBooks.isEmpty()) {
                System.out.println("データベースには保存されておりません");
            } else {
                System.out.println("こちらの本を「削除」でよろしいですか(Y/N)");
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("y")) {
                    booksDao.Delete_bookDAO(isbnLong);
                } else {
                    System.out.println("キャンセルしました");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
    }





}
