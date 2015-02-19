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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import locDev.CourseManager;
import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.types.Shift;
import locDev.ui.handlers.CloseAttendancesHandler;
import locDev.ui.handlers.GenerateTableHandler;
import locDev.ui.handlers.ReadTableHandler;
/**
 * 
 */
public class AttendancesWindow extends JFrame {

	/**
	 * serialization id.
	 */
	private static final long serialVersionUID = 4475676957870035785L;
	
	/**
	 * Own reference -> Singleton.
	 */
	private static AttendancesWindow instance = null;
	
	/**
	 * Course manager instance.
	 */
	private CourseManager courseManager;
		
	/**
	 * Buttons.
	 */
	private JButton btGenerateTable;
	private JButton btUploadTable;
	private JButton btClose;
	
	/**
	 * Panels
	 */
	private JPanel pnlSouth;
	private JPanel pnlCenter;
	
	/**
	 * Top label
	 */
	private JLabel lblAttendances;
	
	/**
	 * Initiate all the components
	 */
	private void setupWindow() {
		//Create and set up the window. 
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		this.setLayout(new BorderLayout());
		
		// Configure North panel
		this.lblAttendances = new JLabel(
				"To create an attendance table you need to select which " +
				    "shifts you want to be included in the table.",
				SwingConstants.CENTER);
		this.getContentPane().add(this.lblAttendances, BorderLayout.NORTH);
		
		// Configure Center panel
		//   - Get all shifts 
		//	 - Write in the screen
		Map<String,Shift> map = this.courseManager.getShifts();
		this.pnlCenter = new JPanel(new GridLayout(map.size(), 1));
		for(Shift shift : map.values()) {
			this.pnlCenter.add(new JCheckBox(shift.getShiftName()));
		}
		this.getContentPane().add(this.pnlCenter,BorderLayout.CENTER);
		
		
		// Configure South panel
		this.btGenerateTable = new JButton("Generate Table");
		this.btClose = new JButton("Close");
		this.btUploadTable = new JButton("Upload Table");
		this.pnlSouth = new JPanel();
		this.pnlSouth.add(this.btGenerateTable);
		this.pnlSouth.add(this.btUploadTable);
		this.pnlSouth.add(this.btClose);
		this.btGenerateTable.addActionListener(new GenerateTableHandler());
		this.btUploadTable.addActionListener(new ReadTableHandler());
		this.btClose.addActionListener(new CloseAttendancesHandler());
		this.getContentPane().add(this.pnlSouth, BorderLayout.SOUTH);
		
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
	 * Private constructor to avoid new objects.
	 */
	private AttendancesWindow() {
		super("Student's Attendances");
		this.courseManager = new CourseManager();
		getCoursePath();
		this.setupWindow();
	}
	
	/**
	 * Method that disposes (closes) the current window.
	 */
	@Override
	public void dispose() {
		super.dispose();
		instance = null;
	}
	/**
	 * ask the user for the course path
	 */
	private void getCoursePath() {
		String coursePath = null;
	    if(MainWindow.CourseConfigurationDirectory != null) {
	    	// check if the user wants to use a previously defined path.
	    	int dialogResult = 
	    			JOptionPane.showConfirmDialog (
	    					null, 
	    					"Would you like to use this directory: " + 
	    					MainWindow.CourseConfigurationDirectory + "?",
	    					"Select Directory",
	    					JOptionPane.YES_NO_OPTION);
	    	if(dialogResult == JOptionPane.YES_OPTION) {
	    		coursePath = MainWindow.CourseConfigurationDirectory;
	    	}
	    }
	    if (coursePath == null) {
	    	// ask the user for the location of the configuration directory.
	    	JFileChooser chooser = new JFileChooser();
	    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	if(chooser.showOpenDialog(this) 
	    		== JFileChooser.APPROVE_OPTION) {
	    		coursePath = chooser.getSelectedFile().getAbsolutePath();
	    	}  
	    	else {
	    		this.dispose();
	    		return;
	    	}
	    }
	    MainWindow.CourseConfigurationDirectory = coursePath;
	       try {
	    	courseManager.load(coursePath);
	       } catch (InvalidFormatMetaInfoFileException e1) {
				JOptionPane.showMessageDialog(
						CourseWindow.Instance(), 
						"Error loading inforamtion (bad format).");
			}
	}
	
	/**
	 * Method to return the AttendancesWindow's instance.
	 * @return CreateNewCourseWindow's instance.
	 */
	public static AttendancesWindow Instance() {
		if (instance == null) {
			instance = new AttendancesWindow();
		}
		return instance;
	}
	
	/**
	 * Getters
	 */	
	public CourseManager getCourseManager() { return this.courseManager; }
	public Component[] getCheckBoxes() { return this.pnlCenter.getComponents(); }
}
