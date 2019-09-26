#ifndef CAST_H
#define CAST_H

#include "scratch.h"

float toNumber (ScratchValue);

bool toBoolean (ScratchValue) __attribute__ ((const));

float compare (ScratchValue, ScratchValue);

int isWhiteSpace (ScratchValue) __attribute__ ((const));

const char* toString(ScratchValue, bool*);

bool isInt (ScratchValue) __attribute__ ((const));

#endif /* CAST_H */
