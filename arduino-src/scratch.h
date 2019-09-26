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


ScratchValue scratchNumber(float) __attribute__ ((const));

ScratchValue scratchString(const char*) __attribute__ ((const));

ScratchValue scratchBoolean(int) __attribute__ ((const));

#endif /* SCRATCH_H */
