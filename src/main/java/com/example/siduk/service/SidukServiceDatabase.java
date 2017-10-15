package com.example.siduk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.siduk.dao.SidukMapper;
import com.example.siduk.model.KeluargaModel;
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
	 * HTML : penduduk-update NOT DONE.
	 */
	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		String nikbaru = generateNIK(penduduk);
		log.info("update penduduk nik lama {}", penduduk.getNik());
		log.info("update penduduk nik baru {}", nikbaru);
		sidukMapper.updatePenduduk(penduduk);
		sidukMapper.updateNik(penduduk.getNik(), nikbaru);
		log.info("update kelamin penduduk {}", penduduk.getJenis_kelamin());
		
	}

	/**
	 * METHOD SELECT ALAMAT:
	 * mengambil object keluarga berdasarkan id_keluarga
	 * DONE.
	 * HTML :VIEW DONE
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
	 * HTML: addpenduduk NOT DONE
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
	 * @return
	 */
	public String generateNIK(PendudukModel penduduk) {
		String nik = sidukMapper.selectAlamat(penduduk.getId_keluarga()).getNomor_kk().substring(0,6);
		log.info("add penduduk dengan id 3 {}", nik);
		
		String[] tgl = penduduk.getTanggal_lahir().split("-");
		nik += tgl[0].substring(2)+tgl[1];
		nik += (Integer.parseInt(tgl[2]) + 
				Integer.parseInt(penduduk.getJenis_kelamin())*40) + "";
				
		log.info("add penduduk dengan id 2 {}", nik);
		PendudukModel doubles = sidukMapper.checkDouble(nik);
		
		if (doubles == null || penduduk.getNik().equals(doubles.getNik())) {
			log.info("belum ada nik tersebut {}", nik+"0001");
			nik += "0001";
		}
		else {
			long doublesL = Long.parseLong(doubles.getNik())+1;
			nik = doublesL+"";
			log.info(nik+" berhasil");
		}
		return nik;
	}

//	@Override
//	public void addKeluarga(KeluargaModel keluarga) {
//		keluarga.setNomor_kk(generateNkk(keluarga));
//		
//	}

//	private String generateNkk( KeluargaModel keluarga) {
//		String nkk = keluarga.getNomor_kk().substring(0,6);
//		return nkk;
//	}

}
