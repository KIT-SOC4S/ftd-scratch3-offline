#include "cast.h"
#include "scratch.h"
#include "operators.h"
#include <stdlib.h>
#include <math.h>

ScratchValue s_add(ScratchValue num1, ScratchValue num2) {
  return scratchNumber(toNumber(num1) + toNumber(num2));
}

ScratchValue s_subtract(ScratchValue num1, ScratchValue num2) {
  return scratchNumber(toNumber(num1) - toNumber(num2));
}

ScratchValue s_multiply(ScratchValue num1, ScratchValue num2) {
  return scratchNumber(toNumber(num1) * toNumber(num2));
}

ScratchValue s_divide(ScratchValue num1, ScratchValue num2) {
  return scratchNumber(toNumber(num1) / toNumber(num2));
}



ScratchValue s_lt(ScratchValue op1, ScratchValue op2) {
  return scratchBoolean(compare(op1, op2) < 0);
}

ScratchValue s_equals(ScratchValue op1, ScratchValue op2) {
  return scratchBoolean(compare(op1, op2) == 0);
}

ScratchValue s_gt(ScratchValue op1, ScratchValue op2) {
  return scratchBoolean(compare(op1, op2) > 0);
}



ScratchValue s_and(ScratchValue op1, ScratchValue op2) {
  return scratchBoolean(toBoolean(op1) && toBoolean(op2));
}

ScratchValue s_or(ScratchValue op1, ScratchValue op2) {
  return scratchBoolean(toBoolean(op1) || toBoolean(op2));
}

ScratchValue s_not(ScratchValue op) {
  return scratchBoolean(!toBoolean(op));
}

float randomFloat() {
  return rand() / (RAND_MAX + 1.);
}

ScratchValue s_random(ScratchValue from, ScratchValue to) {
  float nFrom = toNumber(from);
  float nTo = toNumber(to);
  float low = nFrom <= nTo ? nFrom : nTo;
  float high = nFrom <= nTo ? nTo : nFrom;
  if (low == high) return scratchNumber(low);
  // If both arguments are ints, truncate the result to an int.
  if (isInt(from) && isInt(to)) {
    return scratchNumber(low + floor(randomFloat() * ((high + 1) - low)));
  }
  return scratchNumber((randomFloat() * (high - low)) + low);
}
