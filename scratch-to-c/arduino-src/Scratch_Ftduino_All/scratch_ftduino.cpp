#include "scratch_ftduino.h"
#include "scratch.h"
#include "cast.h"
#include <assert.h>

#define FTDUINO_NO_SHORTHANDS
#include <Ftduino.h>
#undef FTDUINO_NO_SHORTHANDS

class FreeableString {
    char* string;
    bool shouldFree;
  public:
    FreeableString(const char* string, bool shouldFree) : string(string), shouldFree(shouldFree) {
    }
    ~FreeableString() {
      if (shouldFree) {
        free(string);
      }
    }
};

MotorStopMode toMotorStopMode(ScratchValue stopMode) {
  bool shouldFree;
  const char* string = toString(stopMode, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "OFF") == 0) {
    return MotorStopMode::STOP;
  } else if (strcasecmp(string, "BRAKE") == 0) {
    return MotorStopMode::BRAKE;
  } else {
    return MotorStopMode::INVALID_MODE;
  }
}

MotorDir toMotorDir(ScratchValue motorDir) {
  bool shouldFree;
  const char* string = toString(motorDir, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "RIGHT") == 0) {
    return MotorDir::RIGHT;
  } else if (strcasecmp(string, "LEFT") == 0) {
    return MotorDir::LEFT;
  } else {
    return MotorDir::INVALID_DIRECTION;
  }
}

CounterSpecifier toCounterSpecifier(ScratchValue cunterSpecifier) {
  bool shouldFree;
  const char* string = toString(cunterSpecifier, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "C1") == 0) {
    return CounterSpecifier::C1;
  } else if (strcasecmp(string, "C2") == 0) {
    return CounterSpecifier::C2;
  } else if (strcasecmp(string, "C3") == 0) {
    return CounterSpecifier::C3;
  } else if (strcasecmp(string, "C4") == 0) {
    return CounterSpecifier::C4;
  }  else {
    return CounterSpecifier::INVALID_SPECIFIER;
  }
}

MotorSpecifier toMotorSpecifier(ScratchValue motorSpecifier) {
  bool shouldFree;
  const char* string = toString(motorSpecifier, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "M1") == 0) {
    return MotorSpecifier::M1;
  } else if (strcasecmp(string, "M2") == 0) {
    return MotorSpecifier::M2;
  } else if (strcasecmp(string, "M3") == 0) {
    return MotorSpecifier::M3;
  } else if (strcasecmp(string, "M4") == 0) {
    return MotorSpecifier::M4;
  }  else {
    return MotorSpecifier::INVALID_SPECIFIER;
  }
}

OutputSpecifier toOutputSpecifier(ScratchValue outputSpecifier) {
  bool shouldFree;
  const char* string = toString(outputSpecifier, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "O1") == 0) {
    return OutputSpecifier::O1;
  } else if (strcasecmp(string, "O2") == 0) {
    return OutputSpecifier::O2;
  } else if (strcasecmp(string, "O3") == 0) {
    return OutputSpecifier::O3;
  } else if (strcasecmp(string, "O4") == 0) {
    return OutputSpecifier::O4;
  } else if (strcasecmp(string, "O5") == 0) {
    return OutputSpecifier::O5;
  } else if (strcasecmp(string, "O6") == 0) {
    return OutputSpecifier::O6;
  } else if (strcasecmp(string, "O7") == 0) {
    return OutputSpecifier::O7;
  } else if (strcasecmp(string, "O8") == 0) {
    return OutputSpecifier::O8;
  } else {
    return OutputSpecifier::INVALID_SPECIFIER;
  }
}

AnalogInputSpecifier toAnalogInputSpecifier(ScratchValue inputSpecifier) {
  bool shouldFree;
  const char* string = toString(inputSpecifier, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "I1") == 0) {
    return AnalogInputSpecifier::I1;
  } else if (strcasecmp(string, "I2") == 0) {
    return AnalogInputSpecifier::I2;
  } else if (strcasecmp(string, "I3") == 0) {
    return AnalogInputSpecifier::I3;
  } else if (strcasecmp(string, "I4") == 0) {
    return AnalogInputSpecifier::I4;
  } else if (strcasecmp(string, "I5") == 0) {
    return AnalogInputSpecifier::I5;
  } else if (strcasecmp(string, "I6") == 0) {
    return AnalogInputSpecifier::I6;
  } else if (strcasecmp(string, "I7") == 0) {
    return AnalogInputSpecifier::I7;
  } else if (strcasecmp(string, "I8") == 0) {
    return AnalogInputSpecifier::I8;
  } else {
    return AnalogInputSpecifier::INVALID_SPECIFIER;
  }
}

