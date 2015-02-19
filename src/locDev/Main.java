package locDev;

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

import java.util.List;
import java.util.Map;

import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.exceptions.NotTheSameCourseInfoException;
import locDev.types.Shift;
import locDev.types.StudentAttendance;
import locDev.util.FileOperations;
import locDev.util.ProcessRouterOutput;
import java.util.LinkedList;
//For the commented test
//import locDev.util.converter.ConvertDate;

/**
 * Main class. Entry point of the application.
 */
public class Main {

	/**
	 * Application entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		// Force the creation of the MainWindow
		locDev.ui.MainWindow.Instance();
		
		//Test giving the path manually
//		noGraphicalTests("C:/TesteHTML/NewPages/www2");
		
	}

	public static void nonGraphicalTests(String path) {
		//-Test the load-
		CourseManager course = new CourseManager(); 
		TestLoadMetaInfo(path, course);
		String s = FileOperations.generateShiftPinsCSV(course, course.getShifts().get("T01"));
		System.out.println(s);
		//--Test only the string MetaInfo--
		TestWriteMetaInfo(course);
		
		//---Test the creation of all meta info and folders---
		TestSaveCourse(path, course);
		
		//----Test Process shifts attendances----
		List<Shift> shifts = new LinkedList<Shift>();
		shifts.add(course.getShifts().get("T01"));
		shifts.add(course.getShifts().get("T02"));
		shifts.add(course.getShifts().get("L01"));
		Map<String,StudentAttendance> students = TestProcessStudentList(course,shifts);
		ProcessRouterOutput.printDictionary(students);
		
		//-----Test generation of attendances csv-----
		TestGenerateCSVTable(course, students, shifts);
		
		//------Test generation of pins csv -------
		shifts = new LinkedList<Shift>();
		shifts.add(course.getShifts().get("T01"));
		shifts.add(course.getShifts().get("T02"));
		TestGeneratePinCSV(course, shifts);
		
		//------Test read attendance csv ------
		// only the file
		TestReadAttendanceCSV("C:/TesteHTML/NewPages/Computacao Movel-T01.csv", course);
		// read the file and write the new presences
		TestProcessReadAttendanceCSV("C:/TesteHTML/NewPages/Computacao Movel-T01.csv", course);
		
//		CourseManager course = new CourseManager();
//		course.setCourseName("CM");
//		course.setCourseStartDate(ConvertDate.convert("01-05-2013"));
//		course.setCourseEndDate(ConvertDate.convert("01-06-2013"));
//		TestLoadByURL(course, "https://fenix.ist.utl.pt/disciplinas/cmov764/2012-2013/2-semestre");
//		TestSaveCourse("C:/TesteHTML/images", course);

	}

	public static void TestProcessReadAttendanceCSV(String path, CourseManager course) {
		try {
			locDevCore.processStudentAttendances(path, course);
		} catch (InvalidFormatMetaInfoFileException e) {
			e.printStackTrace();
		} catch (NotTheSameCourseInfoException e) {
			e.printStackTrace();
		}
	}
	
	public static void TestReadAttendanceCSV(String path, CourseManager course) {
		try {
			Map<String,StudentAttendance> set = FileOperations.readAttendanceCSV(path, course);
			ProcessRouterOutput.printDictionary(set);
		} catch (InvalidFormatMetaInfoFileException e) {
			e.printStackTrace();
		} catch (NotTheSameCourseInfoException e) {
			e.printStackTrace();
		}
	}
	
	public static void TestSaveCourse(String path, CourseManager course) {
		course.save(path);
	}
	
	public static void TestGeneratePinCSV(CourseManager course,List<Shift> shifts) {
		for(Shift shift : shifts) {
			String s = FileOperations.generateShiftPinsCSV(course, course.getShifts().get(shift.getShiftName()));
			System.out.println(s);
		}
	}
	
	public static void TestLoadMetaInfo(String path, CourseManager course) {
		try {
			course.load(path);
			System.out.println(course.getCourseName());
			System.out.println(course.getCourseStartDate());
			System.out.println(course.getCourseEndDate());
			for(Shift s : course.getShifts().values()) {
				System.out.println(s.getShiftName());
			}
		} catch (InvalidFormatMetaInfoFileException e) {
			System.out.println("Warning not correct file.");
		}		
	}
	
	public static void TestWriteMetaInfo(CourseManager course) {
		String s = FileOperations.generateMetaInfoFile(course);
		System.out.println(s);
	}
	
	public static Map<String,StudentAttendance> TestProcessStudentList(CourseManager course, List<Shift> shifts) {
		return locDevCore.processShifts(course, shifts);
	}
	
	public static void TestGenerateCSVTable(CourseManager course, 
			                                Map<String,StudentAttendance> students,
			                                List<Shift> shifts) {
		String s = FileOperations.generateAttendanceCSV(course, shifts, students);
		System.out.println(s);
	}
}
