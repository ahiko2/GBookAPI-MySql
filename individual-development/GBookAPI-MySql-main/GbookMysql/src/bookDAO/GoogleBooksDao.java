package bookDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Util.DbUtil;
import bookDTO.BookDto;

public class GoogleBooksDao {
	
	//delcaration
	private static final String NM_TABLE = "booklist";
	
	
    private Gson gson;
    
    //一般のデータベース
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
            String description = "";

            if (volumeInfoObject.has("industryIdentifiers")) {
                JsonArray identifiersArray = volumeInfoObject.getAsJsonArray("industryIdentifiers");
                for (int i = 0; i < identifiersArray.size(); i++) {
                    JsonObject identifierObject = identifiersArray.get(i).getAsJsonObject();
                    String type = identifierObject.get("type").getAsString();
                    if (type.equals("ISBN_13")) {
                        isbn = identifierObject.get("identifier").getAsString();
                        break; // ISBNは2個あり、ISBN13だけ取得する
                    }
                }
            }

            if (volumeInfoObject.has("title")) {
                title = volumeInfoObject.get("title").getAsString(); // title取得
            }
            

            if (volumeInfoObject.has("authors")) {
                JsonArray authorsArray = volumeInfoObject.getAsJsonArray("authors");
                for (JsonElement authorElement : authorsArray) {
                    authors.add(authorElement.getAsString()); 
                    //作家は１人以上に場合のため
                    //googleapiで、authorsは array型のため
                }
            }

            if (volumeInfoObject.has("publishedDate")) {
                publishedDate = volumeInfoObject.get("publishedDate").getAsString(); 
                

                if (publishedDate.length() == 7) {
                    publishedDate += "-01"; 
                    // some books doesnt have day, and database only accept y/m/d so, if it didnt has date, +01
                }

            }
            if (volumeInfoObject.has("description")) {
                description = volumeInfoObject.get("description").getAsString(); // Retrieve the description
                if (description.length() > 500) {
                    description = description.substring(0, 500); // Truncate the description to 500 characters
                }
            }

            BookDto bookDto = new BookDto();
            bookDto.setIsbn(isbn);
            bookDto.setTitle(title);
            bookDto.setAuthors(authors);
            bookDto.setPublishedDate(publishedDate);
            bookDto.setDescription(description);
            
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
                book.setTimestamp(rs.getTimestamp("timestamp"));
                books.add(book);
            }
        } finally {
            DbUtil.closeStatement(this.stmt);
        }
        return books;
    }

    public void saveBookToDatabase(BookDto book) throws SQLException {
        
        String sql = "INSERT INTO "+NM_TABLE+" (isbn,title, authors, published_date,description) VALUES (?, ?, ?, ?, ?)";
        
        try {
            this.stmt = this.con.prepareStatement(sql);
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, String.join(", ", book.getAuthors())); // 複数人の１行にまとめる
            stmt.setString(4, book.getPublishedDate());
            stmt.setString(5,book.getDescription());
            
            
            int rowsAffected = stmt.executeUpdate();
            
            if(rowsAffected  ==1) {
            System.out.println("データベースに保存しました");
            }
        } finally {
            DbUtil.closeStatement(this.stmt);
        }
    }
    
    public  List<BookDto> selectbyISBN(long ISBN) throws SQLException{
    	
    	
    	List<BookDto> rtnList = new ArrayList<BookDto>();
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT * FROM ");
    	sql.append(NM_TABLE);
    	sql.append(" WHERE isbn =?");
    	
    	try {
    		this.stmt = con.prepareStatement(sql.toString());
    		stmt.setLong(1, ISBN); 
    		rs = stmt.executeQuery();
    		
    		while (rs.next()) {
                BookDto book = new BookDto();
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthors(Arrays.asList(rs.getString("authors").split(", ")));
                book.setPublishedDate(rs.getString("published_date"));
                book.setDescription(rs.getString("description"));
                
               

                
                rtnList.add(book);
            }
	
    	}finally {
            DbUtil.closeStatement(this.stmt);
        }
    	
    	return rtnList;
    }
    
    public void Delete_bookDAO(long ISBN) throws SQLException {
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE  FROM ");
        sql.append(NM_TABLE);
        sql.append(" WHERE isbn =?");
        try {
	        // Preparing the statement
	        this.stmt = con.prepareStatement(sql.toString());
	        
	        // Setting the parameter
	        stmt.setLong(1, ISBN);
	        
	        // Executing the query
	        int row_deleted = stmt.executeUpdate();
	        
	        // Checking the result
	        if(row_deleted > 0) {
	            System.out.println("データ削除しました");
	        }
        }finally {
        	 DbUtil.closeStatement(this.stmt);
        }
    }

    


}
