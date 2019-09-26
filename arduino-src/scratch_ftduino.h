#ifndef SCRATCH_FTDUINO_H
#define SCRATCH_FTDUINO_H

#include "scratch.h"
#include <inttypes.h>

enum class AnalogInputSpecifier : int8_t { INVALID_SPECIFIER, I1, I2, I3, I4, I5, I6, I7, I8};

enum class DigitalInputSpecifier : int8_t { INVALID_SPECIFIER, I1, I2, I3, I4, I5, I6, I7, I8, C1, C2, C3, C4};

enum class OutputSpecifier : int8_t { INVALID_SPECIFIER, O1, O2, O3, O4, O5, O6, O7, O8};

enum class MotorSpecifier : int8_t {INVALID_SPECIFIER, M1, M2, M3, M4};

enum class MotorDir : int8_t {INVALID_DIRECTION, LEFT, RIGHT};

enum class CounterSpecifier : int8_t {INVALID_SPECIFIER, C1, C2, C3, C4};

enum class InputMode : int8_t {INVALID_MODE, VOLTAGE, RESISTANCE, SWITCH};

ScratchValue scratch_ftduino_input_analog(ScratchValue, ScratchValue);

ScratchValue scratch_ftduino_input(ScratchValue);

void scratch_ftduino_clear_counter(ScratchValue);

void scratch_ftduino_output(ScratchValue, ScratchValue);

void scratch_ftduino_output_analog(ScratchValue, ScratchValue);

void scratch_ftduino_motor(ScratchValue, ScratchValue, ScratchValue);

ScratchValue scratch_ftduino_input_counter(ScratchValue);

#endif /* SCRATCH_FTDUINO_H */
