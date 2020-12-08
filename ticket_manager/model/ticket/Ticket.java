/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.ticket;

import java.util.ArrayList;

import edu.ncsu.csc216.ticket_manager.model.command.Command;
import edu.ncsu.csc216.ticket_manager.model.command.Command.CancellationCode;
import edu.ncsu.csc216.ticket_manager.model.command.Command.FeedbackCode;
import edu.ncsu.csc216.ticket_manager.model.command.Command.ResolutionCode;

/**
 * Ticket objects that holds all the information of a ticket submitted by
 * employees. Has a repective amount of information and a state, signifying
 * which part of the management system the ticket currently lies
 * 
 * @author Jared Faulk
 *
 */
public class Ticket {

	/**	String version of a Category enum REQUEST*/
	public static final String TT_REQUEST = "Request";

	/**	String version of a Category enum INCIDENT*/
	public static final String TT_INCIDENT = "Incident";

	/**	String version of a Category enum INQUIRY*/
	public static final String C_INQUIRY = "Inquiry";

	/**	String version of a Category enum SOFTWARE*/
	public static final String C_SOFTWARE = "Software";

	/**	String version of a Priority enum HARDWARE*/
	public static final String C_HARDWARE = "Hardware";

	/**	String version of a Catefory enum NETWORK*/
	public static final String C_NETWORK = "Network";

	/**	String version of a Category enum DATABASE*/
	public static final String C_DATABASE = "Database";

	/**	String version of a Priority enum URGENT*/
	public static final String P_URGENT = "Urgent";

	/**	String version of a Priority enum HIGH*/
	public static final String P_HIGH = "High";

	/**	String version of a Priority enum MEDIUM*/
	public static final String P_MEDIUM = "Medium";

	/**	String version of a Priority enum LOW*/
	public static final String P_LOW = "Low";

	/**	String version of a New state*/
	public static final String NEW_NAME = "New";

	/**	String version of a working state*/
	public static final String WORKING_NAME = "Working";

	/**	String version of a feedback state*/
	public static final String FEEDBACK_NAME = "Feedback";

	/**	String version of a resolved state*/
	public static final String RESOLVED_NAME = "Resolved";

	/**	String version of a closed state*/
	public static final String CLOSED_NAME = "Closed";

	/**	String version of a cancelled state*/
	public static final String CANCELED_NAME = "Canceled";

	/**
	 * A static field that keeps track of the id value that should be given to the
	 * next Ticket created
	 */
	private static int counter = 1;
	/** Unique id for a ticket. */
	private int ticketId;
	/** Ticket’s subject information from when the ticket is created */
	private String subject;
	/** User id of person who reported the ticket */
	private String caller;
	/**
	 * User id of the ticket owner or "" (empty string) if there is not an assigned
	 * owner
	 */
	private String owner;
	/** An ArrayList of notes */
	private ArrayList<String> notes;
	/** Final instance of the ResolvedState inner class */
	private final TicketState resolvedState = new ResolvedState();
	/** Final instance of the NewState inner class */
	private final TicketState newState = new NewState();
	/** Final instance of the WorkingState inner class */
	private final TicketState workingState = new WorkingState();
	/** Final instance of the ClosedState inner class */
	private final TicketState closedState = new ClosedState();
	/** Final instance of the FeedbackState inner class */
	private final TicketState feedbackState = new FeedbackState();
	/** Final instance of the CanceledState inner class */
	private final TicketState canceledState = new CanceledState();
	/** Current state for the ticket */
	private TicketState state;
	/**TicketType of the ticket. One of the TicketType values	*/
	private TicketType ticketType;
	/** Category of the ticket. One of the Category values */
	private Category category;
	/** Priority of the ticket. One of the Priority values */
	private Priority priority;
	/**
	 * Cancellation code for the ticket. One of the CancellationCode values or null
	 * if the ticket isn’t in the CanceledState
	 */
	private CancellationCode cancellationCode;
	/**
	 * Resolution code for the ticket. One of the ResolutionCode values or null if
	 * the ticket isn’t in the ResolvedState or ClosedState
	 */
	private ResolutionCode resolutionCode;
	/**
	 * FeedbackCode for the ticket. One of the FeedbackCode values or null if the
	 * ticket isn’t in the FeedbackState
	 */
	private FeedbackCode feedbackCode;

