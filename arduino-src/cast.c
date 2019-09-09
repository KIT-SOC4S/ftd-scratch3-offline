#include "cast.h"
#include "scratch.h"
#include <math.h>
#include <string.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <float.h>


bool otherToBoolean(ScratchValue value) {
  if (value.type == ScratchNumber) {
    if (isnan(value.number) || value.number == 0 || value.number == -0) {
      return false;
    }
  } else {
    printf("Unknown type: %i", value.type);
    return true;
  }
}

bool toBoolean (ScratchValue value) {
  // Already a boolean?
  if (value.type == ScratchBool) {
    return value.boolValue;
  }
  if (value.type == ScratchString) {
    // These specific strings are treated as false in Scratch.
    if ((strcmp(value.string, "") == 0) ||
        (strcmp(value.string, "0") == 0) ||
        (strcasecmp(value.string, 'false') == 0)) {
      return false;
    }
    // All other strings treated as true.
    return true;
  }
  // Coerce other values and numbers.
  return otherToBoolean(value);
}

float toNumber (ScratchValue value) {
  // If value is already a number we don't need to coerce it with
  // Number().
  if (value.type == ScratchNumber) {
    // Scratch treats NaN as 0, when needed as a number.
    // E.g., 0 + NaN -> 0.
    if (isnan(value.number)) {
      return 0;
    }
    return value.number;
  }
  // TODO FIX
  float n = strtod(value.string, NULL);
  if (isnan(n)) {
    // Scratch treats NaN as 0, when needed as a number.
    // E.g., 0 + NaN -> 0.
    return 0;
  }
  return n;
}

int is_empty(const char *s) {
  while (*s != '\0') {
    if (!isspace((unsigned char)*s))
      return 0;
    s++;
  }
  return 1;
}

int isWhiteSpace (ScratchValue val) {
  return /*val == null ||*/ (val.type == ScratchString && is_empty(val.string) == 0);
}

char* trueString = "true";
char* falseString = "false";

char* toString(ScratchValue val, int* free) {
  if (val.type == ScratchString) {
    *free = 0;
    return val.string;
  } else if (val.type == ScratchNumber) {
    char buf[33];
    char* partResult = dtostrf(val.number, (__DECIMAL_DIG__ + 2), __DECIMAL_DIG__, buf);
    char* result = strdup(partResult);
    *free = 1;
    return result;
  } else if (val.type == ScratchBool) {
    *free = 0;
    return val.boolValue ? trueString : falseString;
  } else {
    printf("Unknown type: %i", val.type);
    *free = 0;
    return "vf";
  }
}


bool isInt (ScratchValue val) {
  // Values that are already numbers.
  if (val.type == ScratchNumber) {
    if (isnan(val.number)) { // NaN is considered an integer.
      return true;
    }
    // True if it's "round" (e.g., 2.0 and 2).
    return roundf(val.number) == val.number;
  } else if (val.type == ScratchBool) {
    // `True` and `false` always represent integer after Scratch cast.
    return true;
  } else if (val.type == ScratchString) {
    // If it contains a decimal point, don't consider it an int.
    return strchr(val.string, '.') == NULL;
  }
  return false;
}

float compare (ScratchValue v1, ScratchValue v2) {
  float n1 = toNumber(v1);
  float n2 = toNumber(v2);
  if (n1 == 0 && isWhiteSpace(v1)) {
    n1 = NAN;
  } else if (n2 == 0 && isWhiteSpace(v2)) {
    n2 = NAN;
  }
  if (isnan(n1) || isnan(n2)) {
    // At least one argument can't be converted to a number.
    // Scratch compares strings as case insensitive.
    int free1, free2;
    char* s1 = toString(v1, &free1);
    char* s2 = toString(v2, &free2);
    int result = strcasecmp(s1, s2);
    if (free2) free(s2);
    if (free1) free(s1);
    return result;
  }
  // Handle the special case of Infinity
  if (
    (n1 == INFINITY && n2 == INFINITY) ||
    (n1 == -INFINITY && n2 == -INFINITY)
  ) {
    return 0;
  }
  // Compare as numbers.
  return n1 - n2;
}
