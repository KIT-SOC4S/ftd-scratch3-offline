package com.github.intrigus.ftd.exception;

/**
 * Thrown when there is more than one top level block i.e. hat. Top level blocks
 * can run concurrently in scratch which is problematic for less powerful
 * microprocessors. On an Arduino more powerful than the ftduino this might be
 * possible.
 */
@SuppressWarnings("serial")
public class ScratchTooManyTopLevelBlocksException extends RuntimeException {

}
