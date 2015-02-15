package locDev.util.converter;

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

import java.util.Calendar;

import locDev.exceptions.WeekdayNotValidException;

public class ConvertWeekday {
	
	public static int convertFenix(String weekday) 
			throws WeekdayNotValidException {
		int ret = 
				weekday.equals("weekday0") ? Calendar.MONDAY :
				weekday.equals("weekday1") ? Calendar.TUESDAY :
				weekday.equals("weekday2") ? Calendar.WEDNESDAY :
				weekday.equals("weekday3") ? Calendar.THURSDAY :
				weekday.equals("weekday4") ? Calendar.FRIDAY :
				weekday.equals("weekday5") ? Calendar.SATURDAY :
				-1;
		if(ret == -1) { throw new WeekdayNotValidException(); }
		return ret;
	}
	
	/**
	 * Converts weekday from String representation to Calendar representation.
	 * @param weekday 
	 *     the string representation
	 * @return int
	 *     the calendar representation
	 * @throws WeekdayNotValidException
	 *     if the input is not valid.
	 */
	public static int convert(String weekday) 
			throws WeekdayNotValidException {
		int ret = 
				weekday.equals("Monday") ? Calendar.MONDAY :
				weekday.equals("Tuesday") ? Calendar.TUESDAY :
				weekday.equals("Wednesday") ? Calendar.WEDNESDAY :
				weekday.equals("Thursday") ? Calendar.THURSDAY :
				weekday.equals("Friday") ? Calendar.FRIDAY :
				weekday.equals("Saturday") ? Calendar.SATURDAY :
				-1;
		if(ret == -1) { throw new WeekdayNotValidException(); }
		return ret;
	}
	
	/**
	 * Converts weekday from Calendar representation to String representation.
	 * @param weekday
	 *     the calendar representation
	 * @return String
	 *     the string representation
	 * @throws WeekdayNotValidException
	 *     if the input is not valid.
	 */
	public static String convert(int weekday) 
			throws WeekdayNotValidException {
		switch(weekday) {
		case Calendar.MONDAY:
			return "Monday";
		case Calendar.TUESDAY:
			return "Tuesday";
		case Calendar.WEDNESDAY:
			return "Wednesday";
		case Calendar.THURSDAY:
			return "Thursday";
		case Calendar.FRIDAY:
			return "Friday";
		case Calendar.SATURDAY:
			return "Saturday";
		default:
			throw new WeekdayNotValidException(); 
		}
	}

}
