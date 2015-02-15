package locDev.ui.handlers;

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import locDev.CourseManager;
import locDev.ui.CourseWindow;
import locDev.ui.MainWindow;

/**
 * Handler to perform the creation of a new course configuration directory.
 */
public class SaveCourseHandler implements ActionListener {

	/**
	 * Where the action happens.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String configPath = null;
		
		// check if the course name if filled.
		if(CourseWindow.Instance().
				getCourseName().getText().length() == 0) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"You have to specify the course name!");
			return;
		}
		
		// check if the id and password fields are filled.
		if((CourseWindow.Instance().
				getProfessorID().getText().length() == 0) ||
		   (new String(CourseWindow.Instance().
			    getProfessorPassword().getPassword()).length() == 0)) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"You must specify an ID and password!");
			return;
		}
		
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
	    		configPath = MainWindow.CourseConfigurationDirectory;
	    	}
	    }
	    if (configPath == null) {
	    	// ask the user for the location of the configuration directory.
	    	JFileChooser chooser = new JFileChooser();
	    	chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
	    	if(chooser.showOpenDialog(CourseWindow.Instance()) 
	    		== JFileChooser.APPROVE_OPTION) {
	    		configPath = chooser.getSelectedFile().getAbsolutePath();
	    	}  
	    	else {
	    		return;
	    	}
	    }
		
	    MainWindow.CourseConfigurationDirectory = configPath;
	    // Update course management information.
	    CourseManager cm = CourseWindow.Instance().getCourseManager(); 
	    cm.setCourseName(
	    		CourseWindow.Instance().getCourseName().getText());
	    cm.setCourseStartDate(
	    		CourseWindow.Instance().getCourseStartDate().getDate());
	    cm.setCourseEndDate(
	    		CourseWindow.Instance().getCourseEndDate().getDate());
	    cm.setProfessorID(
	    		CourseWindow.Instance().getProfessorID().getText());
	    cm.setProfessorPassword(
	    		new String(
	    				CourseWindow.Instance().getProfessorPassword().getPassword()));
	    
	    // Save course management information.
	    try {
	    	cm.save(configPath);
	    } catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					ex.getMessage());
			return;
	    }
	}

}