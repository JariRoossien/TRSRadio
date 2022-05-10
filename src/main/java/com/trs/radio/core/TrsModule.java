package com.trs.radio.core;

import java.util.logging.Logger;

public abstract class TrsModule {

    public TrsModule() {
        Logger.getLogger(getClass().getName()).info("Succesfully loaded in Module: " + getClass().getSimpleName());
    }

    public abstract void setup();
}
