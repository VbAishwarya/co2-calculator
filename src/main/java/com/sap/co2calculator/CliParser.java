package com.sap.co2calculator;

import java.util.HashMap;
import java.util.Map;

public class CliParser {

    private final Map<String, String> arguments = new HashMap<>();

    public CliParser(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.startsWith("--")) {
                if (arg.contains("=")) {
                    // Handle "--key=value" format
                    String[] parts = arg.split("=", 2);
                    arguments.put(parts[0].substring(2), parts[1]);
                } else if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    // Handle "--key value" format
                    arguments.put(arg.substring(2), args[i + 1]);
                    i++; // Skip next value since it's already processed
                }
            }
        }
    }

    public String getStartCity() {
        return arguments.get("start");
    }

    public String getEndCity() {
        return arguments.get("end");
    }

    public String getTransportMethod() {
        return arguments.get("transportation-method");
    }

    public boolean isValid() {
        return getStartCity() != null && getEndCity() != null && getTransportMethod() != null;
    }
}
