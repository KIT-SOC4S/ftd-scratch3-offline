#ifndef OPERATORS_H
#define OPERATORS_H

#include "scratch.h"

ScratchValue s_add(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_subtract(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_multiply(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_divide(ScratchValue, ScratchValue) __attribute__ ((const));

ScratchValue s_lt(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_equals(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_gt(ScratchValue, ScratchValue) __attribute__ ((const));

ScratchValue s_and(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_or(ScratchValue, ScratchValue) __attribute__ ((const));
ScratchValue s_not(ScratchValue) __attribute__ ((const));

ScratchValue s_random(ScratchValue, ScratchValue);




#endif /* OPERATORS_H */
