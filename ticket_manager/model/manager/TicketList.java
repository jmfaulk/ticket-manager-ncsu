/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.manager;



import java.util.ArrayList;

import edu.ncsu.csc216.ticket_manager.model.command.Command;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Category;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Priority;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.TicketType;

/**
 * A list of Ticket objects 
 * @author Jared Faulk
 *
 */
public class TicketList {
	
	private ArrayList<Ticket> tickets; 
	
	/**
	 * TicketList Constructor
	 */
	public TicketList() {
		Ticket.setCounter(1);
		
	}
	
	
	/**
	 * Adds a ticket to the ticket list
	 * @param type type of ticket
	 * @param subject subject of ticket
	 * @param caller creator of ticket
	 * @param category which category the ticket is in
	 * @param priority level of priority assocaited with task
	 * @param note additional info
	 * @return integer to return
	 */
	//TicketType type, String subject, String caller, Category category, Priority priority, String notes
	public int addTicket(TicketType type, String subject, String caller, Category category, 
			Priority priority, String note) {
		try {
			Ticket newTicket = new Ticket(type, subject, caller, category, priority, note);
			tickets.add(newTicket);
			return 1;
		} catch(IllegalArgumentException e) {
			return -1; 
		}
		
	}

	
	/**
	 * Adds created ticket to a list of tickets
	 * @param ticketList a sent in list of tickets
	 */
	public void addTickets(ArrayList<Ticket> ticketList) {
		tickets.clear();
		tickets = ticketList;
		
		//set counter to max id
		int max = 1;
		int min = 1;
		for(int i = 0; i < ticketList.size(); i++) {
			min = ticketList.get(i).getTicketId();
			if(min > max) max = min; 
			//we dont care about finding min here
		}
		Ticket.setCounter(max + 1); //1 more than max id
		
	}
	
	
	/**
	 * gets a list of tickets
	 * @return this list of tickets
	 */
	public ArrayList<Ticket> getTickets() {
		
		return this.tickets; 
	}
	
	
	/**
	 * Gets a ticket by its type
	 * @param type type of ticket
	 * @return list of ticketss by type
	 */
	public ArrayList<Ticket> getTicketsByType(TicketType type) {
		ArrayList<Ticket> typeList = new ArrayList<Ticket>();
		for(int i = 0; i < tickets.size(); i++) {
			if(tickets.get(i).getTicketType() == type) {
				typeList.add(tickets.get(i));
			}
		}
		
		
		return typeList;
	}
	
	
	/**
	 * Gets ticket by its ID
	 * @param id id of ticket
	 * @return return the ticket found by id
	 */
	public Ticket getTicketById(int id) {
		try {
			
			return tickets.get(id);
			
		} catch(NullPointerException e) {
			return null;
		}
		
		


	}
	
	
	/**
	 * update a Ticket in the list through execution of a Command
	 * @param id 
	 * @param command command to execute
	 */
	public void executeCommand(int id, Command command) {
		tickets.get(id).update(command);
		
	}
	
	
	/**
	 * deletes the ticket
	 * @param id the id
	 */
	public void deleteTicketById(int id) {

		tickets.remove(id);
		
	}
	
	
	
	
	
}
