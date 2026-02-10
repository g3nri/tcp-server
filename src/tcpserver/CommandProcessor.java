package tcpserver;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CommandProcessor {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public String process(String input) {
        // Remove leading and trailing whitespace
        input = input.trim();

        // Split input into command and argument
        // parts[0] = command
        // parts[1] = argument.
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toUpperCase();
        String argument = parts.length > 1 ? parts[1] : "";

        // Command dispatcher
        switch (command) {
            case "TIME":
                return !argument.isEmpty()
                        ? "ERROR: TIME does not accept arguments"
                        : LocalTime.now().format(TIME_FORMAT);

            case "UPPER":
                return argument.isEmpty()
                        ? "ERROR: Missing text"
                        : argument.toUpperCase();

            case "REVERSE":
                return argument.isEmpty()
                        ? "ERROR: Missing text"
                        : new StringBuilder(argument).reverse().toString();

            case "QUIT":
                return "QUIT";

            default:
                if (parts.length == 1) {
                    return "ERROR: Unknown command";
                }
                return "ECHO: " + input;
        }
    }
}
