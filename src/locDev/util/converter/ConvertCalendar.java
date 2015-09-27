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
import java.util.Date;

import locDev.types.Time;

// TODO - used? Delete? rbruno
public class ConvertCalendar {

	/**
	 * convert date from string to calendar
	 * @param date
	 * 	string instance
	 * @return
	 * 	calendar instance
	 */
	public static Calendar convert(Date date, Time begin_time) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, begin_time.getHours());
		c.set(Calendar.MINUTE, begin_time.getMinutes());
		return c;
	}
}
