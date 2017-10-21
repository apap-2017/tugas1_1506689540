package com.example.siduk.service;

import com.example.siduk.model.KeluargaModel;
import com.example.siduk.model.PendudukModel;

public interface SidukService{
	PendudukModel selectPenduduk (String nik);

	KeluargaModel selectKeluarga (String nkk);
//
//	void tambahPenduduk(PendudukModel penduduk);

	void updatePenduduk(PendudukModel penduduk);

	KeluargaModel selectAlamat(String id_keluarga);

	void addPenduduk(PendudukModel penduduk);

	void addKeluarga(KeluargaModel keluarga);

	void updateKeluarga(KeluargaModel keluarga);

}
