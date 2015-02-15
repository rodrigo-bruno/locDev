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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.michaelbaranov.microba.calendar.DatePicker;

import locDev.CourseManager;
import locDev.types.Shift;
import locDev.ui.handlers.AddShiftHandler;
import locDev.ui.handlers.CloseCreateCourseHandler;
import locDev.ui.handlers.EditShiftHandler;
import locDev.ui.handlers.ListShiftsHandler;
import locDev.ui.handlers.LoadCourseByURLHandler;
import locDev.ui.handlers.LoadCourseHandler;
import locDev.ui.handlers.SaveCourseHandler;
import locDev.ui.handlers.RemoveShiftHandler;
import locDev.ui.handlers.SelectStudentsListHandle;

/**
 * Class that represents the create new course window.
 */
public class CourseWindow extends JFrame {

	/**
	 * serialization id.
	 */
	private static final long serialVersionUID = -5735162046337657465L;
	
	/**
	 * Own instance -> singleton
	 */
	private static CourseWindow instance;
	
	/**
	 * Course manager instance.
	 */
	private CourseManager courseManager;
	
	/**
	 * CSV file with the shift student's list.
	 */
	private String shiftStudentsList = null;
	
	/**
	 * Panels.
	 */
	private JPanel pnlCenter;
	private JPanel pnlCenterCourse;
	private JPanel pnlCenterCourse1;
	private JPanel pnlCenterCourse2;
	private JPanel pnlCenterCourse3;
	private JPanel pnlCenterNewShift;
	private JPanel pnlNorth;
	private JPanel pnlSouth;	
	
	/**
	 * Combo boxes.
	 */
	private JComboBox<String> cbShiftType;
	private JComboBox<String> cbShiftWeekday;
	
	/**
	 * Text fields
	 */
	private TextField tfCourseName;
	private TextField tfShiftName;
	private TextField tfShiftRoom;
	private TextField tfShiftHour;
	private TextField tfShiftMinute;
	private TextField tfMaxDelay;
	private TextField tfProfID;
	private JPasswordField pfProfPassword;
	
	/**
	 * Shift List
	 */
	private JList<Shift> listShifts;
	
	/**
	 * Calendars (begin and end of course)
	 */
	private DatePicker dpStartCourse;
	private DatePicker dpEndCourse;
	
	/**
	 * Labels
	 */
	private JLabel lblTopLabel; 
	private JLabel lblCourseName;
	private JLabel lblStartCourse;
	private JLabel lblEndCourse;
	private JLabel lblShiftName;
	private JLabel lblShiftType;
	private JLabel lblShiftWeekday;
	private JLabel lblShiftRoom;
	private JLabel lblShiftTime;
	private JLabel lblColon;
	private JLabel lblShiftDuration;
	private JLabel lblProfID;
	private JLabel lblProfPassword;
	
	/**
	 * Shift types.
	 */
	private String[] shiftTypes = { 
			"Theoretical", 
			"Practical", 
			"Theoretical and practical", 
			"Laboratory", 
			"Fieldwork",
			"Problems",
			"Seminars",
			"Internship",
			"Tutorship Orientation" };
	/**
	 * Weekdays.
	 */
	private String[] weekdays = { 
			"Monday", 
			"Tuesday", 
			"Wednesday", 
			"Thursday", 
			"Friday",
			"Saturday" };
	
	/**
	 * Buttons
	 */
	private JButton btnSaveCourse;
	private JButton btnLoadCourseByRouterFolder;
	private JButton btnCancel;
	private JButton btnDeleteShift;
	private JButton btnAddShift;
	private JButton btnEditShift;
	private JButton btnStudentsList;
	private JButton btnLoadCourseByURL;
	
