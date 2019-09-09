#ifdef __cplusplus
extern "C" {
#endif

#ifndef CAST_H
#define CAST_H

#include "scratch.h"

float toNumber (ScratchValue);

bool toBoolean (ScratchValue);

float compare (ScratchValue, ScratchValue);

int isWhiteSpace (ScratchValue);

char* toString(ScratchValue, int*);

bool isInt (ScratchValue);

#endif /* CAST_H */

#ifdef __cplusplus
}
#endif
