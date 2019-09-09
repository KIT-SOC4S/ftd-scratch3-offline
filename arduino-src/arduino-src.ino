#include <Ftduino.h>

#include "cast.h"

#include "scratch.h"

#include "operators.h"

void setup() {
  ftduino.init();
  pinMode(LED_BUILTIN, OUTPUT);
}
void loop() {
  for (uint32_t i = 0; i < 10; i++) {
    if (toBoolean(s_not((s_gt((s_random(scratchNumber(1.0), scratchNumber(10.0))), (scratchString("50"))))))) {
      if (toBoolean(s_and((s_lt((s_divide((scratchNumber(1.0)), (scratchNumber(1.0)))), (scratchString("50")))), (s_equals((s_subtract((scratchNumber(50.0)), (scratchNumber(0.0)))), (scratchString("50"))))))) {
        while (1) {
          digitalWrite(LED_BUILTIN, HIGH);
        }
      }
      else {
        while (toBoolean(s_not(s_or((s_gt((s_add((scratchNumber(500.0)), (scratchNumber(0.0)))), (scratchString("50")))), (s_gt((s_multiply((scratchNumber(200.0)), (scratchNumber(2.0)))), (scratchString("50")))))))) {
          digitalWrite(LED_BUILTIN, LOW);
        }
      }
    }
    while (toBoolean(s_not((s_equals((scratchString("500")), (scratchString("50"))))))) {
    }
    digitalWrite(LED_BUILTIN, LOW);
  }
}