	private String code; // code string whenever set

	/**
	 * The priority of the ticket
	 */
	public enum Priority {
		/**
		 * urgent, high, medium, low
		 */
		URGENT, HIGH, MEDIUM, LOW;
	}

	/**
	 * Category of ticket enum
	 */
	public enum Category {
		
		/**
		 * Inquiry, software, hardware, network, database
		 */
		INQUIRY, SOFTWARE, HARDWARE, NETWORK, DATABASE
	}

	/**
	 * TicketType of ticket enum
	 */
	public enum TicketType {
		/**
		 * Request of incident
		 */
		REQUEST, INCIDENT
	}

	// inner classez
	/**
	 * Used for loading tickets from a file
	 * @param id id of ticket
	 * @param state state of ticket
	 * @param ticketType the type of ticket
	 * @param subject the subject
	 * @param caller the caller unity id
	 * @param category the category string
	 * @param priority the priority string
	 * @param owner the owner unity id
	 * @param code the code: either feedback, resolutioin, or closed
	 * @param notes notes arraylist
	 */
	public Ticket(int id, String state, String ticketType, String subject, String caller, String category,
			String priority, String owner, String code, ArrayList<String> notes) {

		// todo Rules in UC1
		try {
			setTicketId(id);
			setCounter(id + 1);
			setState(state);
			setTicketType(ticketType);
			setSubject(subject);
			setCaller(caller);
			setCategory(category);
			setPriority(priority);
			setOwner(owner);
			setCode(code);
			setNotes(notes);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Ticket cannot be created.");
		}

	}


	/**
	 * Seconf ticket constructor used for a adding a new Ticket to the system
	 * @param type ticket type
	 * @param subject subject of ticket
	 * @param caller caller of the ticket
	 * @param category the category of the ticket
	 * @param priority the priority of the ticket
	 * @param notes String of notes
	 */
	public Ticket(TicketType type, String subject, String caller, Category category, Priority priority, String notes) {
		// ticketType, category, priority, owner and code should be empty
		this(counter, NEW_NAME, "", subject, caller, "", "", "", "", null);
		try {
			incrementCounter(); // counter increments by 1
			setTicketType(type);
			setCategory(category);
			setPriority(priority);
			setNotes(notes);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Ticket cannot be created.");
		}

	}

	/**
	 * Increments the counter by 1
	 */
	public static void incrementCounter() {
		counter += 1;
	}

	/**
	 * Sets the counter to the sent in number; counter will be used as id for second
	 * constructor
	 * @param number number to set sounter
	 */
	public static void setCounter(int number) {

		if (number >= 0) {
			counter = number;
		} else
			throw new IllegalArgumentException("Invalid id");

	}

	// SETTERZ

	/**
	 * Sets notes for second constructor 
	 * 
	 * @param notes2 String of notes to add to this.notes array list
	 */
	private void setNotes(String notes2) {
		if (notes2 == null || notes2.equals(""))
			throw new IllegalArgumentException("Empty notes");
		else
			this.notes.add(notes2);

	}

	/**
	 * Setter for second constructor; priority
	 * 
	 * @param priority2 priority enum
	 */
	private void setPriority(Priority priority2) {
		this.priority = priority2;

	}

	/**
	 * Sets the category with a Category
	 * @param category2 to set to 
	 */
	private void setCategory(Category category2) {
		this.category = category2;

	}

	/**
	 * sets the ticketype with a TicketType
	 * @param type
	 */
	private void setTicketType(TicketType type) {
		this.ticketType = type;

	}

	/**
	 * Sets the ticket id with an int
	 * @param ticketId the ticketId to set
	 */
	private void setTicketId(int ticketId) {
		if (ticketId >= 0) {
			this.ticketId = ticketId;
		} else
			throw new IllegalArgumentException("Invalid id");

	}

	/**
	 * set subject
	 * @param subject the subject to set
	 */
	private void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * sets the caller
	 * @param caller the caller to set
	 */
	private void setCaller(String caller) {
		this.caller = caller;
	}

	/**
	 * Sets the owner
	 * @param owner the owner to set
	 */
	private void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Sets the notes
	 * @param notes the notes to set
	 */
	private void setNotes(ArrayList<String> notes1) {
		if (notes1 == null) { // for constructor 2
			this.notes = new ArrayList<String>(); // create empty array list
		} else
			this.notes = notes1;
	}

