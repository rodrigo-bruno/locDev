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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDate {

	public static Date convert(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
		try {
			Date d = sdf.parse(date);
			return d;
		} catch (ParseException e) {
			throw new RuntimeException("Date must be in this format d-M-yyyy. Received:" + date);
		}
	}
	
	public static String convert(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String d = sdf.format(date);
		return d;
	}
}
