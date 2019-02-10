package com.bayu.sejarahbatang.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Koment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tampilkan")
    @Expose
    private String tampilkan;

    public String getTampilkan() {
        return tampilkan;
    }

    public void setTampilkan(String tampilkan) {
        this.tampilkan = tampilkan;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }

    @SerializedName("tanggapan")
    @Expose
    private String tanggapan;
    @SerializedName("id_sejarah")
    @Expose
    private String idSejarah;
    @SerializedName("tgl_komen")
    @Expose
    private String tglKomen;
    @SerializedName("komentar")
    @Expose
    private String komentar;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSejarah() {
        return idSejarah;
    }

    public void setIdSejarah(String idSejarah) {
        this.idSejarah = idSejarah;
    }

    public String getTglKomen() {
        return tglKomen;
    }

    public void setTglKomen(String tglKomen) {
        this.tglKomen = tglKomen;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}
