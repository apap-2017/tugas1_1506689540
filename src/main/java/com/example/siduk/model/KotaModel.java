package com.example.siduk.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KotaModel {
	private String id;
	private String nama_kota;
	private String kode_kota;
	private List<KecamatanModel> kecamatans;
}
