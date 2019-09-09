#ifdef __cplusplus
extern "C" {
#endif

#ifndef SCRATCH_H
#define SCRATCH_H

#include <stdbool.h>

typedef enum {
  ScratchNumber, ScratchString, ScratchBool
} Type;

typedef struct {
  Type type;
  union {
   char* string;
   float number;
   bool boolValue;
  };
} ScratchValue;


ScratchValue scratchNumber(float);

ScratchValue scratchString(char*);

#endif /* SCRATCH_H */

#ifdef __cplusplus
}
#endif
