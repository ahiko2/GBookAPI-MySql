package bookPrintout;

import bookDTO.BookDto;
import java.util.List;

public class BookPrinter {
    public static void printBookData(List<BookDto> books) {
        for (BookDto book : books) {
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Authors: " + book.getAuthors());
            System.out.println("Published Date: " + book.getPublishedDate());
            System.out.println();
        }
    }
}
