package dbtool;

import dbtool.data.*;
import dbtool.DataTool;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataToolTest {
    DataTool dt;

    @BeforeEach
    void beforeEach() {
        this.dt = new DataTool();
    }

    @Test
    void testAddBook() {
        book b = new bookBuilder().setName("语文").setPublisher("人民出版社").setStoreCount(10).getBook();
        dt.insertBook(b);
    }

    @Test
    void testAddBooks() {
        book b1 = new bookBuilder().setName("语文").setPublisher("人民出版社").setStoreCount(10).getBook();
        book b2 = new bookBuilder().setName("语文").setPublisher("人民出版社").setStoreCount(10).getBook();
        List<book> bl = new ArrayList<>();
        bl.add(b1);
        bl.add(b2);
        dt.insertBook(bl);
    }

    @Test
    void testUpdateBook() {
        book b = new bookBuilder().setName("语文").setPublisher("人民出版社").setStoreCount(10).setID(18).getBook();
        b.setName("Math");
        dt.updateBook(b);
    }

    @Test
    void testUpdateBooks() {
        book b = new bookBuilder().setName("语文").setPublisher("人民出版社").setStoreCount(10).setID(18).getBook();
        b.setName("Math");
        List<book> books = new ArrayList<>();
        books.add(b);
        dt.updateBook(books);
    }

    @Test
    void testSearchBook() {
        List<book> res = dt.searchBookByName("语文");
        System.out.println(res.size());
    }

    @Test
    void testSearchAllBook() {
        Map<String, Object> con = new HashMap<>();
//        con.put("aa", 0);
        List<book> res = dt.searchBook(con);
        System.out.println(res.size());
    }

    @Test
    void testDeleteBook() {
        Map<String, Object> con = new HashMap<>();
        con.put("name", "语文");
        int count = dt.deleteBook(con);
        System.out.println(count);
    }

    @Test
    void temp() {
        System.out.println(book.attr.id);
    }
}