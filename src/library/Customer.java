package library;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer implements Serializable {

	private String name;
	private String adress;
	private String number;
	private double debt;
	private ArrayList<Book> currentLoans;
	private ArrayList<Book> loanHistory;
	private String personnummer;
	final String END_OF_LINE = System.lineSeparator();// Skips A Line

	public Customer(String name, String adress, String personnummer, String number) throws Exception {

		this.name = name;
		this.adress = adress;
		this.personnummer = personnummer;
		this.number = number;
		this.currentLoans = new ArrayList<Book>();
		this.loanHistory = new ArrayList<Book>();

		customerBooks("res/" + personnummer + "CurrentLoans.txt");
		customerBooks("res/" + personnummer + "LoanHistory.txt");
	}

	// TODO Getters and Setters

	public String getName() {
		return this.name;
	}

	public String getAdress() {
		return this.adress;
	}

	public String getPersonnummer() {
		return this.personnummer;
	}

	public String getNumber() {
		return this.number;
	}

	public double getDebt() {
		return this.debt;
	}

	public ArrayList<Book> getCurrentLoans() {
		return this.currentLoans;
	}

	public ArrayList<Book> getloanHistory() {
		return this.loanHistory;
	}

	public void setName(String newName) throws Exception {
		if (newName.equals("")) {
			throw new Exception("Name can not be empty");
		} else {
			this.name = newName;
		}
	}

	public void setAdress(String newAdress) throws Exception {
		if (newAdress.equals("")) {
			throw new Exception("Adress can not be empty");
		} else {
			this.adress = newAdress;
		}
	}

	public void setNumber(String newNumber) throws Exception {
		if (newNumber.trim().matches("[0-9]+") && newNumber.trim().length() == 10) {
			this.number = newNumber;
		} else {
			throw new Exception("Phone number MUST consist of 10 digits");
		}
	}

	public void setDebt(double newDebt) {
		this.debt = newDebt + this.debt;
	}
	
	public void payDebt(double payement) {
		this.debt = this.debt - payement;
	}

	public void addToCurrentLoan(Book book) {
		currentLoans.add(book);
	}

	public void addToLoanHistory(Book book) {
		loanHistory.add(book);
	}

	public ArrayList<Book> getLoanHistory() {
		return loanHistory;
	}

	public void removeFromCurrentLoan(Book book) {
		currentLoans.remove(book);
	}

	public Book getFromCurrentLoan(String bookTitle) {
		for (Book book : currentLoans) {
			if (book.getTitle().equalsIgnoreCase(bookTitle)) {
				return book;
			}
		}
		return null;
	}

	public void customerBooks(String path) throws FileNotFoundException {
		File file = new File(path);
		Scanner input = new Scanner(file);
		input.useDelimiter("/|\n");
		System.out.println("current customer: " + this.name);
		while (input.hasNext()) {
			String title = input.next().trim();
			String authors = input.next().trim();
			String publisher = input.next().trim();
			String genre = input.next().trim();
			String shelf = input.next().trim();
			String timesBorrowed = input.next().trim();
			String returnDate = input.next();
			Book book = null;
			try {
				book = new Book(title, authors, publisher, genre, shelf);
				book.setTimesBorrowed(Integer.parseInt(timesBorrowed));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // need to change this?
				book.setReturnDate(LocalDate.parse(returnDate, formatter));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (path.contains("CurrentLoans")) {
					currentLoans.add(book);
				} else if (path.contains("LoanHistory")) {
					loanHistory.add(book);
				}
			}
		}
	}

	@Override
	public String toString() {
		String customerToString = " " + END_OF_LINE;
		customerToString += "||----------------------------------------------------------||" + END_OF_LINE;
		customerToString += "Customer Name: " + this.name + END_OF_LINE;
		customerToString += "Customer Personnummer: " + this.personnummer + END_OF_LINE;
		customerToString += "Customer Adress: " + this.adress + END_OF_LINE;
		customerToString += "Customer Phone Number: " + this.number + END_OF_LINE;
		customerToString += "Customer Debt: " + this.debt + END_OF_LINE;

		if (this.currentLoans.isEmpty()) {
			customerToString += "\n--------- Customer's has no current loan(s) ---------" + END_OF_LINE;
		} else {
			String current = "";

			for (int i = 0; i < currentLoans.size(); i++) {
				current += currentLoans.get(i).toString() + END_OF_LINE;
			}
			customerToString += "\n--------- Customer's current loan(s): ---------" + END_OF_LINE + current;
		}
		
		if (this.loanHistory.isEmpty()) {
			customerToString += "\n--------- Customer's loan history is empty ---------" + END_OF_LINE;
		} else {
			String history = "";
			for (int i = 0; i < loanHistory.size(); i++) {
				history += loanHistory.get(i).toString() + END_OF_LINE;
			}
			customerToString += "\n--------- Customer's loan History: ---------" + END_OF_LINE + history;
		}

		return customerToString;
	}

}
