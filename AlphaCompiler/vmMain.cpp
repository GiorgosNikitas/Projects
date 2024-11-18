#include "vmLib.h"

int main() {
    read_from_binary();
    
    for (int i = 0; i < AVM_STACKSIZE; i++) {
        AVM_WIPEOUT(avmstack[i]);
        avmstack[i].type = undef_m;
    }

    top = topsp = AVM_STACKSIZE - programVarOffset - 1;

    while (executionFinished == 0) {
        execute_cycle();
    }

    return 0;
}