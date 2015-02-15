package locDev.types;

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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class StudentAttendance {
	/**
	 * number of presences
	 */
	private int nb_presences;
	/**
	 * max presences of a shift
	 */
	private int shift_max_presences;
	/**
	 * student name
	 */
	private String _name;
	/**
	 * student id
	 */
	private String _number;
	/**
	 * dates of all shifts he went
	 */
	private Set<String> _presence_dates;
	/**
	 * Constructor
	 * @param number
	 * 	student id
	 * @param name
	 * 	student name
	 */
	public StudentAttendance(String number,String name, int max) {
		_name = name;
		_number = number;
		nb_presences = 0;
		_presence_dates = new TreeSet<String>();
		shift_max_presences = max;
	}
	
	public StudentAttendance(String number, String name) {
		_name = name;
		_number = number;
		nb_presences = 0;
		_presence_dates = new TreeSet<String>();
		shift_max_presences = -1;
	}
	
	/**
	 * add one more date if he doesn't have
	 * @param date
	 * 	string that represent the class
	 */
	public void addPresence(String date) {
		if(_presence_dates.contains(date))
			return;
		nb_presences++;
		_presence_dates.add(date);
	}
	
	/**
	 * GETTER
	 * @return
	 * 	name
	 */
	public String getName() {
		return _name;
	}
	/**
	 * GETTER
	 * @return
	 * 	number
	 */
	public String getNumber() {
		return _number;
	}
	/**
	 * GETTER
	 * @return
	 * 	all dates
	 */
	public Set<String> getAttendanceDates() {
		return _presence_dates;
	}
	/**
	 * GETTER
	 * @return
	 * 	int
	 */
	public int getShiftMax() {
		return shift_max_presences;
	}
	/**
	 * GETTER
	 * @param shift_max
	 * 	add the shift max
	 */
	public void addShiftMax(int shift_max) {
		shift_max_presences += shift_max;
	}
	/**
	 * GETTER
	 * @return
	 * 	int
	 */
	public int getNbAttendance() {
		return nb_presences;
	}
	/**
	 * string representation
	 * @return
	 * 	string
	 */
	public String toString() {
		String s = new String();
		s += "Number of Attendances : " + nb_presences + "\n";
		Iterator<String> it= _presence_dates.iterator();
        while(it.hasNext())
        {
          String value=(String)it.next();
          s += "Date: " + value + "\n";
        }
        return s;
	}
}
