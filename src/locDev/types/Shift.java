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

import locDev.util.converter.ConvertWeekday;

/**
 * Class representing a shift.
 *
 */
public class Shift {
	
	/**
	 * Shift name.
	 */
	private String shiftName;
	
	/**
	 * Shift type.
	 */
	private String shiftType;
	
	/**
	 * Weekday.
	 */
	private int shiftWeekday;
	
	/**
	 * Shift room.
	 */
	private String shiftRoom;
	
	/**
	 * Shift start time.
	 */
	private Time shiftStartTime;
	
	/**
	 * Shift max delay.
	 * 
	 */
	private Time shiftMaxDelay;
	
	/**
	 * Shift seed.
	 */
	private String seed;
	
	/**
	 * Path to a csv file containing a students list.
	 */
	private String studentsListPath;
	
	/**
	 * Constructor.
	 * @param name
	 *     see doc above.
	 * @param type
	 *     see doc above.
	 * @param weekday
	 *     see doc above.
	 * @param room
	 *     see doc above.
	 * @param startTime
	 *     see doc above.
	 * @param maxDelay
	 *     see doc above.
	 * @param seed
	 *     see doc above.
	 */
	public Shift(
			String name, 
			String type,
			int weekday, 
			String room,
			Time startTime,
			Time maxDelay,
			String seed,
			String studentsListPath) {
		this.shiftName = name;
		this.shiftType = type;
		this.shiftWeekday = weekday;
		this.shiftRoom = room;
		this.shiftStartTime = startTime;
		this.shiftMaxDelay = maxDelay;
		this.seed = seed;
		this.studentsListPath = studentsListPath;
	}
	
	/**
	 * Method that returns a shift string representation.
	 * @return string representation.
	 */
	@Override
	public String toString() {
		String ret = 
				"Name: " + this.shiftName + ", " + 
				"Type: " + this.shiftType + ", " +
				"Weekday: " + ConvertWeekday.convert(this.shiftWeekday) + ", " +
				"Room: " + this.shiftRoom + ", " +
				"Start Time: " + this.shiftStartTime + ", " +
				"Max Delay: " + this.shiftMaxDelay + ", " +
				"Students List: " + this.studentsListPath + ", " +
				"Seed: " + this.seed;
		return ret;
	}
	
	/**
	 * Getters
	 */
	public String getShiftName() { return this.shiftName; }
	public String getShiftType() { return this.shiftType; }
	public int getWeekday() { return this.shiftWeekday; }
	public String getRoom() { return this.shiftRoom; }
	public Time getStartTime() { return this.shiftStartTime; }
	public Time getMaxDelay() { return this.shiftMaxDelay; }
	public String getSeed() { return this.seed; }
	public String getStudentsListPath() { return this.studentsListPath; }
}
