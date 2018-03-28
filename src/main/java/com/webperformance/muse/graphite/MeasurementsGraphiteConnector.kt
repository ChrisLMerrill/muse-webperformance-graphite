package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*
import java.io.*

class MeasurementsGraphiteConnector(val provider: OutputStreamProvider)
{
	private var output : DataOutputStream
	private var close = false

	init
	{
		output = DataOutputStream(provider.createStream())
	}
	
	fun sendMeasurement(measurement : Measurement, timestamp : Number)
	{
		var retry = false
		try
		{
			output.writeBytes(formatter.asString(measurement, timestamp) + "\n")
		}
		catch (e: Exception)
		{
			output.close()
			retry = true
		}
		
		if (retry)
		{
			output = DataOutputStream(provider.createStream())
			output.writeBytes(formatter.asString(measurement, timestamp) + "\n")
		}
	}
	
	fun sendMeasurements(measurements: Measurements)
	{
		val list = mutableListOf<Measurement>()
		
		var timestamp : Number? = null
		for (m in measurements.iterator())
		{
			if (m.metadata["subject"] == "sample" && m.metadata["metric"] == "timestamp")
				timestamp = m.value
			else
				list.add(m)
		}
		
		if (timestamp == null)
			timestamp = System.currentTimeMillis()

		for (m in list)
			sendMeasurement(m, timestamp)
		output.flush()
		if (close)
			output.close()

	}

	fun close()
	{
		close = true
	}
	
	private val formatter = GraphiteMeasurementFormatter()
}