	/**
	 * Sets the state according to state string passed in
	 * 
	 * @param String state the string used to set cooresponding state
	 */
	private void setState(String state) {

		// assign this. state based on string sent in
		switch (state) {
		case NEW_NAME:
			this.state = newState;
			break;
		case WORKING_NAME:
			this.state = workingState;
			break;
		case FEEDBACK_NAME:
			this.state = feedbackState;
			break;
		case CANCELED_NAME:
			this.state = canceledState;
			break;
		case RESOLVED_NAME:
			this.state = resolvedState;
			break;
		case CLOSED_NAME:
			this.state = closedState;
			break;
		default:
			throw new IllegalArgumentException("Invalid state");
		}

	}

	/**
	 * Sets the ticketType
	 * 
	 * @param String ticketType the ticketType to set
	 */
	private void setTicketType(String ticketType) {

		switch (ticketType) {
		case TT_INCIDENT:
			this.ticketType = TicketType.INCIDENT;
			break;
		case TT_REQUEST:
			this.ticketType = TicketType.REQUEST;
			break;
		case "": // do nothing if new Ticket
			if (!state.equals(newState))
				throw new IllegalArgumentException("Invalid type");
			break;
		default:
			throw new IllegalArgumentException("Invalid type");
		}
	}

	/**
	 * Sets the category of the ticket
	 * 
	 * @param category the category to set
	 */
	private void setCategory(String category) {

		switch (category) {
		case C_INQUIRY:
			this.category = Category.INQUIRY;
			break;
		case C_SOFTWARE:
			this.category = Category.SOFTWARE;
			break;
		case C_HARDWARE:
			this.category = Category.HARDWARE;
			break;
		case C_NETWORK:
			this.category = Category.NETWORK;
			break;
		case C_DATABASE:
			this.category = Category.DATABASE;
			break;
		case "":
			if (!state.equals(newState))
				throw new IllegalArgumentException("Invalid category");
			break;
		default:
			throw new IllegalArgumentException("Invalid category");
		}

	}

	/**
	 * Sets the priority of the ticket URGENT, HIGH, MEDIUM, LOW;
	 * 
	 * @param priority the priority to set
	 */
	private void setPriority(String priority) {

		switch (priority) {
		case P_URGENT:
			this.priority = Priority.URGENT;
			break;
		case P_HIGH:
			this.priority = Priority.HIGH;
			break;
		case P_MEDIUM:
			this.priority = Priority.MEDIUM;
			break;
		case P_LOW:
			this.priority = Priority.LOW;
			break;
		case "":
			if (!state.equals(newState))
				throw new IllegalArgumentException("Invalid priority");
			break;
		default:
			throw new IllegalArgumentException("Invalid priority");
		}

	}

	/**
	 * Checks for type of code, and assigns accordingly
	 * 
	 * @param code string of a code to be interpreted
	 */
	private void setCode(String code) {
		setFeedbackCode(code);
		setResolutionCode(code);
		setCancellationCode(code);

	}

	private void setFeedbackCode(String code) {
		// feedback code
		switch (code) {
		case Command.F_CALLER:
			this.feedbackCode = Command.FeedbackCode.AWAITING_CALLER;
			this.code = "Awaiting Caller";
			break;
		case Command.F_CHANGE:
			this.feedbackCode = Command.FeedbackCode.AWAITING_CHANGE;
			this.code = "Awaiting Change";
			break;
		case Command.F_PROVIDER:
			this.feedbackCode = Command.FeedbackCode.AWAITING_PROVIDER;
			this.code = "Awaiting Provider";
			break;
		default:
			break;
		}
	}

	private void setResolutionCode(String code) {
		switch (code) {
		// resolution code
		case Command.RC_CALLER_CLOSED:
			this.resolutionCode = Command.ResolutionCode.CALLER_CLOSED;
			this.code = "Caller Closed";
			break;
		case Command.RC_COMPLETED:
			this.resolutionCode = Command.ResolutionCode.COMPLETED;
			this.code = "Completed";
			break;
		case Command.RC_NOT_COMPLETED:
			this.resolutionCode = Command.ResolutionCode.NOT_COMPLETED;
			this.code = "Not Completed";
			break;
		case Command.RC_NOT_SOLVED:
			this.resolutionCode = Command.ResolutionCode.NOT_SOLVED;
			this.code = "Not Solved";
			break;
		case Command.RC_SOLVED:
			this.resolutionCode = Command.ResolutionCode.SOLVED;
			this.code = "Solved";
			break;
		case Command.RC_WORKAROUND:
			this.resolutionCode = Command.ResolutionCode.WORKAROUND;
			this.code = "Workaround";
			break;
		default:
			break;
		
		}
	}

