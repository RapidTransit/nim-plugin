package org.nim.project;

public class ProjectData {
    protected String name;
    protected String author;
    protected String description;
    protected String license;
    protected String nimVersion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getNimVersion() {
        return nimVersion;
    }

    public void setNimVersion(String nimVersion) {
        this.nimVersion = nimVersion;
    }
}
