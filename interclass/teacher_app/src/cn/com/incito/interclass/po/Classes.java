package cn.com.incito.interclass.po;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Classes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4858685768837161501L;

	private int id;
	private int schoolId;
	private int year;// 年级，哪年入学
	private int number;//班级，如2班等
	private Date ctime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		year = year - this.year;
		if (month >= 9) {
			year += 1;
		}
		String className = "%d年级%d班";
		return String.format(className, year, number);
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + number;
		result = prime * result + schoolId;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Classes other = (Classes) obj;
		if (id != other.id)
			return false;
		if (number != other.number)
			return false;
		if (schoolId != other.schoolId)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	
}
