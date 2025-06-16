package com.pinguela.rentexpres.desktop.util;

/** Simple Thread wrapper that executes an ActionCallback. */
public class ActionCallbackThread extends Thread {
    private final ActionCallback action;

    public ActionCallbackThread(ActionCallback action) {
        this.action = action;
    }

    @Override
    public void run() {
        if (action != null) {
            action.execute();
        }
    }
}
