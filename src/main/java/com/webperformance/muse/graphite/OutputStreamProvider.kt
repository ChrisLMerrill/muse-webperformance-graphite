package com.webperformance.muse.graphite

import java.io.*

interface OutputStreamProvider
{
	fun createStream() : OutputStream
}