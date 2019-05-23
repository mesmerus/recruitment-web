package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
        //TODO instantiate the library and the repository
    	library = new LibraryImpl();
    	bookRepository = new BookRepository();
    	
    	
    	//TODO add some test books (use BookRepository#addBooks)
        List<Book> books = new ArrayList<Book>();
        
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src\\test\\resources\\books.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray bookList = (JSONArray) obj;
            
            if (bookList != null) { 
        	   for (int i=0;i<bookList.size();i++){ 
        		   JSONObject book =  (JSONObject) bookList.get(i);
        		   String title = book.get("title").toString();
        		   String author = book.get("author").toString();
        		   JSONObject isbn =  (JSONObject) book.get("isbn");
        		   Long isbnCode = Long.parseLong(isbn.get("isbnCode").toString());
        		   
        		   books.add(new Book(title,author,new ISBN(isbnCode)));
        	   } 
        	}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        bookRepository.addBooks(books);
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available(){
        //create a member
    	Member member = new Resident("laurent", "DUPONT" , 200, "resident", false);
    	
    	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
    	
    	//asserts
    	assertEquals(harryPotter.getTitle() , library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.now(), bookRepository).getTitle());
    	assertFalse(bookRepository.getBorrowedBooks().isEmpty());
    }
    

    @Test
    public void borrowed_book_is_no_longer_available(){
        //create a member
    	Member member = new Resident("laurent", "DUPONT" , 200, "resident", false);  	
    	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MAY, 10), bookRepository);
    	
   
    	//assert
    	assertEquals(null, bookRepository.findBook(Long.valueOf("46578964513")));
    	
    	
    	//try to borrow the same book again
    	Member member2 = new Resident("laurent", "DUCLOS" , 200, "resident", false);    	
    	//asserts
    	assertEquals(null, library.borrowBook(Long.valueOf("46578964513"), member2, LocalDate.now(), bookRepository));
    	
    }

   @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
       //create a member resident
   	Member member = new Resident("laurent", "DUPONT" , 200, "resident", false);  	
   	
   	//Harry Potter
	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
   	
	//borrow a book
	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MAY, 12), bookRepository);
	
	//return the book
	library.returnBook(harryPotter, member, bookRepository);
   	
   	
   	//the resident spent 10 days. so at the end we should have 0.1*10 = 1 Eur.  200 - 1 = 199 
	assertEquals(Float.valueOf("199"), member.getWallet(),0.0f);
   	
    }

 /*  @Test
    public void students_pay_10_cents_the_first_30days(){
        fail("Implement me");
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        fail("Implement me");
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
        fail("Implement me");
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        fail("Implement me");
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){
        fail("Implement me");
    }*/
}
