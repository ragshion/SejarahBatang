package com.bayu.sejarahbatang.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Carousel {

    @SerializedName("id_files")
    @Expose
    private String idFiles;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("id_sejarah")
    @Expose
    private String idSejarah;

    public String getIdFiles() {
        return idFiles;
    }

    public void setIdFiles(String idFiles) {
        this.idFiles = idFiles;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getIdSejarah() {
        return idSejarah;
    }

    public void setIdSejarah(String idSejarah) {
        this.idSejarah = idSejarah;
    }

}
