/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemmanajemenhotel;

/**
 *
 * @author RaynSaptra
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemHotel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaHotel;
    private HashMap<String, Tamu> daftarTamu = new HashMap<>();
    private ArrayList<Kamar> daftarKamar = new ArrayList<>();
    private ArrayList<Transaksi> logTransaksi = new ArrayList<>();

    public SistemHotel(String namaHotel) {
        this.namaHotel = namaHotel;
    }

    public void tambahTamu(String id, String nama) {
        daftarTamu.put(id, new Tamu(id, nama));
    }

    public void tambahKamar(String kode, String nama, int kuota) {
        Kamar k;
        if (nama.contains("Suite")) {
            k = new SuiteRoom(kode, nama, kuota);
        } else if (nama.contains("Reguler")) {
            k = new RegulerRoom(kode, nama, kuota);
        } else {
            k = new StandardRoom(kode, nama, kuota);
        }
        daftarKamar.add(k);
    }

    private Kamar cariKamar(String kode) throws DataTidakDitemukanException {
        for (Kamar k : daftarKamar) {
            if (k.getKode().equals(kode)) return k;
        }
        throw new DataTidakDitemukanException("Kamar dengan kode " + kode + " tidak ditemukan");
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                sb.append(baris).append(" ");
            }
        }

        String[] daftarTrx = sb.toString().trim().split("\\s+");
        int nomor = 0;
        for (String mentah : daftarTrx) {
            if (mentah.isEmpty()) continue;
            nomor++;
            try {
                prosesSatuTransaksi(mentah);
            } catch (FormatTransaksiException | DataTidakDitemukanException | KamarPenuhException e) {
                System.out.println("[GAGAL] Transaksi #" + nomor + " (" + mentah + "): " + e.getMessage());
            }
        }
    }

    private void prosesSatuTransaksi(String mentah)
            throws FormatTransaksiException, DataTidakDitemukanException, KamarPenuhException {
        if (mentah.endsWith(",")) mentah = mentah.substring(0, mentah.length() - 1);
        String[] field = mentah.split(",");
        if (field.length != 3) {
            throw new FormatTransaksiException("Jumlah field tidak sesuai: " + mentah);
        }

        String id = null, kd = null;
        int jml;
        try {
            for (String f : field) {
                String[] kv = f.split("=");
                if (kv.length != 2) throw new FormatTransaksiException("Field rusak: " + f);
                switch (kv[0]) {
                    case "ID": id = kv[1]; break;
                    case "KD": kd = kv[1]; break;
                    case "JML": break;
                    default: throw new FormatTransaksiException("Key tidak dikenal: " + kv[0]);
                }
            }
            jml = Integer.parseInt(field[2].split("=")[1]);
        } catch (NumberFormatException e) {
            throw new FormatTransaksiException("JML bukan angka: " + mentah);
        }

        if (id == null || kd == null) {
            throw new FormatTransaksiException("ID/KD tidak lengkap: " + mentah);
        }

        Tamu tamu = daftarTamu.get(id);
        if (tamu == null) {
            throw new DataTidakDitemukanException("Tamu dengan ID " + id + " tidak ditemukan");
        }
        Kamar kamar = cariKamar(kd);

        kamar.pesan(jml); 
        Transaksi t = new Transaksi(tamu, kamar, jml);
        logTransaksi.add(t);
        System.out.println("[OK] " + tamu.getNama() + " memesan " + jml + " x " + kamar.getNama()
                + " | total Rp" + String.format("%,.0f", t.getTotalBiaya()));
    }

    public void simpanLaporanAkhir() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("archive_mem.bin"))) {
            oos.writeObject(this);
            System.out.println("[INFO] Status memori disimpan ke archive_mem.bin");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal serialisasi: " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter("laporan.txt")) {
            fw.write("=== LAPORAN SISA KUOTA KAMAR - " + namaHotel + " ===\n");
            for (Kamar k : daftarKamar) {
                fw.write(k.getKode() + " | " + k.getNama() + " | Sisa kuota: " + k.getKuota() + "\n");
            }
            System.out.println("[INFO] Laporan sisa kuota ditulis ke laporan.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan: " + e.getMessage());
        }
    }
}