package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*
import com.webperformance.muse.measurements.containers.*
import org.junit.*
import java.io.*

class MeasurementsGraphiteConnectorTests
{
	@Test
	fun sendOne()
	{
		connector.sendMeasurement(m1, 1000L)
		
		val lines = getLines()
		Assert.assertEquals(1, lines.size)
		Assert.assertEquals(GraphiteMeasurementFormatter().asString(m1, 1000L), lines[0])
	}

	@Test
	fun sendTwoSequentially()
	{
		connector.sendMeasurement(m1, 1000L)
		connector.sendMeasurement(m2, 2000L)
		
		val lines = getLines()
		Assert.assertEquals(2, lines.size)
		Assert.assertEquals(GraphiteMeasurementFormatter().asString(m1, 1000L), lines[0])
		Assert.assertEquals(GraphiteMeasurementFormatter().asString(m2, 2000L), lines[1])
	}
	
	@Test
	fun sendTwoAtOnce()
	{
		val meta = Measurement(1000L)
		meta.addMetadata("subject", "sample")
		meta.addMetadata("metric", "timestamp")
		
		val measurements = MultipleMeasurement(m1)
		measurements.add(m2)
		measurements.add(meta)
		connector.sendMeasurements(measurements)

		val lines = getLines()
		Assert.assertEquals(2, lines.size)
		Assert.assertEquals(GraphiteMeasurementFormatter().asString(m1, 1000L), lines[0])
		Assert.assertEquals(GraphiteMeasurementFormatter().asString(m2, 1000L), lines[1])
	}
	
	fun getLines() : List<String>
	{
		val bytes = output.toByteArray()
		val reader = LineNumberReader(InputStreamReader(ByteArrayInputStream(bytes)))
		val lines = mutableListOf<String>()
		var done = false;
		while (!done)
		{
			val line = reader.readLine()
			if (line == null)
				done = true
			else
				lines.add(line)
		}
		return lines
	}
	
	@Before
	fun setup()
	{
		m1.addMetadata("metric", "metric1")
		m1.addMetadata("subject", "subject1")
		m2.addMetadata("metric", "metric2")
		m2.addMetadata("subject", "subject1")
	}
	
	val m1 = Measurement(111L)
	val m2 = Measurement(222L)
	val output = ByteArrayOutputStream()
	val connector = MeasurementsGraphiteConnector(object: OutputStreamProvider {
		override fun createStream(): OutputStream
		{
			return output
		}
	})
}