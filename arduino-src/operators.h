#ifdef __cplusplus
extern "C" {
#endif

#ifndef OPERATORS_H
#define OPERATORS_H

#include "scratch.h"

ScratchValue s_add(ScratchValue, ScratchValue);
ScratchValue s_subtract(ScratchValue, ScratchValue);
ScratchValue s_multiply(ScratchValue, ScratchValue);
ScratchValue s_divide(ScratchValue, ScratchValue);

ScratchValue s_lt(ScratchValue, ScratchValue);
ScratchValue s_equals(ScratchValue, ScratchValue);
ScratchValue s_gt(ScratchValue, ScratchValue);

ScratchValue s_and(ScratchValue, ScratchValue);
ScratchValue s_or(ScratchValue, ScratchValue);
ScratchValue s_not(ScratchValue);

ScratchValue s_random(ScratchValue, ScratchValue);




#endif /* OPERATORS_H */

#ifdef __cplusplus
}
#endif
