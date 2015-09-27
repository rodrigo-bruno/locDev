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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import locDev.CourseManager;
import locDev.locDevCore;
import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.exceptions.NotTheSameCourseInfoException;
import locDev.types.Shift;
import locDev.types.StudentAttendance;
import locDev.types.Time;
import locDev.util.converter.ConvertDate;
import locDev.util.converter.ConvertWeekday;

public class FileOperations {

	private static String CSV_SEPARATOR = ",";
	private static String ROUTER_INFO_SEPARATOR = ";";
	
	/**
	 * load the student list.
	 * Expected format:
	 * ID,Name,Max
	 * 67074,Rodrigo Bruno,20
	 * ...
	 * @param path
	 * 	path until the csv file
	 */
	public static void readStudentList(String path, Map<String,StudentAttendance> students) {
		BufferedReader reader = null;
		try {
			//variables to read the file
			reader = new BufferedReader(new FileReader(new File(path)));
			String line = reader.readLine();
			String[] tokens = line.split(CSV_SEPARATOR);
			//verify if the file have the same number columns we expect
			if(tokens.length != 3) {
				throw new IOException();
			}
			//read each line until reach the end of the table
			while((line = reader.readLine()) != null) {
				readStudentLine(line, students);
			}
		} catch (IOException e) {
			throw new RuntimeException("ERROR: readStudentList : I/O errors for this file:" + path);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException("ERROR: readStudentList : error closing file file:" + path);
			}
		}
	}
	
	public static void readStudentLine(String line, Map<String,StudentAttendance> students) {
		String[] tokens = line.split(CSV_SEPARATOR);
		
		if(tokens.length != 3) { 
			new IOException(); 
		}
		if (!students.containsKey(tokens[0])) {
			students.put(tokens[0], new StudentAttendance(tokens[0], tokens[1], Integer.parseInt(tokens[2])));
		}
	}
	
	public static String generateShiftPinsCSV(CourseManager course, Shift shift) {
		List<Shift> list = new LinkedList<Shift>();
		list.add(shift);
		Set<String> dates = locDevCore.generateDatesofShifts(course, list);
		StringBuilder table = new StringBuilder();
		table.append(generateShiftPinsHeader(course, shift));
		table.append(generateShiftPinsRows(shift, dates));
		return table.toString();
	}
	
	public static String generateShiftPinsHeader(CourseManager course,Shift shift) {
		StringBuilder header = new StringBuilder();
		header.append("Course Name");
		header.append(CSV_SEPARATOR);
		header.append(course.getCourseName());
		header.append("\n");
		header.append("Shift");
		header.append(CSV_SEPARATOR);
		header.append(shift.getShiftName());
		header.append("\n");
		header.append("Date");
		header.append(CSV_SEPARATOR);
		header.append("Pin\n");
		return header.toString();
	}
	
	public static String generateShiftPinsRows(Shift shift, Set<String> dates) {
		StringBuilder rows = new StringBuilder();
		for(String date : dates) {
			rows.append(generateShiftPinsRow(shift, date));
		}
		return rows.toString();
	}
	
	public static String generateShiftPinsRow(Shift shift, String date) {
		StringBuilder row = new StringBuilder();
		StringBuilder content = new StringBuilder();
		content.append(shift.getShiftName());
		content.append(shift.getSeed());
		// Example date: 2015-9-24-16-0 (year,month,day,hour,minute)
		String[] sdate = date.split("-");
		content.append(sdate[2]);
		content.append(sdate[1]);
		content.append(sdate[0]);
		
		row.append(date);
		row.append(CSV_SEPARATOR);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(content.toString().getBytes());
			byte[] hashMd4 = md.digest();
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < 2; i++) {
				byte b = hashMd4[i];
			    result.append(String.format("%02X", b));
			}
			row.append(result.toString());
			row.append("\n");
			return row.toString();
		} catch (NoSuchAlgorithmException e) {
			//Stupid Exception..
			throw new RuntimeException("Doesn't exist such algorithm.");
		}
	}
	
	public static String generateAttendanceCSV(CourseManager course, List<Shift> shifts, Map<String,StudentAttendance> students) {
		Set<String> dates = locDevCore.generateDatesofShifts(course, shifts);
		StringBuilder table = new StringBuilder();
		table.append(generateAttendanceCSVHeader(course, dates));
		table.append(generateAttendanceCSVRows(dates, students));
		return table.toString();
	}
	
	public static String generateAttendanceCSVHeader(CourseManager course, Set<String> dates) {
		StringBuilder header = new StringBuilder();
		header.append("Course Name" + CSV_SEPARATOR);
		header.append(course.getCourseName() + "\n");
		header.append("Number" + CSV_SEPARATOR);
		header.append("Name" + CSV_SEPARATOR);
		header.append("Max#Presences" + CSV_SEPARATOR);
		header.append("#Presences"+ CSV_SEPARATOR);
		header.append("%Presences" + CSV_SEPARATOR);
		for(String d : dates) {
			header.append(d);
			header.append(CSV_SEPARATOR);
		}
		header.append("\n");
		return header.toString();
	}
	
	public static String generateAttendanceCSVRows(Set<String> dates, Map<String,StudentAttendance> students) {
		StringBuilder rows = new StringBuilder();
		Set<String> numbers = students.keySet();
		Set<String> ordered_numbers = new TreeSet<String>();
		ordered_numbers.addAll(numbers);
		for(String number : ordered_numbers) {
			rows.append(generateAttendanceCSVRow(dates, students.get(number)));
		}
		return rows.toString();
	}
	
	public static String generateAttendanceCSVRow(Set<String> dates, StudentAttendance student) {
		StringBuilder row = new StringBuilder();
		double d = new Double(student.getNbAttendance()) / student.getShiftMax();
		DecimalFormat df = new DecimalFormat("##.##");
		Set<String> student_dates = student.getAttendanceDates();
		row.append(student.getNumber());
		row.append(CSV_SEPARATOR);
		row.append(student.getName());
		row.append(CSV_SEPARATOR);
		row.append(student.getShiftMax());
		row.append(CSV_SEPARATOR);
		row.append(student.getNbAttendance());
		row.append(CSV_SEPARATOR);
		row.append(df.format(d));
		row.append(CSV_SEPARATOR);
		for(String date : dates) {
			if(student_dates.contains(date))
				row.append("1" + CSV_SEPARATOR);
			else 
				row.append("0" + CSV_SEPARATOR);
		}
		row.append("\n");
		return row.toString();
	}
	
	public static String generateMetaInfoFile(CourseManager course) {
		StringBuilder file = new StringBuilder();
		file.append(generateMetaInfoHeader(course));
		file.append(generateMetaInfoShifts(course.getShifts()));
		return file.toString();
	}
	
	public static String generateMetaInfoHeader(CourseManager course) {
		StringBuilder header = new StringBuilder();
		header.append(course.getCourseName()); 
		header.append(ROUTER_INFO_SEPARATOR);
		header.append(ConvertDate.convert(course.getCourseStartDate(), "d-M-yyyy"));
		header.append(ROUTER_INFO_SEPARATOR);
		header.append(ConvertDate.convert(course.getCourseEndDate(), "d-M-yyyy"));
		header.append(ROUTER_INFO_SEPARATOR);
		header.append(course.getProfessorID());
		header.append(ROUTER_INFO_SEPARATOR);
		header.append(course.getProfessorPassword());
		header.append("\n");
		return header.toString();
	}
	
	public static String generateMetaInfoShifts(Map<String,Shift> shifts) {
		String allshifts = new String();
		for(Shift shift : shifts.values()) {
			allshifts += generateMetaInfoShift(shift);
		}
		return allshifts;
	}
	
	public static String generateMetaInfoShift(Shift shift) {
		StringBuilder sb_shift = new StringBuilder();
		sb_shift.append(shift.getShiftName());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getSeed());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getShiftType());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(ConvertWeekday.convert(shift.getWeekday()));
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getStartTime().getStringHours());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getStartTime().getStringMinutes());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getRoom());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getMaxDelay().getStringHours());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		sb_shift.append(shift.getMaxDelay().getStringMinutes());
		sb_shift.append(ROUTER_INFO_SEPARATOR);
		String[] tokens = shift.getStudentsListPath().split("[/|\\\\]");
		sb_shift.append(tokens[tokens.length -1]);
		sb_shift.append("\n");
		return sb_shift.toString();
	}
	
	public static void readMetaInfoFile(String path, CourseManager course ) 
			throws InvalidFormatMetaInfoFileException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String line = reader.readLine();
			readCourseMetaInfo(course, line);
			while((line = reader.readLine()) != null) {
				readShiftMetaInfo(course,line);
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("I/O errors. Trying to read course meta info from " + path);
		}
	}
	
	public static void readCourseMetaInfo(CourseManager course, String line) 
			throws InvalidFormatMetaInfoFileException {
		String[] tokens = line.split(ROUTER_INFO_SEPARATOR);
		if(tokens.length != 5) {
			throw new InvalidFormatMetaInfoFileException(
					"CSVOperations: readCouseMetaInfo - not the format expected.");
		}
		course.setCourseName(tokens[0]);
		course.setCourseStartDate(ConvertDate.convert(tokens[1]));
		course.setCourseEndDate(ConvertDate.convert(tokens[2]));
		course.setProfessorID(tokens[3]);
		course.setProfessorPassword(tokens[4]);
	}
	
	public static void readShiftMetaInfo(CourseManager course, String line) 
			throws InvalidFormatMetaInfoFileException {
		String[] tokens = line.split(ROUTER_INFO_SEPARATOR);		
		if(tokens.length != 10) {
			throw new InvalidFormatMetaInfoFileException(
					"CSVOperations: readShiftMetaInfo - not the format expected.");
		}
		course.AddShift(tokens[0], 
				        tokens[2], 
				        ConvertWeekday.convert(tokens[3]), 
				        tokens[6], 
				        new Time(Integer.parseInt(tokens[4]),
				        		 Integer.parseInt(tokens[5])), 
				        new Time(Integer.parseInt(tokens[7]),
				        		 Integer.parseInt(tokens[8])),
				        tokens[9],
				        tokens[1]);
	}
	
	public static void generateStudentListCSV(String source, String dest, int max) {
		Map<String, StudentAttendance> map = new HashMap<String, StudentAttendance>();
		readStudentList(source, map);
		//Write New file
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Number");
			sb.append(CSV_SEPARATOR);
			sb.append("Max");
			sb.append(CSV_SEPARATOR);
			sb.append("Presences\n"); 
			for(StudentAttendance student : map.values()) {
				sb.append(student.getNumber());
				sb.append(CSV_SEPARATOR);
				sb.append(student.getName());
				sb.append(CSV_SEPARATOR);
				sb.append(student.getShiftMax());
				sb.append("\n");
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(dest)));
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("FileOperations : generateStudentListCSV errors writting.");
		}	
	}
	
	// TODO - is this needed?
	public static Map<String, StudentAttendance> readAttendanceCSV(String path, CourseManager course) 
			throws InvalidFormatMetaInfoFileException, NotTheSameCourseInfoException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String line = reader.readLine();
			readAttendanceCSVHeader(line, course);
			line = reader.readLine(); //ignore new line
			line = reader.readLine(); //this is the header get the size of each row
			String[] tokens = line.split(CSV_SEPARATOR);
			String[] dates = readAttendanceCSVDates(tokens);
			Map<String, StudentAttendance> students = new HashMap<String,StudentAttendance>();
			while((line = reader.readLine()) != null) {
				StudentAttendance student = readAttendanceCSVRow(line, dates);
				students.put(student.getNumber(),student);
			}
			reader.close();
			return students;
		} catch (IOException e) {
			throw new RuntimeException("I/O errors. Trying to read course meta info from " + path);
		}
	}
	
	// TODO - is this needed?
	public static void readAttendanceCSVHeader(String line, CourseManager course) 
			throws InvalidFormatMetaInfoFileException, NotTheSameCourseInfoException {
		String[] tokens = line.split(CSV_SEPARATOR);
		if(tokens.length != 2) {
			throw new InvalidFormatMetaInfoFileException(
					"FileOperations: readAttendanceCSVHeader - not the format expected.");
		}
		if(tokens[1].equals(course.getCourseName()))
			return;
		throw new NotTheSameCourseInfoException("FileOperations: readAttendanceCSVHeader - not the same course.");
	}
	
	// TODO - is this needed?
	public static String[] readAttendanceCSVDates(String[] tokens) 
			throws InvalidFormatMetaInfoFileException {
		if(tokens.length < 5) {
			throw new InvalidFormatMetaInfoFileException(
					"FileOperations: readAttendanceCSVDates - not the format expected.");
		}
		String[] dates = new String[tokens.length - 5];
		for(int i = 5, j = 0; i < tokens.length; i++, j++) {
			dates[j] = tokens[i];
		}
		return dates;
	}
	
	// TODO - is this needed?
	public static StudentAttendance readAttendanceCSVRow(String line, String[] dates) 
			throws InvalidFormatMetaInfoFileException {
		String[] tokens = line.split(";");
		if(tokens.length != (dates.length + 5)) {
			throw new InvalidFormatMetaInfoFileException(
					"FileOperations: readAttendanceCSVRow - not the format expected.");
		}
		StudentAttendance student = new StudentAttendance(tokens[0], tokens[1]);
		for(int i = 5, j = 0; i < tokens.length;i++, j++) {
			if(tokens[i].equals("1"))
				student.addPresence(dates[j]);
		}
		return student;
	}
}

