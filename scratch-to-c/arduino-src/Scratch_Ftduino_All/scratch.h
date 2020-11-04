#ifndef SCRATCH_H
#define SCRATCH_H

#include <stdbool.h>
#include <inttypes.h>

enum class Type : int8_t {
  ScratchNumber, ScratchString, ScratchBool
};

typedef struct {
  Type type;
  union {
    char* string;
    float number;
    bool boolValue;
  };
} ScratchValue;

// TODO?
// Maybe make this a global read-only constant or something?
// Whether this is possible depends on whether a ScratchValue is considered immutable,
// which I think is how I designed it...
#define SCRATCH_FALSE scratchBoolean(0)
#define SCRATCH_ZERO scratchNumber(0)

ScratchValue scratchNumber(float) __attribute__ ((const));

ScratchValue scratchString(const char*) __attribute__ ((const));

ScratchValue scratchBoolean(int) __attribute__ ((const));

#endif /* SCRATCH_H */
