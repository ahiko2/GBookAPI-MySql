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
    public static void printBookData_description(List<BookDto> books) {
        int i = 1;
        for (BookDto book : books) {
            System.out.println(i);
            
            System.out.println("ISBNコード		: " + book.getIsbn());
            System.out.println("タイトル		: " + book.getTitle());
            System.out.println("作家名			: " + book.getAuthors());
            System.out.println("出版日			: " + book.getPublishedDate());
            System.out.println("概要			: " + book.getDescription());
            System.out.println();
            i++;
        }
    }

    public static void printBookData_intableform(List<BookDto> books) {
        // Print header
        System.out.println(String.format("%-5s %-15s %-15s %-25s %-30s %-20s", "No.", "ISBN","Published Date","TimeStamp" , "Authors", "Title"));
        System.out.println(String.format("%-5s %-15s %-15s %-25s %-30s %-20s", "----", "----", "--------------", "-------", "-----","-----"));

        // Print book data
        for (int i = 0; i < books.size(); i++) {
            BookDto book = books.get(i);
            String authors = String.join(", ", book.getAuthors());
            if (authors.length() > 20) {
                authors = authors.substring(0, 20); // Limit the authors' names to 20 characters
            }
            System.out.println(String.format("%-5s %-15s %-15s %-25s %-30s %-20s",
                    i + 1,
                    book.getIsbn(),
                    book.getPublishedDate(),
                    book.getTimestamp(),
                    authors,
                    book.getTitle()
                   
            		));
        }
    }


    
    
}
