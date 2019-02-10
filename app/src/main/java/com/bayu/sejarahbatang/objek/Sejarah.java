package com.bayu.sejarahbatang.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sejarah {
    @SerializedName("id_sejarah")
    @Expose
    private String idSejarah;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("kecamatan")
    @Expose
    private String kecamatan;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("jam_buka")
    @Expose
    private String jamBuka;
    @SerializedName("jam_tutup")
    @Expose
    private String jamTutup;
    @SerializedName("sejarah")
    @Expose
    private String sejarah;
    @SerializedName("sumber")
    @Expose
    private String sumber;
    @SerializedName("jarak")
    @Expose
    private String jarak;

    public String getIdSejarah() {
        return idSejarah;
    }

    public void setIdSejarah(String idSejarah) {
        this.idSejarah = idSejarah;
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

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getJamTutup() {
        return jamTutup;
    }

    public void setJamTutup(String jamTutup) {
        this.jamTutup = jamTutup;
    }

    public String getSejarah() {
        return sejarah;
    }

    public void setSejarah(String sejarah) {
        this.sejarah = sejarah;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

}
