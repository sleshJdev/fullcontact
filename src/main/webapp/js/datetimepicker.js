//Global variables
var datePickerWindow;
var dateToday = new Date();
var calendar;
var datePickerDocument;
var MONTH_NAMES = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December"];
var WEEK_DAY_NAMES = ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];	
var existingsDate;//Existing Date

//Configurable parameters
var POSITION_TOP = "200";//top coordinate of calendar window.
var POSITION_LEFT = "500";//left coordinate of calendar window
var WINDOW_TITLE ="DateTime Picker";//Date Time Picker title.
var WEEK_CHAR = 2;//number of character for week day. if 2 then Mo,Tu,We. if 3 then Mon,Tue,Wed.
var CELL_WIDTH = 20;//Width of day cell.
var DATE_SEPARATOR = "-";//Date Separator, you can change it to "/" if you want.

var WEEK_HEAD_COLOR="#0099CC";//Background Color in Week headerHtml.
var SUNDAY_COLOR="#6699FF";//Background color of Sunday.
var SATUR_DAY_COLOR="#CCCCFF";//Background color of Saturday.
var WEEK_DAY_COLOR="white";//Background color of weekdays.
var FONT_COLOR="blue";//color of font in Calendar day cell.
var TODAY_COLOR="#FFFF33";//Background color of today.
var SELECT_DATE_COLOR="#FFFF99";//Backgrond color of selected date in textbox.
var YEAR_SELECT_COLOR="#cc0033";//color of font of Year selector.
//end Configurable parameters

//end Global variable

function DateTimePick(inputId) {
	calendar = new Calendar(dateToday);
	if (inputId != null) {
		calendar.inputId = inputId;
	}

	existingsDate = document.getElementById(inputId).value;
	if (existingsDate!="")//Parse Date String
	{
		//parse month
		var separator1 = existingsDate.indexOf(DATE_SEPARATOR, 0)
		var separator2 = existingsDate.indexOf(DATE_SEPARATOR, (parseInt(separator1) + 1));
		var length = existingsDate.length;
		
		var strMonth = existingsDate.substring(separator1 + 1, separator2);
		var strDate = existingsDate.substring(separator2 + 1, length);
		var intMonth = parseInt(strMonth, 10) - 1;
				
		if ((parseInt(intMonth, 10) >= 0) && (parseInt(intMonth, 10) < 12)){
			calendar.month = intMonth;
		}
		//end parse month
		
		//parse Date
		if ((parseInt(strDate, 10) <= calendar.getMonDays()) && (parseInt(strDate, 10) >= 1)){
			calendar.date = strDate;
		}
		//end parse Date
		
		//parse year
		var strYear = existingsDate.substring(0, 4);
		var YearPattern = /^\d{4}$/;
		if (YearPattern.test(strYear)){
			calendar.year = parseInt(strYear, 10);
		}
		//end parse year
	}
	
	datePickerWindow=window.open("","DateTimePicker","width=200, height=180, status=no, toolbar=no, location=no, menubar=no, resizable=no, scrollbars=no, top=" + POSITION_TOP + ",left=" + POSITION_LEFT);
	datePickerDocument = datePickerWindow.document;
	renderCalendar();
}

