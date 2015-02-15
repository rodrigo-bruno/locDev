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

import locDev.ui.PinsWindow;

/**
 * Class that will handle the close of the student's attendances window. 
 */
public class ClosePinsHandler implements ActionListener {

	/**
	 * Method that does the job.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		PinsWindow.Instance().dispose();
	}

}