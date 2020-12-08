/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.command;

/**
 * 
 * @author Jared Faulk
 *
 */
/**
 * @author jarodhowls
 *
 */
public class Command {

	/**		*/
	public static final String F_CALLER = "Awaiting Caller";

	/**		*/
	public static final String F_CHANGE = "Awaiting Change";

	/**		*/
	public static final String F_PROVIDER = "Awaiting Provider";

	/**		*/
	public static final String RC_COMPLETED = "Completed";

	/**		*/
	public static final String RC_NOT_COMPLETED = "Not Completed";

	/**		*/
	public static final String RC_SOLVED = "Solved";

	/**		*/
	public static final String RC_WORKAROUND = "Workaround";

	/**		*/
	public static final String RC_NOT_SOLVED = "Not solved";

	/**		*/
	public static final String RC_CALLER_CLOSED = "Caller closed";

	/**		*/
	public static final String CC_DUPLICATE = "Duplicate";

	/**		*/
	public static final String CC_INAPPROPRIATE = "Inappropriate";

	/**		*/
	private String ownerId;
	/**		*/
	private String note;
	/**		*/
	private CommandValue c;
	/**		*/
	private ResolutionCode resolutionCode;
	/**		*/
	private FeedbackCode feedbackCode;
	/**		*/
	private CancellationCode cancellationCode;

	/** cancellation code enums for dupliacte or inappropriate */
	public enum CancellationCode {
		
		DUPLICATE, INAPPROPRIATE
	}

	/** command value enum for different commonds user can commit */
	public enum CommandValue {
		PROCESS, FEEDBACK, RESOLVE, CONFIRM, REOPEN, CANCEL

	}

	/** feedback codes */
	public enum FeedbackCode {
		AWAITING_CALLER, AWAITING_CHANGE, AWAITING_PROVIDER
	}

	/** resolution codes */
	public enum ResolutionCode {
		COMPLETED, NOT_COMPLETED, SOLVED, WORKAROUND, NOT_SOLVED, CALLER_CLOSED;
	}

	/**
	 * Constructs a command object which holds the codes represented with each
	 * ticket in a certain state
	 * 
	 * @param c                commandvalue to commit
	 * @param ownerId          the owner's unityID
	 * @param feedbackCode     the feedback code sent
	 * @param resolutionCode   the resolution code sent
	 * @param cancellationCode the canellation code sent
	 * @param note             the notes edited by the user
	 */
	public Command(CommandValue c, String ownerId, FeedbackCode feedbackCode, ResolutionCode resolutionCode,
			CancellationCode cancellationCode, String note) {

		try {

			setCommandValue(c);
			setOwnerId(ownerId);
			setFeedbackCode(feedbackCode);
			setResolutionCode(resolutionCode);
			setCancellationCode(cancellationCode);
			setNote(note);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());

		}

	}

	/**
	 * sets the command value; cannot be null
	 * 
	 * @param c the c to set
	 */
	public void setCommandValue(CommandValue c) {

		if (c == null)
			throw new IllegalArgumentException();
		else
			this.c = c;

	}

	/**
	 * sets the owner id, cannot be null if commandValue in process
	 * 
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		if (this.c == CommandValue.PROCESS) {
			if (ownerId == null || ownerId.equals("")) {
				throw new IllegalArgumentException();
			}			
		}
		this.ownerId = ownerId;
	}

	/**
	 * sets the note, cannot be null or empty
	 * 
	 * @param note the note to set
	 */
	public void setNote(String note) {
		if (note.contentEquals("") || note == null) {
			throw new IllegalArgumentException();
		} else
			this.note = note;
	}

	/**
	 * sets the resolution code, cannot be null if commandValue is in Resolve
	 * 
	 * @param resolutionCode the resolutionCode to set
	 */
	public void setResolutionCode(ResolutionCode resolutionCode) {
		if (this.c == CommandValue.RESOLVE && resolutionCode == null) {
			throw new IllegalArgumentException();
		} else
			this.resolutionCode = resolutionCode;
	}

	/**
	 * sets the feedbackCode, cannot be null if commandValue is in FEEDBACK
	 * 
	 * @param feedbackCode the feedbackCode to set
	 */
	public void setFeedbackCode(FeedbackCode feedbackCode) {
		//TODO should i set to null?
		if (this.c == CommandValue.FEEDBACK && feedbackCode == null) {
			throw new IllegalArgumentException();
		} else
			this.feedbackCode = feedbackCode;
	}

	/**
	 * sets the cancellationCode, cannot be null id the commandValue is in CANCEL
	 * 
	 * @param cancellationCode the cancellationCode to set
	 */
	public void setCancellationCode(CancellationCode cancellationCode) {
		if (this.c == CommandValue.CANCEL && cancellationCode == null) {
			throw new IllegalArgumentException();
		} else
			this.cancellationCode = cancellationCode;
	}

	/**
	 * Gets the current command
	 * 
	 * @return null
	 */
	public CommandValue getCommand() {
		return c;
	}

	/**
	 * Gets the resolution code
	 * 
	 * @return null
	 */
	public ResolutionCode getResolutionCode() {
		return resolutionCode;
	}

	/**
	 * gets the feedback code
	 * 
	 * @return null
	 */
	public FeedbackCode getFeedbackCode() {
		return feedbackCode;
	}

	/**
	 * gets the cancellation code
	 * 
	 * @return null
	 */
	public CancellationCode getCancellationCode() {
		return cancellationCode;
	}

	/**
	 * gets the owner's id
	 * 
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * gets the notes
	 * 
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

}
