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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import locDev.CourseManager;
import locDev.locDevCore;
import locDev.types.Shift;
import locDev.types.StudentAttendance;
import locDev.ui.AttendancesWindow;
import locDev.ui.CourseWindow;

/**
 * Class that will handle the generation of the attendance's table.
 */
public class GenerateTableHandler implements ActionListener {

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// variables
		AttendancesWindow window = AttendancesWindow.Instance();
		CourseManager course = window.getCourseManager();
		String path;
		List<Shift> shifts = new LinkedList<Shift>();
		//Popup ask the path
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int returnVal = chooser.showOpenDialog(window);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	path = chooser.getSelectedFile().getAbsolutePath();
	    }
	    else 
	    	return;
		//Get selected shifts
	    for (Component c : window.getCheckBoxes()) {
	        if (c instanceof JCheckBox && ((JCheckBox)c).isSelected()) { 
	        	String name = ((JCheckBox)c).getText();
	        	shifts.add(course.getShifts().get(name));
	        }
	    }
	    //Process selected shifts
	    Map<String,StudentAttendance> students = locDevCore.processShifts(course, shifts);
		//Write table
	    try {
	    	locDevCore.generateAttendanceCSV(course, path, students, shifts);	    	
	    } catch (RuntimeException ex) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					ex.getMessage());
			return;
	    }
	}

}