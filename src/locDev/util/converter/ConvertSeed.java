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

import locDev.CourseManager;

public class ConvertSeed {

	public static String convert(int seed) {
		switch(CourseManager.SHIFT_SEED_BASE) {
		case 2:
			return Integer.toBinaryString(seed);
		case 8:
			return Integer.toOctalString(seed);
		case 16:
			return Integer.toHexString(seed);
		default:
			return Integer.toString(seed);
		}
	}
	
}
