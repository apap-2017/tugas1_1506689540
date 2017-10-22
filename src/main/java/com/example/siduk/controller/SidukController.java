package com.example.siduk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.siduk.model.KeluargaModel;
import com.example.siduk.model.KotaModel;
import com.example.siduk.model.PendudukModel;
import com.example.siduk.service.SidukService;


@Controller
public class SidukController {
	
	@Autowired
    SidukService sidukDAO;
	
	@RequestMapping("/")
    public String index (Model model)
    {
		model.addAttribute("title", "Sistem Kependudukan DKI Jakarta");
        return "index";
    }
	
	@RequestMapping("/penduduk/cari")
	public String cariPenduduk(Model model) {
		model.addAttribute("title", "Pencarian Penduduk");
		List<KotaModel> kotas = sidukDAO.getListKota();
		model.addAttribute("kotas", kotas);
		return "cari-data";
	}
	
	@RequestMapping("/penduduk")
    public String viewPenduduk (Model model,
            @RequestParam(value = "nik", required = false) String nik)
    {
        PendudukModel penduduk = sidukDAO.selectPenduduk (nik);
        model.addAttribute("title", "Detil Penduduk");

        if (penduduk != null) {
            model.addAttribute ("penduduk", penduduk);
            model.addAttribute ("keluarga", sidukDAO.selectAlamat(penduduk.getId_keluarga()));
            return "view";
        } 
            model.addAttribute ("nik", nik);
            return "not-found";
    }
	
	@RequestMapping("/keluarga")
    public String viewKK (Model model,
            @RequestParam(value = "nkk", required = false) String nkk)
    {
        KeluargaModel keluarga = sidukDAO.selectKeluarga (nkk);
        model.addAttribute("title", "Detil Keluarga");
        
        if (keluarga != null) {
            model.addAttribute ("keluarga", keluarga);
            return "viewkk";
        } else {
            model.addAttribute ("nkk", nkk);
            return "not-found";
        }
    }
	

	@RequestMapping("/penduduk/tambah")
	public String addPenduduk(Model model) {
        model.addAttribute("title", "Tambah Penduduk Baru");
		return "addpenduduk";
	}
	
	@RequestMapping(value="/penduduk/tambah", method=RequestMethod.POST)
	public String addPendudukModel(Model model, @ModelAttribute PendudukModel penduduk) {
		model.addAttribute("title", "Sukses ditambahkan");
		sidukDAO.addPenduduk(penduduk);
		model.addAttribute("tipe_sukses", "Penduduk dengan NIK "+penduduk.getNik());
		return "success-add";
	}
	
	@RequestMapping("/keluarga/tambah")
	public String addKeluarga() {
		return "addkeluarga";
	}
	
	@RequestMapping(value="/keluarga/tambah", method=RequestMethod.POST)
	public String addKeluargaModel(Model model, @ModelAttribute KeluargaModel keluarga) {
		sidukDAO.addKeluarga(keluarga);
		model.addAttribute("tipe_sukses", "Keluarga dengan NKK "+keluarga.getNomor_kk());
		return "success-add";
	}
	
	@RequestMapping("/penduduk/ubah/{nik}")
    public String ubahPenduduk (Model model,
            @PathVariable(value = "nik") String nik)
    {
        PendudukModel penduduk = sidukDAO.selectPenduduk (nik);
        model.addAttribute("title", "Perbaharui Keluarga - "+penduduk.getNik());

        if (penduduk != null) {
            model.addAttribute ("penduduk", penduduk);
            return "penduduk-update";
        } else {
            model.addAttribute ("nik", nik);
            return "not-found";
        }
    }
	
	@RequestMapping(value="/penduduk/ubah/{nik}", method=RequestMethod.POST)
    public String pendudukSubmit(Model model, @ModelAttribute PendudukModel penduduk) {
    	sidukDAO.updatePenduduk(penduduk);
    	model.addAttribute(penduduk);
        model.addAttribute("tipe_sukses", "Data Penduduk dengan NIK "+penduduk.getNik());
    	return "success-update";
    }
	
	@RequestMapping(value="/penduduk/mati")
	public String nonAktifPenduduk(Model model, @RequestParam(value = "nik", required = false) String nik) {
		sidukDAO.updateKematian(nik);
		model.addAttribute("link", "/penduduk?nik="+nik);
		model.addAttribute("tipe_sukses", "Penduduk dengan NIK "+nik);
		return "mati";
	}
	
	@RequestMapping("/keluarga/ubah/{nkk}")
    public String ubahKeluarga (Model model,
            @PathVariable(value = "nkk") String nkk)
    {
        KeluargaModel keluarga = sidukDAO.selectKeluarga (nkk);
        model.addAttribute("title", "Perbaharui Keluarga - "+nkk);

        if (keluarga != null) {
            model.addAttribute ("keluarga", keluarga);
            model.addAttribute("tipe_sukses", "Data Keluarga dengan NKK "+keluarga.getNomor_kk());
            return "keluarga-update";
        } else {
            model.addAttribute ("nkk", nkk);
            return "not-found";
        }
    }
	
	@RequestMapping(value="/keluarga/ubah/{nkk}", method=RequestMethod.POST)
    public String keluargaSubmit(Model model, @PathVariable(value = "nkk") String nkk, @ModelAttribute KeluargaModel keluarga) {
    	sidukDAO.updateKeluarga(keluarga,nkk);
    	model.addAttribute(keluarga);
        model.addAttribute("title", "Perbaharui Keluarga - "+nkk);
    	return "success-update";
    }
}
