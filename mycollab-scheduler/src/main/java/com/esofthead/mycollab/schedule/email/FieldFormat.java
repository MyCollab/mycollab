package com.esofthead.mycollab.schedule.email;

public interface FieldFormat<T> {
	String formatField(T value, String timeZone);
}
