# RentExpresGUI

This project depends on a middleware library located in `lib/middleware`. The
provided JAR in that directory was built from an older version of the sources
and lacks some features used by the GUI. To avoid distributing binary files
and use the latest source code, you can build the middleware jar locally.

## Building the middleware library

Run the following command from the project root:

```bash
./build_middleware.sh
```

This compiles the sources under `middleware_src/src` and creates
`lib/middleware/RentExpres.jar`.

If you notice that features such as the automatic reservation ID
assignment are not working, verify that you have built the middleware JAR
with this script. Using the old prebuilt JAR may cause certain functions
to fail.

## Running

After building, compile and run the GUI as usual with your IDE or `javac`.

## Calendar view

The main window now includes a weekly calendar powered by the
SwingCalendar component provided in the project. Open it using the
"Calendario" button in the side navigation to see all `Reserva` and
`Alquiler` entries plotted on their corresponding days. Clicking an event
shows a small dialog with its details.
