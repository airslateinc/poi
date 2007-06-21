/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.hdgf.chunks;

import org.apache.poi.hdgf.chunks.ChunkFactory.CommandDefinition;

/**
 * Base of all chunks, which hold data, flags etc
 */
public class Chunk {
	/** 
	 * The contents of the chunk, excluding the header, 
	 * trailer and separator 
	 */
	private byte[] contents;
	private ChunkHeader header;
	/** May be null */
	private ChunkTrailer trailer;
	/** May be null */
	private ChunkSeparator separator;
	/** The possible different commands we can hold */
	protected CommandDefinition[] commandDefinitions;
	
	public Chunk(ChunkHeader header, ChunkTrailer trailer, ChunkSeparator separator, byte[] contents) {
		this.header = header;
		this.trailer = trailer;
		this.separator = separator;
		this.contents = contents;
	}
	
	public byte[] _getContents() {
		return contents;
	}
	public ChunkHeader getHeader() {
		return header;
	}
	/** Gets the separator between this chunk and the next, if it exists */
	public ChunkSeparator getSeparator() {
		return separator;
	}
	/** Gets the trailer for this chunk, if it exists */
	public ChunkTrailer getTrailer() {
		return trailer;
	}
	/**
	 * Gets the command definitions, which define and describe much
	 *  of the data held by the chunk.
	 */
	public CommandDefinition[] getCommandDefinitions() {
		return commandDefinitions;
	}

	/**
	 * Returns the size of the chunk, including any
	 *  headers, trailers and separators.
	 */
	public int getOnDiskSize() {
		int size = header.getSizeInBytes() + contents.length;
		if(trailer != null) {
			size += trailer.trailerData.length;
		}
		if(separator != null) {
			size += separator.separatorData.length;
		}
		return size;
	}
}
