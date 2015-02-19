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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import locDev.CourseManager;
import locDev.util.DateComparator;
import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.exceptions.NotTheSameCourseInfoException;
import locDev.types.Shift;
import locDev.types.StudentAttendance;
import locDev.types.Time;
import locDev.util.FileOperations;
import locDev.util.ProcessRouterOutput;
import locDev.util.converter.ConvertCalendar;
import locDev.util.converter.ConvertDate;

public class locDevCore {

	/**
	 * This read all the presence files of the given shifts and 
	 * return a structure with that information
	 * @param course
	 * 	class with course information
	 * @param shifts
	 * 	selected shifts
	 * @return
	 * 	students stucture with the presence information
	 */
	public static Map<String, StudentAttendance> processShifts(CourseManager course, List<Shift> shifts) {
		Map<String,StudentAttendance> map = new HashMap<String,StudentAttendance>();
		//Load all students from the shifts
		for(Shift s : shifts) {
			String path = course.getCourseConfigurationDirectoryPath() 
					    + "/course/shifts/"
					    + s.getShiftName() + "/"
					    + s.getStudentsListPath();
			FileOperations.readStudentList(path, map);
		}
		//process the folders of the router
		ProcessRouterOutput.readShiftsAttendance(course.getCourseConfigurationDirectoryPath() + "/course", shifts, map);
		return map;
	}	
	/**
	 * Upload a attendance csv file and save the presences in the
	 * persistent structure. 
	 * @param path
	 * 	path to the csv file
	 * @param course
	 * 	class with course information
	 * @throws InvalidFormatMetaInfoFileException
	 * @throws NotTheSameCourseInfoException
	 */
	// FIXME - needed? main -> attendances -> upload students attendance info.
	public static void processStudentAttendances(String path, CourseManager course) 
			throws InvalidFormatMetaInfoFileException, NotTheSameCourseInfoException {
		String config_path = course.getCourseConfigurationDirectoryPath();
		config_path += "/course/shifts";
		Map<String,StudentAttendance> students = FileOperations.readAttendanceCSV(path, course);
		for(Shift shift : course.getShifts().values()) {
			String shift_path = config_path + "/" + shift.getShiftName();
			Set<String> dates = generateDatesofShift(course.getCourseStartDate(),
													 course.getCourseEndDate(),
													 shift.getWeekday(),
													 shift.getStartTime());
			for(String date : dates) {
				Set<String> students_went = new TreeSet<String>();
				String date_path = shift_path + "/" + date + "/attendances.dat";
				try {
					if(new File(date_path).exists()) {						
						BufferedReader reader = new BufferedReader(new FileReader(new File(date_path)));
						String line;
						while((line = reader.readLine()) != null) {
							String[] tokens = line.split(";"); // FIXME
							for(String token : tokens)
								students_went.add(token);
						}
						reader.close();
					}
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(date_path, true)));
					for(StudentAttendance student : students.values()) {
						if(student.getAttendanceDates().contains(date))
								out.print(";" + student.getNumber());
						continue;
					}
					out.close();
				} catch (IOException e) {
					    throw new RuntimeException("I/O problems in file:" + date_path);
				}
			}
		}
	}

	/**
	 * With the course information and the students' presence structure
	 * this function generate the attendance csv
	 * @param course
	 *  class with course information
	 * @param dest_path
	 *  path to the destination for the generated file
	 * @param students
	 *  students' structure
	 * @param shifts
	 * 	selected shifts
	 */
	public static void generateAttendanceCSV(CourseManager course, String dest_path, Map<String, StudentAttendance> students, List<Shift> shifts) {
		String content = FileOperations.generateAttendanceCSV(course, shifts, students);
		BufferedWriter writer;
		dest_path += "/" + course.getCourseName();
		for(Shift s : shifts) {
			dest_path += "-" + s.getShiftName();
		}
		dest_path += ".csv";
		try {
			writer = new BufferedWriter(new FileWriter(new File(dest_path)));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("I/O errors. Trying to write csv attendance file.");
		}
	}
	/**
	 * This generate a table with all the pins to use in all dates 
	 * of that shift
	 * @param course
	 * 	class with course information
	 * @param dest_path
	 *  path to the generated csv
	 * @param shift
	 *  shift selected
	 */
	public static void generatePinCSV(CourseManager course, String dest_path, Shift shift) {
		String content = FileOperations.generateShiftPinsCSV(course, shift);
		BufferedWriter writer;
		dest_path += "/" + course.getCourseName();
		dest_path += "-" + shift.getShiftName();
		dest_path += "-pins"; 
		dest_path += ".csv";
		try {
			writer = new BufferedWriter(new FileWriter(new File(dest_path)));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("I/O errors. Trying to write csv attendance file.");
		}
	}
	/**
	 * This will create all the structure for the course.
	 * Including the folders, meta-info file and students' lists
	 * for each shift
	 * @param path
	 * @param old_path
	 * @param course
	 */
	public static void generateRouterMetaInfo(String path, String old_path, CourseManager course) {
		//First Level
		new File(path + "/course").mkdir();
		//Second Level
		new File(path + "/course/shifts").mkdir();
		String content = FileOperations.generateMetaInfoFile(course);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + "/course/meta-info")));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("I/O errors. Trying to write csv attendance file.");
		}
		path = path + "/course/shifts/";
		old_path = old_path + "/course/shifts/";
		//Third Level
		for(Shift shift : course.getShifts().values()) {
			String tmp_path = path + shift.getShiftName();
			new File(tmp_path).mkdir();
			Set<String> dates = locDevCore.generateDatesofShift(
					course.getCourseStartDate(), 
					course.getCourseEndDate(), 
					shift.getWeekday(),
					shift.getStartTime());
			//Fourth Level
			String[] tokens = shift.getStudentsListPath().split("[/|\\\\]"); // TODO - shouln't it be just a file copy?
			if(tokens.length != 1) {
				FileOperations.generateStudentListCSV(
						shift.getStudentsListPath(), 
						tmp_path + "/" + tokens[tokens.length - 1], 
						dates.size());
			}
			else { 
				FileOperations.generateStudentListCSV(
						old_path + shift.getShiftName() + "/" + tokens[tokens.length - 1], 
						tmp_path + "/" + tokens[tokens.length - 1],
						dates.size());
			}
			
			for(String date : dates) {
				new File(tmp_path + "/" + date).mkdir();
				//Fifth Level
				new File(tmp_path + "/" + date + "/macs").mkdir();
			}
		}
	}
	/**
	 * This create a set with all the dates of a shift between the 
	 * course's start date and course's end date
	 * @param course
	 * @param shifts
	 * @return
	 */
	public static Set<String> generateDatesofShifts(CourseManager course, List<Shift> shifts) {
		Set<String> set = new TreeSet<String>(new DateComparator());
		for(Shift s : shifts) {
			set.addAll(generateDatesofShift(course.getCourseStartDate(), course.getCourseEndDate(), s.getWeekday(), s.getStartTime()));
		}
		return set;
	}
	
	/**
	 * from the begin_date until the end_date will find all dates for the week_day and
	 * save them in a set
	 * @param begin_date
	 * 	first day(can be before, like class is Tuesday and give first day Monday)
	 * @param end_date
	 * 	last day 
	 * @param week_day
	 * 	the day of the week
	 * @return
	 * 	set with all dates
	 */
	public static Set<String> generateDatesofShift(Date begin_date, Date end_date, int week_day, Time begin_time) {
		//init variables
		Calendar begin = ConvertCalendar.convert(begin_date, begin_time);
		Calendar end = ConvertCalendar.convert(end_date, begin_time);
		Set<String> set = new TreeSet<String>();
		//save all dates between begin and end
		//	1 - get the first weekday from begin date
		int weekday = begin.get(Calendar.DAY_OF_WEEK);  
		if (weekday != week_day) {
			//difference between saturday and week day we want
			int diff = (Calendar.SATURDAY + week_day) % 7;
			//calculate the days left
			int days = (Calendar.SATURDAY - weekday + diff) % 7;  
			//add
			begin.add(Calendar.DAY_OF_YEAR, days); 
		}
		//	2 - save each weekday until reach end date
		while (begin.before(end) || begin.equals(end)) {
			String s_date = ConvertDate.convert(begin.getTime(), "d-M-yyyy-H-m");
			set.add(s_date);
			begin.add(Calendar.DAY_OF_YEAR, 7);
		}
		return set;
	}
}