package locDev.ui;

/*
 * Copyright ( C ) 2012 	       
 * Joao Nuno Amaral joao.nuno.amaral@ist.utl.pt
 * Rodrigo Bruno rodrigo.bruno@ist.utl.pt
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301,USA.
 */

import java.awt.*; 

import javax.swing.*;

import locDev.ui.handlers.GetPinTablesHandler;
import locDev.ui.handlers.ManageCourses;
import locDev.ui.handlers.GetAttendanceTablesHandler;

/**
 * Class hosting the main Window configuration and behavior.
 */
public class MainWindow extends JFrame {

	/**
	 * serialization id.
	 */
	private static final long serialVersionUID = -5739379058595027042L;
	
	/**
	 * Own reference -> Singleton.
	 */
	private static MainWindow instance = null;
	
	public static String CourseConfigurationDirectory = null; 
	
	/**
	 * Center panel.
	 */
	private JPanel pnlCenter;
	
	/**
	 * Buttons.
	 */
	private JButton btnManageCourses;
	private JButton btnGetAttendanceTables;
	private JButton btnGetPinTables;
	
	/**
	 * Label placed on top of the window.
	 */
	private JLabel lblTopLabel; 
	
	/**
	 * Method that sets up the main window.
	 */
	private void setupWindow() {
		
		//Create and set up the window. 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setDefaultLookAndFeelDecorated(true);
		this.setLayout(new BorderLayout());
		
		this.pnlCenter = new JPanel(new FlowLayout());		
		this.btnManageCourses = new JButton("Manage Courses");
		this.btnGetAttendanceTables = new JButton("Get Attendance Tables");
		this.btnGetPinTables = new JButton("Get Pins Tables");
		this.btnManageCourses.addActionListener(new ManageCourses());
		this.btnGetAttendanceTables.addActionListener(
				new GetAttendanceTablesHandler());
		this.btnGetPinTables.addActionListener(new GetPinTablesHandler());
		this.pnlCenter.add(this.btnManageCourses);
		this.pnlCenter.add(this.btnGetAttendanceTables);
		this.pnlCenter.add(this.btnGetPinTables);
		this.getContentPane().add(pnlCenter,BorderLayout.CENTER);
	 
		this.lblTopLabel = new JLabel(
				"Welcome to the locDev Management Wizard!",
				SwingConstants.CENTER); 
		this.lblTopLabel.setPreferredSize(new Dimension(300, 100));
		this.getContentPane().add(this.lblTopLabel, BorderLayout.NORTH);
	 
		//Display the window.
		this.setLocationRelativeTo(null); 
		this.pack();
		// Get the screen size  
		GraphicsConfiguration gc = this.getGraphicsConfiguration();  
		Rectangle bounds = gc.getBounds(); 
		// Set the Location and Activate  
		Dimension size = this.getPreferredSize();
		this.setLocation((int) ((bounds.width / 2) - (size.getWidth() / 2)),  
                		 (int) ((bounds.height / 2) - (size.getHeight() / 2))); 
		this.setVisible(true); 
	} 
	
	/**
	 * Private constructor to ensure that nobody can create a new object.
	 */
	private MainWindow() {
		super("locDev Management Wizard");
		this.setupWindow();
	}
	
	/**
	 * Method to obtain the MainWindow instance.
	 * @return the MainWindow's instance.
	 */
	public static MainWindow Instance()
	{
		if (instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}
}
