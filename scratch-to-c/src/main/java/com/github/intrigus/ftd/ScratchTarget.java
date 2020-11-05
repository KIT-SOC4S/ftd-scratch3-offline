package com.github.intrigus.ftd;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Models a scratch target. See
 * https://en.scratch-wiki.info/wiki/Scratch_File_Format#Targets
 * 
 * @author sg
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScratchTarget {
	@JsonProperty("blocks")
	ScratchBlocks blocks;

	/**
	 * The blocks of this target.
	 * 
	 * @return returns the blocks of this target.
	 */
	public ScratchBlocks getBlocks() {
		return blocks;
	}

}