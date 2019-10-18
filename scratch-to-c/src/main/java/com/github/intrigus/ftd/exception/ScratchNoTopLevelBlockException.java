package com.github.intrigus.ftd.exception;

/**
 * Thrown when there is no top level block i.e. hat. Without a top level block
 * there are no blocks to convert, since a scratch sketch has to start with a
 * top level block.
 */
@SuppressWarnings("serial")
public class ScratchNoTopLevelBlockException extends RuntimeException {
}