	private void setCancellationCode(String code) {
		// cancellation code
		switch (code) {
		case Command.CC_DUPLICATE:
			this.cancellationCode = Command.CancellationCode.DUPLICATE;
			this.code = "Duplicate";
			break;
		case Command.CC_INAPPROPRIATE:
			this.cancellationCode = Command.CancellationCode.INAPPROPRIATE;
			this.code = "Inappropriate";
			break;
		default:
			break;
		}
	}

	/**
	 * Getter for ticketId
	 * @return the ticketId
	 */
	public int getTicketId() {
		return ticketId;
	}

	/**
	 * get subject 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**gets the caller
	 * @return the caller
	 */
	public String getCaller() {
		return caller;
	}

	/**
	 * gets the owner
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * gets the notes
	 * @return the notes
	 */
	public String getNotes() {
		String holder = "";
		if (notes.size() > 0) {
			for (int i = 0; i < notes.size() - 1; i++) {
				holder = holder + "-" + notes.get(i) + "\n";
			}
			holder = holder + "-" + notes.get(notes.size() - 1); //dont want linefeed on last one!
		}
		return holder;
	}

	/**
	 * gets the current states
	 * @return the state
	 */
	public String getState() {
		return state.getStateName();
	}

	/**
	 * gets the ticketTyoe of type5
	 * @return the ticketType
	 */
	public TicketType getTicketType() {
		return ticketType;
	}

	/**
	 * gets the cancellationCode
	 * @return the cancellationCode
	 */
	public String getCancellationCode() {
		// DUPLICATE, INAPPROPRIATE
		if (cancellationCode == null)
			return "";
		switch (cancellationCode) {
		case DUPLICATE:
			return "Duplicate";
		case INAPPROPRIATE:
			return "Inappropriate";
		default:
			return "";
		}
	}

	/**
	 * gets the resolutionCode
	 * @return the resolutionCode
	 */
	public String getResolutionCode() {
		// COMPLETED, NOT_COMPLETED, SOLVED, WORKAROUND, NOT_SOLVED, CALLER_CLOSED;
		if (resolutionCode == null)
			return "";
		switch (resolutionCode) {
		case COMPLETED:
			return "Completed";
		case NOT_COMPLETED:
			return "Not Completed";
		case SOLVED:
			return "Solved";
		case NOT_SOLVED:
			return "Not Solved";
		case WORKAROUND:
			return "Workaround";
		case CALLER_CLOSED:
			return "Caller Closed";
		default:
			return "";
		}
	}

	/**
	 * gets the feedbackcode
	 * @return the feedbackCode
	 */
	public String getFeedbackCode() {
		// AWAITING_CALLER, AWAITING_CHANGE, AWAITING_PROVIDER
		if (feedbackCode == null)
			return "";
		switch (feedbackCode) {
		case AWAITING_CALLER:
			return "Awaiting Caller";
		case AWAITING_PROVIDER:
			return "Awaiting Provider";
		case AWAITING_CHANGE:
			return "Awaiting Change";
		default:
			return "";
		}
	}

	/**
	 * gets the ticketType
	 * @return the ticketType
	 */
	public String getTicketTypeString() {
		switch (ticketType) {
		case REQUEST:
			return "Request";
		case INCIDENT:
			return "Incident";
		default:
			throw new IllegalArgumentException("Invalid TicketType");
		}
	}

	/**
	 * gets the category
	 * @return category
	 */
	public String getCategory() {

		switch (category) {
		case INQUIRY:
			return "Inquiry";
		case SOFTWARE:
			return "Software";
		case HARDWARE:
			return "Hardware";
		case NETWORK:
			return "Network";
		case DATABASE:
			return "Database";
		default:
			throw new IllegalArgumentException("Invalid category");
		}

	}

