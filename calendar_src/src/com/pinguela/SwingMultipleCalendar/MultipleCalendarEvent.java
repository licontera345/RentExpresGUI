package com.pinguela.SwingMultipleCalendar;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;


public class MultipleCalendarEvent {
	    private static final Color DEFAULT_COLOR = Color.PINK;
	    private static final Color[] DEFAULT_COLORS = {Color.PINK, Color.RED, Color.BLUE};
	    private LocalDate date;
	    private LocalTime start;
	    private LocalTime end;
	    private String text;
	    private Color color;
	    private int index;

	    public MultipleCalendarEvent(LocalDate date, LocalTime start, LocalTime end, String text, int index) {
	    	this(date, start, end, text, DEFAULT_COLORS[index], index);

	    }

	    public MultipleCalendarEvent(LocalDate date, LocalTime start, LocalTime end, String text, Color color, int index) {
	        this.date = date;
	        this.start = start;
	        this.end = end;
	        this.text = text;
	        this.color = color;
	        this.index=index;
	    }

	    public LocalDate getDate() {
	        return date;
	    }

	    public void setDate(LocalDate date) {
	        this.date = date;
	    }

	    public LocalTime getStart() {
	        return start;
	    }

	    public void setStart(LocalTime start) {
	        this.start = start;
	    }

	    public LocalTime getEnd() {
	        return end;
	    }

	    public void setEnd(LocalTime end) {
	        this.end = end;
	    }

	    public String getText() {
	        return text;
	    }

	    public void setText(String text) {
	        this.text = text;
	    }

	    public String toString() {
	        return getDate() + " " + getStart() + "-" + getEnd() + ". " + getText()+" - index: "+ getIndex();
	    }

	    public Color getColor() {
	        return color;
	    }

	    public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        MultipleCalendarEvent that = (MultipleCalendarEvent) o;

	        if (!date.equals(that.date)) return false;
	        if (!start.equals(that.start)) return false;
	        if (!end.equals(that.end)) return false;
	        return index == that.index;

	    }

	    @Override
	    public int hashCode() {
	        int result = date.hashCode();
	        result = 31 * result + start.hashCode();
	        result = 31 * result + end.hashCode();
	        return result+index;
	    }

}
