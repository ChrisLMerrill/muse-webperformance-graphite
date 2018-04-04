package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*
import org.musetest.core.*
import org.musetest.core.events.*
import org.musetest.core.plugins.*
import org.musetest.core.suite.*
import java.io.*
import java.net.*

class MeasurementsToGraphitePlugin(val configuration: MeasurementsToGraphiteConfiguration) : GenericConfigurablePlugin(configuration), MeasurementsConsumer
{
	private lateinit var socket : Socket
	private lateinit var connector : MeasurementsGraphiteConnector
	private var hostname : String? = null
	private var port = 0
	
	override fun acceptMeasurements(measurements: Measurements)
	{
		connector.sendMeasurements(measurements)
	}
	
	override fun initialize(context: MuseExecutionContext)
	{
		if (context is TestSuiteExecutionContext)
		{
			configure(context)
			
			if (hostname == null)
			{
				context.raiseEvent(TestErrorEventType.create("hostname parameter is required for MeasurementsToGraphitePlugin"))
				return;
			}
			
			connector = MeasurementsGraphiteConnector(object: OutputStreamProvider {
				override fun createStream(): OutputStream
				{
					return Socket(hostname, port).getOutputStream()
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
	
	private fun configure(context: MuseExecutionContext)
	{
	    hostname = configuration.getHostname(context)
		port = configuration.getPort(context) as Int
	}
	
	override fun applyToContextType(context: MuseExecutionContext?): Boolean
	{
		return context is TestSuiteExecutionContext
	}
}