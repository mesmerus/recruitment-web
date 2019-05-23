package fr.d2factory.libraryapp.member;

public class Resident extends Member {


	private static  int MAX_DAYS = 60;
	private static float INITIAL_TARIF = 0.10f;
	private static float LATE_TARIF = 0.20f;
	
	public Resident(String firstName, String lastName, float wallet, String typeMember, boolean late) {
		super(firstName, lastName, wallet, typeMember, late);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void payBook(int numberOfDays) {
		// TODO Auto-generated method stub
		if(numberOfDays<=MAX_DAYS) {
			setWallet(getWallet()-numberOfDays*INITIAL_TARIF);
		}else {
			int lateDays = numberOfDays - MAX_DAYS;
			setWallet(getWallet() - MAX_DAYS*INITIAL_TARIF);
			setWallet(getWallet() - lateDays*LATE_TARIF);
		}
	}

}
