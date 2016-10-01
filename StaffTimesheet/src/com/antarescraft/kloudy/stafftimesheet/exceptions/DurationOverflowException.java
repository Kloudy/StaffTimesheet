package com.antarescraft.kloudy.stafftimesheet.exceptions;

/**
 * Thrown when adding two durations results in a duration greater than 99:59:59
 */

public class DurationOverflowException extends Exception
{
	private static final long serialVersionUID = 1L;
}