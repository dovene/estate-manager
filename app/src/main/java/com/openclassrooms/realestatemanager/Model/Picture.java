package com.openclassrooms.realestatemanager.Model;

public class Picture {

    private String file;
    private String description;

    public Picture(String file, String description) {
        this.file = file;
        this.description = description;
    }

    public String getFile() { return file; }

    public void setFile(String file) { this.file = file; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
