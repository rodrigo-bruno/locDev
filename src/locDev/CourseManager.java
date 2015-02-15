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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import locDev.exceptions.InvalidFormatMetaInfoFileException;
import locDev.exceptions.ShiftAlreadyExistsException;
import locDev.types.Shift;
import locDev.types.Time;
import locDev.util.FileOperations;
import locDev.util.converter.ConvertSeed;

/**
 * Class that manages all the course information.
 */
public class CourseManager {
	
	/**
	 * Number of digits for the seed.
	 */
	public static final int SHIFT_SEED_SIZE = 4;
	
	/**
	 * Desired seed representation.
	 * 16 - hex
	 *  8 - oct
	 * 10 - dec
	 *  2 - bin
	 */
	public static final int SHIFT_SEED_BASE = 16;
	
	/**
	 * Dictionary holding all shifts.
	 * This dictionary maps shift names into shifts.
	 */
	private HashMap<String, Shift> shifts;
	
	/**
	 * Course name.
	 */
	private String courseName = null;
	
	/**
	 * Course start date.
	 */
	private Date courseStartDate = null;
	
	/**
	 * Course end date.
	 */
	private Date courseEndDate = null;
	
	/**
	 * Path to the configuration directory.
	 */
	private String courseConfigurationDirectoryPath = null;
	
	/**
	 * The professor identification and password.
	 * Used for some special requests.
	 */
	private String professorID = null;
	private String professorPassword = null;

	/**
	 * Buffer for students list file
	 * Used when is creating course by URL
	 */
	private Map<String,String> bufferStudentsFiles = null;
	/**
	 * Empty constructor. Creates a new course.
	 */
	public CourseManager() {
		this.shifts = new HashMap<String, Shift>();
		this.bufferStudentsFiles = new HashMap<String, String>();
	}
	
	/**
	 * Method that adds shift. Receive all parameters. Dont
	 * do verifications 
	 * @param shiftName
	 * @param shiftType
	 * @param shiftWeekday
	 * @param shiftRoom
	 * @param shiftStartTime
	 * @param shiftMaxDelay
	 * @param shiftStudentsListPath
	 * @param seed
	 * @throws ShiftAlreadyExistsException
	 * 	if the shift already exists
	 */
	public void AddShift(
			String shiftName, 
			String shiftType,
			int shiftWeekday, 
			String shiftRoom,
			Time shiftStartTime,
			Time shiftMaxDelay,
			String shiftStudentsListPath,
			String seed) throws ShiftAlreadyExistsException {
		
		
		Shift shift = new Shift(
				shiftName, 
				shiftType, 
				shiftWeekday, 
				shiftRoom, 
				shiftStartTime, 
				shiftMaxDelay, 
				seed, 
				shiftStudentsListPath);
		
		this.shifts.put(shiftName, shift);
	}
	/**
	 * Method that adds shift. This method checks if the new shift can be 
	 * added.
	 * @param shiftName
	 * @param shiftType
	 * @param shiftWeekday
	 * @param shiftRoom
	 * @param shiftStartTime
	 * @param shiftMaxDelay
	 * @param shiftStudentsListPath
	 * @throws ShiftAlreadyExistsException
	 * 	if the shift with the same name already exists
	 */
	public void AddNewShift(
			String shiftName, 
			String shiftType,
			int shiftWeekday, 
			String shiftRoom,
			Time shiftStartTime,
			Time shiftMaxDelay,
			String shiftStudentsListPath) throws ShiftAlreadyExistsException {
		
		Iterator<Entry<String, Shift>> it = this.shifts.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Shift> entry = it.next();
			if (entry.getValue().getShiftName().equals(shiftName)) {
				throw new ShiftAlreadyExistsException();
			}
		}


