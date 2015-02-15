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
import java.beans.PropertyVetoException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.types.Shift;
import locDev.ui.CourseWindow;
import locDev.ui.MainWindow;

/**
 * 
 */
public class LoadCourseHandler implements ActionListener{

	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
	    CourseWindow instance = CourseWindow.Instance();
	    String courseConfigPath = null;
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
	    		courseConfigPath = MainWindow.CourseConfigurationDirectory;
	    	}
	    }
	    if (courseConfigPath == null) {
	    	// ask the user for the location of the configuration directory.
	    	JFileChooser chooser = new JFileChooser();
	    	chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
	    	if(chooser.showOpenDialog(CourseWindow.Instance()) 
	    		== JFileChooser.APPROVE_OPTION) {
	    		courseConfigPath = chooser.getSelectedFile().getAbsolutePath();
	    	}  
	    	else {
	    		return;
	    	}
	    }
	    
	    MainWindow.CourseConfigurationDirectory = courseConfigPath;
	    
    	try {
    		instance.getCourseManager().load(courseConfigPath);
	    } catch (InvalidFormatMetaInfoFileException e1) {
				JOptionPane.showMessageDialog(
						instance, 
						"Error loading information (bad format).");
		} catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					ex.getMessage());
			return;
		}
	       
       // update window info.
		Shift[] shifts = {};  
	    shifts = instance.
	    		    getCourseManager().
	    		        getShifts().
	    		            values().
	    		                toArray(shifts);		
	    instance.getShiftsList().setListData(shifts);
	    
		instance.getCourseName().setText(instance.getCourseManager().getCourseName());
		instance.getProfessorID().setText(instance.getCourseManager().getProfessorID());
		try {
			instance.getCourseStartDate().setDate(
					instance.getCourseManager().getCourseStartDate());
			instance.getCourseEndDate().setDate(
					instance.getCourseManager().getCourseEndDate());
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
