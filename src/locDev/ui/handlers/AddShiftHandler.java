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

import locDev.exceptions.ShiftAlreadyExistsException;
import locDev.exceptions.TimeNotValidException;
import locDev.types.Shift;
import locDev.types.Time;
import locDev.ui.CourseWindow;
import locDev.util.converter.ConvertWeekday;

/**
 * Handler triggered when the user adds a new shift.
 */
public class AddShiftHandler implements ActionListener {

	/**
	 * Where the action happens.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		CourseWindow instance = CourseWindow.Instance();
		
		if(!checkFieldsFilled(instance))
			return;
			
		// check shift start time
		Time start, delay;
		try {
			start = new Time(
					new Integer(instance.getShiftHour().getText()), 
					new Integer(instance.getShiftMinute().getText()));
		} catch(TimeNotValidException e) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"Time not valid!");
			return;
		}
		// check shift maximum delay
		try {
			int minutes = new Integer(instance.getShiftMaxDelay().getText());
			int hours = minutes/60;
			minutes = minutes%60;
			delay = new Time(hours, minutes);
		} catch(TimeNotValidException e) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"Maximum delay not valid!");
			return;
		}
		// add shift.
		try {

			instance.getCourseManager().AddNewShift(
					instance.getShiftName().getText(), 
					(String)instance.getShiftType().getSelectedItem(), 
					ConvertWeekday.convert(
							(String)instance.
							    getShiftWeekday().
							        getSelectedItem()), 
					instance.getShiftRoom().getText(),
					start, 
					delay,
					instance.getShiftStudentsList());
		} catch(ShiftAlreadyExistsException e) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"Shift with the same name already exists!");
			return;
		} catch(RuntimeException e) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					e.getMessage());
			return;
		}
		
		// add new shift to the JList
		Shift[] shifts = {};  
	    shifts = instance.getCourseManager().getShifts().values().toArray(
	    		shifts);		
		instance.getShiftsList().setListData(shifts);
		
		instance.resetShiftStudentsList();
		
	}
	
	public static boolean checkFieldsFilled(CourseWindow instance) {
		// check if the user filled all text fields
		if(instance.getShiftName().getText().length() == 0 ||
		   instance.getShiftRoom().getText().length() == 0 ||
		   instance.getShiftHour().getText().length() == 0 ||
		   instance.getShiftMinute().getText().length() == 0 ||
		   instance.getShiftMaxDelay().getText().length() == 0) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"All fields must be filled in order to create a shift!");
			return false;
		}
				
		// check if the user identified a list of students.
		if(instance.getShiftStudentsList() == null) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					"You must upload a student's list for this shift!");
			return false;
		}
		
		return true;
	}
}
