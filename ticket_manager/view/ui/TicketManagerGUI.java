package edu.ncsu.csc216.ticket_manager.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.ticket_manager.model.command.Command;
import edu.ncsu.csc216.ticket_manager.model.command.Command.CancellationCode;
import edu.ncsu.csc216.ticket_manager.model.command.Command.FeedbackCode;
import edu.ncsu.csc216.ticket_manager.model.command.Command.ResolutionCode;
import edu.ncsu.csc216.ticket_manager.model.manager.TicketManager;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Category;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.Priority;
import edu.ncsu.csc216.ticket_manager.model.ticket.Ticket.TicketType;

/**
 * Container for the TicketManager that has the menu options for new ticket 
 * tracking files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 */
public class TicketManagerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Ticket Manager";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New menu item. */
	private static final String NEW_XML_TITLE = "New";
	/** Text for the Load menu item. */
	private static final String LOAD_XML_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_XML_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing Tickets. */
	private JMenuItem itemNewTicketFile;
	/** Menu item for loading a file containing Tickets. */
	private JMenuItem itemLoadTicketFile;
	/** Menu item for saving the TicketList. */
	private JMenuItem itemSaveTicketFile;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify ticketListPanel for  CardLayout. */
	private static final String TICKET_LIST_PANEL = "TicketListPanel";
	/** Constant to identify NewPanel for CardLayout. */
	private static final String NEW_PANEL = "NewPanel";
	/** Constant to identify Working for CardLayout. */
	private static final String WORKING_PANEL = "WorkingPanel";
	/** Constant to identify FeedbackPanel for CardLayout. */
	private static final String FEEDBACK_PANEL = "FeedbackPanel";
	/** Constant to identify ResolvedPanel for CardLayout. */
	private static final String RESOLVED_PANEL = "ResolvedPanel";
	/** Constant to identify ClosedPanel for CardLayout. */
	private static final String CLOSED_PANEL = "ClosedPanel";
	/** Constant to identify CanceledPanel for CardLayout. */
	private static final String CANCELED_PANEL = "CanceledPanel";
	/** Constant to identify CreateTicketPanel for CardLayout. */
	private static final String CREATE_TICKET_PANEL = "CreateTicketPanel";
	/** Ticket List panel - we only need one instance, so it's final. */
	private final TicketListPanel pnlTicketList = new TicketListPanel();
	/** New panel - we only need one instance, so it's final. */
	private final NewPanel pnlNew = new NewPanel();
	/** Working panel - we only need one instance, so it's final. */
	private final WorkingPanel pnlWorking = new WorkingPanel();
	/** Feedback panel - we only need one instance, so it's final. */
	private final FeedbackPanel pnlFeedback = new FeedbackPanel();
	/** Resolved panel - we only need one instance, so it's final. */
	private final ResolvedPanel pnlResolved = new ResolvedPanel();
	/** Closed panel - we only need one instance, so it's final. */
	private final ClosedPanel pnlClosed = new ClosedPanel();
	/** Canceled panel - we only need one instance, so it's final. */
	private final CanceledPanel pnlCanceled = new CanceledPanel();
	/** Add Ticket panel - we only need one instance, so it's final. */
	private final AddTicketPanel pnlAddTicket = new AddTicketPanel();
	/** Reference to  CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a TicketManagerGUI object that will contain a JMenuBar and a
	 *  JPanel that will hold different possible views of the data in
	 * the TicketManager.
	 */
	public TicketManagerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlTicketList, TICKET_LIST_PANEL);
		panel.add(pnlNew, NEW_PANEL);
		panel.add(pnlWorking, WORKING_PANEL);
		panel.add(pnlFeedback, FEEDBACK_PANEL);
		panel.add(pnlResolved, RESOLVED_PANEL);
		panel.add(pnlClosed, CLOSED_PANEL);
		panel.add(pnlCanceled, CANCELED_PANEL);
		panel.add(pnlAddTicket, CREATE_TICKET_PANEL);
		cardLayout.show(panel, TICKET_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a file
	 * containing tickets or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewTicketFile = new JMenuItem(NEW_XML_TITLE);
		itemLoadTicketFile = new JMenuItem(LOAD_XML_TITLE);
		itemSaveTicketFile = new JMenuItem(SAVE_XML_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewTicketFile.addActionListener(this);
		itemLoadTicketFile.addActionListener(this);
		itemSaveTicketFile.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveTicketFile.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewTicketFile);
		menu.add(itemLoadTicketFile);
		menu.add(itemSaveTicketFile);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use TicketManager's singleton to create/get the sole instance.
		TicketManager model = TicketManager.getInstance();
		if (e.getSource() == itemNewTicketFile) {
			//Create a new ticket list
			model.createNewTicketList();
			itemSaveTicketFile.setEnabled(true);
			pnlTicketList.updateTable(null);
			cardLayout.show(panel, TICKET_LIST_PANEL);
			validate();
			repaint();			
		} else if (e.getSource() == itemLoadTicketFile) {
			//Load an existing ticket list
			try {
				model.loadTicketsFromFile(getFileName(true));
				itemSaveTicketFile.setEnabled(true);
				pnlTicketList.updateTable(null);
				cardLayout.show(panel, TICKET_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to load ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveTicketFile) {
			//Save current ticket list
			try {
				model.saveTicketsToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveTicketsToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save ticket file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a  JFileChooser
	 * object.
	 * @return the file name selected through  JFileChooser
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the TicketManager application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new TicketManagerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * shows the list of tickets.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class TicketListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Button for creating a new ticket */
		private JButton btnAddTicket;
		/** Button for deleting the selected ticket in the list */
		private JButton btnDeleteTicket;
		/** Button for editing the selected ticket in the list */
		private JButton btnEditTicket;
		/** Drop down for ticket type */
		private JComboBox<String> comboTicketType;
		/** Button for filtering by ticket type */
		private JButton btnFilterByTicketType;
		/** Button that will show all tickets that are currently tracked */
		private JButton btnShowAllTickets;
		/** JTable for displaying the list of tickets */
		private JTable table;
		/** TableModel for Tickets */
		private TicketTableModel ticketTableModel;
		
		/**
		 * Creates the ticket list.
		 */
		public TicketListPanel() {
			super(new BorderLayout());
			
			//Set up the JPanel that will hold action buttons		
			btnAddTicket = new JButton("Add New Ticket");
			btnAddTicket.addActionListener(this);
			btnDeleteTicket = new JButton("Delete Ticket");
			btnDeleteTicket.addActionListener(this);
			btnEditTicket = new JButton("Edit Ticket");
			btnEditTicket.addActionListener(this);
			comboTicketType = new JComboBox<String>();
			comboTicketType.addItem(Ticket.TT_REQUEST);
			comboTicketType.addItem(Ticket.TT_INCIDENT);
			btnFilterByTicketType = new JButton("List by Ticket Type");
			btnFilterByTicketType.addActionListener(this);
			btnShowAllTickets = new JButton("Show All Tickets");
			btnShowAllTickets.addActionListener(this);
			
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(btnAddTicket);
			pnlActions.add(btnDeleteTicket);
			pnlActions.add(btnEditTicket);
			pnlActions.add(comboTicketType);
			pnlActions.add(btnFilterByTicketType);
			pnlActions.add(btnShowAllTickets);
						
			//Set up table
			ticketTableModel = new TicketTableModel();
			table = new JTable(ticketTableModel);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setPreferredScrollableViewportSize(new Dimension(500, 500));
			table.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(table);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddTicket) {
				//If the add button is pressed switch to the createTicketPanel
				cardLayout.show(panel, CREATE_TICKET_PANEL);
			} else if (e.getSource() == btnDeleteTicket) {
				//If the delete button is pressed, delete the ticket
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No ticket selected");
				} else {
					try {
						int ticketId = Integer.parseInt(ticketTableModel.getValueAt(row, 0).toString());
						TicketManager.getInstance().deleteTicketById(ticketId);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket id");
					}
				}
				updateTable(null);
			} else if (e.getSource() == btnEditTicket) {
				//If the edit button is pressed, switch panel based on state
				int row = table.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No ticket selected");
				} else {
					try {
						int ticket = Integer.parseInt(ticketTableModel.getValueAt(row, 0).toString());
						String stateName = TicketManager.getInstance().getTicketById(ticket).getState();
						if (stateName.equals(Ticket.FEEDBACK_NAME)) {
							cardLayout.show(panel, FEEDBACK_PANEL);
							pnlFeedback.setTicketInfo(ticket);
						} 
						if (stateName.equals(Ticket.NEW_NAME)) {
							cardLayout.show(panel, NEW_PANEL);
							pnlNew.setTicketInfo(ticket);
						} 
						if (stateName.equals(Ticket.WORKING_NAME)) {
							cardLayout.show(panel, WORKING_PANEL);
							pnlWorking.setTicketInfo(ticket);
						} 
						if (stateName.equals(Ticket.RESOLVED_NAME)) {
							cardLayout.show(panel, RESOLVED_PANEL);
							pnlResolved.setTicketInfo(ticket);
						} 
						if (stateName.equals(Ticket.CLOSED_NAME)) {
							cardLayout.show(panel, CLOSED_PANEL);
							pnlClosed.setTicketInfo(ticket);
						} 
						if (stateName.equals(Ticket.CANCELED_NAME)) {
							cardLayout.show(panel, CANCELED_PANEL);
							pnlCanceled.setTicketInfo(ticket);
						} 
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket id");
					}
				}
			} else if (e.getSource() == btnFilterByTicketType) {
				int ticketTypeIdx = comboTicketType.getSelectedIndex();
				TicketType ticketType = null;
				switch(ticketTypeIdx) {
				case 0: ticketType = TicketType.REQUEST; break;
				case 1: ticketType = TicketType.INCIDENT; break;
				default: ticketType = null;
				}
				updateTable(ticketType);
			} else if (e.getSource() == btnShowAllTickets) {
				updateTable(null);
			}
			TicketManagerGUI.this.repaint();
			TicketManagerGUI.this.validate();
		}
		
		public void updateTable(TicketType ticketType) {
			if (ticketType == null) {
				ticketTableModel.updateTicketData();
			} else {
				ticketTableModel.updateTicketDataByTicketType(ticketType);
			}
		}
		
		/**
		 * TicketTableModel is the object underlying the  JTable object that displays
		 * the list of Tickets to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 */
		private class TicketTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"ID", "Type", "State", "Subject", "Category", "Priority"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the  TicketTableModel by requesting the latest information.
			 */
			public TicketTableModel() {
				updateTicketData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row location to modify the data.
			 * @param column location to modify the data.
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Ticket information from the TicketManager.
			 */
			private void updateTicketData() {
				TicketManager m = TicketManager.getInstance();
				data = m.getTicketsForDisplay();
			}
			
			/**
			 * Updates the given model with Ticket information for the 
			 * given ticket type from the TicketManager.
			 * @param ticketType category type to search for.
			 */
			private void updateTicketDataByTicketType(TicketType ticketType) {
				try {
					TicketManager m = TicketManager.getInstance();
					data = m.getTicketsForDisplayByType(ticketType);
				} catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket type");
				}
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with a new ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class NewPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Label for owner id field */
		private JLabel lblOwnerId;
		/** Text field for owner id */
		private JTextField txtOwnerId;
		/** Label for resolution */
		private JLabel lblCancellationCode;
		/** ComboBox for resolution options */
		private JComboBox<String> comboCancellationCode;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Investigate action */
		private JButton btnInvestigate;
		/** Cancel action */
		private JButton btnCancel;
		/** Return action */
		private JButton btnReturn;
		/** Current  Ticket's id */
		private int ticketId;
		
		/**
		 * Constructs the JPanel for editing a  Ticket in the NewState.
		 */
		public NewPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			lblOwnerId = new JLabel("Owner Id");
			txtOwnerId = new JTextField(15);
			lblCancellationCode = new JLabel("Cancellation Code");
			comboCancellationCode = new JComboBox<String>();
			lblNote = new JLabel("Work Note");
			txtNote = new JTextArea(30, 1);

			btnInvestigate = new JButton("Investigate");
			btnCancel = new JButton("Cancel");
			btnReturn = new JButton("Return");
			
			btnInvestigate.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlOwner = new JPanel();
			pnlOwner.setLayout(new GridLayout(1, 2));
			pnlOwner.add(lblOwnerId);
			pnlOwner.add(txtOwnerId);
			
			JPanel pnlCancellation = new JPanel();
			pnlCancellation.setLayout(new GridLayout(1, 2));
			pnlCancellation.add(lblCancellationCode);
			pnlCancellation.add(comboCancellationCode);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnInvestigate);
			pnlBtnRow.add(btnCancel);
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlOwner, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancellation, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the TicketInfoPanel with the given ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
			
			comboCancellationCode.removeAllItems();
			
			comboCancellationCode.addItem(Command.CC_DUPLICATE);
			comboCancellationCode.addItem(Command.CC_INAPPROPRIATE);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Take care of note.
			String note = txtNote.getText();
			if (e.getSource() == btnReturn) {
				reset = true;
			}
			if (e.getSource() == btnInvestigate) {
				String ownerId = txtOwnerId.getText();

				//Otherwise, try a Command.  If command fails, stay in panel.
				try {
					Command c = new Command(Command.CommandValue.PROCESS, ownerId, null, null, null, note);
					TicketManager.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					reset = false;
				}		
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationCode.getSelectedIndex();
				CancellationCode cancellationCode = null;
				switch (idx) {
				case 0:
					cancellationCode = CancellationCode.DUPLICATE;
					break;
				case 1:
					cancellationCode = CancellationCode.INAPPROPRIATE;
					break;
				default:
					cancellationCode = null;
				}
				
				//Try a Command.  If command fails, stay in panel
				try {
					Command c = new Command(Command.CommandValue.CANCEL, null, null, null, cancellationCode, note);
					TicketManager.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead to ticket list if valid command or return
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				//Reset fields
				comboCancellationCode.setSelectedIndex(0);
				txtOwnerId.setText("");
				txtNote.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct owner input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with an in progress ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class WorkingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the  Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for selecting a feedback code */
		private JLabel lblFeedbackCode;
		/** ComboBox for feedback code */
		private JComboBox<String> comboFeedbackCode;
		/** Feedback action */
		private JButton btnFeedback;
		/** Label for selecting a resolution code */
		private JLabel lblResolutionCode;
		/** ComboBox for resolution code */
		private JComboBox<String> comboResolutionCode;
		/** Resolve action */
		private JButton btnResolve;
		/** Label for selecting a cancellation code */
		private JLabel lblCancellationCode;
		/** ComboBox for cancellationCode */
		private JComboBox<String> comboCancellationCode;
		/** Cancel action */
		private JButton btnCancel;
		/** Return action */
		private JButton btnReturn;
		/** Current Ticket's id */
		private int ticketId;

		/**
		 * Constructs a JPanel for editing a  Ticket in the WorkingState.
		 */
		public WorkingPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			lblFeedbackCode = new JLabel("Feedback Code");
			comboFeedbackCode = new JComboBox<String>();
			lblResolutionCode = new JLabel("Resolution Code");
			comboResolutionCode = new JComboBox<String>();
			lblCancellationCode = new JLabel("Cancellation Code");
			comboCancellationCode = new JComboBox<String>();
			lblNote = new JLabel("Work Note");
			txtNote = new JTextArea(30, 5);
			
			btnFeedback = new JButton("Feedback");
			btnResolve = new JButton("Resolve");
			btnCancel = new JButton("Cancel");
			btnReturn = new JButton("Return");
			
			btnFeedback.addActionListener(this);
			btnResolve.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			JPanel pnlFeedbackCode = new JPanel();
			pnlFeedbackCode.setLayout(new GridLayout(1, 2));
			pnlFeedbackCode.add(lblFeedbackCode);
			pnlFeedbackCode.add(comboFeedbackCode);
			
			JPanel pnlResolution = new JPanel();
			pnlResolution.setLayout(new GridLayout(1, 2));
			pnlResolution.add(lblResolutionCode);
			pnlResolution.add(comboResolutionCode);
			
			JPanel pnlCancellationCode = new JPanel();
			pnlCancellationCode.setLayout(new GridLayout(1, 2));
			pnlCancellationCode.add(lblCancellationCode);
			pnlCancellationCode.add(comboCancellationCode);
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnFeedback);
			pnlBtnRow.add(btnResolve);
			pnlBtnRow.add(btnCancel);
			pnlBtnRow.add(btnReturn);

			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlFeedbackCode, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolution, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancellationCode, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the  TicketInfoPanel with the given Ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
			
			comboFeedbackCode.removeAllItems();
			comboFeedbackCode.addItem(Command.F_CALLER);
			comboFeedbackCode.addItem(Command.F_CHANGE);
			comboFeedbackCode.addItem(Command.F_PROVIDER);
			
			comboResolutionCode.removeAllItems();
			comboResolutionCode.addItem(Command.RC_COMPLETED);
			comboResolutionCode.addItem(Command.RC_NOT_COMPLETED);
			comboResolutionCode.addItem(Command.RC_SOLVED);
			comboResolutionCode.addItem(Command.RC_WORKAROUND);
			comboResolutionCode.addItem(Command.RC_NOT_SOLVED);
			comboResolutionCode.addItem(Command.RC_CALLER_CLOSED);
			
			comboCancellationCode.removeAllItems();
			comboCancellationCode.addItem(Command.CC_DUPLICATE);
			comboCancellationCode.addItem(Command.CC_INAPPROPRIATE);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnReturn) {
				reset = true;
			} else if (e.getSource() == btnResolve) {
				//Get resolution
				int idx = comboResolutionCode.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No resolution selected");
				} else {
					ResolutionCode resolutionCode = null;
					switch (idx) {
					case 0: 
						resolutionCode = ResolutionCode.COMPLETED;
						break;
					case 1:
						resolutionCode = ResolutionCode.NOT_COMPLETED;
						break;
					case 2: 
						resolutionCode = ResolutionCode.SOLVED;
						break;
					case 3:
						resolutionCode = ResolutionCode.WORKAROUND;
						break;
					case 4:
						resolutionCode = ResolutionCode.NOT_SOLVED;
						break;
					case 5:
						resolutionCode = ResolutionCode.CALLER_CLOSED;
						break;
					default:
						resolutionCode = null;
					}
					//Try a command.  If problem, stay on panel.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, null, null, resolutionCode, null, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			} else if (e.getSource() == btnFeedback) {
				int idx = comboFeedbackCode.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No feedback code selected");
				} else {
					FeedbackCode feedbackCode = null;
					switch (idx) {
					case 0: 
						feedbackCode = FeedbackCode.AWAITING_CALLER;
						break;
					case 1:
						feedbackCode = FeedbackCode.AWAITING_CHANGE;
						break;
					case 2:
						feedbackCode = FeedbackCode.AWAITING_PROVIDER;
						break;
					default:
						feedbackCode = null;
					}
					//Try a command.  If problem, stay on panel.
					try {
						Command c = new Command(Command.CommandValue.FEEDBACK, null, feedbackCode , null, null, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationCode.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No cancellation code selected");
				} else {
					CancellationCode canellationCode = null;
					switch (idx) {
					case 0: 
						canellationCode = CancellationCode.DUPLICATE;
						break;
					case 1:
						canellationCode = CancellationCode.INAPPROPRIATE;
						break;
					default:
						canellationCode = null;
					}
					//Try a command.  If problem, stay on panel.
					try {
						Command c = new Command(Command.CommandValue.CANCEL, null, null, null, canellationCode, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			}
			if (reset) {
				//All buttons lead to back Ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				//Reset fields
				comboFeedbackCode.setSelectedIndex(0);
				comboResolutionCode.setSelectedIndex(0);
				comboCancellationCode.setSelectedIndex(0);
				txtNote.setText("");
			}
			//Otherwise, stay on panel
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with a confirmed Ticket
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class FeedbackPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for resolution */
		private JLabel lblResolutionCode;
		/** ComboBox for resolution options */
		private JComboBox<String> comboResolutionCode;
		/** Label for selecting a cancellation code */
		private JLabel lblCancellationCode;
		/** ComboBox for cancellationCodes */
		private JComboBox<String> comboCancellationCode;
		/** Reopen action */
		private JButton btnReopen;
		/** Resolve action */
		private JButton btnResolve;
		/** Cancel action */
		private JButton btnCancel;
		/** Return action */
		private JButton btnReturn;
		/** Current  Ticket's id */
		private int ticketId;
		
		/**
		 * Constructs the JPanel for editing a Ticket in the FeedbackState.
		 */
		public FeedbackPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 1);
			lblCancellationCode = new JLabel("Cancellation Code");
			comboCancellationCode = new JComboBox<String>();
			lblResolutionCode = new JLabel("Resolution");
			comboResolutionCode = new JComboBox<String>();

			btnReopen = new JButton("Reopen");
			btnResolve = new JButton("Resolve");
			btnCancel = new JButton("Cancel");
			btnReturn = new JButton("Return");
			
			btnReopen.addActionListener(this);
			btnResolve.addActionListener(this);
			btnCancel.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			GridBagConstraints c = new GridBagConstraints();
					
			JPanel pnlResolutionCode = new JPanel();
			pnlResolutionCode.setLayout(new GridLayout(1, 2));
			pnlResolutionCode.add(lblResolutionCode);
			pnlResolutionCode.add(comboResolutionCode);
			
			JPanel pnlCancellationCode = new JPanel();
			pnlCancellationCode.setLayout(new GridLayout(1, 2));
			pnlCancellationCode.add(lblCancellationCode);
			pnlCancellationCode.add(comboCancellationCode);

			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 4));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnResolve);
			pnlBtnRow.add(btnCancel);
			pnlBtnRow.add(btnReturn);
				
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlResolutionCode, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlCancellationCode, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);		
		}
		
		/**
		 * Set the  TicketInfoPanel with the given Ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
			
			comboResolutionCode.removeAllItems();
			comboResolutionCode.addItem(Command.RC_COMPLETED);
			comboResolutionCode.addItem(Command.RC_NOT_COMPLETED);
			comboResolutionCode.addItem(Command.RC_SOLVED);
			comboResolutionCode.addItem(Command.RC_WORKAROUND);
			comboResolutionCode.addItem(Command.RC_NOT_SOLVED);
			comboResolutionCode.addItem(Command.RC_CALLER_CLOSED);
			
			comboCancellationCode.removeAllItems();
			comboCancellationCode.addItem(Command.CC_DUPLICATE);
			comboCancellationCode.addItem(Command.CC_INAPPROPRIATE);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Take care of note
			String note = txtNote.getText();
			if (e.getSource() == btnReturn) {
				reset = true;
			} else if (e.getSource() == btnResolve) {
				//Get resolution
				int idx = comboResolutionCode.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No resolution selected");
				} else {				
					ResolutionCode resolutionCode = null;
					switch (idx) {
					case 0: 
						resolutionCode = ResolutionCode.SOLVED;
						break;
					case 1:
						resolutionCode = ResolutionCode.WORKAROUND;
						break;
					case 2:
						resolutionCode = ResolutionCode.NOT_SOLVED;
						break;
					case 3:
						resolutionCode = ResolutionCode.CALLER_CLOSED;
						break;
					default:
						resolutionCode = null;
					}
					//Try a command.  If problem, go back to Ticket list.
					try {
						Command c = new Command(Command.CommandValue.RESOLVE, null, null, resolutionCode, null, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			} else if (e.getSource() == btnReopen) {
					//Otherwise, try a Command.  If command fails, go back to Ticket list
					try {
						Command c = new Command(Command.CommandValue.REOPEN, null, null, null, null, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}		
			} else if (e.getSource() == btnCancel) {
				int idx = comboCancellationCode.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No cancellation code selected");
				} else {
					CancellationCode canellationCode = null;
					switch (idx) {
					case 0: 
						canellationCode = CancellationCode.DUPLICATE;
						break;
					case 1:
						canellationCode = CancellationCode.INAPPROPRIATE;
						break;
					default:
						canellationCode = null;
					}
					//Try a command.  If problem, go back to Ticket list.
					try {
						Command c = new Command(Command.CommandValue.CANCEL, null, null, null, canellationCode, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			}
			if (reset) {
				//Add buttons lead to back Ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				//Reset elements
				txtNote.setText("");
				comboResolutionCode.setSelectedIndex(0);
				comboCancellationCode.setSelectedIndex(0);
			}
		}
		
	}

	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with a resolved Ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ResolvedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Label for selecting a feedback code */
		private JLabel lblFeedbackCode;
		/** ComboBox for feedback codes */
		private JComboBox<String> comboFeedbackCode;
		/** Feedback action */
		private JButton btnFeedback;
		/** Reopen action */
		private JButton btnReopen;
		/** Confirm action */
		private JButton btnConfirm;
		/** Return action */
		private JButton btnReturn;
		/** Current Ticket's id */
		private int ticketId;

		/**
		 * Constructs a JFrame for editing a Ticket in the ResolvedState.
		 */
		public ResolvedPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			lblFeedbackCode = new JLabel("Feedback Code");
			comboFeedbackCode = new JComboBox<String>();
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnFeedback = new JButton("Feedback");
			btnReopen = new JButton("Reopen");
			btnConfirm = new JButton("Confirm");
			btnReturn = new JButton("Return");
			
			btnFeedback.addActionListener(this);
			btnReopen.addActionListener(this);
			btnConfirm.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");

			GridBagConstraints c = new GridBagConstraints();
			
			JPanel pnlFeedbackCode = new JPanel();
			pnlFeedbackCode.setLayout(new GridLayout(1, 2));
			pnlFeedbackCode.add(lblFeedbackCode);
			pnlFeedbackCode.add(comboFeedbackCode);
						
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 5));
			pnlBtnRow.add(btnFeedback);
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnConfirm);
			pnlBtnRow.add(btnReturn);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlFeedbackCode, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the  TicketInfoPanel with the given Ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
			
			comboFeedbackCode.removeAllItems();
			comboFeedbackCode.addItem(Command.F_CALLER);
			comboFeedbackCode.addItem(Command.F_CHANGE);
			comboFeedbackCode.addItem(Command.F_PROVIDER);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnReturn) {
				reset = true;
			} else if (e.getSource() == btnFeedback) {
				int idx = comboFeedbackCode.getSelectedIndex();
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No feedback code selected");
				} else {
					FeedbackCode feedbackCode = null;
					switch (idx) {
					case 0: 
						feedbackCode = FeedbackCode.AWAITING_CALLER;
						break;
					case 1:
						feedbackCode = FeedbackCode.AWAITING_CHANGE;
						break;
					case 2:
						feedbackCode = FeedbackCode.AWAITING_PROVIDER;
						break;
					default:
						feedbackCode = null;
					}
					//Try command.  If problem, go to Ticket list.
					try {
						Command c = new Command(Command.CommandValue.FEEDBACK, null, feedbackCode, null, null, note);
						TicketManager.getInstance().executeCommand(ticketId, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
					}
				}
			} else if (e.getSource() == btnReopen) {
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null, null, null, null, note);
					TicketManager.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
				}
			} else if (e.getSource() == btnConfirm) {
				try {
					Command c = new Command(Command.CommandValue.CONFIRM, null, null, null, null, note);
					TicketManager.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
				}
			} 
			if (reset) {
				//Add buttons lead to back Ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				//Reset elements
				txtNote.setText("");
				comboFeedbackCode.setSelectedIndex(0);
			}
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with a closed ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ClosedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Note label for the state update */
		private JLabel lblNote;
		/** Note for the state update */
		private JTextArea txtNote;
		/** Reopen action */
		private JButton btnReopen;
		/** Return action */
		private JButton btnReturn;
		/** Current Ticket's id */
		private int ticketId;

		/**
		 * Constructs a JPanel for editing a Ticket in the ClosedState.
		 */
		public ClosedPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(30, 5);
			
			btnReopen = new JButton("Reopen");
			btnReturn = new JButton("Return");
			
			btnReopen.addActionListener(this);
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JScrollPane scrollNote = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnReturn);
			
			GridBagConstraints c = new GridBagConstraints();
						
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 3;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(scrollNote, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the TicketInfoPanel with the given Ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			//Handle note
			String note = txtNote.getText();
			if (e.getSource() == btnReturn) {
				reset = true;
			} else if (e.getSource() == btnReopen) {
				
				//Try command.  If problem, return to Ticket list.
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null, null, null, null, note);
					TicketManager.getInstance().executeCommand(ticketId, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid command");
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid state transition");
				}
			}
			if (reset) {
				//All buttons lead to back Ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				//Reset fields
				txtNote.setText("");
			} 
			//Otherwise, stay on panel so user can fix
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * interacts with a canceled ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class CanceledPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/**  TicketInfoPanel that presents the  Ticket's information to the user */
		private TicketInfoPanel pnlTicketInfo;
		/** Return action */
		private JButton btnReturn;
		/** Current Ticket's id */
		private int ticketId;

		/**
		 * Constructs a JPanel for editing a Ticket in the CanceledState.
		 */
		public CanceledPanel() {
			pnlTicketInfo = new TicketInfoPanel();
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Ticket Information");
			pnlTicketInfo.setBorder(border);
			pnlTicketInfo.setToolTipText("Ticket Information");
			
			btnReturn = new JButton("Return");
			btnReturn.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			pnlCommands.setLayout(new GridBagLayout());
			lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");

			GridBagConstraints c = new GridBagConstraints();
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnReturn);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
			
			
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketInfo, c);
			
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
		}
		
		/**
		 * Set the  TicketInfoPanel with the given Ticket data.
		 * @param ticketId id of the Ticket
		 */
		public void setTicketInfo(int ticketId) {
			this.ticketId = ticketId;
			pnlTicketInfo.setTicketInfo(this.ticketId);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			//All buttons lead to back Ticket list
			cardLayout.show(panel, TICKET_LIST_PANEL);
			pnlTicketList.updateTable(null);
			TicketManagerGUI.this.repaint();
			TicketManagerGUI.this.validate();
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * shows information about the ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class TicketInfoPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for id */
		private JLabel lblId;
		/** Field for id */
		private JTextField txtId;
		/** Label for state */
		private JLabel lblState;
		/** Field for state */
		private JTextField txtState;
		/** Label for ticket type */
		private JLabel lblTicketType;
		/** Field for ticket type */
		private JTextField txtTicketType;
		/** Label for subject */
		private JLabel lblSubject;
		/** Field for lblSubject */
		private JTextField txtSubject;
		/** Label for caller */
		private JLabel lblCaller;
		/** Field for caller */
		private JTextField txtCaller;
		/** Label for owner */
		private JLabel lblOwner;
		/** Field for owner */
		private JTextField txtOwner;		
		/** Label for category */
		private JLabel lblCategory;
		/** Field for category */
		private JTextField txtCategory;
		/** Label for priority */
		private JLabel lblPriority;
		/** Field for priority */
		private JTextField txtPriority;
		/** Label for feedback/resolution/cancellation code */
		private JLabel lblCode;
		/** Field for feedback/resolution/cancellation code */
		private JTextField txtCode; 
		/** Label for notes */
		private JLabel lblNotes;
		/** Field for notes */
		private JTextArea txtNotes;
		
		/** 
		 * Construct the panel for the ticket information.
		 */
		public TicketInfoPanel() {
			super(new GridBagLayout());
			
			lblId = new JLabel("Ticket Id");
			lblState = new JLabel("State");
			lblTicketType = new JLabel("Ticket Type");
			lblSubject = new JLabel("Subject");
			lblCaller = new JLabel("Caller");
			lblOwner = new JLabel("Owner");
			lblCategory = new JLabel("Category");
			lblPriority = new JLabel("Priority");
			lblCode = new JLabel("Code"); //Leave generic for now
			lblNotes = new JLabel("Notes");
			
			txtId = new JTextField(15);
			txtState = new JTextField(15);
			txtTicketType = new JTextField(15);
			txtSubject = new JTextField(40);
			txtCaller = new JTextField(15);
			txtOwner = new JTextField(15);
			txtCategory = new JTextField(15);
			txtPriority = new JTextField(15);
			txtCode = new JTextField(15);
			txtNotes = new JTextArea(30, 5);
			
			txtId.setEditable(false);
			txtState.setEditable(false);
			txtTicketType.setEditable(false);
			txtSubject.setEditable(false);
			txtCaller.setEditable(false);
			txtOwner.setEditable(false);
			txtCategory.setEditable(false);
			txtPriority.setEditable(false);
			txtCode.setEditable(false);
			txtNotes.setEditable(false);
			
			JScrollPane notesScrollPane = new JScrollPane(txtNotes, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - ID 
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 4));
			row1.add(lblId);
			row1.add(txtId);
			row1.add(new JLabel(""));
			row1.add(new JLabel(""));
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 2 - State and Ticket Type
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 4));
			row2.add(lblState);
			row2.add(txtState);
			row2.add(lblTicketType);
			row2.add(txtTicketType); 
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row2, c);
						
			//Row 3 - Subject Label
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblSubject, c);
			
			//Row 4 - Subject text
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(txtSubject, c);
			
			//Row 5 - Caller and Owner
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 4));
			row5.add(lblCaller);
			row5.add(txtCaller);
			row5.add(lblOwner);
			row5.add(txtOwner); 
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
			
			//Row 6 - Category & Priority
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 4));
			row6.add(lblCategory);
			row6.add(txtCategory);
			row6.add(lblPriority);
			row6.add(txtPriority);
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row6, c);
			
			//Row 7 - Code
			JPanel row7 = new JPanel();
			row7.setLayout(new GridLayout(1, 2));
			row7.add(lblCode);
			row7.add(txtCode);
			row7.add(new JLabel(""));
			row7.add(new JLabel(""));
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row7, c);
			
			//Row 8 - Notes title
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNotes, c);
			
			//Row 9 - Notes text area
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 4;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(notesScrollPane, c);
		}
		
		/**
		 * Adds information about the ticket to the display.  
		 * @param ticketId the id for the ticket to display information about.
		 */
		public void setTicketInfo(int ticketId) {
			//Get the ticket from the model
			Ticket t = TicketManager.getInstance().getTicketById(ticketId);
			if (t == null) {
				//If the ticket doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket id");
				cardLayout.show(panel, TICKET_LIST_PANEL);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
			} else {
				//Otherwise, set all of the fields with the information
				txtId.setText("" + t.getTicketId());
				txtState.setText(t.getState());
				txtTicketType.setText(t.getTicketTypeString());
				txtSubject.setText(t.getSubject());				
				txtCaller.setText(t.getCaller());
				txtOwner.setText(t.getOwner());
				txtCategory.setText(t.getCategory());
				txtPriority.setText(t.getPriority());
				
				if (t.getFeedbackCode() != null) {
					lblCode.setText("Feedback Code");
					txtCode.setText(t.getFeedbackCode());
				} else if (t.getResolutionCode() != null) {
					lblCode.setText("Resolution Code");
					txtCode.setText(t.getResolutionCode());
				} else if (t.getCancellationCode() != null) {
					lblCode.setText("Cancellation Code");
					txtCode.setText(t.getCancellationCode());
				} else {
					lblCode.setText("");
					txtCode.setText("");
				}
				
				txtNotes.setText(t.getNotes());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the  JPanel that 
	 * allows for creation of a new ticket.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class AddTicketPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Label for ticket type */
		private JLabel lblTicketType;
		/** Combo box for ticket type */
		private JComboBox<String> comboTicketType;
		/** Label for subject */
		private JLabel lblSubject;
		/** Text field for subject */
		private JTextField txtSubject;
		/** Label for identifying caller text field */
		private JLabel lblCaller;
		/** Text field for entering caller id */
		private JTextField txtCaller;
		/** Label for category text field */
		private JLabel lblCategory;
		/** Combo box for category */
		private JComboBox<String> comboCategory;
		/** Label for priority */
		private JLabel lblPriority;
		/** Combo box for priority */
		private JComboBox<String> comboPriority;
		/** Label for identifying note */
		private JLabel lblNote;
		/** Text field for entering note information */
		private JTextArea txtNote;
		
		/** Button to add a ticket */
		private JButton btnAdd;
		/** Button for returning to menu with out creating a new ticket */
		private JButton btnReturn;
		
		/**
		 * Creates the  JPanel for adding new ticket to the 
		 * manager.
		 */
		public AddTicketPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblTicketType = new JLabel("Ticket Type");
			comboTicketType = new JComboBox<String>();
			comboTicketType.addItem(Ticket.TT_REQUEST);
			comboTicketType.addItem(Ticket.TT_INCIDENT);
			lblSubject = new JLabel("Subject");
			txtSubject = new JTextField(30);
			lblCaller = new JLabel("Caller");
			txtCaller = new JTextField(30);
			lblCategory = new JLabel("Category");
			comboCategory = new JComboBox<String>();
			comboCategory.addItem(Ticket.C_INQUIRY);
			comboCategory.addItem(Ticket.C_SOFTWARE);
			comboCategory.addItem(Ticket.C_HARDWARE);
			comboCategory.addItem(Ticket.C_NETWORK);
			comboCategory.addItem(Ticket.C_DATABASE);
			lblPriority = new JLabel("Priority");
			comboPriority = new JComboBox<String>();
			comboPriority.addItem(Ticket.P_URGENT);
			comboPriority.addItem(Ticket.P_HIGH);
			comboPriority.addItem(Ticket.P_MEDIUM);
			comboPriority.addItem(Ticket.P_LOW);
			lblNote = new JLabel("Note");
			txtNote = new JTextArea(5, 30);
			
			btnAdd = new JButton("Add Ticket");
			btnReturn = new JButton("Return");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnReturn.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Builds ticket type panel, which is a 1 row, 2 col grid
			JPanel pnlTicketType = new JPanel();
			pnlTicketType.setLayout(new GridLayout(1, 2));
			pnlTicketType.add(lblTicketType);
			pnlTicketType.add(comboTicketType);
			
			//Builds subject panel, which is a 1 row, 2 col grid
			JPanel pnlSubject = new JPanel();
			pnlSubject.setLayout(new GridLayout(1, 2));
			pnlSubject.add(lblSubject);
			pnlSubject.add(txtSubject);
			
			//Builds caller panel, which is a 1 row, 2 col grid
			JPanel pnlCaller = new JPanel();
			pnlCaller.setLayout(new GridLayout(1, 2));
			pnlCaller.add(lblCaller);
			pnlCaller.add(txtCaller);
			
			//Builds category panel, which is a 1 row, 2 col grid
			JPanel pnlCategory = new JPanel();
			pnlCategory.setLayout(new GridLayout(1, 2));
			pnlCategory.add(lblCategory);
			pnlCategory.add(comboCategory);
			
			//Builds priority panel, which is a 1 row, 2 col grid
			JPanel pnlPriority = new JPanel();
			pnlPriority.setLayout(new GridLayout(1, 2));
			pnlPriority.add(lblPriority);
			pnlPriority.add(comboPriority);
			
			//Creates scroll for notes area
			JScrollPane scrollSummary = new JScrollPane(txtNote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			//Build button panel, which is a 1 row, 2 col grid
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout(1, 2));
			pnlButtons.add(btnAdd);
			pnlButtons.add(btnReturn);
			
			//Adds all panels to main panel
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlTicketType, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlSubject, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCaller, c);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCategory, c);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlPriority, c);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(lblNote, c);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(scrollSummary, c);
			
			c.gridx = 0;
			c.gridy = 8;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlButtons, c);
			
			//Empty panel to cover the bottom portion of the screen
			JPanel pnlFiller = new JPanel();
			c.gridx = 0;
			c.gridy = 9;
			c.weightx = 1;
			c.weighty = 10;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlFiller, c);
		}

		/**
		 * Performs an action based on the given  ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				//Add ticket to the list
				int ticketTypeIdx = comboTicketType.getSelectedIndex();
				int categoryIdx = comboCategory.getSelectedIndex();
				int priorityIdx = comboPriority.getSelectedIndex();
				if (ticketTypeIdx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No ticket type selected");
				} else if (categoryIdx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No category selected");
				} else if (priorityIdx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(TicketManagerGUI.this, "No priority selected");
				} else {
					String caller = txtCaller.getText();
					String subject = txtSubject.getText();
					String note = txtNote.getText();
					
					TicketType ticketType = null;
					switch (ticketTypeIdx) {
					case 0: ticketType = TicketType.REQUEST; break;
					case 1: ticketType = TicketType.INCIDENT; break;
					default: ticketType = null;
					}
					
					Category category = null;
					switch (categoryIdx) {
					case 0: category = Category.INQUIRY; break;
					case 1: category = Category.SOFTWARE; break;
					case 2: category = Category.HARDWARE; break;
					case 3: category = Category.NETWORK; break;
					case 4: category = Category.DATABASE; break;
					default: category = null;
					}
					
					Priority priority = null;
					switch(priorityIdx) {
					case 0: priority = Priority.URGENT; break;
					case 1: priority = Priority.HIGH; break;
					case 2: priority = Priority.MEDIUM; break;
					case 3: priority = Priority.LOW; break;
					default: priority = null;
					}
					
					//Get instance of model and add ticket
					try {
						TicketManager.getInstance().addTicketToList(ticketType, subject, caller, category, priority, note);
					} catch (IllegalArgumentException exp) {
						reset = false;
						JOptionPane.showMessageDialog(TicketManagerGUI.this, "Invalid ticket information.");
					}
				}
			} 
			if (reset) {
				//All buttons lead to back ticket list
				cardLayout.show(panel, TICKET_LIST_PANEL);
				pnlTicketList.updateTable(null);
				TicketManagerGUI.this.repaint();
				TicketManagerGUI.this.validate();
				txtCaller.setText("");
				txtSubject.setText("");
				txtNote.setText("");
			}
		}
	}
}