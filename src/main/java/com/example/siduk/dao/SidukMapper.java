package com.example.siduk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.siduk.model.KecamatanModel;
import com.example.siduk.model.KeluargaModel;
import com.example.siduk.model.KelurahanModel;
import com.example.siduk.model.KotaModel;
import com.example.siduk.model.PendudukModel;

@Mapper
public interface SidukMapper {
	@Select("select * from penduduk where nik = #{nik}")
	PendudukModel selectPenduduk(@Param("nik") String nik);
	
	@Select("select * from penduduk where id_keluarga = #{id}")
	List<PendudukModel> selectAnggota(@Param("id") String id);
	
	 @Select("select * from keluarga join kelurahan on id_kelurahan=kelurahan.id"
	 		+ " join kecamatan on kecamatan.id=id_kecamatan join kota on kota.id=id_kota "
	 		+ "where nomor_kk = #{nkk}")
	    @Results(value= {
	    		@Result(property="nomor_kk", column="nomor_kk"),
	    		@Result(property="alamat", column="alamat"),
	    		@Result(property="rt", column="rt"),
	    		@Result(property="rw", column="rw"),
	    		@Result(property="id_kelurahan", column="id_kelurahan"),
	    		@Result(property="kelurahan", column="nama_kelurahan"),
	    		@Result(property="kecamatan", column="nama_kecamatan"),
	    		@Result(property="kota", column="nama_kota"),
	    		@Result(property="is_tidak_berlaku", column="is_tidak_berlaku"),
	    		@Result(property="anggota", column="id",
	    		javaType=List.class, 
	    		many=@Many(select="selectAnggota"))
	    })
	KeluargaModel selectKeluarga(@Param("nkk") String nkk);

	 @Select("select * from keluarga join kelurahan on id_kelurahan=kelurahan.id"
		 		+ " join kecamatan on kecamatan.id=id_kecamatan join kota on kota.id=id_kota "
		 		+ "where keluarga.id = #{id_keluarga}")
		    @Results(value= {
		    		@Result(property="nomor_kk", column="nomor_kk"),
		    		@Result(property="alamat", column="alamat"),
		    		@Result(property="rt", column="rt"),
		    		@Result(property="rw", column="rw"),
		    		@Result(property="id_kelurahan", column="id_kelurahan"),
		    		@Result(property="kelurahan", column="nama_kelurahan"),
		    		@Result(property="kecamatan", column="nama_kecamatan"),
		    		@Result(property="kota", column="nama_kota"),
		    		@Result(property="is_tidak_berlaku", column="is_tidak_berlaku"),
		    		@Result(property="anggota", column="id",
		    		javaType=List.class, 
		    		many=@Many(select="selectAnggota"))
		    })
	KeluargaModel selectAlamat(String id_keluarga);

	@Insert ("INSERT INTO penduduk (nik,nama,tanggal_lahir,tempat_lahir,jenis_kelamin,is_wni,id_keluarga,agama,"
			+ "pekerjaan,status_perkawinan,status_dalam_keluarga,golongan_darah,is_wafat"
			+ ") values(#{nik} , #{nama} , #{tanggal_lahir} , #{tempat_lahir} , "
			+ "#{jenis_kelamin} , #{is_wni} , #{id_keluarga} , #{agama} , #{pekerjaan} , #{status_perkawinan} , "
			+ "#{status_dalam_keluarga} , #{golongan_darah} , #{is_wafat})")
	void addPenduduk(PendudukModel penduduk);

	@Select("select * from penduduk where nik like CONCAT(#{nik},'%') order by nik desc limit 1")
	PendudukModel checkDoubleNIK(String nik);
	
	
	 @Update ("update penduduk set nama=#{nama}, "
	 		+ "id_keluarga=#{id_keluarga}, pekerjaan=#{pekerjaan}, jenis_kelamin=#{jenis_kelamin}, "
	 		+ "agama=#{agama}, tanggal_lahir=#{tanggal_lahir}, is_wni=#{is_wni},"
	 		+ " status_dalam_keluarga=#{status_dalam_keluarga}, golongan_darah=#{golongan_darah},"
	 		+ " tempat_lahir=#{tempat_lahir}, status_perkawinan=#{status_perkawinan} where nik=#{nik}")
	 void updatePenduduk(PendudukModel penduduk);
	 
	@Update ("UPDATE penduduk set nik=#{nik} where nik=#{niklama}")
	void updateNIK(@Param("nik") String nik, @Param("niklama") String niklama);
	
	
	@Select("select kode_kelurahan from kelurahan where nama_kelurahan=#{kelurahan}")
	String selectNoKelurahan(String kelurahan);

	@Select("select * from keluarga where nomor_kk like CONCAT(#{nkk},'%') order by nomor_kk desc limit 1")
	KeluargaModel checkDoubleNKK(String nkk);

	@Insert("INSERT INTO keluarga (nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku) values (#{nomor_kk}, #{alamat}, #{rt}, #{rw}, "
			+ "#{id_kelurahan}, #{is_tidak_berlaku})")
	void addKeluarga(KeluargaModel keluarga);
	
	@Update ("UPDATE keluarga set alamat=#{alamat}, rt=#{rt}, rw=#{rw}, "
			+ "id_kelurahan=#{id_kelurahan} where nomor_kk=#{nomor_kk}")
	void updateKeluarga(@Param("alamat") String alamat, @Param("id_kelurahan") String id_kelurahan, 
			@Param("rt") String rt, @Param("rw") String rw, @Param("nomor_kk") String nkklama);
	
	@Update ("UPDATE keluarga set nomor_kk=#{nkkbaru} where nomor_kk=#{nkklama}")
	void updateNKK(@Param("nkkbaru") String nkkbaru, @Param("nkklama") String nkklama);
	
	@Select("select * from kota")
	List<KotaModel> getListKota();

	@Select("select * from kelurahan where id_kecamatan=#{id_kecamatan}")
	List<KelurahanModel> getListKelurahan(String id_kecamatan);

	@Select("select * from kelurahan where id_kota=#{id_kota}")
	List<KecamatanModel> getListKecamatan(String id_kota);
	
	@Update ("update penduduk set is_wafat='1' where nik=#{nik}")
	void updateKematian(String nik);
	
	@Update ("update keluarga set is_tidak_berlaku='1' where nomor_kk=#{nomor_kk}")
	void updateStatKeluarga(String nomor_kk);
	
	@Select ("select * from penduduk join keluarga on id_keluarga=keluarga.id where "
			+ "id_keluarga in (select id_keluarga from penduduk where nik=#{nik})")
	List<PendudukModel> selectAnggotaDariIdFam(String nik);
	
	@Select ("select * from penduduk join keluarga on id_keluarga=keluarga.id where nomor_kk=#{nomor_kk} and is_wafat='1'")
	List<PendudukModel> selectAnggotaMati(String nomor_kk);

	@Select ("select * from keluarga join penduduk on id_keluarga=keluarga.id where nik=#{nik} limit 1")
	KeluargaModel selectKeluargaByAnggota(String nik);
	

}
