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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import locDev.types.Shift;
import locDev.ui.CourseWindow;
import locDev.util.converter.ConvertShiftType;

public class ListShiftsHandler implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		CourseWindow instance = CourseWindow.Instance();
		Shift shift = instance.getShiftsList().getSelectedValue();
		if(shift == null)
			return;
		//Update the text fields
		instance.getShiftType().setSelectedIndex(ConvertShiftType.convertString(shift.getShiftType()));
		instance.getShiftWeekday().setSelectedIndex((shift.getWeekday() + 5) % 7);
		instance.getShiftName().setText(shift.getShiftName());
		instance.getShiftRoom().setText(shift.getRoom());
		instance.getShiftHour().setText(shift.getStartTime().getStringHours());
		instance.getShiftMinute().setText(shift.getStartTime().getStringMinutes());
		instance.getShiftMaxDelay().setText(shift.getMaxDelay().getDelayinMinutes());
		instance.setShiftStudentsList(shift.getStudentsListPath());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
	}

}
