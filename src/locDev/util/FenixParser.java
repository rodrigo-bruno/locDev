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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import locDev.CourseManager;
import locDev.types.Time;
import locDev.util.converter.ConvertShiftType;
import locDev.util.converter.ConvertWeekday;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FenixParser {

	
	public static String getMarksPage(String home_page) throws IOException {
		Document doc = Jsoup.connect(home_page + "/avaliacao").get();
		Elements links = doc.select("a[href]");
		for(Element el : links) {
			if(el.text().trim().equals("Pauta")) {
				return el.attr("abs:href");
			}
		}
		return "Can't Find";
	}
	
	public static void getShifts(String home_page, CourseManager course, String shiftStudentsListPath) throws IOException {
		Document doc = Jsoup.connect(home_page + "/horario").get();
		Elements all_slots = doc.select("td.period-first-slot");
		Map<String,Integer> nb_shifts = FenixParser.numberShift();
		for(Element el : all_slots) {
			String[] info = el.text().split("\\xa0");
			//Name
			nb_shifts.put(info[0], nb_shifts.get(info[0]) + 1);
			String shiftName = info[0] + String.format("%02d", nb_shifts.get(info[0]));
			//Type and room
			String shiftType = ConvertShiftType.convert(info[0]);
			String shiftRoom = el.text().substring(el.text().indexOf(info[1]));
			//Start time and delay
			String[] begin = el.attr("title").split("-")[0].split(":");
			Time shiftStartTime = new Time(Integer.parseInt(begin[0]), Integer.parseInt(begin[1]));
			Time shiftMaxDelay = new Time(0,20);
			//WeekDay
			String weekday = el.attr("headers").split(" ")[0];
			int shiftWeekday = ConvertWeekday.convertFenix(weekday);
			course.AddNewShift(shiftName, shiftType, shiftWeekday, shiftRoom, shiftStartTime, shiftMaxDelay, shiftStudentsListPath);
		}
	}
	
	public static Map<String, String> getStudentsTable(String mark_page) throws IOException {
		Document doc = Jsoup.connect(mark_page).get();
		Elements tables = doc.select("table.tab_complex");
		Element table = tables.first();
		Map<String, String> students = new HashMap<String, String>();
		Iterator<Element> it = table.select("td").iterator();
		while(it.hasNext()) {
			Element number = it.next();
			Element name = it.next();
			it.next(); // course name, skip
			students.put(number.text(), name.text());
		}
		return students;
	}
	
	public static Map<String,Integer> numberShift() {
		Map<String,Integer> nb_shifts = new HashMap<String,Integer>();
		nb_shifts.put("T",0);
		nb_shifts.put("P",0);
		nb_shifts.put("TP",0);
		nb_shifts.put("L",0);
		nb_shifts.put("TC",0);
		nb_shifts.put("Pb",0);
		nb_shifts.put("S",0);
		nb_shifts.put("E",0);
		nb_shifts.put("OT",0);
		return nb_shifts;
	}
}
