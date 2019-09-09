#include "scratch.h"

ScratchValue scratchNumber(float number) {
  ScratchValue value;
  value.type = ScratchNumber;
  value.number = number;
  return value;
}

ScratchValue scratchString(char* string) {
  ScratchValue value;
  value.type = ScratchString;
  value.string = string;
  return value;
}
