/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.ncsu.csc216.ticket_manager.model.command.Command;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Category;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Priority;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.TicketType;

/**
 * Reads in a file with given ticket information on it
 * 
 * @author Jared Faulk
 *
 */
public class TicketReader {

	/**
	 * Constructor
	 */
	public TicketReader() {

	}

	/**
	 * reads in a file with valid ticket information
	 * 
	 * @param filename filename of file
	 * @return an arrayList of files read in from the file
	 */
	public static ArrayList<Ticket> readTicketFile(String filename) {

		try {
			ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
			File f = new File(filename);
			Scanner in = new Scanner(f);

			while (in.hasNextLine()) {
				Ticket ticket = readTicket(in.nextLine());
				if (!(ticket == null)) {
					ticketList.add(ticket);
				} //check duplicate?

			}
			in.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to load file.");
		}

		return null;
	}

	private static Ticket readTicket(String ticket) {
		// *id#state#ticket-type#subject#caller#category#priority#owner#code-appropriate-for-state
		// must!! *id#state#ticketType#subject#caller#category#priority#(owner is
		// optional)#(code is optional)
		// -notes are must

		try {

			Scanner s = new Scanner(ticket);
			if (!s.next().equals("*")) { // check first "*"
				s.close();
				return null;
			}
			String line1 = s.nextLine(); // first line minus notes! doesnt include linefeed at end!!!
			s.next(); // eat linefeed

			//Check Notes
			String lineNotes = "";
			ArrayList<String> notes = new ArrayList<String>();
			while (s.hasNextLine()) {
				
				lineNotes = s.nextLine();
				if(lineNotes.startsWith("-")) {
					notes.add(lineNotes);
				} 
				s.next(); // eat linefeed
			}
			
			s.close();
			if (notes.isEmpty()) {
				return null; // EMPTY NOTES!!
			}
			

			// now check for valid ticket parameters
			String[] ticketMaybe = line1.split("#", -2); // split into an array all using "#" delimiter

			// BEHMOTH switch that basically tests if a string can legitmately make a ticket
			// arrString should be 9 elements! 0-8 some may be empty or not there thats OKAY
			
			int id = 0; // holder for id int
			if (ticketMaybe.length >= 7 || ticketMaybe.length <= 9) {// it is loaded string, working, feedback,
																		// resolved, closed (maybe cancelled)
				int change = 1;
				switch (change) {
				case 1:// test id
						// test integer first
					if (isInteger(ticketMaybe[0])) {
						id = Integer.parseInt(ticketMaybe[0]);
						change = 2;
					} else
						return null;
					break;
				case 2:
					if (isValidState(ticketMaybe[1])) {
						change = 3;
					} else
						return null;
					break;
				case 3:
					if (isValidTicketType(ticketMaybe[2])) {
						change = 4;
					} else
						return null;
					break;
				case 4:// test category
					if (isValidCategory(ticketMaybe[5])) {
						// dont check arrString[3 or 4] because those can just be strings
						change = 5;
					} else
						return null;
					break;
				case 5:// test priority
					if (isValidPriority(ticketMaybe[6])) {
						if (ticketMaybe.length > 7) {
							change = 6;
						} else if (isNewState(ticketMaybe[1])) { // can ONLY be NEW at this many parameters
							change = 42; // tO ENDGAME
						}

					} else
						return null;
					break;
				case 6:// test owner or code
					if (isValidCode(ticketMaybe[7])) {
						// if true that means there are no owners and it MUST be cancelled, already checked for new
						// check if new state or Cancelled
						if (isCancelled(ticketMaybe[1])) {// if true NO OWNER and end of line
							change = 42;
						} else
							return null;
					} else // if false it may be a string for an owner, go to state 7 to check fo
							// code/state
						change = 100;
					break;
				case 100:// has  owner, is check valid code
					if (isValidCode(ticketMaybe[8])) {
						change = 42;
					} else
						return null;
					break;
				case 42:// end game, made it to here then we have a LEGIT TIKKET;
					break;
				default:
					return null;
				}

			} else return null; //not right length
			//TICKET CONSTRUCTION
			Ticket returnTicket = null;
			if (ticketMaybe.length == 7) { //NEW
				returnTicket = new Ticket(id, ticketMaybe[1], ticketMaybe[2], ticketMaybe[3], ticketMaybe[4], ticketMaybe[5],
						ticketMaybe[6], "", "", notes);
			} else if(ticketMaybe.length == 8) {//Just cancelled no owner
				returnTicket = new Ticket(id, ticketMaybe[1], ticketMaybe[2], ticketMaybe[3], ticketMaybe[4], ticketMaybe[5],
						ticketMaybe[6], "", ticketMaybe[7], notes); 
			} else if(ticketMaybe.length == 9) {//all other codes with owner
				returnTicket = new Ticket(id, ticketMaybe[1], ticketMaybe[2], ticketMaybe[3], ticketMaybe[4], ticketMaybe[5],
						ticketMaybe[6], ticketMaybe[7], ticketMaybe[8], notes); 
			}
			return returnTicket;
		} catch (NoSuchElementException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}

	}

	private static boolean isNewState(String state) {
		return state.equals(Ticket.NEW_NAME);
	}

	private static boolean isValidCode(String code) {
		// feedback code
		switch (code) {
		case Command.F_CALLER:
			return true;
		case Command.F_CHANGE:
			return true;
		case Command.F_PROVIDER:
			return true;
		default:
			break;
		}

		// resolution code
		switch (code) {
		case Command.RC_CALLER_CLOSED:
			return true;
		case Command.RC_COMPLETED:
			return true;
		case Command.RC_NOT_COMPLETED:
			return true;
		case Command.RC_NOT_SOLVED:
			return true;
		case Command.RC_SOLVED:
			return true;
		case Command.RC_WORKAROUND:
			return true;
		default:
			break;

		}

		// cancellation code
		switch (code) {
		case Command.CC_DUPLICATE:
			return true;
		case Command.CC_INAPPROPRIATE:
			return true;
		default:
			break;
		}
		return false;
	}

	private static boolean isCancelled(String state) {
		return state.equals(Ticket.CANCELED_NAME);

	}

	private static boolean isValidPriority(String priority) {
		switch (priority) {
		case Ticket.P_URGENT:
			return true;
		case Ticket.P_HIGH:
			return true;
		case Ticket.P_MEDIUM:
			return true;
		case Ticket.P_LOW:
			return true;
		default:
			return false;
		}
	}

	private static boolean isValidCategory(String category) {
		switch (category) {
		case Ticket.C_INQUIRY:
			return true;
		case Ticket.C_SOFTWARE:
			return true;
		case Ticket.C_HARDWARE:
			return true;
		case Ticket.C_NETWORK:
			return true;
		case Ticket.C_DATABASE:
			return true;
		default:
			return false;
		}

	}

	/**
	 * Checks if read in string is a valid ticketType
	 * 
	 * @param ticketType
	 * @return
	 */
	private static boolean isValidTicketType(String ticketType) {

		switch (ticketType) {
		case Ticket.TT_INCIDENT:
			return true;
		case Ticket.TT_REQUEST:
			return true;
		default:
			return false;
		}

	}

	private static boolean isValidState(String state) {
		switch (state) {
		case Ticket.NEW_NAME:
			return true;
		case Ticket.WORKING_NAME:
			return true;
		case Ticket.FEEDBACK_NAME:
			return true;
		case Ticket.CANCELED_NAME:
			return true;
		case Ticket.RESOLVED_NAME:
			return true;
		case Ticket.CLOSED_NAME:
			return true;
		default:
			return false;
		}

	}

	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;

		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

	}

}
