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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import locDev.types.*;

public class ProcessRouterOutput {

	/**
	 * receive the list of shifts to load. Save for each student the classes they went
	 * @param shifts
	 * 	list with shift's information
	 * @param students
	 * 	dictionary to add the presences
	 * @return
	 * 	return the dictionary updated
	 */
	public static Map<String, StudentAttendance> readShiftsAttendance(String path, List<Shift> shifts, Map<String,StudentAttendance> students) {
		path = path + "/shifts";
		for(Shift shift : shifts) {
			try {
				readShiftAttendance(new File(path + "/" + shift.getShiftName()), students);
			} catch (IOException e) {
				throw new RuntimeException("ERROR - ProcessOutput - generateTable - Try to read folder " + shift);
			}
		}
		return students;
	}
	/**
	 * read all files in the shift folder and load the information :
	 *  saving the presences in the dictionary
	 * @param shift_folder
	 * 	File object to represent the shift folder
	 * @throws IOException
	 * 	throw if can't read a file
	 */
	public static void readShiftAttendance(final File shift_folder, Map<String,StudentAttendance> students) throws IOException {
		for (final File dateEntry : shift_folder.listFiles()) {
			if (dateEntry.isFile()) {
	            continue;
	        } else {
	        	String tmp_path = dateEntry.getAbsolutePath() + "/attendances.dat";
	        	if(!new File(tmp_path).exists())
	        		continue;
	        	Set<String> set = readDateAttendance(tmp_path);
        		String name = dateEntry.getName();
        		name = name.split("/.")[0];
	        	for(String number : set) {
	        		if(number.contains("ist1")) { number = number.substring(4); }
	        		else if(number.contains("ist")) { number = number.substring(3); }
	        		// Test is student number appears in attendances.dat
	        		if(!students.containsKey(number))
	        			continue;
	        		//add shift to student presence
	        		students.get(number).addPresence(name);
	        	}
	        }
	    }
	}
	/**
	 * read the file and create a set with all students' numbers that went to that shift in that date
	 * @param path
	 * 	path to a specific date to load
	 * @return
	 * 	return a set with all students' numbers
	 * @throws IOException
	 * 	throw if can't read a file
	 */
	public static Set<String> readDateAttendance(String path) throws IOException {
		Set<String> set = new TreeSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = "";
		while((line = reader.readLine()) != null) {
			for(String number : line.split(";")) {
				set.add(number.trim());
			}
		}
		reader.close();
		return set;
	}
	/**
	 * receive a dictionary and print it
	 * @param students
	 * 	dictionary Key: String | Value: StudentAttendance
	 */
	public static void printDictionary( Map<String,StudentAttendance> students) {
        Set<String> keys =students.keySet();
        for(String key : keys)
        {
          System.out.println("Number :" + key);
          System.out.println(students.get(key));
        }
	}

}