	/**
	 * Sets the window up.
	 */
	private void setupWindow() {
		
		//Create and set up the window. 
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1000, 600));
		
		// Configure North panel
		this.pnlNorth = new JPanel(new FlowLayout());
		this.lblTopLabel = new JLabel(
				"To create a new course, you just need to fill the fields " +
				"above and add some shifts.", 
				SwingConstants.CENTER);
		this.lblTopLabel.setPreferredSize(new Dimension(600, 100));
		this.pnlNorth.add(this.lblTopLabel);
		this.getContentPane().add(this.pnlNorth, BorderLayout.NORTH);

		// Configure Center panel
		this.pnlCenter = new JPanel(new GridLayout(3, 1, 10, 1));
		
		// Configure Center Course panel
		this.pnlCenterCourse = new JPanel(new GridLayout(3,1));
		this.pnlCenterCourse1 = new JPanel(new FlowLayout());
		this.pnlCenterCourse2 = new JPanel(new FlowLayout());
		this.pnlCenterCourse3 = new JPanel(new FlowLayout());
		this.lblCourseName = new JLabel(
				"Course Name:", SwingConstants.CENTER);
		this.tfCourseName = new TextField(30);
		this.lblStartCourse = new JLabel("Course Start Day:");
		this.dpStartCourse = new DatePicker();
		this.lblEndCourse = new JLabel("Course End Day:");
		this.dpEndCourse = new DatePicker();
		this.lblProfID = new JLabel("Professor ID:");
		this.tfProfID = new TextField(10);
		this.lblProfPassword = new JLabel("Professor Password:");
		this.pfProfPassword = new JPasswordField(10);
		this.pnlCenterCourse1.add(this.lblCourseName);
		this.pnlCenterCourse1.add(this.tfCourseName);
		this.pnlCenterCourse2.add(this.lblStartCourse);
		this.pnlCenterCourse2.add(this.dpStartCourse);
		this.pnlCenterCourse2.add(this.lblEndCourse);
		this.pnlCenterCourse2.add(this.dpEndCourse);
		this.pnlCenterCourse3.add(this.lblProfID);
		this.pnlCenterCourse3.add(this.tfProfID);
		this.pnlCenterCourse3.add(this.lblProfPassword);
		this.pnlCenterCourse3.add(this.pfProfPassword);
		this.pnlCenterCourse.add(this.pnlCenterCourse1);
		this.pnlCenterCourse.add(this.pnlCenterCourse2);
		this.pnlCenterCourse.add(this.pnlCenterCourse3);
		this.pnlCenter.add(pnlCenterCourse);
		
		// Configure Center panel
		this.listShifts = new JList<Shift>();
		this.listShifts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listShifts.addMouseListener(new ListShiftsHandler());
		this.pnlCenter.add(new JScrollPane(this.listShifts));
		
		// Configure Center new shift panel
		this.pnlCenterNewShift = new JPanel(new FlowLayout());
		this.lblShiftName = new JLabel("Shift Name:");
		this.tfShiftName = new TextField(10);
		
		this.lblShiftType = new JLabel("Type:");
		this.cbShiftType = new JComboBox<String>(this.shiftTypes);
		this.cbShiftType.setSelectedIndex(0);
		
		this.lblShiftWeekday = new JLabel("Weekday:");
		this.cbShiftWeekday = new JComboBox<String>(this.weekdays);
		this.cbShiftWeekday.setSelectedIndex(0);
		
		this.lblShiftRoom = new JLabel("Room:");
		this.tfShiftRoom = new TextField(5);
		
		this.lblShiftTime = new JLabel("Time (hh:mm):");
		this.tfShiftHour = new TextField(2);
		this.lblColon = new JLabel(":");
		this.tfShiftMinute = new TextField(2);
		
		this.lblShiftDuration = new JLabel("Max Delay (mins):");
		this.tfMaxDelay = new TextField(3);
		
		this.btnStudentsList = new JButton("Upload Students List");
		this.btnStudentsList.addActionListener(new SelectStudentsListHandle());
		
		this.pnlCenterNewShift.add(this.lblShiftName);
		this.pnlCenterNewShift.add(this.tfShiftName);
		this.pnlCenterNewShift.add(this.lblShiftType);
		this.pnlCenterNewShift.add(this.cbShiftType);
		this.pnlCenterNewShift.add(this.lblShiftWeekday);
		this.pnlCenterNewShift.add(this.cbShiftWeekday);
		this.pnlCenterNewShift.add(this.lblShiftRoom);
		this.pnlCenterNewShift.add(this.tfShiftRoom);
		this.pnlCenterNewShift.add(this.lblShiftTime);
		this.pnlCenterNewShift.add(this.tfShiftHour);
		this.pnlCenterNewShift.add(this.lblColon);
		this.pnlCenterNewShift.add(this.tfShiftMinute);
		this.pnlCenterNewShift.add(this.lblShiftDuration);
		this.pnlCenterNewShift.add(this.tfMaxDelay);
		this.pnlCenterNewShift.add(this.btnStudentsList);
		this.pnlCenter.add(this.pnlCenterNewShift);
		
		this.getContentPane().add(this.pnlCenter);
		
		
		// Configure South panel
		this.pnlSouth = new JPanel(new FlowLayout());
		this.btnSaveCourse = new JButton("Save Course");
		this.btnCancel = new JButton("Close");
		this.btnAddShift = new JButton("Add Shift");
		this.btnEditShift = new JButton("Update Shift");
		this.btnDeleteShift = new JButton("Delete Shift");
		this.btnLoadCourseByRouterFolder = new JButton("Load Course");
		this.btnLoadCourseByURL = new JButton("Fetch Shifts From Fenix");
		this.btnSaveCourse.addActionListener(new SaveCourseHandler());
		this.btnAddShift.addActionListener(new AddShiftHandler());
		this.btnEditShift.addActionListener(new EditShiftHandler());
		this.btnCancel.addActionListener(
				new CloseCreateCourseHandler());
		this.btnDeleteShift.addActionListener(new RemoveShiftHandler());
		this.btnLoadCourseByRouterFolder.addActionListener(new LoadCourseHandler());
		this.btnLoadCourseByURL.addActionListener(new LoadCourseByURLHandler());
		this.pnlSouth.add(this.btnAddShift);
		this.pnlSouth.add(this.btnEditShift);
		this.pnlSouth.add(this.btnDeleteShift);
		this.pnlSouth.add(this.btnLoadCourseByURL);
		this.pnlSouth.add(this.btnLoadCourseByRouterFolder);
		this.pnlSouth.add(this.btnSaveCourse);
		this.pnlSouth.add(this.btnCancel);
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
	private CourseWindow() {
		super("Manage Courses");
		this.courseManager = new CourseManager();
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
	 * Method to return the CreateNewCourseWindow's instance.
	 * @return CreateNewCourseWindow's instance.
	 */
	public static CourseWindow Instance() {
		if (instance == null) {
			instance = new CourseWindow();
		}
		return instance;
	}
	
	/**
	 * Getters
	 */	
	public CourseManager getCourseManager() { return this.courseManager; }
	public TextField getShiftName() { return this.tfShiftName; }
	public JComboBox<String> getShiftType() { return this.cbShiftType; }
	public JComboBox<String> getShiftWeekday() { return this.cbShiftWeekday; }
	public TextField getCourseName() { return this.tfCourseName; }
	public TextField getShiftRoom() { return this.tfShiftRoom; }
	public TextField getShiftHour() { return this.tfShiftHour; }
	public TextField getShiftMinute() { return this.tfShiftMinute; }
	public TextField getShiftMaxDelay() { return this.tfMaxDelay; }
	public DatePicker getCourseStartDate() { return this.dpStartCourse; }
	public DatePicker getCourseEndDate() { return this.dpEndCourse; }
	public String getShiftStudentsList() { return this.shiftStudentsList; }
	public JList<Shift> getShiftsList() { return this.listShifts; }
	public TextField getProfessorID() { return this.tfProfID; }
	public JPasswordField getProfessorPassword() { return this.pfProfPassword; }
	
	/**
	 * Setters
	 */
	public void setShiftStudentsList(String listPath) 
	{ this.shiftStudentsList = listPath; }
	public void resetShiftStudentsList()
	{ this.shiftStudentsList = null; }

}
