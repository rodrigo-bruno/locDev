package locDev.util;

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

import java.util.Comparator;

public class DateComparator implements Comparator<String> {

	@Override
	public int compare(String date1, String date2) {
		int[] d1 = convertStringtoInt(date1);
		int[] d2 = convertStringtoInt(date2);
		for(int i = 0; i < 5; i++) {
			if(d1[i] != d2[i]) {
				return d1[i] - d2[i];
			}
		}
		return 0;
	}

	private int[] convertStringtoInt(String date) {
		String[] tokens = date.split("-");
		int[] number = new int[5];
		if(tokens.length != 5)
			throw new RuntimeException("Comparator: Incorrect date format! date:" + date);
		int i = 0;
		for(String t : tokens) {
			number[i] = Integer.parseInt(t);
			i++;
		}
		return number;
	}
	
}
