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
import locDev.locDevCore;
import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.exceptions.NotTheSameCourseInfoException;
import locDev.ui.AttendancesWindow;
import locDev.ui.CourseWindow;

/**
 * Class that will handle the generation of the attendance's table.
 */
public class ReadTableHandler implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// variables
		AttendancesWindow window = AttendancesWindow.Instance();
		CourseManager course = window.getCourseManager();
		String path;
		//Pop-up ask the path
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    int returnVal = chooser.showOpenDialog(window);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	path = chooser.getSelectedFile().getAbsolutePath();
	    }
	    else 
	    	return;
		//Upload Table
	    try {
			locDevCore.processStudentAttendances(path, course);
		} catch (InvalidFormatMetaInfoFileException e1) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					e1.getMessage());
			return;
		} catch (NotTheSameCourseInfoException e1) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					e1.getMessage());
			return;
		} catch (RuntimeException e1) {
			JOptionPane.showMessageDialog(
					CourseWindow.Instance(), 
					e1.getMessage());
			return;
		}
	}

}