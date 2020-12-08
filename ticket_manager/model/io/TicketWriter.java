/**
 * 
 */
package edu.ncsu.csc216.ticket_manager.model.io;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket;

/**
 * Writes a list of tickets to a file
 * @author Jared Faulk
 *
 */
public class TicketWriter {
	
	/**
	 * empty constructor
	 */
	public TicketWriter() {
		
	}

	
	
	/**
	 * Writes a formatted ticket to a file
	 * @param filename filename to write to
	 * @param list list of tickets to write to file
	 * @throws FileNotFoundException if file is not found throw exception
	 */
	public static void writeTicketFile(String filename, List<Ticket> list) {
		
		try {
			File f = new File(filename);
			PrintStream stream = new PrintStream(f);
			
			for(int i = 0; i < list.size(); i++) {
				stream.println(list.get(i).toString());			
			}
			stream.close();
			
		} catch(FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to save file.");
		}
		
		
		
	}
}
