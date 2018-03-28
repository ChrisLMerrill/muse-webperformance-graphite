package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*
import org.musetest.core.*
import org.musetest.core.events.*
import org.musetest.core.plugins.*
import org.musetest.core.resource.generic.*
import org.musetest.core.suite.*
import java.io.*
import java.net.*

class MeasurementsToGraphitePlugin(configuration: GenericResourceConfiguration) : GenericConfigurablePlugin(configuration), MeasurementsConsumer
{
	private lateinit var socket : Socket
	private lateinit var connector : MeasurementsGraphiteConnector
	
	override fun acceptMeasurements(measurements: Measurements)
	{
		println("send these: " + measurements.toString())
		connector.sendMeasurements(measurements)
	}
	
	override fun initialize(context: MuseExecutionContext)
	{
		if (context is TestSuiteExecutionContext)
		{
			println("open the connection...")
			connector = MeasurementsGraphiteConnector(object: OutputStreamProvider {
				override fun createStream(): OutputStream
				{
					return Socket("ec2-184-73-121-41.compute-1.amazonaws.com", 2003).getOutputStream() // TODO use the configured params
				}
			})
			context.addEventListener({ event ->
				if (EndSuiteEventType.TYPE_ID == event.typeId)
				{
					connector.close()
					socket.close()
				}
			})
		}
	}
	
	override fun applyToContextType(context: MuseExecutionContext?): Boolean
	{
		return context is TestSuiteExecutionContext
	}
}