package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
   /**
    * 
    */
    private String firstName;
    
    /**
     * 
     */
     private String lastName;
    
	/**
     * An initial sum of money the member has
     */
    private float wallet;
    
    /**
     * type of the member
     */
    private String typeMember;
    
    /**
     * tells if the member is late or not
     */
    private boolean late;

    
    public Member(String firstName, String lastName, float wallet, String typeMember, boolean late) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.wallet = wallet;
		this.typeMember = typeMember;
		this.late = late;
	}

    
    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

	public String getTypeMember() {
		return typeMember;
	}

	public void setTypeMember(String typeMember) {
		this.typeMember = typeMember;
	}

	public boolean isLate() {
		return late;
	}

	public void setLate(boolean late) {
		this.late = late;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
    
}