	/**
	 * gets the priority
	 * @return priority
	 */
	public String getPriority() {
		switch (priority) {
		case URGENT:
			return "Urgent";
		case HIGH:
			return "High";
		case MEDIUM:
			return "Medium";
		case LOW:
			return "Low";
		default:
			throw new IllegalArgumentException("Invalid priority");
		}

	}

	private String getCode() {
		if (code == null) {
			return "";
		} else
			return code;
	}

	/**
	 * Represents a ticket in a String with format:
	 * "*id#state#ticket-type#subject#caller#category#priority#owner#code-appropriate-for-state\n
	 * -notes"
	 * @return string formatted ticket
	 */
	public String toString() {

		String dummy = "*" + ticketId + "#" + state.getStateName() + "#" + getTicketTypeString() + "#" + subject + "#"
				+ caller + "#" + getCategory() + "#" + getPriority() + "#" + owner + "#" + getCode() + "\n"
				+ getNotes();

		return dummy;
	}

	/**
	 * updates a ticket
	 * @param command command to be executed
	 */
	public void update(Command command) {

	}

	private void cancellationTransition(Command command) {
		switch (command.getCancellationCode()) {
		case DUPLICATE:
			setState(CANCELED_NAME); // sets to cancelled state
			setCode(Command.CC_DUPLICATE);
			break;
		case INAPPROPRIATE:
			setState(CANCELED_NAME);
			setCode(Command.CC_INAPPROPRIATE);
			break;
		default:
			break;
		}

	}

	private void feedbackTransition(Command command) {
		switch (command.getFeedbackCode()) {
		case AWAITING_CALLER:
			setState(FEEDBACK_NAME); // set to feedback state
			setCode(Command.F_CALLER);
			break;
		case AWAITING_CHANGE:
			setState(FEEDBACK_NAME);
			setCode(Command.F_CHANGE);
			break;
		case AWAITING_PROVIDER:
			setState(FEEDBACK_NAME);
			setCode(Command.F_PROVIDER);
			break;
		default:
			break;

		}

	}

	private void resolutionTransition(Command command) {

		switch (getTicketType()) {
		case INCIDENT:
			switch (command.getResolutionCode()) {
			case NOT_SOLVED:
				setState(RESOLVED_NAME);
				setCode(Command.RC_NOT_SOLVED);
				break;
			case SOLVED:
				setState(RESOLVED_NAME);
				setCode(Command.RC_SOLVED);
				break;
			case WORKAROUND:
				setState(RESOLVED_NAME);
				setCode(Command.RC_WORKAROUND);
				break;
			case CALLER_CLOSED:
				setState(RESOLVED_NAME); // sets to working state
				setCode(Command.RC_CALLER_CLOSED);
				break;
			default:
				break;
			}
			break;

		case REQUEST:
			switch (command.getResolutionCode()) {
			case CALLER_CLOSED:
				setState(RESOLVED_NAME); // sets to working state
				setCode(Command.RC_CALLER_CLOSED);
				break;
			case NOT_COMPLETED:
				setState(RESOLVED_NAME);
				setCode(Command.RC_NOT_COMPLETED);
				break;
			case COMPLETED:
				setState(RESOLVED_NAME);
				setCode(Command.RC_COMPLETED);
				break;
			default:
				break;
				// if we get a resolution code but its not one of these
			}
			break;
		default:
			break;

		}

	}

	// INNER CLASSES

	/**
	 * New state inner class
	 * 
	 *
	 */
	public class NewState implements TicketState {

		private NewState() {

		}

		/**
		 * returns new state name
		 * @return new name
		 */
		public String getStateName() {
			return NEW_NAME;
		}

		/**
		 * updates state from newstate
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {
			// only has two options: 1) --> working state 2) --> cancelled state
			switch (command.getCommand()) {
			// working state has no codes
			case PROCESS:
				setState(WORKING_NAME); // sets to working state
				break;

			case CANCEL:
				setState(CANCELED_NAME);
				// now set cancellation code: nested switch
				cancellationTransition(command);
				break;

			default:
				throw new UnsupportedOperationException("Invalid command");
			}

			// SET NOTEZ
			try {
				setNotes(command.getNote());
			} catch (IllegalArgumentException e) {
				throw new UnsupportedOperationException("Invalid command");
			}

		}

	}

	/**
	 *  state and other fields necessary updated --> also setCode() after
	 * FeedbackState inner class
	 *
	 */
	public class FeedbackState implements TicketState {

