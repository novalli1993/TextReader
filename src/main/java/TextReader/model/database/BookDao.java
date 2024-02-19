package TextReader.model.database;

import TextReader.model.data.Book;
import TextReader.model.data.Chapter;
import TextReader.model.data.Paragraph;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class BookDao {
    DataSource ds;

    public BookDao(DataSource ds) {
        this.ds = ds;
    }
    public Book getById(Long id) {
        Book book = null;
        try(Connection conn = ds.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM reader WHERE id=?");
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    book = new Book(
                            rs.getString("name"),
                            rs.getString("filepath"),
                            (List<Paragraph>) rs.getObject("paragraphs"),
                            (List<Chapter>) rs.getObject("chapters")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public void setBook(Book book) {
        try(Connection conn = ds.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM reader WHERE name=?")) {
                ps.setString(1,book.getName());
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    throw new RuntimeException("The book \""+ book.getName() +"\" exists.");
                }
            }
            String name;
            String filePath;
            List<Paragraph> paragraphs;
            List<Chapter> chapters;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
