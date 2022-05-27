package com.piesat.quartz.constant;

public enum FileTypeEnum {

    HTML("HTML", "html"),
    XML("XML", "xml"),
    JSON("JSON", "json"),
    TEXT("TEXT", "txt"),
    CSV("CSV", "csv");

    private String type;
    private String suffix;

    private FileTypeEnum(String type, String desc) {
        this.type = type;
        this.suffix = desc;
    }

    public static String getValue(String type) {
        FileTypeEnum[] fileTypeEnums = values();
        for (FileTypeEnum fileTypeEnum : fileTypeEnums) {
            if (fileTypeEnum.type().equals(type)) {
                return fileTypeEnum.suffix();
            }
        }
        return null;
    }

    public static String getType(String desc) {
        FileTypeEnum[] fileTypeEnums = values();
        for (FileTypeEnum fileTypeEnum : fileTypeEnums) {
            if (fileTypeEnum.suffix().equals(desc)) {
                return fileTypeEnum.type();
            }
        }
        return null;
    }

    private String type() {
        return this.type;
    }

    private String suffix() {
        return this.suffix;
    }
}
