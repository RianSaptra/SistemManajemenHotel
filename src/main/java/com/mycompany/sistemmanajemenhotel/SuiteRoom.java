/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemmanajemenhotel;

/**
 *
 * @author RaynSaptra
 */
public class SuiteRoom extends Kamar {
    private static final long serialVersionUID = 1L;

    public SuiteRoom(String kode, String nama, int kuota) {
        super(kode, nama, kuota);
    }

    @Override
    public double getHargaPerMalam() { return 1500000; }

    @Override
    public String getKategori() { return "Premium"; }

}