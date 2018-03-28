package com.webperformance.muse.graphite

import org.musetest.core.*
import org.musetest.core.plugins.*
import org.musetest.core.resource.generic.*
import org.musetest.core.resource.types.*
import org.musetest.core.values.*
import org.musetest.core.values.descriptor.*

@MuseTypeId("com.webperformance.measurements-to-graphite")
@MuseSubsourceDescriptors(
	MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM),
	MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM),
	MuseSubsourceDescriptor(displayName = "Hostname", description = "Hostname of Graphite server", type = SubsourceDescriptor.Type.Named, name = MeasurementsToGraphiteConfiguration.HOSTNAME_PARAM),
	MuseSubsourceDescriptor(displayName = "Port", description = "Port on Graphite server (defaults to 2003)", type = SubsourceDescriptor.Type.Named, name = MeasurementsToGraphiteConfiguration.PORT_PARAM, optional = true)
)
class MeasurementsToGraphiteConfiguration : GenericResourceConfiguration(), PluginConfiguration
{
	override fun getType(): ResourceType
	{
		return MeasurementsToGraphiteType()
	}
	
	override fun createPlugin(): MeasurementsToGraphitePlugin
	{
		return MeasurementsToGraphitePlugin(this)
	}
	
	class MeasurementsToGraphiteType : ResourceSubtype(TYPE_ID, "Send Measurements to Graphite", MeasurementsToGraphiteConfiguration::class.java, PluginConfiguration.PluginConfigurationResourceType())
	{
		override fun create(): MuseResource
		{
			val config = MeasurementsToGraphiteConfiguration()
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true))
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true))
			return config
		}
		
		override fun getDescriptor(): ResourceDescriptor
		{
			return DefaultResourceDescriptor(this, "Sends measurements to a Graphite server.")
		}
	}
	
	companion object
	{
		val TYPE_ID = MeasurementsToGraphiteConfiguration::class.java.getAnnotation(MuseTypeId::class.java).value
		const val HOSTNAME_PARAM = "hostname"
		const val PORT_PARAM = "port"
	}
	
}