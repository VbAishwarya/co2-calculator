package com.sap.co2calculator;

public class CliParser {

    private String startCity;
    private String endCity;
    private String transportMethod;

    public CliParser(String[] args) {
        for (int i = 0; i < args.length - 1; i++){
            if (args[i].equals("--start")) {
                startCity = args[i + 1];
            } else if (args[i].equals("--end")) {
                endCity = args[i + 1];
            } else if (args[i].equals("--transportation-method")) {
                transportMethod = args[i + 1];
            }
        }
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getTransportMethod() {
        return transportMethod;
    }

    public boolean isValid(){
        return startCity != null && endCity != null && transportMethod != null;
    }
}
