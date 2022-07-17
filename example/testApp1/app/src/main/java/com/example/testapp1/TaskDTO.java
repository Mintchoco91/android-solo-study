package com.example.testapp1;

public class TaskDTO {
    private modeType mode;
    private String result;

    public modeType getMode() {
        return mode;
    }

    public void setMode(modeType mode) {
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public enum modeType {
        INSERT,
        UPDATE,
        SELECT,
        DELETE
    }
}
