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

import com.example.siduk.model.KeluargaModel;
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
	
	
	 @Update ("update penduduk set nik=#{nikbaru}, nama=#{penduduk.nama}, "
	 		+ "id_keluarga=#{penduduk.id_keluarga}, pekerjaan=#{penduduk.pekerjaan}, jenis_kelamin=#{penduduk.jenis_kelamin}, "
	 		+ "agama=#{penduduk.agama}, tanggal_lahir=#{penduduk.tanggal_lahir}, is_wni=#{penduduk.is_wni},"
	 		+ " status_dalam_keluarga=#{penduduk.status_dalam_keluarga}, golongan_darah=#{penduduk.golongan_darah},"
	 		+ " tempat_lahir=#{penduduk.tempat_lahir}, status_perkawinan=#{penduduk.status_perkawinan} where nik=#{penduduk.nik}")
	 void updatePenduduk(@Param("penduduk") PendudukModel penduduk, @Param("nikbaru") String nikbaru);
	 
//	@Update("UPDATE penduduk set nik=#{nikbaru} WHERE nik=#{niklama}")
//	void updateNik(@Param("niklama") String niklama,@Param("nikbaru") String nikbaru);
	
	
	@Select("select kode_kelurahan from kelurahan where nama_kelurahan=#{kelurahan}")
	String selectNoKelurahan(String kelurahan);

	@Select("select * from keluarga where nomor_kk like CONCAT(#{nkk},'%') order by nomor_kk desc limit 1")
	KeluargaModel checkDoubleNKK(String nkk);

	@Insert("INSERT INTO keluarga (nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku) values (#{nomor_kk}, #{alamat}, #{rt}, #{rw}, "
			+ "#{id_kelurahan}, #{is_tidak_berlaku})")
	void addKeluarga(KeluargaModel keluarga);
	
	@Update ("UPDATE keluarga set nomor_kk=#{nkkbaru}, alamat=#{keluarga.alamat}, rt=#{keluarga.rt}, rw=#{keluarga.rw}, #id_kelurahan={keluarga.id_keluharan} where nomor_kk=#{keluarga.nomor_kk}")
	void updateKeluarga(@Param("keluarga") KeluargaModel keluarga, @Param("nkkbaru") String nkkbaru);

}