		private FeedbackState() {

		}

		/**
		 *Gets feedback name
		 *@return feedback name
		 */
		public String getStateName() {
			return FEEDBACK_NAME;
		}

		/**
		 * updates state from feedback state
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {

			// has 3 options: 1)Cancelled 2)Working 3) Resolved
			// only two codes: cancelled and resolved
			switch (command.getCommand()) {
			// working state has no codes
			case REOPEN:
				setState(WORKING_NAME); // sets to working state
				break;
			case CANCEL:
				setState(CANCELED_NAME); // sets to cancelled state
				// now set cancellation code: nested switch
				cancellationTransition(command);
				break;
			case RESOLVE: // sets to resolved state
				setState(RESOLVED_NAME);
				resolutionTransition(command);
				break;
			default:
				break;
			}

			// SET NOTEZ
			try {
				setNotes(command.getNote());
			} catch (IllegalArgumentException e) {
				throw new UnsupportedOperationException("Invalid command");
			}

		}

	}

	/**
	 * WorkingState inner class
	 *
	 */
	public class WorkingState implements TicketState {

		private WorkingState() {

		}

		/**
		 *gets working name
		 *@return working name
		 */
		public String getStateName() {
			return WORKING_NAME;
		}

		/**
		 * updates state from newstate
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {

			// has 3 options. 1)cancelled 2)resolved 3)feedback
			// For CANCELLATION transition
			switch (command.getCommand()) {
			case FEEDBACK:
				setState(FEEDBACK_NAME); // sets to working state
				feedbackTransition(command);
				break;

			case CANCEL:
				setState(CANCELED_NAME);
				// now set cancellation code: nested switch
				cancellationTransition(command);
				break;
			case RESOLVE:
				setState(RESOLVED_NAME);
				resolutionTransition(command);
				break;
			default:
				break;
			}

			// SET NOTEZ
			try {
				setNotes(command.getNote());
			} catch (IllegalArgumentException e) {
				throw new UnsupportedOperationException("Invalid command");
			}

		} // updateState
	}

	/**
	 * ClosedState inner class
	 *
	 */
	public class ClosedState implements TicketState {

		private ClosedState() {

		}

		/**
		 * gets closed name
		 * @return closed name
		 */
		public String getStateName() {
			return CLOSED_NAME;
		}

		/**
		 * updates state from closed state
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {

			// has one options: 1) --> Working
			switch (command.getCommand()) {
			// working state has no codes
			case REOPEN:
				setState(WORKING_NAME); // sets to working state
				// no working code
				break;
			default:
				break;
			}

			// SET NOTEZ  dont set when clicks return, may be an issue now
			try {
				setNotes(command.getNote());
			} catch (IllegalArgumentException e) {
				throw new UnsupportedOperationException("Invalid command");
			}

		}
	}

	/**
	 * CanceledState inner class
	 *
	 */
	public class CanceledState implements TicketState {

		private CanceledState() {

		}

		/**
		 * gets cancelled name
		 * @return canceled name
		 */
		public String getStateName() {
			return CANCELED_NAME;
		}

		/**
		 * updates state from closed
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {

			// the user cannot do anything to a cancelled ticket

		}

	}

	/**
	 * ResolvedState inner class
	 *
	 */
	public class ResolvedState implements TicketState {

		private ResolvedState() {

		}

		/**
		 * gets resolved name
		 * @return resolved name
		 */
		public String getStateName() {
			return RESOLVED_NAME;
		}

		/**
		 * updates state from resolved state
		 * @param command the command object to be executed
		 */
		public void updateState(Command command) {

			// has three options: 1) --> Feedback 2) working 3)closed
			switch (command.getCommand()) {
			// working state has no codes
			case REOPEN:
				setState(WORKING_NAME); // sets to working state
				break;
			case FEEDBACK:
				setState(FEEDBACK_NAME); // sets to working state
				feedbackTransition(command);
				break;
			case CONFIRM: // sets to resolved state
				setState(CLOSED_NAME);
				break;
			default:
				break;
			}

			// SET NOTEZ 
			try {
				setNotes(command.getNote());
			} catch (IllegalArgumentException e) {
				throw new UnsupportedOperationException("Invalid command");
			}

		}
	}
}
