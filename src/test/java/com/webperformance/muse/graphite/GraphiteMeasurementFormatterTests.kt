package com.webperformance.muse.graphite

import com.webperformance.muse.measurements.*
import org.junit.*

class GraphiteMeasurementFormatterTests
{
	@Test
	fun testFormat()
	{
		val m = Measurement(123)
		m.addMetadata("metric", "metric1")
		m.addMetadata("subject", "thing1")

		val formatter = GraphiteMeasurementFormatter()
		Assert.assertEquals("thing1.metric1 123 1", formatter.asString(m, 1234L))
	}
	
	@Test
	fun metricMissing()
	{
		val m = Measurement(123)
		m.addMetadata("subject", "thing1")

		val formatter = GraphiteMeasurementFormatter()
		try
		{
			formatter.asString(m, 1000L)
			Assert.assertTrue("should have thrown exception for missing metric attribute", false)
		}
		catch (e: IllegalArgumentException)
		{
			Assert.assertTrue(e.message!!.contains("metric"))
		}
	}

	@Test
	fun metricNotString()
	{
		val m = Measurement(123)
		m.addMetadata("metric", 987)
		m.addMetadata("subject", "thing1")

		val formatter = GraphiteMeasurementFormatter()
		try
		{
			formatter.asString(m, 1000L)
			Assert.assertTrue("should have thrown exception for bad metric type", false)
		}
		catch (e: IllegalArgumentException)
		{
			Assert.assertTrue(e.message!!.contains("metric"))
			Assert.assertTrue(e.message!!.contains("string"))
		}
	}

	@Test
	fun subjectMissing()
	{
		val m = Measurement(123)
		m.addMetadata("metric", "metric1")

		val formatter = GraphiteMeasurementFormatter()
		try
		{
			formatter.asString(m, 1000L)
			Assert.assertTrue("should have thrown exception for missing subject attribute", false)
		}
		catch (e: IllegalArgumentException)
		{
			Assert.assertTrue(e.message!!.contains("subject"))
		}
	}

	@Test
	fun subjectNotString()
	{
		val m = Measurement(123)
		m.addMetadata("metric", "metric1")
		m.addMetadata("subject", 123L)

		val formatter = GraphiteMeasurementFormatter()
		try
		{
			formatter.asString(m, 1000L)
			Assert.assertTrue("should have thrown exception for bad metric type", false)
		}
		catch (e: IllegalArgumentException)
		{
			Assert.assertTrue(e.message!!.contains("subject"))
			Assert.assertTrue(e.message!!.contains("string"))
		}
	}

	@Test
	fun timestampNotLong()
	{
		val m = Measurement(123)
		m.addMetadata("timestamp", "not_a_long")
		m.addMetadata("metric", "metric1")
		m.addMetadata("subject", "thing1")

		val formatter = GraphiteMeasurementFormatter()
		try
		{
			formatter.asString(m, 123.456)
			Assert.assertTrue("should have thrown exception for bad metric timestamp type", false)
		}
		catch (e: IllegalArgumentException)
		{
			Assert.assertTrue(e.message!!.contains("timestamp"))
			Assert.assertTrue(e.message!!.contains("long"))
		}
	}
}