function renderCalendar() {
	var headerHtml;
	var dataHtml;
	var firstDay;
	var dayCount = 0;

	datePickerDocument.open();
	datePickerDocument.writeln("<html><head><title>" + WINDOW_TITLE + "</title>");
	datePickerDocument.writeln("<script>var winMain=window.opener;</script>");
	datePickerDocument.writeln("</head><body link=" + FONT_COLOR + " vlink=" + FONT_COLOR + "><form name='calendar'>");

	headerHtml = "<table border=1 cellpadding=1 cellspacing=1 width='100%' align=\"center\" valign=\"top\">\n";
	// Month Selector
	headerHtml += "<tr>\n<td colspan='7'><table border=0 width='100%' cellpadding=0 cellspacing=0><tr><td align='left'>\n";
	headerHtml += "<select name=\"MonthSelector\" onChange=\"javascript:winMain.calendar.switchMonth(this.selectedIndex);winMain.renderCalendar();\">\n";
	for (var i = 0; i < 12; ++i) {
		var isSelect = "";	
		if (i  ==  calendar.month){
			isSelect = "selected";
		}  
		headerHtml += "<option " + isSelect + " value >" + MONTH_NAMES[i] + "\n";
	}
	headerHtml+="</select></td>";
	
	//Year selector
	headerHtml+="\n<td align='right'><a href=\"javascript:winMain.calendar.decYear();winMain.renderCalendar()\"><b><font color=\"" + YEAR_SELECT_COLOR + "\"><</font></b></a><font face=\"Verdana\" color=\"" + YEAR_SELECT_COLOR + "\" size=2><b> "+calendar.year+" </b></font><a href=\"javascript:winMain.calendar.incYear();winMain.renderCalendar()\"><b><font color=\""+YEAR_SELECT_COLOR+"\">></font></b></a></td></tr></table></td>\n";	
	headerHtml+="</tr>";
	
	//Week day headerHtml
	headerHtml += "<tr bgcolor=" + WEEK_HEAD_COLOR + ">";
	for (var i = 0; i < 7; ++i) {
		headerHtml += "<td align='center'><font face='Verdana' size='2'>" + WEEK_DAY_NAMES[i].substr(0, WEEK_CHAR) + "</font></td>";
	}
	headerHtml += "</tr>";	
	datePickerDocument.write(headerHtml);
	
	//Calendar detail
	calendarDate = new Date(calendar.year, calendar.month);
	calendarDate.setDate(1);
	firstDay = calendarDate.getDay();
	dataHtml = "<tr>";
	for (var i = 0; i < firstDay; i++) {
		dataHtml = dataHtml + createCell();
		dayCount = dayCount + 1;
	}
	for (var j = 1; j <= calendar.getMonDays(); j++) {
		var strCell;
		dayCount = dayCount + 1;
		if ((j == dateToday.getDate()) && (calendar.month == dateToday.getMonth()) && (calendar.year == dateToday.getFullYear()))
			strCell = createCell(j, true, TODAY_COLOR);// Highlight today's date
		else {
			if (j == calendar.date) {
				strCell = createCell(j, true, SELECT_DATE_COLOR);
			} else {
				if (dayCount % 7 == 0) {
					strCell = createCell(j, false, SATUR_DAY_COLOR);
				} else if ((dayCount + 6) % 7 == 0) {
					strCell = createCell(j, false, SUNDAY_COLOR);
				} else {
					strCell = createCell(j, null, WEEK_DAY_COLOR);
				}
			}
		}
		dataHtml = dataHtml + strCell;

		if ((dayCount % 7 == 0) && (j < calendar.getMonDays())) {
			dataHtml = dataHtml + "</tr>\n<tr>";
		}
	}
	datePickerDocument.writeln(dataHtml);	
	
	datePickerDocument.writeln("\n</table>");
	datePickerDocument.writeln("</form></body></html>");
	datePickerDocument.close();
}

function createCell(value, highLight, color) {//create table cell with value
	var cellValue;
	var cellHtml;
	var bc;
	var hl1;// HighLight string
	var hl2;
	var vTimeStr;

	if (value == null) {
		cellValue = "";
	} else {
		cellValue = value;
	}

	if (color != null) {
		bc = "bgcolor=\"" + color + "\"";
	} else {
		bc = "";
	}

	if ((highLight != null) && (highLight)) {
		hl1 = "color='red'><b>";
		hl2 = "</b>";
	} else {
		hl1 = ">";
		hl2 = "";
	}

	cellHtml = "<td " + bc + " width=" + CELL_WIDTH + " align='center'><font face='verdana' size='2'" + hl1 + "<a href=\"javascript:winMain.document.getElementById('" + calendar.inputId + "').value='" + calendar.formatDate(cellValue) + "';window.close();\">" + cellValue + "</a>" + hl2 + "</font></td>";
	
	return cellHtml;
}

function Calendar(date, inputId) {
	//Properties
	this.date = date.getDate();//selected date
	this.month = date.getMonth();//selected month number
	this.year = date.getFullYear();//selected year in 4 digits
	
	this.MyWindow = datePickerWindow;
	this.inputId = inputId;
	this.separator = DATE_SEPARATOR;
}

function getMonthIndex(shortMonthName) {
	for (i = 0; i < 12; i++) {
		if (MONTH_NAMES[i].substring(0, 3).toUpperCase() == shortMonthName.toUpperCase()) {
			return i;
		}
	}
}
Calendar.prototype.getMonthIndex=getMonthIndex;

function incYear() {
	calendar.year++;
}
Calendar.prototype.incYear = incYear;

function decYear() {
	calendar.year--;
}
Calendar.prototype.decYear = decYear;

	
function switchMonth(intMonth) {
	calendar.month = intMonth;
}
Calendar.prototype.switchMonth = switchMonth;

function getMonthName() {
	var month = MONTH_NAMES[this.month];

	return month.substr(0, 3);
}
Calendar.prototype.getMonthName = getMonthName;

function getMonDays() { // Get number of days in a month
	var daysInMonth = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];
	if (this.isLeapYear()) {
		daysInMonth[1] = 29;
	}

	return daysInMonth[this.month];
}
Calendar.prototype.getMonDays = getMonDays;

function isLeapYear() {
	if ((this.year % 4) == 0) {
		if ((this.year % 100 == 0) && (this.year % 400) != 0) {
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}
Calendar.prototype.isLeapYear = isLeapYear;

function formatDate(day) {
	var m = this.month + 1;
	if (m < 10) {
		m = "0" + m;
	}
	var d = day;
	if (d < 10) {
		d = "0" + d;
	}

	return this.year + DATE_SEPARATOR + m + DATE_SEPARATOR + d;
}
Calendar.prototype.formatDate=formatDate;	