package com.example.siduk.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KecamatanModel {
	private String id;
	private String id_kota;
	private String nama_kecamatan;
	private String kode_kecamatan;
	private List<KelurahanModel> kelurahans;
}
