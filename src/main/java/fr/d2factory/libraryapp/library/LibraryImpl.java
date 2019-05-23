package fr.d2factory.libraryapp.library;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.generic.Borrow;
import fr.d2factory.libraryapp.member.Member;

public class LibraryImpl implements Library {
	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt, BookRepository bookRepository) throws HasLateBooksException {
		Book book = null;
		try {
			//find the book
			book = bookRepository.findBook(isbnCode);
			if(book!=null && !member.isLate()) {
				//create the borrow
				Borrow borrow = new Borrow(member, borrowedAt);
				
				//save the borrowed book
				bookRepository.saveBookBorrow(book, borrow);
				
				//remove from the avalaible books and add
				bookRepository.removeBook(book);
			}else {
				return null;
			}
			
		} catch(HasLateBooksException e) {
			e.getMessage();
		}
		return book;
	}

	@Override
	public void returnBook(Book book, Member member, BookRepository bookRepository) {
		//dates
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime dayOfTheBorrow = LocalDateTime.of(bookRepository.findBorrowedBookDate(book), LocalTime.MIN);
		
		//Duration: The number of days in the duration between the day the member borrowed the book, and today 
		Duration numberOfDays = Duration.between(dayOfTheBorrow,today);
		
		//Pay the book
		member.payBook((int) numberOfDays.toDays());
		
		//remove the book
		bookRepository.removeBorrowedBook(book);
		
		//add to avalaible Books
		List<Book> bookToAdd = new ArrayList<Book>();
		bookToAdd.add(book);
		bookRepository.addBooks(bookToAdd);
	}

	@Override
	public int borrowDays(Book book, Member member, BookRepository bookRepository) {
		//dates
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime dayOfTheBorrow = LocalDateTime.of(bookRepository.findBorrowedBookDate(book), LocalTime.MIN);
		
		//Duration
		Duration numberOfDays = Duration.between(dayOfTheBorrow,today);
		
		//Pay the book
		return (int) numberOfDays.toDays();
	}

}
