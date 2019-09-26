#include "scratch.h"

ScratchValue scratchNumber(float number) {
  ScratchValue value;
  value.type = Type::ScratchNumber;
  value.number = number;
  return value;
}

ScratchValue scratchBoolean(int number_) {
  ScratchValue value;
  value.type = Type::ScratchBool;
  value.boolValue = number_;
  return value;
}

ScratchValue scratchString(const char* string) {
  ScratchValue value;
  value.type = Type::ScratchString;
  value.string = string;
  return value;
}
