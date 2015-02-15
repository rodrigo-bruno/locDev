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

import locDev.exceptions.TimeNotValidException;

public class Time {
	/**
	 * represent the hours
	 */
	private int _hours;
	/**
	 * represent the minutes
	 */
	private int _minutes;
	/**
	 * Constructor
	 * @param hours
	 * 	class begin's hours
	 * @param minutes
	 *  class begin's minutes
	 *  @throws TimeNotValidException
	 *      if hours or minutes don't have a valid value.
	 */
	public Time(int hours, int minutes) {
		if(hours < 0 || hours > 23) {
			throw new TimeNotValidException(); 
		}
		if(minutes < 0 || minutes > 59) {
			throw new TimeNotValidException();
		}
		_hours = hours;
		_minutes = minutes;
	}
	/**
	 * GETTER
	 * @return
	 * 	hours
	 */
	public int getHours() {
		return _hours;
	}
	/**
	 * GETTER
	 * @return
	 * 	hours
	 */
	public String getStringHours() {
		return "" + _hours;
	}
	/**
	 * GETTER
	 * @return
	 * minutes
	 */
	public int getMinutes() {
		return _minutes;
	}
	/**
	 * GETTER
	 * @return
	 * minutes
	 */
	public String getStringMinutes() {
		return "" + _minutes;
	}
	/**
	 * string representation
	 * @return
	 * 	string
	 */
	public String toString() {
		return _hours + "-" + _minutes;
	}
	/**
	 * string representation, but in minutes
	 */
	public String getDelayinMinutes() {
		return "" + (_hours * 60 + _minutes);
	}
}
