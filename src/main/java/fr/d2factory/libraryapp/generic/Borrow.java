package fr.d2factory.libraryapp.generic;

import java.time.LocalDate;

import fr.d2factory.libraryapp.member.Member;

/**
 * 
 * @author mesmerus
 * Borrower as a wrapper
 *
 */
public class Borrow {
	private LocalDate localDate;
	private Member member ;

	public Borrow(Member member , LocalDate localDate) {
		this.localDate = localDate;
		this.member = member;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	
}
