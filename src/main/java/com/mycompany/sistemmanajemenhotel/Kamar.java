/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemmanajemenhotel;

/**
 *
 * @author RaynSaptra
 */
import java.io.Serializable;

public abstract class Kamar implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kode;
    private String nama;
    private int kuota;

    public Kamar(String kode, String nama, int kuota) {
        this.kode = kode;
        this.nama = nama;
        this.kuota = kuota;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getKuota() { return kuota; }

    public void pesan(int jumlah) throws KamarPenuhException {
        if (jumlah > kuota) {
            throw new KamarPenuhException("Kuota " + nama + " (" + kode + ") tidak cukup. Sisa: "
                    + kuota + ", diminta: " + jumlah);
        }
        kuota -= jumlah;
    }

    public abstract double getHargaPerMalam();

    public abstract String getKategori();

    public double hitungBiaya(int malam) {
        return getHargaPerMalam() * malam;
    }
}