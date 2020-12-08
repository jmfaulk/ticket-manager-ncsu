/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.manager;

import edu.ncsu.csc216.ticket_manager.model.command.Command;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Category;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Priority;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.TicketType;

/**
 * Manages the transfering of tickets to and from files, arranges tickets to 
 * be displayed, adds or deletes ticket from a list
 * @author jarodhowls
 *
 */
public class TicketManager {

	/**
	 * constructor
	 */
	private TicketManager() {

	}

	/**
	 * gets an instance of TicketManager
	 * @return
	 */
	public static TicketManager getInstance() {
		return null;
	}
	

	/**
	 * saves a ticket into a file
	 * @param ticket
	 */
	public void saveTicketsToFile(String ticket) {

	}
	

	/**
	 * loads a ticket out a file 
	 * @param filename
	 */
	public void loadTicketsFromFile(String filename) {

	}
	

	/**
	 * Creates an empty ticket List
	 */
	public void createNewTicketList() {

	}
	

	/**
	 * Gets a ticket for a general display in the GUI
	 * @return
	 */
	public String[][] getTicketsForDisplay() {
		return null;
	}
	

	/**
	 * gets a ticket to display by type
	 * @param type
	 * @return
	 */
	public String[][] getTicketsForDisplayByType(TicketType type) {
		return null;
	}

	
	/**
	 * Gets a ticket by its ID number
	 * @param id
	 * @return
	 */
	public Ticket getTicketById(int id) {
		return null;
	}

	
	/**
	 * Executes given demand
	 * @param number
	 * @param command
	 */
	public void executeCommand(int number, Command command) {

	}

	/**
	 * Delete ticket of given ID
	 * @param id
	 */
	public void deleteTicketById(int id) {
		
	}

	/**
	 * Adds a ticket to a list of tickets
	 * @param type
	 * @param holder
	 */
	public void addTicketToList(TicketType type, String holder, String holder2, Category category, Priority priority, 
			String holder3) {
		
	}
}
