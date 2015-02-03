<?php
//////////////////////////////////////////////////
//ftp class
//author:paul.ren
//e-mail:rsr_cn@yahoo.com.cn
//website:www.yawill.com
//create:2004-6-23 09:22
//modify:
//////////////////////////////////////////////////


class ClsFTP{
	var $host = "localhost";//FTP HOST
	var $port = "21";		//FTP port
	var $user = "Anonymous";//FTP user
	var $pass = "Email";	//FTP password
	var $link_id = "";		//FTP hand
	var $is_login = "";		//is login 
	var $debug = 1;
	var $local_dir = "";	//local path for upload or download
	var $rootdir = "";		//FTP root path of FTP server
	var $dir = "/";			//FTP current path
	
	
	function ClsFTP($user="Anonymous",$pass="Email",$host="localhost",$port="21"){
		if($host) $this->host = $host;
		if($port) $this->port = $port;
		if($user) $this->user = $user;
		if($pass) $this->pass = $pass;
		$this->login();
		$this->rootdir 	= $this->pwd();
		$this->dir 		= $this->rootdir;
	}
	function halt($msg,$line=__LINE__){
		echo "FTP Error in line:$line<br/>\n";
		echo "FTP Error message:$msg<br/>\n";
		exit();
	}
	function login(){
		if(!$this->link_id){
			$this->link_id = ftp_connect($this->host,$this->port) or $this->halt("can not connect to host:$this->host:$this->port",__LINE__);
		}
		if(!$this->is_login){
			$this->is_login = ftp_login($this->link_id, $this->user, $this->pass) or $this->halt("ftp login faild.invaid user or password",__LINE__);
		}
	}
	function systype(){
		return ftp_systype($this->link_id);
	}
	function pwd(){
		$this->login();
		$dir = ftp_pwd($this->link_id);
		$this->dir = $dir;
		return $dir;
	}
	function cdup(){
		$this->login();
		$isok =  ftp_cdup($this->link_id);
		if($isok) $this->dir = $this->pwd();
		return $isok;
	}
	function cd($dir){
		$this->login();
		$isok = ftp_chdir($this->link_id,$dir);
		if($isok) $this->dir = $dir;
		return $isok;
	}
	function nlist($dir=""){
		$this->login();
		if(!$dir) $dir = ".";
		$arr_dir = ftp_nlist($this->link_id,$dir);
		return $arr_dir;
	}
	function rawlist($dir="/"){
		$this->login();
		$arr_dir = ftp_rawlist($this->link_id,$dir);
		return $arr_dir;
	}
	function mkdir($dir){
		$this->login();
		return @ftp_mkdir($this->link_id,$dir);
	}
	function file_size($file){
		$this->login();
		$size = ftp_size($this->link_id,$file);
		return $size;
	}
	function chmod($file,$mode=0666){
		$this->login();
		return ftp_chmod($this->link_id,$file,$mode);
	}
	function delete($remote_file){
		$this->login();
		return ftp_delete($this->link_id,$remote_file);
	}
	function get($local_file,$remote_file,$mode=FTP_BINARY){
		$this->login();
		return ftp_get($this->link_id,$local_file,$remote_file,$mode);
	}
	function put($remote_file,$local_file,$mode=FTP_BINARY){
		$this->login();
		return @ftp_put($this->link_id,$remote_file,$local_file,$mode);
	}
	function put_string($remote_file,$data,$mode=FTP_BINARY){
		$this->login();
		$tmp = "/tmp";//ini_get("session.save_path");
		$tmpfile = tempnam($tmp,"tmp_");
		$fp = @fopen($tmpfile,"w+");
		if($fp){
			fwrite($fp,$data);
			fclose($fp);
		}else return 0;
		$isok = $this->put($remote_file,$tmpfile,FTP_BINARY);
		@unlink($tmpfile);
		return $isok;
	}
	function p($msg){
		echo "<pre>";
		print_r($msg);
		echo "</pre>";
	}

	function close(){
		@ftp_quit($this->link_id);
	}
}


##############Erik Sefkow
/*
 *
 */
function rs_connect($ftp_host="",$ftp_user="", $ftp_pass="",$ftp_port=21){

	global $rs_isconnectet;
	if(!isset($rs_isconnectet)){
		 if($ftp_host==""and $ftp_user==""and $ftp_pass=="")rs_error( "Logindata missing!!" ,3);
		$ftp = new ClsFTP($ftp_user,$ftp_pass,$ftp_host,$ftp_port);
		$rs_isconnectet=$ftp;
	}

	return $rs_isconnectet;
}
function rs_disconnect(){
	global $rs_isconnectet;
	$rs_isconnectet->close();
}
function rs_writefile($ziel, $quelle){
	$rs_isconnectet= rs_connect();
	if($rs_isconnectet->put ($ziel,$quelle )===false){
	 
	$dir=rs_recursivemkdir($ziel);
	 
	if($rs_isconnectet->put ($ziel,$quelle )===false){
		echo "Have not enoth rigths to create Folders. tryed ($dir): ".$ziel;
		exit;
	}
	}
	
	
}
function rs_writecontent($ziel,$content){
	$rs_isconnectet= rs_connect();
	
		if($rs_isconnectet->put_string ( $ziel,$content )===false){
	 
	$dir=rs_recursivemkdir($ziel);
	 
	if($rs_isconnectet->put_string ( $ziel,$content )===false){
		echo "Have not enoth rigths to create Folders. tryed ($dir): ".$ziel;
		exit;
	}
	}
	
}
function rs_deletefile($datei){
	$rs_isconnectet= rs_connect();
	$rs_isconnectet->delete($datei);

}
function rs_recursivemkdir($ziel){
#echo $ziel;
	$rs_isconnectet= rs_connect();
	$dir=split("/", $ziel);
	##
	unset($dir[count($dir)-1]);
	$dir=implode("/",$dir);
	$ddir=$dir;
	do{
		do{
		#echo "$dir<hr>";
			$fh =@$rs_isconnectet->mkdir($dir);
			$okdir=$dir;
			$dir=split("/",$dir);
			unset($dir[count($dir)-1]);
			$dir=implode("/",$dir);

	 }while($dir!="" and $fh===false);
	 if($fh===false)die(str_replace("%folder%","$ziel",__("Im no able to create the directory %folder%! Please check writings rights!", 'reallystatic')));
	 $dir=$ddir;
	}while($okdir!=$dir);
	##
	
	return $dir;

}
/**
 * text= errortex
 * type 1=just debug 2=error-> halt
 */
function rs_error($text,$type){
	$fh = @fopen("transportlog.txt", "a+");
	@fwrite($fh, date("d M Y H:i:s").": ".$text."\r\n");
	@fclose($fh);
	if($type==3)die($text);

}
?>