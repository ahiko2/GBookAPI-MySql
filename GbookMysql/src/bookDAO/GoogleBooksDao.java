package bookDAO;

import Util.DbUtil;
import bookDTO.BookDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleBooksDao {
    private Gson gson;

    public Connection con;

    PreparedStatement stmt;

    ResultSet rs;

    public GoogleBooksDao(Connection con) {
        gson = new Gson();
        this.con = con;
    }

    public List<BookDto> parseJsonResponse(String jsonResponse) {
        List<BookDto> books = new ArrayList<>();

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray itemsArray = jsonObject.getAsJsonArray("items");

        for (JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            JsonObject volumeInfoObject = itemObject.getAsJsonObject("volumeInfo");

            String isbn = "";
            String title = "";
            List<String> authors = new ArrayList<>();
            String publishedDate = "";

            if (volumeInfoObject.has("industryIdentifiers")) {
                JsonArray identifiersArray = volumeInfoObject.getAsJsonArray("industryIdentifiers");
                for (int i = 0; i < identifiersArray.size(); i++) {
                    JsonObject identifierObject = identifiersArray.get(i).getAsJsonObject();
                    String type = identifierObject.get("type").getAsString();
                    if (type.equals("ISBN_13")) {
                        isbn = identifierObject.get("identifier").getAsString();
                        break; // Assuming you want to retrieve only the first ISBN-13
                    }
                }
            }

            if (volumeInfoObject.has("title")) {
                title = volumeInfoObject.get("title").getAsString(); // Fixed point: Retrieves the "title" value
            }

            if (volumeInfoObject.has("authors")) {
                JsonArray authorsArray = volumeInfoObject.getAsJsonArray("authors");
                for (JsonElement authorElement : authorsArray) {
                    authors.add(authorElement.getAsString()); // Fixed point: Retrieves each "author" value
                }
            }

            if (volumeInfoObject.has("publishedDate")) {
                publishedDate = volumeInfoObject.get("publishedDate").getAsString(); // Fixed point: Retrieves the "publishedDate" value

                if (publishedDate.length() == 7) {
                    publishedDate += "-01"; // Append "-01" to set the day as 1
                }

            }

            BookDto bookDto = new BookDto();
            bookDto.setIsbn(isbn);
            bookDto.setTitle(title);
            bookDto.setAuthors(authors);
            bookDto.setPublishedDate(publishedDate);

            books.add(bookDto);
        }

        return books;
    }
    public List<BookDto> selectAll() throws SQLException, ClassNotFoundException {
        List<BookDto> books = new ArrayList<>();
        String sql = "SELECT * FROM booklist";

        try {
            this.stmt = this.con.prepareStatement(sql);
            this.rs = stmt.executeQuery();
            while (rs.next()) {
                BookDto book = new BookDto();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthors(Arrays.asList(rs.getString("authors").split(", ")));
                book.setPublishedDate(rs.getString("published_date"));
                books.add(book);
            }
        } finally {
            DbUtil.closeStatement(this.stmt);
        }
        return books;
    }

}
