package com.example.siduk.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.siduk.dao.SidukMapper;
import com.example.siduk.model.KecamatanModel;
import com.example.siduk.model.KeluargaModel;
import com.example.siduk.model.KelurahanModel;
import com.example.siduk.model.KotaModel;
import com.example.siduk.model.PendudukModel;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class SidukServiceDatabase implements SidukService {
	@Autowired
	SidukMapper sidukMapper;
	
	
	/**
	 * METHOD SELECT PENDUDUK:
	 * Untuk mengembalikan object penduduk dengan input NIK
	 * DONE.
	 * HTML : VIEW
	 */
	@Override
	public PendudukModel selectPenduduk (String nik){
		log.info("select penduduk dengan nik {}", nik);
		return sidukMapper.selectPenduduk(nik);
	}
	
	@Override
	public KeluargaModel selectKeluarga (String nkk){
		log.info("select penduduk dengan nkk {}", nkk);
		return sidukMapper.selectKeluarga(nkk);
	}

	/**
	 * METHOD UPDATE PENDUDUK:
	 * Update informasi penduduk,
	 * bisa generate nik baru jika,
	 * id_keluarga, jenis_kelamin dan tgl lahir berubah
	 * DONE.
	 * HTML : PENDUDUK-UPDATE
	 */
	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		String nik = generateNIK(penduduk);
		String niklama = penduduk.getNik();
		log.info("update penduduk nik lama {}", niklama);
		log.info("update penduduk nik baru {}", nik);
		sidukMapper.updatePenduduk(penduduk);
		if(!nik.equals(penduduk.getNik())) {
			sidukMapper.updateNIK(nik, niklama);
		}
		log.info("update kelamin penduduk {}", penduduk.getJenis_kelamin());
		
	}

	/**
	 * METHOD SELECT ALAMAT:
	 * mengambil object keluarga berdasarkan id_keluarga
	 * DONE.
	 * HTML :VIEW
	 */
	@Override
	public KeluargaModel selectAlamat(String id_keluarga) {
		log.info("select alamat penduduk dengan id {}", id_keluarga);
		return sidukMapper.selectAlamat(id_keluarga);
	}

	/**
	 * METHOD ADD PENDUDUK:
	 * menambah penduduk baru + generateNIK
	 * is_wafat ototmatis 0 tiap dimasukan.
	 * (asumsi setiap penduduk yang baru dimasukan selalu hidup)
	 * DONE.
	 * HTML: ADDPENDUDUK
	 */
	@Override
	public void addPenduduk(PendudukModel penduduk) {
		penduduk.setNik(generateNIK(penduduk));
		penduduk.setIs_wafat("0");
		log.info("add penduduk dengan id {} berhasil!", penduduk.getNik());
		sidukMapper.addPenduduk(penduduk);
		
	}
	
	/**
	 * METHOD GENERATE NIK:
	 * Membuat nomor induk kependudukan untuk penduduk sesuai data yang diberikan
	 * @param penduduk
	 * @return String nik
	 */
	public String generateNIK(PendudukModel penduduk) {
		String nik = sidukMapper.selectAlamat(penduduk.getId_keluarga()).getNomor_kk().substring(0,6);
		log.info("add penduduk dengan id 3 {}", nik);
		
		String[] tgl = penduduk.getTanggal_lahir().split("-");
		int tanggal = Integer.parseInt(tgl[2]) + Integer.parseInt(penduduk.getJenis_kelamin())*40;
		if(tanggal<10) nik+="0";
		nik += tanggal + tgl[1]+tgl[0].substring(2);
		log.info("add penduduk dengan id 2 {}", nik);
		PendudukModel doubles = sidukMapper.checkDoubleNIK(nik);
		
		if (doubles == null) {
			log.info("belum ada nik tersebut {}", nik);
			nik += "0001";
		} 
		else {
			long doublesL = Long.parseLong(doubles.getNik())+1;
			nik = doublesL+"";
			log.info(nik+" berhasil");
		}
		return nik;
	}


	@Override
	public void addKeluarga(KeluargaModel keluarga) {
		keluarga.setNomor_kk(generateNkk(keluarga));
		keluarga.setIs_tidak_berlaku("0");
		log.info(keluarga.getNomor_kk().substring(0, 6));
		String id = sidukMapper.checkDoubleNKK(keluarga.getNomor_kk().substring(0, 6)).getId_kelurahan();
		log.info(id);
		keluarga.setId_kelurahan(id);
		sidukMapper.addKeluarga(keluarga);
	}
	/**
	 * Method Generate NKK:
	 * membuat nomorkk sesuai masukan
	 * @param keluarga
	 * @return String nomor_kk
	 */
	private String generateNkk( KeluargaModel keluarga) {
		String nkk = sidukMapper.selectNoKelurahan(keluarga.getKelurahan()).substring(0,6);
		log.info("Nomor KK 1 {}", nkk);
		LocalDate date = LocalDate.now(); 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
		nkk+= date.format(formatter);
		log.info(nkk);
		KeluargaModel doubles = sidukMapper.checkDoubleNKK(nkk);
		
		if (doubles == null) {
			log.info("belum ada nkk tersebut {}", nkk+"0001");
			nkk += "0001";
		}
		else {
			long doublesL = Long.parseLong(doubles.getNomor_kk())+1;
			nkk = doublesL+"";
			log.info(nkk+" berhasil");
		}
		return nkk;
	}

	@Override
	public void updateKeluarga(KeluargaModel keluarga, String nkk) {
		String nkkbaru = generateNkk(keluarga);
		KeluargaModel kellama = sidukMapper.checkDoubleNKK(nkk);
		log.info("NKK lama {}", kellama.getNomor_kk());
		String nkid = sidukMapper.selectNoKelurahan(keluarga.getKelurahan()).substring(0,6);
		String id = sidukMapper.checkDoubleNKK(nkid.substring(0, 6)).getId_kelurahan();
		log.info(id);
		sidukMapper.updateKeluarga(keluarga.getAlamat(), id, keluarga.getRt(), keluarga.getRw(), kellama.getNomor_kk());
		log.info("NKK baru {}", nkkbaru);
		if (!kellama.getNomor_kk().equals(nkkbaru))
		sidukMapper.updateNKK(nkkbaru, kellama.getNomor_kk());
		
	}

	@Override
	public List<KotaModel> getListKota() {
		return sidukMapper.getListKota();
	}
	
	@Override
	public List<KelurahanModel> getListKelurahan(String id_kecamatan) {
		return sidukMapper.getListKelurahan(id_kecamatan);
	}
	
	@Override
	public List<KecamatanModel> getListKecamatan(String id_kota) {
		return sidukMapper.getListKecamatan(id_kota);
	}

	@Override
	public String updateKematian(String nik) {
		sidukMapper.updateKematian(nik);
		log.info("diediediedie");
		KeluargaModel id = sidukMapper.selectKeluargaByAnggota(nik);
		log.info(id.getNomor_kk());
		List<PendudukModel> anggota = sidukMapper.selectAnggotaDariIdFam(nik);
		List<PendudukModel> anggotamati = sidukMapper.selectAnggotaMati(id.getNomor_kk());
		if (anggota.equals(anggotamati)) {
		sidukMapper.updateStatKeluarga(id.getNomor_kk());
		log.info("diediediedie 3");
		} return "1" ;
	}
	

}
