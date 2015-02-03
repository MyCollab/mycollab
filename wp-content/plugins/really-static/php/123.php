<?php
#echo "<pre>";
#print_R($_POST);
#echo "</pre>";
#ison(!@touch(REALLYSTATICHOME."static/")),1,"red","green")
global $ison;
$ison=0;
if($_POST["pos"]==""){




	echo "<h1>".__("Really-Static Quickstart", 'reallystatic')."</h1><b>";
	echo __("Thank you for choosing Really-Static! This configurationhelper will support you, choosing the right settings.", 'reallystatic');
	echo '</b><br /><br /><form method="post">
	<table><tr><td width="300px"><input type="hidden" name="pos" value="1" /><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" />
	Choose this if you want to try really-static! All static files are stored into a tempoary-folder and no wordpresssettings are changed.<br><br>
	
	<input name="test" type="submit" value="'.__('running  really-static in testmode', 'reallystatic').'"  style="height: 400px; width: 400px" />
	</td><td  width="10px"><span style="font-size:50px;">OR</span>
	</td><td  width="300px">
	Choose this if you know that your kind of wordpressinstallation support really-static. If you never used really-static bevore please first use the testmodus.
	
	<input name="live" type="submit" value="'.__('running  really-static in livemode', 'reallystatic').'"  style="height: 400px; width: 400px" />
	</td></tr></table></form>
	
	';
		
	
 
}elseif($_POST["pos"]=="1"){	
		echo "<br><h2>".__("STEP 2: set the storage location", "reallystatic")."</h2>";
		echo '<script type="text/javascript" src="' . REALLYSTATICURLHOME . 'sonstiges/admin_2.js"></script>';
		echo '<h2>Please choose a way and a destionation where the generatet files should be saved:</h2><form method="post">';


		if($_POST[test]!=""){
			#echo "<hr>GEN FOR TEST";
			$_POST[datawasgeneratet]=1;

			update_option('rs_save',"local"); 
			update_option ( 'rs_lokalerspeicherpfad', REALLYSTATICHOME.'static/');
			update_option ( 'rs_remoteurl', REALLYSTATICURLHOME.'static/');
			update_option("home",get_option('siteurl') );
			
			update_option ( 'rs_designlocal', get_bloginfo('template_directory')."/");
		update_option ( 'rs_designremote', get_bloginfo('template_directory')."/");
		update_option ( 'rs_localurl',get_option('siteurl')."/");

		}
		elseif($_POST[live]!=""){
			#echo "<hr>GEN FOR LIFE";
			$_POST[datawasgeneratet]=1;
			update_option('rs_save',"local") ;
			update_option ( 'rs_lokalerspeicherpfad', REALLYSTATICHOME.'static/');
			update_option ( 'rs_remoteurl', REALLYSTATICURLHOME.'static/');
			update_option("home",REALLYSTATICURLHOME."static" );
			update_option ( 'rs_designlocal', get_bloginfo('template_directory')."/");
			update_option ( 'rs_designremote', get_bloginfo('template_directory')."/");
			update_option ( 'rs_localurl',get_option('siteurl')."/");
		}
		$dest=apply_filters ( "rs-adminmenu-transport",array());
 
		$desti='';
		foreach($dest as $v){
			$desti.='<input type="radio" onchange="hideshowupload(this);" name="realstaticspeicherart" value="'.$v[name].'" '.ison(loaddaten ( "rs_save"),3,'checked ','',$v[name]).'id="fp'.$v[name].'"><label for="fp'.$v[name].'">'.$v[title].'</label><br><div id="shower'.$v[name].'">'.$v[main]."</div>";
		}
		echo $desti;
 
	
		#}
				echo "<br><h2>After this, please say where visitors can view this files:</h2>";
				echo '<input name="rs_remoteurl" type="text" size="90" value="'.loaddaten ( "rs_remoteurl" , 'reallystatic').'">';
				
				echo "<br><br>";
	echo '<input type="hidden" name="pos" value="2" /><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" /><input name="Submit1" type="submit" value="'.__('Next >>', 'reallystatic').'" /></form>';
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

}elseif($_POST["pos"]=="2"){
if($_POST ['realstaticspeicherart']!=""){
	apply_filters("rs-adminmenu-savealltransportsettings","");
	update_option ( 'rs_save', $_POST ['realstaticspeicherart'] );
update_option ( 'rs_remoteurl', $_POST ['rs_remoteurl'] );

if(	get_option("home")==REALLYSTATICURLHOME."static" ){
	update_option("home", $_POST ['rs_remoteurl'] );
}
}

	echo "<h2>".__("STEP 3: Checking writingrights and try create, read and delete a file", 'reallystatic')."</h2>";
	echo '<table width="400px" height="100px"><tr><td style="text-color: white; font-size:25px; background-color:'.ison(is_writable(LOGFILE),1,"green","red").';">'.__('Logfile writeable','reallystatic').'</td> </tr></table>';
	
	echo '<table width="400px" height="100px"><tr><td style="text-color: white; font-size:25px; background-color:'.ison(is_readable(LOGFILE),1,"green","red").';">'.__('Logfile readable','reallystatic').'</td> </tr></table>';
		
	 
	
	$isonn=reallystatic_testdestinationsetting(true);

	echo '<table  width="400px" height="100px"><tr><td style="text-color: white; font-size:25px; background-color:'.ison($isonn==true,1,"green","red").';">'.__("Trying Write/Read/Delete File", 'reallystatic').'</td> </tr></table>';
	global $ison;
 
	if($ison==3)echo '<form method="post"><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" /><input type="hidden" name="pos" value="3" /><input name="Submit1" type="submit" value="'.__('Next >>', 'reallystatic').'" /></form>';
	else echo '<form method="post"><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" /><input type="hidden" name="pos" value="1" /><input name="Submit1" type="submit" value="'.__('Back <<', 'reallystatic').'" /></form><form method="post"><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" /><input type="hidden" name="pos" value="2" /><input name="Submit1" type="submit" value="'.__('Refresh', 'reallystatic').'" /></form>';

}elseif($_POST["pos"]=="3"){
	echo "<h2>".__("STEP 4: Generating your current Blog into the ", 'reallystatic')."</h2>";
	echo __("Really-static is now ready to use.", 'reallystatic');
	echo '<form  method="post" id="my_fieldset"><input type="hidden" name="strid2" value="rs_refresh" />
	<input type="hidden" name="hideme" value="hidden" /><input type="hidden" name="datawasgeneratet" value="'.$_POST[datawasgeneratet].'" />
	<input type="hidden" name="pos" value="4" />
	<input type="submit" value="'.__("start generating files... this will take some time", 'reallystatic').'"></form><a href="options-general.php?page='.$base.'">'.__('or goto the Settingspage', 'reallystatic').'</a>';
}elseif($_POST["pos"]=="4"){
 

$lastposts = get_posts ( 'numberposts=1 ' );
foreach ( $lastposts as $post ) {
	$r=get_permalink ( $post->ID );
	$r=loaddaten( "rs_remoteurl" ).str_replace(get_option('home')."/","",nonpermanent($r));
}
  
echo "<hr><div id='refreshallinfo'>Really-static is now generating<br>Please wait!</div>";
echo " <div class='tabs' id='tabs' style='display: none'><br><b>";
echo __("OK, Ready!", 'reallystatic');
echo "</b><br>";
echo sprintf(__("You static files are locatet here: <a target='_blank' href='%s'>%s</a>", 'reallystatic'), loaddaten( "remoteurl" ), loaddaten( "remoteurl" ));
echo "<br>";
echo "<br>";
echo __("On your server the files are locatet: ", 'reallystatic'). loaddaten( "rs_lokalerspeicherpfad" );
echo "<br>";
echo "<br>";
echo sprintf(__("An example: <a target='_blank' href='%s'>%s</a>", 'reallystatic'),$r,$r);
if(really_static_demodetect())echo "<br><br>"."keep in mind that really-static runs in demo-mode. If you want that every visitor of your site  profits from the faster loading times, you need to reconfige really-static";
echo "<br>Have fun and please dont forget to donate!</div>";

 really_static_rebuildentireblog();
	reallystatic_configok ( __ ( "done scanning for files", 'reallystatic' ), 2 );	

}else die("error");
?>