		AddShift(shiftName, 
				 shiftType, 
				 shiftWeekday, 
				 shiftRoom, 
				 shiftStartTime, 
				 shiftMaxDelay, 
				 shiftStudentsListPath, 
				 ConvertSeed.convert(this.generateSeed()));
	}
	
	/**
	 * This updates a shift. Don't need to verify if exists.
	 * Because this remove the old with the same name 
	 * if it exists and add a new one.
	 * @param old_shiftName
	 * @param new_shiftName
	 * @param shiftType
	 * @param shiftWeekday
	 * @param shiftRoom
	 * @param shiftStartTime
	 * @param shiftMaxDelay
	 * @param shiftStudentsListPath
	 * @param seed
	 */
	public void EditShift(String old_shiftName,
			String new_shiftName,
			String shiftType,
			int shiftWeekday, 
			String shiftRoom,
			Time shiftStartTime,
			Time shiftMaxDelay,
			String shiftStudentsListPath,
			String seed) {
		this.shifts.remove(old_shiftName);
		AddShift(new_shiftName, shiftType, 
				 shiftWeekday, shiftRoom, 
				 shiftStartTime, shiftMaxDelay, 
				 shiftStudentsListPath, seed);
	}
	
	/**
	 * Method that removes a shift.
	 * @param shiftName
	 */
	public void RemoveShift(String shiftName) {
		this.shifts.remove(shiftName);
	}
	
	/**
	 * Method that will load all configuration info from a configuration 
	 * directory. 
	 * @param configDirPath
	 * @throws InvalidFormatMetaInfoFileException 
	 */
	public void load(String configDirPath) throws InvalidFormatMetaInfoFileException {
		this.courseConfigurationDirectoryPath = configDirPath;
		FileOperations.readMetaInfoFile(configDirPath + "/course/meta-info", this);
	}
	
	/**
	 * Method that will save the current configuration into a configuration
	 * directory.
	 * @param configDirPath
	 * @throws CantCreateDirectoryException 
	 */
	public void save(String configDirPath) {
		String old_path = this.courseConfigurationDirectoryPath;
		this.courseConfigurationDirectoryPath = configDirPath;
		locDevCore.generateRouterMetaInfo(configDirPath, old_path, this);
	}
	
	/**
	 * Method that generates a new seed. This seed is unique for all shifts.
	 * @return
	 *     a new seed.
	 */
	private int generateSeed() {
		int seed = 0, size = SHIFT_SEED_SIZE;
		Random r = new Random();
		while (size-- > 0) {
			seed = seed * SHIFT_SEED_BASE + r.nextInt(SHIFT_SEED_BASE);
		}
		return seed;
	}
	
	/**
	 * Setters
	 */
	public void setCourseName(String courseName) 
	{ this.courseName = courseName; }
	public void setCourseStartDate(Date courseStartDate)
	{ this.courseStartDate = courseStartDate; }
	public void setCourseEndDate(Date courseEndDate)
	{ this.courseEndDate = courseEndDate; }
	public void setBufferFile(String name, String content) 
	{ this.bufferStudentsFiles.put(name, content); }
	public void setProfessorID(String id)
	{ this.professorID = id; }
	public void setProfessorPassword(String password) { 
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] hashMd5 = md.digest();
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < hashMd5.length; i++) {
				byte b = hashMd5[i];
				result.append(String.format("%02X", b));
			}
			this.professorPassword = result.toString(); 
		} catch (NoSuchAlgorithmException e) {
			// Stupid verification. Never gonna happen with MD5.
		}
	}
	/**
	 * Getters
	 */
	public HashMap<String, Shift> getShifts() { return this.shifts; }
	public String getCourseConfigurationDirectoryPath() 
		{ return this.courseConfigurationDirectoryPath; }
	public Date getCourseStartDate() {return this.courseStartDate; }
	public Date getCourseEndDate() { return this.courseEndDate; }
	public String getCourseName() { return this.courseName; }
	public String getProfessorID() { return this.professorID; }
	public String getProfessorPassword() { return this.professorPassword; }
	public String getBufferFile(String shift_name) 
		{ return this.bufferStudentsFiles.get(shift_name); }

}
