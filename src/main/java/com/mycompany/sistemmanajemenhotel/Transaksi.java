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

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tamu tamu;
    private Kamar kamar;
    private int jumlah;
    private double totalBiaya;

    public Transaksi(Tamu tamu, Kamar kamar, int jumlah) {
        this.tamu = tamu;
        this.kamar = kamar;
        this.jumlah = jumlah;
        this.totalBiaya = kamar.hitungBiaya(jumlah);
    }

    public Tamu getTamu() { return tamu; }
    public Kamar getKamar() { return kamar; }
    public int getJumlah() { return jumlah; }
    public double getTotalBiaya() { return totalBiaya; }
}