package fr.d2factory.libraryapp.member;

public class Student extends Member {

	public Student(String firstName, String lastName, float wallet, String typeMember, boolean late) {
		super(firstName, lastName, wallet, typeMember, late);
		// TODO Auto-generated constructor stub
	}

	private static  int MAX_DAYS = 30;
	private static  int FREE_DAYS = 15;
	private static float INITIAL_TARIF = 0.10f;
	private static float LATE_TARIF = 0.15f;
	

	/**
	 *  year of the student
	 */
	private int yearStudent;
	
	@Override
	public void payBook(int numberOfDays) {
		// TODO Auto-generated method stub
		if(yearStudent>1) { // more than 1 year student
			if(numberOfDays<=MAX_DAYS) {
				setWallet(getWallet()-numberOfDays*INITIAL_TARIF);
			}else {
				int lateDays = numberOfDays - MAX_DAYS;
				setWallet(getWallet() - MAX_DAYS*INITIAL_TARIF);
				setWallet(getWallet() - lateDays*LATE_TARIF);
			}
		}else if(yearStudent==1) { //1 year student
			if(numberOfDays>FREE_DAYS) { 	
				if(numberOfDays<=MAX_DAYS) {
					int extraDays = numberOfDays - FREE_DAYS;
					setWallet(getWallet()-extraDays*INITIAL_TARIF);
				}else {
					int lateDays = numberOfDays - MAX_DAYS;
					int extraDays = numberOfDays - FREE_DAYS - lateDays;
					setWallet(getWallet() - extraDays*INITIAL_TARIF);
					setWallet(getWallet() - lateDays*LATE_TARIF);
				}
			}
		}
	}

	public int getYearStudent() {
		return yearStudent;
	}

	public void setYearStudent(int yearStudent) {
		this.yearStudent = yearStudent;
	}

}
