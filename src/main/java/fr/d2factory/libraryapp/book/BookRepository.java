package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.d2factory.libraryapp.generic.Borrow;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book,Borrow> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books){
        //TODO implement the missing feature
    	books.forEach(book -> availableBooks.put(book.getIsbn(),book));
    }

    public Book findBook(long isbnCode) {
        //TODO implement the missing feature
    	for (Map.Entry<ISBN, Book> aBook : availableBooks.entrySet()) {
            ISBN isbn = (ISBN) aBook.getKey();
            if(isbn.getIsbnCode()==isbnCode) {
            	return aBook.getValue();
            }
        }
        return null;
    }

    public void saveBookBorrow(Book book, Borrow borrow){
        //TODO implement the missing feature
    	borrowedBooks.put(book, borrow);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        //TODO implement the missing feature
        for (Map.Entry<Book, Borrow> aBook : borrowedBooks.entrySet()) {
            if(book.getIsbn().getIsbnCode()==aBook.getKey().getIsbn().isbnCode) {
            	return aBook.getValue().getLocalDate();
            }
        }
        return null;
    }
    
    /**
     * 
     * @param isbnCode
     */
    public void removeBook(Book book) {
    	if(book!=null) {
    		availableBooks.remove(book.getIsbn());
    	}
    }
    
    /**
     * remove a borrowed book
     * @param book
     */
    public void removeBorrowedBook(Book book) {
    	for (Map.Entry<Book, Borrow> aBook : borrowedBooks.entrySet()) {
            if(book.getIsbn().getIsbnCode()==aBook.getKey().getIsbn().isbnCode) {
            	borrowedBooks.remove(aBook.getKey());
            	break;
            }
        }
    }

	public Map<ISBN, Book> getAvailableBooks() {
		return availableBooks;
	}

	public void setAvailableBooks(Map<ISBN, Book> availableBooks) {
		this.availableBooks = availableBooks;
	}

	public Map<Book, Borrow> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(Map<Book, Borrow> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}
    
    
}
