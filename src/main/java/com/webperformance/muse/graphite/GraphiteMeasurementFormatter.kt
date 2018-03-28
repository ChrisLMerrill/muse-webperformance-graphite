package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*

class GraphiteMeasurementFormatter
{
	fun asString(measurement: Measurement, timestamp: Number) : String
	{
		val metric = measurement.metadata["metric"]
		if (metric == null)
			throw IllegalArgumentException("metric is missing")
		if (!(metric is String))
			throw IllegalArgumentException("metric is not a string")
		
		val subject = measurement.metadata["subject"]
		if (subject == null)
			throw IllegalArgumentException("subject is missing")
		if (!(subject is String))
			throw IllegalArgumentException("subject is not a string")
		
		if (!(timestamp is Long))
			throw IllegalArgumentException("timestamp is not a long")
		
		val timestamp_seconds = timestamp.toLong() / 1000L
		return "${subject}.${metric} ${measurement.value} ${timestamp_seconds}"
	}
}