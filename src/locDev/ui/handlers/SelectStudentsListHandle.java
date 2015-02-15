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

import locDev.ui.CourseWindow;

/**
 * Handler to triggered when the user selects a student's list.
 */
public class SelectStudentsListHandle implements ActionListener {

	/**
	 * Where the action happens.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		CourseWindow instance = CourseWindow.Instance();
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode( JFileChooser.FILES_ONLY);
	    int returnVal = chooser.showOpenDialog(instance);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       instance.setShiftStudentsList(
	    		   chooser.getSelectedFile().getAbsolutePath());
	    }
	}
}