<?php

/**
 * 
 * @desc Teste Zielservereinstellungen
 * @return boolean=true wenn ok
 * @param none
 */
function reallystatic_testdestinationsetting($silent=false) {
	global $rs_messsage;
	$ok = 0;
	$transport = apply_filters ( "rs-transport", array () );
	call_user_func_array ( $transport [loaddaten ( "rs_save" )] [0], array () );
	if (call_user_func_array ( $transport [loaddaten ( "rs_save" )] [5], array () ) !== true){
		if(!$silent) rs_addmessage(true, __ ( "Cannot Login, please check your Logindata", 'reallystatic' ),2);
	}else {
		$da = time () . "test.rstest";
		$te = "TESTESTSETSE" . time ();
		$out=call_user_func_array ( $transport [loaddaten ( "rs_save" )] [4], array ($da, $te ) );
		
		if ($out!==false and really_static_download ( rs_remoteurl() . $da ) == $te) {
			if(!$silent)$rs_messsage[o][]= __ ( "TEST passed!", "reallystatic" );
			$ok = 1;
		} else{
			 
			if(!$silent)$rs_messsage[e][]=  __ ( "TEST failed!", "reallystatic" );
			}
		@call_user_func_array ( $transport [loaddaten ( "rs_save" )] [3], array ($da ) );
	}
	if ($ok == 1)
		return true;
	else
		return false;
}

function exportsettings($autoinstall=false){
	$transport = apply_filters ( "rs-transport", array () );
	$array=array();	 
	foreach ( $transport as $k=>$v)	$array[saveroutines][$k]=@call_user_func_array ( $v [9], array ("") );
	
		$array[plugins] = apply_filters ( "rs-saveplugindata", $array[plugins]);
	foreach ( rs_options() as $v)$array[options][$v]=get_option($v);
 if($autoinstall)$array[autoinstall]=1;
	return serialize($array);
}


function importsettings($settings){
	$array=unserialize($settings);
	$transport = apply_filters ( "rs-transport", array () );

	foreach ( $transport as $k=>$v)	@call_user_func_array ( $v [10], array ($array[saveroutines][$k]) );
 	foreach ($array[options] as $k=>$v)update_option($k,$v);
	$array[plugins] = apply_filters ( "rs-loadplugindata", $array[plugins]);
}
/*
$fh = fopen ( REALLYSTATICHOME ."settings.dat", "w+" );
fwrite ( $fh, exportsettings() );
fclose ( $fh );*/
/*
$fh = fopen ( REALLYSTATICHOME ."settings.dat", "r" );
   while (($b  = fgets($fh, 4096)) !== false) {
        $buffer.=$b;
    }
fclose ( $fh );
 importsettings($buffer);*/
?>