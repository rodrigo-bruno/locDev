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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import locDev.exceptions.ShiftTypeNotValidException;

public class ConvertShiftType {

	public static Map<String, String> types =
			Collections.unmodifiableMap(
                    /* All types */ 
                    new HashMap<String, String>() {
						private static final long serialVersionUID = 1L;
						{
                			put("T","Theoretical");
                			put("P","Practical");
                			put("TP","Theoretical and practical");
                			put("L","Laboratory");
                			put("TC","Fieldwork");
                			put("Pb","Problems");
                			put("S","Seminars");
                			put("E","Internship");
                			put("OT","Tutorship Orientation");
                		}
                    });
	
	public static String convert(String acronym) {
		return ConvertShiftType.types.get(acronym);
	}
	
	
	public static int convertString(String complete_name) {
		int ret = 
			complete_name.equals("Theoretical") ? 0 : 
			complete_name.equals("Practical") ? 1 :
			complete_name.equals("Theoretical and practical") ? 2 : 
			complete_name.equals("Laboratory") ? 3 :
			complete_name.equals("Fieldwork") ? 4 :
			complete_name.equals("Problems") ? 5 :
			complete_name.equals("Seminars") ? 6 :
			complete_name.equals("Internship") ? 7 :
			complete_name.equals("Tutorship Orientation") ? 8 :
			-1;
		if (ret == -1) { throw new ShiftTypeNotValidException(); }
		return ret;
	}
}
