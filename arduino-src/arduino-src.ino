#define FTDUINO_NO_SHORTHANDS
#include <Ftduino.h>
#undef FTDUINO_NO_SHORTHANDS

#include "cast.h"

#include "scratch.h"
#include "scratch_ftduino.h"

#include "operators.h"

void setup() {
  ftduino.init();
  pinMode(LED_BUILTIN, OUTPUT);
}
void loop() {
  for (uint32_t i = 0; toBoolean(s_lt(scratchNumber(i), (scratchNumber(10000)))); i++) {
    if (toBoolean(s_not((s_gt((s_random(scratchNumber(1.0), scratchNumber(10.0))), (scratchString("50"))))))) {
      if (toBoolean(s_and((s_lt((s_divide((scratchNumber(1.0)), (scratchNumber(1.0)))), (scratchString("50")))), (s_equals((s_subtract((scratchNumber(50.0)), (scratchNumber(0.0)))), (scratchString("50"))))))) {
        while (1) {
          digitalWrite(LED_BUILTIN, (toBoolean(scratchBoolean(true))) ? HIGH : LOW);
        }
      }
      else {
        while (toBoolean(s_not(s_or((s_gt((s_add((scratchNumber(500.0)), (scratchNumber(0.0)))), (scratchString("50")))), (s_gt((s_multiply((scratchNumber(200.0)), (scratchNumber(2.0)))), (scratchString("50")))))))) {
          digitalWrite(LED_BUILTIN, (toBoolean(scratchBoolean(false))) ? HIGH : LOW);
        }
      }
    }
    while (toBoolean(s_not((s_equals((scratchString("500")), (scratchString("50"))))))) {
    }
    digitalWrite(LED_BUILTIN, (toBoolean(scratchBoolean(false))) ? HIGH : LOW);
    if (toBoolean(scratch_ftduino_input(scratchString("I1")))) {
      scratch_ftduino_output(scratchString("O1"), scratchBoolean(true));
    }
    if (toBoolean(s_gt((scratch_ftduino_input_analog(scratchString("I1"), scratchString("VOLTAGE"))), (scratchString("50"))))) {
      scratch_ftduino_output_analog(scratchString("O1"), scratchNumber(100.0));
      scratch_ftduino_motor(scratchString("M1"), scratchString("LEFT"), scratchNumber(100.0));
      scratch_ftduino_clear_counter(scratchString("C1"));
    }
    if (toBoolean(s_gt((scratch_ftduino_input_counter(scratchString("C1"))), (scratchString("50"))))) {
      scratch_ftduino_output_analog(scratchString("O1"), scratchNumber(100.0));
      scratch_ftduino_motor(scratchString("M1"), scratchString("LEFT"), scratchNumber(100.0));
      scratch_ftduino_clear_counter(scratchString("C1"));
    }
  }
}
