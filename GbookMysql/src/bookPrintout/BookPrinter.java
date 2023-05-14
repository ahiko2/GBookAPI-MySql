package bookPrintout;

import bookDTO.BookDto;
import java.util.List;

public class BookPrinter {
    public static void printBookData(List<BookDto> books) {
        int i = 1;
        for (BookDto book : books) {
            System.out.println(i);
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Authors: " + book.getAuthors());
            System.out.println("Published Date: " + book.getPublishedDate());
            System.out.println();
            i++;
        }
    }

    public static void printBookData_intableform(List<BookDto> books) {
        // Print header
        System.out.println(String.format("%-20s %-50s %-50s %-20s", "ISBN", "Title", "Authors", "Published Date"));
        System.out.println(String.format("%-20s %-50s %-50s %-20s", "----", "-----", "-------", "--------------"));
        // Print book data
        for (BookDto book : books) {
            String authors = String.join(", ", book.getAuthors());
            System.out.println(String.format("%-20s %-50s %-50s %-20s",
                    book.getIsbn(),
                    book.getTitle(),
                    authors,
                    book.getPublishedDate()));
        }
    }
}
