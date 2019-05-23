package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        //create an anonymous member
    	Member member = new Member("laurent", "DUPONT" , 200, "resident", false) {
       		private int maxDays = 60;
       		private float initialTarif = 0.10f;
       		private float lateTarif = 0.20f;
			@Override
			public void payBook(int numberOfDays) {
				// TODO Auto-generated method stub
				if(numberOfDays<=maxDays) {
					setWallet(getWallet()-numberOfDays*initialTarif);
				}else {
					int lateDays = numberOfDays - maxDays;
					setWallet(getWallet() - maxDays*initialTarif);
					setWallet(getWallet() - lateDays*lateTarif);
				}
			}
		};
    	
    	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
    	Book borrowedBook = library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.now(), bookRepository);
    	//asserts
    	assertEquals(harryPotter.getIsbn().getIsbnCode(), borrowedBook.getIsbn().getIsbnCode());
    }
    

    @Test
    public void borrowed_book_is_no_longer_available(){
    	//create an anonymous member
    	Member member = new Member("laurent", "DUPONT" , 200, "resident", false) {
       		private int maxDays = 60;
       		private float initialTarif = 0.10f;
       		private float lateTarif = 0.20f;
			@Override
			public void payBook(int numberOfDays) {
				// TODO Auto-generated method stub
				if(numberOfDays<=maxDays) {
					setWallet(getWallet()-numberOfDays*initialTarif);
				}else {
					int lateDays = numberOfDays - maxDays;
					setWallet(getWallet() - maxDays*initialTarif);
					setWallet(getWallet() - lateDays*lateTarif);
				}
			}
		};
    	
		//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
    	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MAY, 10), bookRepository);
    	
   
    	//assert
    	assertEquals(null, bookRepository.findBook(Long.valueOf("46578964513")));
    	
    	
    	//try to borrow the same book again
    	Member anotherMember = new Resident("laurent", "DUCLOS" , 200, "resident", false);    	
    	//asserts
    	assertEquals(null, library.borrowBook(Long.valueOf("46578964513"), anotherMember, LocalDate.now(), bookRepository));
    	
    	
    	
    	//add : CHECKING IF THE BOOK IS AVAILAIBLE AFTER BEEN RETURNED  
    	
    	//return the book
    	library.returnBook(harryPotter, member, bookRepository);
    	
    	//try to borrow the same book again 
    	Book sameBook = library.borrowBook(Long.valueOf("46578964513"), anotherMember, LocalDate.now(), bookRepository);
    	//asserts
    	assertEquals(harryPotter.getIsbn().getIsbnCode(),sameBook.getIsbn().getIsbnCode());
    	
    	
    }

   @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
       //create a member resident
   	Member member = new Resident("laurent", "DUPONT" , 200, "resident", false);  	
   	
  //Harry Potter
	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
   	
	//borrow a book
	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.APRIL, 23), bookRepository);
	
	//days of borrow
	int borrowDays = library.borrowDays(harryPotter, member, bookRepository);
   	
	//days of borrow
	library.returnBook(harryPotter, member, bookRepository);
   	
   	//asset
	assertEquals(200 - 0.10*borrowDays, member.getWallet(),0.0f);
   	
    }

    @Test
    public void students_pay_10_cents_the_first_30days(){
        //create a member student
       	Member member = new Student("amelie", "DUTOUT" , 50, "student", false);  	
       	
       	//the student is not in the first year
       	((Student) member).setYearStudent(2);
       	
       	
       	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.APRIL, 23), bookRepository);
    	
    	//days of borrow
    	int borrowDays = library.borrowDays(harryPotter, member, bookRepository);
    	
    	//return the book
    	library.returnBook(harryPotter, member, bookRepository);
       	
       	//asset 10 cent
    	//The idea there (and in all the other jUnit test) is just to verify that the wallet has the expected amount.
    	assertEquals(50 - 0.10f*borrowDays, member.getWallet(),0.0f);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        //create a member student
       	Member member = new Student("amelie", "DUTOUT" , 50, "student", false);  	
       	
       	//the student is not in the first year
       	((Student) member).setYearStudent(1);
       	
       	
       	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MAY, 1), bookRepository);
    	
    	//days of borrow
    	int borrowDays = library.borrowDays(harryPotter, member, bookRepository);
    	
    	//return the book
    	library.returnBook(harryPotter, member, bookRepository);
       	
    	//asset 10 cent
    	if(borrowDays<=15) {
    		assertEquals(50, member.getWallet(),0.0f);
    	}else if(borrowDays<=30){
    		assertEquals(50 - 0.10f*(borrowDays - 15), member.getWallet(),0.0f);
    	}else {
    		int borrowDaysLate = borrowDays - 30;
    		int borrowDaysNotFree = borrowDays - 15 - borrowDaysLate;  		
    		assertEquals((50 - 0.10f*borrowDaysNotFree) - 0.15f*borrowDaysLate , member.getWallet(),0.0f);
    	}
       	
    	
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
    	//create a member student
       	Member member = new Student("amelie", "DUTOUT" , 50, "student", false);  	
       	
       	//the student is not in the first year
       	((Student) member).setYearStudent(1);
       	
       	
       	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.APRIL, 10), bookRepository);
    	
    	//days of borrow
    	int borrowDays = library.borrowDays(harryPotter, member, bookRepository);
    	
    	//return the book
    	library.returnBook(harryPotter, member, bookRepository);
       	
    	//asset
    	if(borrowDays<=15) {
    		assertEquals(50, member.getWallet(),0.0f);
    	}else if(borrowDays<=30){
    		assertEquals(50 - 0.10f*(borrowDays - 15), member.getWallet(),0.0f);
    	}else {
    		int lateDays = borrowDays - 30;
    		int borrowDaysNotFree = borrowDays - 15 - lateDays;  		
    		assertEquals((50 - 0.10f*borrowDaysNotFree) - 0.15f*lateDays , member.getWallet(),0.0f);
    	}
    	
    	
    	// ************SAME WITH A greater years STUDENT**********
    	
    	//create a member 
       	Member greaterStudent = new Student("nicolas", "DUTOUT" , 50, "student", false);  	
       	
       	//the student is not in the first year
       	((Student) greaterStudent).setYearStudent(2);
       	
       	
       	//La peau du chagrin
    	Book peauDuChagrin = new Book("La peau de chagrin", "Balzac", new ISBN(Long.valueOf("465789453149")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("465789453149"), greaterStudent, LocalDate.of(2019, Month.APRIL, 10), bookRepository);
    	
    	//days of borrow
    	int borrowDaysGreaterStudent = library.borrowDays(peauDuChagrin, greaterStudent, bookRepository);
    	
    	//return the book
    	library.returnBook(peauDuChagrin, greaterStudent, bookRepository);
       	
    	//asset
    	if(borrowDaysGreaterStudent<=15) {
    		assertEquals(50, greaterStudent.getWallet(),0.0f);
    	}else if(borrowDaysGreaterStudent<=30){
    		assertEquals(50 - 0.10f*(borrowDaysGreaterStudent - 15), greaterStudent.getWallet(),0.0f);
    	}else {
    		int lateDays = borrowDaysGreaterStudent - 30;
    		assertEquals((50 - 0.10f*30) - 0.15f*lateDays , greaterStudent.getWallet(),0.0f);
    	}
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
    	//create a member resident
       	Member member = new Resident("amelie", "DUTOUT" , 50, "resident", false);  	
       	
       	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MARCH, 1), bookRepository);
    	
    	//days of borrow
    	int borrowDays = library.borrowDays(harryPotter, member, bookRepository);
    	
    	//return the book
    	library.returnBook(harryPotter, member, bookRepository);
       	
    	//asset 
    	if(borrowDays<=60) {
    		assertEquals(50 - 0.10f*borrowDays, member.getWallet(),0.0f);
    	}else{
    		int lateDays = borrowDays - 60;
    		assertEquals((50 - 0.10f*60) - 0.20f*lateDays , member.getWallet(),0.0f);
    	}
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books(){
    	//create an anonymous member
       	Member member = new Member("amelie", "DUTOUT" , 50, "resident", false) {
       		private int maxDays = 60;
       		private float initialTarif = 0.10f;
       		private float lateTarif = 0.20f;
			@Override
			public void payBook(int numberOfDays) {
				// TODO Auto-generated method stub
				if(numberOfDays<=maxDays) {
					setWallet(getWallet()-numberOfDays*initialTarif);
				}else {
					int lateDays = numberOfDays - maxDays;
					setWallet(getWallet() - maxDays*initialTarif);
					setWallet(getWallet() - lateDays*lateTarif);
				}
			}
		};  	
       	
       	//Harry Potter
    	Book harryPotter = new Book("Harry Potter", "J.K. Rowling", new ISBN(Long.valueOf("46578964513")));
       	
    	//borrow a book
    	library.borrowBook(Long.valueOf("46578964513"), member, LocalDate.of(2019, Month.MARCH, 1), bookRepository);
    	
    	//assuming that the member didnt return the book in time, and late goes to true,
    	
    	member.setLate(true);
    	
    	//Attempting to borrow another book 
    	
    	//La peau du chagrin
    	Book peauDuChagrin = new Book("La peau de chagrin", "Balzac", new ISBN(Long.valueOf("465789453149")));
    	
    	//borrow the other book
    	Book secondBorrowedBook = library.borrowBook(Long.valueOf("465789453149"), member, LocalDate.of(2019, Month.MARCH, 1), bookRepository);
    	
    	//asset 
    	assertEquals(null , secondBorrowedBook);
    	
    }
}
