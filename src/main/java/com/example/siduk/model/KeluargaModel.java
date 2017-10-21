package com.example.siduk.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	private String id;
	private String nomor_kk;
	private String alamat;
	private String rt;
	private String rw;
	private String id_kelurahan;
	private String kelurahan;
	private String kecamatan;
	private String kota;
	private boolean is_tidak_berlaku;
	private List<PendudukModel> anggota;
}