DigitalInputSpecifier toDigitalInputSpecifier(ScratchValue inputSpecifier) {
  bool shouldFree;
  const char* string = toString(inputSpecifier, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "I1") == 0) {
    return DigitalInputSpecifier::I1;
  } else if (strcasecmp(string, "I2") == 0) {
    return DigitalInputSpecifier::I2;
  } else if (strcasecmp(string, "I3") == 0) {
    return DigitalInputSpecifier::I3;
  } else if (strcasecmp(string, "I4") == 0) {
    return DigitalInputSpecifier::I4;
  } else if (strcasecmp(string, "I5") == 0) {
    return DigitalInputSpecifier::I5;
  } else if (strcasecmp(string, "I6") == 0) {
    return DigitalInputSpecifier::I6;
  } else if (strcasecmp(string, "I7") == 0) {
    return DigitalInputSpecifier::I7;
  } else if (strcasecmp(string, "I8") == 0) {
    return DigitalInputSpecifier::I8;
  }  else if (strcasecmp(string, "C1") == 0) {
    return DigitalInputSpecifier::C1;
  } else if (strcasecmp(string, "C2") == 0) {
    return DigitalInputSpecifier::C2;
  } else if (strcasecmp(string, "C3") == 0) {
    return DigitalInputSpecifier::C3;
  } else if (strcasecmp(string, "C4") == 0) {
    return DigitalInputSpecifier::C4;
  } else {
    return DigitalInputSpecifier::INVALID_SPECIFIER;
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(CounterSpecifier specifier) {
  switch (specifier) {
    case CounterSpecifier::C1 : return Ftduino::C1;
    case CounterSpecifier::C2 : return Ftduino::C2;
    case CounterSpecifier::C3 : return Ftduino::C3;
    case CounterSpecifier::C4 : return Ftduino::C4;
    case CounterSpecifier::INVALID_SPECIFIER : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(MotorStopMode stopMode) {
  switch (stopMode) {
    case MotorStopMode::STOP : return Ftduino::OFF;
    case MotorStopMode::BRAKE : return Ftduino::BRAKE;
    case MotorStopMode::INVALID_MODE : assert(false); // This should never happen. Other functions should have checked that the mode is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(MotorDir dir) {
  switch (dir) {
    case MotorDir::RIGHT : return Ftduino::RIGHT;
    case MotorDir::LEFT : return Ftduino::LEFT;
    case MotorDir::INVALID_DIRECTION : assert(false); // This should never happen. Other functions should have checked that the direction is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(MotorSpecifier specifier) {
  switch (specifier) {
    case MotorSpecifier::M1 : return Ftduino::M1;
    case MotorSpecifier::M2 : return Ftduino::M2;
    case MotorSpecifier::M3 : return Ftduino::M3;
    case MotorSpecifier::M4 : return Ftduino::M4;
    case MotorSpecifier::INVALID_SPECIFIER : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(OutputSpecifier specifier) {
  switch (specifier) {
    case OutputSpecifier::O1 : return Ftduino::O1;
    case OutputSpecifier::O2 : return Ftduino::O2;
    case OutputSpecifier::O3 : return Ftduino::O3;
    case OutputSpecifier::O4 : return Ftduino::O4;
    case OutputSpecifier::O5 : return Ftduino::O5;
    case OutputSpecifier::O6 : return Ftduino::O6;
    case OutputSpecifier::O7 : return Ftduino::O7;
    case OutputSpecifier::O8 : return Ftduino::O8;
    case OutputSpecifier::INVALID_SPECIFIER : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(AnalogInputSpecifier specifier) {
  switch (specifier) {
    case AnalogInputSpecifier::I1 : return Ftduino::I1;
    case AnalogInputSpecifier::I2 : return Ftduino::I2;
    case AnalogInputSpecifier::I3 : return Ftduino::I3;
    case AnalogInputSpecifier::I4 : return Ftduino::I4;
    case AnalogInputSpecifier::I5 : return Ftduino::I5;
    case AnalogInputSpecifier::I6 : return Ftduino::I6;
    case AnalogInputSpecifier::I7 : return Ftduino::I7;
    case AnalogInputSpecifier::I8 : return Ftduino::I8;
    case AnalogInputSpecifier::INVALID_SPECIFIER : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoSpecifier(DigitalInputSpecifier specifier) {
  switch (specifier) {
    case DigitalInputSpecifier::I1 : return Ftduino::I1;
    case DigitalInputSpecifier::I2 : return Ftduino::I2;
    case DigitalInputSpecifier::I3 : return Ftduino::I3;
    case DigitalInputSpecifier::I4 : return Ftduino::I4;
    case DigitalInputSpecifier::I5 : return Ftduino::I5;
    case DigitalInputSpecifier::I6 : return Ftduino::I6;
    case DigitalInputSpecifier::I7 : return Ftduino::I7;
    case DigitalInputSpecifier::I8 : return Ftduino::I8;
    case DigitalInputSpecifier::C1 : return Ftduino::C1;
    case DigitalInputSpecifier::C2 : return Ftduino::C2;
    case DigitalInputSpecifier::C3 : return Ftduino::C3;
    case DigitalInputSpecifier::C4 : return Ftduino::C4;
    case DigitalInputSpecifier::INVALID_SPECIFIER : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

__attribute__ ((const)) uint8_t toFtduinoInputMode(InputMode inputMode) {
  switch (inputMode) {
    case InputMode::VOLTAGE : return Ftduino::VOLTAGE;
    case InputMode::RESISTANCE : return Ftduino::RESISTANCE;
    case InputMode::SWITCH : return Ftduino::SWITCH;
    case InputMode::INVALID_MODE : assert(false); // This should never happen. Other functions should have checked that the specifier is valid!
    default: assert(false);
  }
}

InputMode toInputMode(ScratchValue inputMode) {
  bool shouldFree;
  const char* string = toString(inputMode, &shouldFree);
  FreeableString(string, shouldFree);
  if (strcasecmp(string, "VOLTAGE") == 0) {
    return InputMode::VOLTAGE;
  } else if (strcasecmp(string, "RESISTANCE") == 0) {
    return InputMode::RESISTANCE;
  } else {
    return InputMode::INVALID_MODE;
  }
}

ScratchValue scratch_ftduino_input_analog(ScratchValue scratchInputSpecifier, ScratchValue scratchInputMode) {
  AnalogInputSpecifier inputSpecifier = toAnalogInputSpecifier(scratchInputSpecifier);
  InputMode inputMode = toInputMode(scratchInputMode);
  if (inputSpecifier != AnalogInputSpecifier::INVALID_SPECIFIER && inputMode != InputMode::INVALID_MODE) {
    uint8_t ftduinoInputSpecifier = toFtduinoSpecifier(inputSpecifier);
    uint8_t ftduinoInputMode = toFtduinoInputMode(inputMode);
    ftduino.input_set_mode(ftduinoInputSpecifier, ftduinoInputMode);
    return scratchNumber(ftduino.input_get(ftduinoInputSpecifier));
  } else {
    return scratchNumber(0);
  }
}

ScratchValue scratch_ftduino_input(ScratchValue scratchInputSpecifier) {
  DigitalInputSpecifier inputSpecifier = toDigitalInputSpecifier(scratchInputSpecifier);
  if (inputSpecifier != DigitalInputSpecifier::INVALID_SPECIFIER) {
    uint8_t ftduinoInputSpecifier = toFtduinoSpecifier(inputSpecifier);
    if (inputSpecifier >= DigitalInputSpecifier::I1 && inputSpecifier <= DigitalInputSpecifier::I8) {
      InputMode inputMode = InputMode::SWITCH;
      uint8_t ftduinoInputMode = toFtduinoInputMode(inputMode);
      ftduino.input_set_mode(ftduinoInputSpecifier, ftduinoInputMode);
      return scratchBoolean(ftduino.input_get(ftduinoInputSpecifier));
    } else if (inputSpecifier >= DigitalInputSpecifier::C1 && inputSpecifier <= DigitalInputSpecifier::C4) {
      return scratchBoolean(ftduino.counter_get_state(ftduinoInputSpecifier));
    } else {
      assert(false);
    }
  } else {
    return scratchBoolean(false);
  }
}

void scratch_ftduino_clear_counter(ScratchValue scratchCounterSpecifier) {
  CounterSpecifier counterSpecifier = toCounterSpecifier(scratchCounterSpecifier);
  if (counterSpecifier != CounterSpecifier::INVALID_SPECIFIER) {
    uint8_t ftduinoCounterSpecifier = toFtduinoSpecifier(counterSpecifier);
    ftduino.counter_clear(ftduinoCounterSpecifier);
  }
}

void scratch_ftduino_output(ScratchValue scratchOutputSpecifier, ScratchValue value) {
  OutputSpecifier outputSpecifier = toOutputSpecifier(scratchOutputSpecifier);
  if (outputSpecifier != OutputSpecifier::INVALID_SPECIFIER) {
    uint8_t ftduinoOutputSpecifier = toFtduinoSpecifier(outputSpecifier);
    ftduino.output_set(ftduinoOutputSpecifier, Ftduino::HI, toBoolean(value));
  }
}

void scratch_ftduino_output_analog(ScratchValue scratchOutputSpecifier, ScratchValue value) {
  OutputSpecifier outputSpecifier = toOutputSpecifier(scratchOutputSpecifier);
  if (outputSpecifier != OutputSpecifier::INVALID_SPECIFIER) {
    uint8_t ftduinoOutputSpecifier = toFtduinoSpecifier(outputSpecifier);
    ftduino.output_set(          ftduinoOutputSpecifier, Ftduino::HI, toNumber(value));
  }
}

void scratch_ftduino_motor(ScratchValue scratchMotorSpecifier, ScratchValue scratchDir, ScratchValue value) {
  MotorSpecifier motorSpecifier = toMotorSpecifier(scratchMotorSpecifier);
  MotorDir dir = toMotorDir(scratchDir);
  if (motorSpecifier != MotorSpecifier::INVALID_SPECIFIER && dir != MotorDir::INVALID_DIRECTION) {
    uint8_t ftduinoMotorSpecifier = toFtduinoSpecifier(motorSpecifier);
    uint8_t ftduinoMotorDir = toFtduinoSpecifier(dir);
    ftduino.motor_set(ftduinoMotorSpecifier, ftduinoMotorDir, toNumber(value));
  }
}


void scratch_ftduino_motor_stop(ScratchValue scratchMotorSpecifier, ScratchValue scratchStopMode) {
  MotorSpecifier motorSpecifier = toMotorSpecifier(scratchMotorSpecifier);
  MotorStopMode stopMode = toMotorStopMode(scratchStopMode);
  if (motorSpecifier != MotorSpecifier::INVALID_SPECIFIER && stopMode != MotorStopMode::INVALID_MODE) {
    uint8_t ftduinoMotorSpecifier = toFtduinoSpecifier(motorSpecifier);
    uint8_t ftduinoMotorStopMode = toFtduinoSpecifier(stopMode);
    ftduino.motor_set(ftduinoMotorSpecifier, ftduinoMotorStopMode, 100);
    /*
       Value is fixed to 100 just as the webusb online scratch version does. See
       https://github.com/harbaum/scratch-vm/blob/
       9b63c1117a27b70dc8ef10c8a2ce80d412030104/src/extensions/scratch3_ftduino/
       index.js#L822
    */
  }
}

ScratchValue scratch_ftduino_input_counter(ScratchValue scratchCounterSpecifier) {
  CounterSpecifier counterSpecifier = toCounterSpecifier(scratchCounterSpecifier);
  if (counterSpecifier != CounterSpecifier::INVALID_SPECIFIER) {
    uint8_t ftduinoCounterSpecifier = toFtduinoSpecifier(counterSpecifier);
    return scratchNumber(ftduino.counter_get(ftduinoCounterSpecifier));
  } else {
    return scratchNumber(0);
  }
}
