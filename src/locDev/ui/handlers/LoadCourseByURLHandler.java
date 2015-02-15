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

import javax.swing.JOptionPane;

import locDev.CourseManager;
import locDev.locDevCore;
import locDev.types.Shift;
import locDev.ui.CourseWindow;

/**
 * 
 */
public class LoadCourseByURLHandler implements ActionListener{

	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
	    CourseWindow instance = CourseWindow.Instance();
	    CourseManager course = instance.getCourseManager();
	    
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

		//Set Meta info of course
		course.setCourseName(
	    		CourseWindow.Instance().getCourseName().getText());
		course.setCourseStartDate(
	    		CourseWindow.Instance().getCourseStartDate().getDate());
		course.setCourseEndDate(
	    		CourseWindow.Instance().getCourseEndDate().getDate());
		course.setProfessorID(
	    		CourseWindow.Instance().getProfessorID().getText());
		course.setProfessorPassword(
	    		new String(
	    				CourseWindow.Instance().getProfessorPassword().getPassword()));
	    
    	//Get Path
	    String home_url = JOptionPane.showInputDialog(null, "Enter the URL to course's home page : ", "", 1);
	    if(home_url == null)
	    	return;
	    try{
	    	locDevCore.processCourseURL(course, home_url);
	    } catch(RuntimeException e) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					e.getMessage());
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
	}
}
