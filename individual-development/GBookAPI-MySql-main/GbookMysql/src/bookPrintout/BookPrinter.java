package bookPrintout;

import java.util.List;

import bookDTO.BookDto;

public class BookPrinter {
    public static void printBookData(List<BookDto> books) {
        int i = 1;
        for (BookDto book : books) {
            System.out.println(i);
            
            System.out.println("ISBNコード		: " + book.getIsbn());
            System.out.println("タイトル		: " + book.getTitle());
            System.out.println("作家名			: " + book.getAuthors());
            System.out.println("出版日			: " + book.getPublishedDate());
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
