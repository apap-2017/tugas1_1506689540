package com.example.siduk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.siduk.model.KeluargaModel;
import com.example.siduk.model.PendudukModel;
import com.example.siduk.service.SidukService;


@Controller
public class SidukController {
	
	@Autowired
    SidukService sidukDAO;
	
	@RequestMapping("/")
    public String index ()
    {
        return "index";
    }
	
	@RequestMapping("/penduduk")
    public String viewPenduduk (Model model,
            @RequestParam(value = "nik", required = false) String nik)
    {
        PendudukModel penduduk = sidukDAO.selectPenduduk (nik);

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

        if (keluarga != null) {
            model.addAttribute ("keluarga", keluarga);
            return "viewkk";
        } else {
            model.addAttribute ("nkk", nkk);
            return "not-found";
        }
    }
	
	@RequestMapping("/penduduk/ubah/{nik}")
    public String ubahPenduduk (Model model,
            @PathVariable(value = "nik") String nik)
    {
        PendudukModel penduduk = sidukDAO.selectPenduduk (nik);

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
    	return "success-update";
    }

	@RequestMapping("/penduduk/tambah")
	public String addPenduduk() {
		return "addpenduduk";
	}
	
	@RequestMapping(value="/penduduk/tambah", method=RequestMethod.POST)
	public String addPendudukModel(@ModelAttribute PendudukModel penduduk) {
		sidukDAO.addPenduduk(penduduk);
		return "success-add";
	}
	
//	@RequestMapping("/keluarga/tambah")
//	public String addKeluarga() {
//		return "addkeluarga";
//	}
//	
//	@RequestMapping(value="/keluarga/tambah", method=RequestMethod.POST)
//	public String addKeluargaModel(@ModelAttribute KeluargaModel keluarga) {
//		sidukDAO.addKeluarga(keluarga);
//		return "success-add";
//	}
}
