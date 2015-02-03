<?php

update_option ( "rs_onwork", "0" );
global $rewritestrID;
$ll=WPLANG;
if($ll=="" or $ll{2}!="_")$ll="en_US";
function reallystatic_settingssubmitbutton($titel, $akt = 1, $onclick = "", $name = "submit") {
	if ($akt == 1)
		$farbe = "button-primary";
	else
		$farbe = "button-secondary";
	if ($onclick != "")
		$onclick = ' onclick="' . $onclick . '"';
	return '<span class="submit"><input' . $onclick . ' type="submit" name="' . $name . '" id="submit" class="' . $farbe . '" value="' . $titel . '"  /></span>';
}


#####################
#Source
#$menu[]=array("id"=>"rs_start","name"=>"","content"=>'<iframe src="http://www.sorben.org/really-static/iframe/'.$ll.'.html" style="border:0px #FFFFFF none;" name="meinIFrame" scrolling="no" frameborder="0" align=aus marginheight="0px" marginwidth="0px" height="600" width="800"></iframe>');
if(really_static_demodetect())
	$menu[]=array("id"=>"rs_start","name"=>" ","content"=>str_replace("%remoteurl%", loaddaten ( "rs_remoteurl"),'<h1>Attention!</h1> Really-static is configurated to run in demo-modus! This can cause some problems, for example search engines and other site update services get the url of your wordpress installation. To get really-static out of the demo-modus goto Settings => General and change "Site Address" into the url where really-static put the static files (at the moment %remoteurl%).<br><br>You can fix this running <a href="options-general.php?page=' . REALLYSTATICBASE .'&menu=123">123-quicksetup</a> again.'));
	
	
$menu[]=array("id"=>"rs_source","name"=>__('Source', 'reallystatic'),"content"=>'<form method="post"><table border="0" width="100%">
<tr><td width="400px">'.__('url to wordpressinstalltion', 'reallystatic').'</td><td>:<input name="rs_localurl" size="58" type="text" value="' . loaddaten ( "rs_localurl", 'reallystatic' ) . '"	/>  </td></tr>
<tr><td>'.__('url path to the actuall used templatefolder', 'reallystatic').'</td><td>:<input name="rs_designlocal" size="58" type="text" value="' . loaddaten ( "rs_designlocal", 'reallystatic' ) . '"	/> </td></tr>
</table><br><a target="_blank" href="http://sorben.org/really-static/fehler-quellserver.html">'.__('If you need help please check this manualpage', 'reallystatic').'</a><br>
<input type="hidden" name="strid" value="rs_source" />
'.reallystatic_settingssubmitbutton(__('Save', 'reallystatic')).'
</form>');
#--------------- ZIEL
$dest=apply_filters ( "rs-adminmenu-transport",array());
$desti='<form method="post">';
foreach($dest as $v){
	$desti.='<input type="radio" onchange="hideshowupload(this);" name="realstaticspeicherart" value="'.$v[name].'" '.ison(loaddaten ( "rs_save"),3,'checked ','',$v[name]).'id="fp'.$v[name].'"><label for="fp'.$v[name].'">'.$v[title].'</label><br><div id="shower'.$v[name].'">'.$v[main]."</div>";
}
$desti.='<br><table border="0" width="100%">';
if(get_option("rs_differentpaths")!=true)	
$desti.='<tr><td valign="top" width="400px">'.__('Domainprefix for your cached files', 'reallystatic').'</td><td>:<input name="rs_remoteurl" size="50" type="text" value="' . loaddaten ( "rs_remoteurl" , 'reallystatic') . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'remoteurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="remoteurl">( where your visitors find your blog )</div></td></tr>'

. '<tr><td valign="top" >'.__('Url to the templatefolder', 'reallystatic').'</td><td>:<input name="rs_designremote" size="50" type="text" value="' . loaddaten ( "rs_designremote" ) . '"	/> <a style="cursor:pointer;"  onclick="toggleVisibility(\'designurl\');" >[?]</a>  <div style="max-width:500px; text-align:left; display:none" id="designurl">( for example: '. loaddaten ( "rs_designlocal", 'reallystatic' ).' )</div></td></tr>';

$desti.='<tr><td valign="top" width="400px">'.__('Different prefix for every saving routine', 'reallystatic').'</td><td>:


<input type="checkbox" name="rs_differentpaths"'.ison(get_option ( "rs_differentpaths" ),2," checked ").' value="1">

 '.rs_fragehelp("nodescrition yet").'</td></tr>';
$desti.='</table> ';
$desti.='<input type="hidden" name="strid" value="rs_destination" />
'.reallystatic_settingssubmitbutton(__('Save', 'reallystatic'))
. '&nbsp;'.reallystatic_settingssubmitbutton(__('Test and Save', 'reallystatic'),2,'',"testandsave").'</form>';
$menu[]=array("id"=>"rs_destination","name"=>__('Destination', 'reallystatic'),"content"=>$desti );
 
#------------------ SETTINGS
$tmp="";
foreach(loaddaten ( "rs_fileextensions" ) as $k=>$v)$tmp.= ' <form method="post">'.reallystatic_settingssubmitbutton(__('remove', 'reallystatic')).'<input type="hidden" name="strid2" value="rs_settings" />'.$k.'<input type="hidden" name="go" value="9" /><input type="hidden" name="md5" value="'.md5($k).'" /></form>'."";
$menu[]=array("id"=>"rs_settings","name"=>__('Settings', 'reallystatic'),"content"=>'
<form method="post">'
. '<input type="checkbox" name="refreshallac" '.ison(loaddaten ( "realstaticrefreshallac" ),2," checked ").' value="true"> '.__('On the category/tag page e.g. is a commentcounter (not recomended)', 'reallystatic').' <a target="_blank" href="http://www.sorben.org/really-static/semi-dynamic-categorytag-pages.html">[?]</a><br>'
//. '<input type="checkbox" name="nonpermanent"'.ison(rs_nonpermanent,2," checked ").' value="true"> '.__('I want that Really-Static try to handle with the ? in the url', 'reallystatic').'<br>'
. '<input type="checkbox" name="dontrewritelinked"'.ison(loaddaten ( "dontrewritelinked" ),2," checked ").' value="1"> '.__('Don\'t copy any linked file to the static file folder, just the static Wordpressfiles', 'reallystatic').'<br>'
. '<input type="checkbox" name="rewritealllinked"'.ison(loaddaten ( "rewritealllinked" ),2," checked ").' value="1"> '.str_replace(array("%blogurl%","%staticurl%"),array(loaddaten ( "rs_localurl", 'reallystatic' ),loaddaten ( "rs_remoteurl", 'reallystatic' )),__('Rewrite every %blogurl% with %staticurl% (high security)', 'reallystatic')).'  <a target="blank" href="http://www.sorben.org/really-static/highsecurity.html">[?]</a><br><br>'
. "<b>".__('Also make static:', 'reallystatic').'</b> <a target="blank" href="http://www.sorben.org/really-static/just-a-small-page.html">[?]</a><br>'
. '<input type="checkbox" name="rs_maketagstatic"'.ison(loaddaten ( "rs_maketagstatic" ),2," checked ").' value="1"> '.__('make tag-pages static', 'reallystatic').'<br>'
. '<input type="checkbox" name="rs_makecatstatic"'.ison(loaddaten ( "rs_makecatstatic" ),2," checked ").' value="1"> '.__('make category-pages static', 'reallystatic').'<br>'
. '<input type="checkbox" name="rs_makeauthorstatic"'.ison(loaddaten ( "rs_makeauthorstatic" ),2," checked ").' value="1"> '.__('make author-pages static', 'reallystatic').'<br>'

. '<input type="checkbox" name="rs_makedatestatic"'.ison(loaddaten ( "rs_makedatestatic" ),2," checked ").' value="1"> '.__('make date-pages static', 'reallystatic').'&nbsp;&nbsp;<input type="checkbox" name="rs_makedatetagstatic"'.ison(loaddaten ( "rs_makedatetagstatic" ),2," checked ").' value="1"> '.__('dayly-pages', 'reallystatic').'&nbsp;&nbsp;<input type="checkbox" name="rs_makedatemonatstatic"'.ison(loaddaten ( "rs_makedatemonatstatic" ),2," checked ").' value="1"> '.__('monthly-pages', 'reallystatic').'&nbsp;&nbsp;<input type="checkbox" name="rs_makedatejahrstatic"'.ison(loaddaten ( "rs_makedatejahrstatic" ),2," checked ").' value="1"> '.__('yearly-pages', 'reallystatic').'<br>'
. '<input type="checkbox" name="rs_makeindexstatic"'.ison(loaddaten ( "rs_makeindexstatic" ),2," checked ").' value="1"> '.__('make index-pages static', 'reallystatic').'<br><br>'
. ' <input type="hidden" name="strid" value="rs_settings" />'.reallystatic_settingssubmitbutton(__('Save', 'reallystatic')).'</form><br><br>'
.'<b>'.__('Copy all attached files with following extensions to the destination server:', 'reallystatic').'</b> <a target="blank" href="http://www.sorben.org/really-static/attached-files.html">[?]</a><br>'
.$tmp.'<form method="post"><input type="hidden" name="strid2" value="rs_settings" /><input type="hidden" name="go" value="8" />.<input name="ext" size="10" type="text" value=""	/>'.reallystatic_settingssubmitbutton(__('Add', 'reallystatic')).'</form>'

.'<hr><h3>Settings import/export</h3>'
.'<form method="post">'



.sprintf(__('Export your really-static settings into a settings.dat file inside the really-static pluginfolder (%s)', 'reallystatic'),REALLYSTATICHOME)
.'<br><input type="hidden" name="strid2" value="rs_settings" /><input type="hidden" name="go" value="export" />'.reallystatic_settingssubmitbutton(__('Export', 'reallystatic')).' '.ison((loaddaten("rs_donationid")!="-" and loaddaten("rs_donationid")!=""),2,'<input type="checkbox" name="autoinstall" value="autoinstall"> autoinstall').'</form>'
.'<form method="post">'
.sprintf(__('Import your really-static settings form a settings.dat file inside the really-static pluginfolder (%s)', 'reallystatic'),REALLYSTATICHOME)
.'<br><input type="hidden" name="strid2" value="rs_settings" /><input type="hidden" name="go" value="import" />'.reallystatic_settingssubmitbutton(__('Import', 'reallystatic')).'</form>'


.'<hr><h3>Update</h3>'
.'<form method="post">'
.__('If really-static got a feature update, let wordpress inform me about that.', 'reallystatic')

.'<input type="checkbox" name="rs_informaboutupdate"'.ison(loaddaten ( "rs_informaboutupdate" ),2," checked ").' value="1">'.ison(loaddaten ( "rs_informaboutupdate" ),2,"  (last test:".date_i18n("d.m.Y H:i",get_option("rs_versiondate")).") ")
.' <input type="hidden" name="strid2" value="rs_settings" /><input type="hidden" name="go" value="updateinfo" />'.reallystatic_settingssubmitbutton(__('Save', 'reallystatic')).'   '.
ison(loaddaten ( "rs_informaboutupdate" ),2,reallystatic_settingssubmitbutton(__('Look for a update right now', 'reallystatic'),2,'',"refreshupdate")).'</form>'
);
#------------- Reset
$menu[]=array("id"=>"rs_reset","name"=>__('Reset', 'reallystatic'),"content"=>
 '<form  method="post" id="my_fieldset"><input type="hidden" name="strid2" value="rs_reset" /><input type="hidden" name="hideme2" value="hidden" />'
. __('If you want to renew all static files, first press the "reset filedatabase" button and then the "write all files" button at the "Manual Refresh" tab', 'reallystatic').'<br>'
. ' '.reallystatic_settingssubmitbutton(__('reset filedatabase', 'reallystatic')).'</form><br>');
#-------- Manual
global $reallystaticsystemmessage;
$lastposts = get_posts ( 'numberposts=1 ' );
foreach ( $lastposts as $post ) {
	$tmp=get_permalink ( $post->ID );
	$tmp=nonpermanent($tmp);
}
$menu[]=array("id"=>"rs_refresh","name"=>__('Manual Refresh', 'reallystatic'),"content"=>'<h3>'.__('Refresh a single site manualy', 'reallystatic').'</h3>'
. '<font color="red">'.$reallystaticsystemmessage.'</font>'.

apply_filters("rs-adminmenu-refreshsinglepage",'<form method="post"><input type="hidden" name="strid2" value="rs_refresh" />'
. '<input name="refreshurl" size="50" type="text" value=""	/> '.__('(complete url of the static page)', 'reallystatic').'<a style="cursor:pointer;"  onclick="toggleVisibility(\'manual\');" >[?]</a><b style="max-width:500px; text-align:left; display:none" id="manual">('.__('for example', 'reallystatic').'):'.$tmp.'</b>'
. ' '.reallystatic_settingssubmitbutton(__('Refresh', 'reallystatic'),2).'</form><br>')


. "<h3>".__('Refresh all sites manualy', 'reallystatic')."</h3>".
apply_filters("rs-adminmenu-refreshallpages", '<form  method="post" id="my_fieldset"><input type="hidden" name="strid2" value="rs_refresh" /><input type="hidden" name="hideme" value="hidden" />'
. __('If this Plugin is installed on a Blog with exsiting Posts or for example you changed your design so you shold press the "write all files" Button. If the process is terminatet (e.g. because of a timeout), just press this button again until this menu again appears.', 'reallystatic')
. '<br>'.reallystatic_settingssubmitbutton(__('Write all files', 'reallystatic')).'</form>').'<br>');

#--------Advanced
/*
$tmp="";
$a=loaddaten ( 'rs_everyday' );
if(is_array($a)and count($a)>0){
	$tmp.= "<h3>".__( 'Rewrite every 24 hours' , 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="4" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a= loaddaten ( 'rs_everytime' );
if(is_array($a)and count($a)>0){
	$tmp.= "<h3>".__('Rewrite on every run of Really-Static', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="5" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten ( 'rs_posteditcreatedelete' );
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite on create, edit or delete a post', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="1" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a7");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite on create, edit or delete a comment', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a7" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
###
$a=loaddaten("rs_makestatic_a1");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a index-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a1" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a2");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a tag-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a2" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a3");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a category-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a3" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a4");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a author-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a4" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a5");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a date-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a5" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}
$a=loaddaten("rs_makestatic_a6");
if(is_array($a) and count($a)>0){
	$tmp.= "<h3>".__('Rewrite when a comment-page is createt', 'reallystatic')."</h3>";
	foreach ($a as $v){
		$tmp.= ' <form method="post"><input type="hidden" name="strid2" value="rs_advanced" />'.$v[0];
		if($v[1]!="")$tmp.= " rewrite into ".$v[1];
		$tmp.= '<input type="hidden" name="go" value="a6" /><input type="hidden" name="md5" value="'.$v[0].'" /><input name="Submit1" type="submit" value="x" /></form>'."\n";
	}
}

*/
#$rule[]=array("na"=>"feedrewrite","bo"=>0,"ba"=>0,"go"=>0,"pm"=>0,"pt"=>"","dn"=>1,"to"=>'feed/');
#$rule[]=array("na"=>"qtranslate","bo"=>0,"ba"=>0,"go"=>0,"pm"=>0,"pt"=>"","dn"=>1,"to"=>'de/$1');
#$rule[]=array("na"=>"sitemap","bo"=>0,"ba"=>1,"go"=>0,"pm"=>0,"pt"=>"","dn"=>1,"to"=>'sitemap.xml');
$rule=get_option( 'rs_rule');
@sort($rule);
 
$editrule=-1;
if($_POST["strid2"]=="rs_advanced"){
	foreach($rule as $k=>$v){
		if(md5($v[na])==$_POST[md5]){
			if(isset($_POST[delete2])){
				unset($rule[$k]);
				update_option( 'rs_rule',$rule);
			}elseif(isset($_POST[edit2]) || isset($_POST[save2]))$editrule=$k;
			 
				 

			 
			 
			break;
		}
	}
}
 
if( isset($_POST[save2])){
	
	
		rs_addmessage (1, __("Saved", 'reallystatic') );
	
	if($editrule==-1)$k=count($rule)+1;
	else $k=$editrule;
	
 
	$rule[$k][na]=$_POST[name];
	if($rule[$k][na]=="")$rule[$k][na]=date("d.m.Y h:i:s");
	$rule[$k][bo]=$_POST[bo];
	$rule[$k][ba]=$_POST[ba];
	$rule[$k][go]=$_POST[go];
	$rule[$k][dn]=$_POST[dn];
	$rule[$k][to]=$_POST[to];
	
	if($rule[$k][to]!="")$rule[$k][da]=rs_testfordate($rule[$k][to]);

	
	
	
	
	update_option( 'rs_rule',$rule);
}

$mmmm=__('Everytime $bo is $ba and $go is refreshed<br>than$dn $to', 'reallystatic');

$bo=explode("|",__('something|a post|a page|a post or a page|a comment', 'reallystatic'));
$atmp="";
foreach($bo as $k=>$v){$atmp.='<option '.($rule[$editrule]["bo"]==$k ? "selected" : "").' value="'.$k.'">'.$v.'</option>';}
$mmmm=str_replace('$bo','<select name="bo"  id="wenn">'.$atmp.'</select>',$mmmm);


$ba=explode("|",__('created or edited or deleted|created|edited|created or edited|deleted', 'reallystatic'));
$atmp="";
foreach($ba as $k=>$v)$atmp.='<option '.($rule[$editrule]["ba"]==$k ? "selected" : "").' value="'.$k.'">'.$v.'</option>';
$mmmm=str_replace('$ba','<select name="ba"  id="wenn">'.$atmp.'</select>',$mmmm);

$go=explode("|",__('anotherthing|indexpage|tagpage|authorpage|any datepage|day datepage|month datepage|year dateoage|catpage|current posts or staticpage page|commentpage', 'reallystatic'));
$atmp="";
foreach($go as $k=>$v)$atmp.='<option '.($rule[$editrule]["go"]==$k ? "selected" : "").' value="'.$k.'">'.$v.'</option>';
$mmmm=str_replace('$go','<select name="go"  id="wenn">'.$atmp.'</select>',$mmmm);


$mmmm=str_replace('$pm',"",$mmmm);
$mmmm=str_replace('$pt',"",$mmmm);

$dn=explode("|",__('also rewrite|also rewrite all static files that match with following preg match:|delete|delete all static files that match following regular expression pattern:', 'reallystatic'));
$atmp="";
foreach($dn as $k=>$v)$atmp.='<option '.($rule[$editrule]["dn"]==$k ? "selected" : "").' value="'.$k.'">'.$v.'</option>';
$mmmm=str_replace('$dn','<select name="dn"  id="wenn">'.$atmp.'</select>',$mmmm);

 
$mmmm=str_replace('$to','<input name="to" value="'.($editrule!=-1 ? $rule[$editrule]["to"] : "").'" type="text" />',$mmmm);
 
////////////$editrewrite
$rulerw=get_option( 'rs_rulerw');
@sort($rulerw);
#array("na"=>"feed","pm"=>"#^feed#","rz"=>"feed.xml");


$editrewrite=-1;
if($_POST["strid2"]=="rs_advanced"){
	foreach($rulerw as $k=>$v){
	 
		if(md5($v[na])==$_POST[md5]){ 
			if(isset($_POST[delete1])){
				unset($rulerw[$k]);
				update_option( 'rs_rulerw',$rulerw);
			}elseif(isset($_POST[edit1]) || isset($_POST[save1]))$editrewrite=$k;
			  
			 
			break;
		}
	}
}

if( isset($_POST[save1])){
 
	if($editrewrite==-1)$k=count($rulerw)+1;
	else $k=$editrewrite;
	rs_addmessage (1, __("Saved", 'reallystatic') );
	
	$rulerw[$k][na]=$_POST[name];
	if($rulerw[$k][na]=="")$rulerw[$k][na]=date("d.m.Y h:i:s");
	$rulerw[$k][pm]=$_POST[pm];
	$rulerw[$k][rz]=$_POST[rz];
	if($rulerw[$k][rz]!="")$rulerw[$k][db]=rs_testfordate($rulerw[$k][rz]);
	
	update_option( 'rs_rulerw',$rulerw);
}


$nnnn=__('If this $pm regular expression pattern match than rewrite the url to $rz', 'reallystatic');

$nnnn=str_replace('$pm','<input name="pm" type="text" value="'.($editrewrite!=-1 ? $rulerw[$editrewrite]["pm"] : "").'"/>',$nnnn);
$nnnn=str_replace('$rz','<input name="rz" type="text" value="'.($editrewrite!=-1 ? $rulerw[$editrewrite]["rz"] : "").'"/>',$nnnn);


/////////////////
#$rulecron[]=array("na"=>"taegl","ts"=>"1","tw"=>"1","tm"=>"index.html");
$rulecron=get_option( 'rs_rulecron');
@sort($rulecron);
$editcron=-1;
if($_POST["strid2"]=="rs_advanced"){
	foreach($rulecron as $k=>$v){
		if(md5($v[na])==$_POST[md5]){
			if(isset($_POST[delete3])){
				unset($rulecron[$k]);
				update_option( 'rs_rulecron',$rulecron);
			}elseif(isset($_POST[edit3]) || isset($_POST[save3]))$editcron=$k;
			 
			break;
		}
	}
}
if( isset($_POST[save3])){

	if($editcron==-1)$k=count($rulecron)+1;
	else $k=$editcron;
 
	rs_addmessage (1, __("Saved", 'reallystatic') );
	$rulecron[$k][na]=$_POST[name];
	if($rulecron[$k][na]=="")$rulecron[$k][na]=date("d.m.Y h:i:s");
	$rulecron[$k][ts]=$_POST[ts];
	$rulecron[$k][tw]=$_POST[tw];
	$rulecron[$k][tm]=$_POST[tm];
	if($rulecron[$k][tm]!="")$rulecron[$k][dc]=rs_testfordate($rulecron[$k][tm]);
	update_option( 'rs_rulecron',$rulecron);
}



$oooo=__('Every $ts $tw $tm', 'reallystatic');

$ts=explode("|",__('hour|day|month', 'reallystatic'));
$atmp="";
foreach($ts as $k=>$v)$atmp.='<option '.($rulecron[$editcron]["ts"]==$k ? "selected" : "").'  value="'.$k.'">'.$v.'</option>';
$oooo=str_replace('$ts','<select name="ts" id="wenn">'.$atmp.'</select>',$oooo);

$tw=explode("|",__('refresh|refresh all static files that match following regular expression pattern:'));
$atmp="";
foreach($tw as $k=>$v)$atmp.='<option '.($rulecron[$editcron]["tw"]==$k ? "selected" : "").' value="'.$k.'">'.$v.'</option>';
$oooo=str_replace('$tw','<select name="tw"  id="wenn">'.$atmp.'</select>',$oooo);


$oooo=str_replace('$tm','<input name="tm"  value="'.($editcron!=-1 ? $rulecron[$editcron]["tm"] : "").'" type="text" />',$oooo);


////////////////
$aval='<h2>'.__('Edit rules', 'reallystatic')."</h2><h3>Rewrite url</h3>";
if (is_array($rulerw))foreach($rulerw as $v)$aval.="<form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".md5($v["na"])."' />".$v["na"].": <input name='edit1' type='submit' value='".__('edit rule', 'reallystatic')."' /> <input name='delete1' type='submit' value='".__('delete rule', 'reallystatic')."' /></form>";
$aval.="<h3>".__("Refresh page on Condition", 'reallystatic')."</h3>";
if (is_array($rule))foreach($rule as $v)$aval.="<form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".md5($v["na"])."' />".$v["na"].": <input name='edit2' type='submit' value='".__('edit rule', 'reallystatic')."' /> <input name='delete2' type='submit' value='".__('delete rule', 'reallystatic')."' /></form>";
$aval.="<h3>Refresh page at specific time</h3>";
if (is_array($rulecron))foreach($rulecron as $v)$aval.="<form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".md5($v["na"])."' />".$v["na"].": <input name='edit3' type='submit' value='".__('edit rule', 'reallystatic')."' /> <input name='delete3' type='submit' value='".__('delete rule', 'reallystatic')."' /></form>";






	//<input type="hidden" name="go" value="a6" />


$menu[]=array("id"=>"rs_advanced","name"=>__('Advanced', 'reallystatic'),"content"=>'<h2>'.__('Create rules', 'reallystatic').'</h2>'
.'<font color="#FF0000">NEW! Please report bugs into our forum!</font><br>'
.__("For more information howto use this function, please read <a href='http://really-static-support.php-welt.net/rewrite-rules-and-refresh-by-time-t78.html' target='_blank'>this manual</a>", 'reallystatic')
 
."<h3>".__("Rewrite Url", "reallystatic")."</h3><form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".($editrewrite!=-1 ? md5($rulerw[$editrewrite]["na"]) : "")."' />		
".__("Name of this rule:", "reallystatic")."<input name='name' value='".($editrewrite!=-1 ? $rulerw[$editrewrite]["na"] : "")."' type='text' /><br>
$nnnn <input name='save1' type='submit' value='".__('Submit', 'reallystatic')."' /></form>
		
		
<h3>".__("Refresh page on Condition", "reallystatic")."</h3><form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".($editrule!=-1 ? md5($rule[$editrule]["na"]) : "")."' />		
".__("Name of this rule:","reallystatic")."<input name='name' value='".($editrule!=-1 ? $rule[$editrule]["na"] : "")."' type='text' /><br>
$mmmm<input name='save2' type='submit' value='".__('Submit', 'reallystatic')."' /></form>

<h3>".__("Refresh page at specific time", "reallystatic")."</h3><form  method='post'><input type='hidden' name='strid2' value='rs_advanced' /><input type='hidden' name='md5' value='".($editcron!=-1 ? md5($rulecron[$editcron]["na"]) : "")."' />		
".__("Name of this rule:", "reallystatic")."<input name='name' value='".($editcron!=-1 ? $rulecron[$editcron]["na"] : "")."' type='text' /><br>
$oooo<input name='save3' type='submit' value='".__('Submit', 'reallystatic')."' /></form>
		".$aval		
		
		 );
		
		
			/*
	
		
  '<br><br><br><form  method="post"><input type="hidden" name="strid2" value="rs_advanced" /><input type="hidden" name="ngo" value="1" />
	'.__('Get', 'reallystatic').' '.loaddaten ( "localurl" ).'<input name="url" type="text" /><a style="cursor:pointer;"  onclick="toggleVisibility(\'a1\');" >[?]</a><b style="max-width:500px; text-align:left; display:none" id="a1">('.__('source file e.g.: "?feed=atom"', 'reallystatic').')</b>'.__(', rewrite the filename into', 'reallystatic').' <input name="rewiteinto" type="text" /> <a style="cursor:pointer;"  onclick="toggleVisibility(\'a2\');" >[?]</a><b style="max-width:500px; text-align:left; display:none" id="a2">('.__('destination filename. keep it clear if you want to use the filename from source file', 'reallystatic').')</b> '.__('and make it static', 'reallystatic').' <select name="was" style="width: 340px;">
	<option></option>
	<option value="1">'.__('when a Post is created, edited or deleted', 'reallystatic').'</option>
			<option value="a7">'.__('when a Comment is created, edited or deleted', 'reallystatic').'</option>
	<option value="4">'.__('every 24 hours', 'reallystatic').'</option>
	<option value="5">'.__('everytime Really-Static runs', 'reallystatic').'</option>
	<option value="a1">'.__('when a index-page is createt', 'reallystatic').'</option>
	<option value="a2">'.__('when a tag-page is createt', 'reallystatic').'</option>
	<option value="a3">'.__('when a category-page is createt', 'reallystatic').'</option>
	<option value="a4">'.__('when a author-page is createt', 'reallystatic').'</option>
	<option value="a5">'.__('when a date-page is createt', 'reallystatic').'</option>
		<option value="a6">'.__('when a comment-page is createt', 'reallystatic').'</option>

	</select>&nbsp; <input name="Submit1" type="submit" value="'.__('Submit', 'reallystatic').'" /></form>

 */

/*
 * LOGFILE
 */
 
$array = @file( LOGFILE );
if(is_array($array)){
	$cc=count($array);
	if($cc==1){
	$tmp= __("Logfile is empty!", 'reallystatic');
		if (! @touch ( LOGFILE ))$tmp.= __("Check writing-rights: log.html", 'reallystatic') ;
	}
	else{
		global $wpdb;
		if(is_multisite()) $file= $wpdb->blogid.'-log.html' ;
		else $file='log.html';
		$tmp= sprintf(__("Last 40 Logfileentrys (<a href='%s'>full logfile</a>)", 'reallystatic'),REALLYSTATICURLHOME.$file).": <pre>";
		if($cc>40)$tt=$cc-41;
		else$tt=0;
		$merk="";
		for($i=$cc;$i>$tt;$i--){
			$merk.=  $array[$i];
	 }
	 $tmp.= preg_replace('&(http:\/\/)([\#0-9a-z.\/\-\_\?\=\&]*)&i','<a href="\1\2">\1\2</a>',$merk)."</pre>";
	}
}else $tmp=__("Unable to read logfile!", 'reallystatic').__("Check writing-rights: log.html", 'reallystatic');
$menu[]=array("id"=>"rs_logfile","name"=>__('Logfile', 'reallystatic'),"content"=>$tmp.'<form method="post"><input type="hidden" name="strid2" value="rs_logfile" />'.reallystatic_settingssubmitbutton(__('Reset Logfile', 'reallystatic')).'</form>');
/*
 * DEBUG
 */

global $rs_version,$rs_rlc;
$t=5-strlen($rs_version.$rs_rlc);
if($t>0)$tmp= "Relaseid: ".$rs_version.str_repeat("0",$t).$rs_rlc."\n";
else $tmp= "Relaseid: ".$rs_version.$rs_rlc."\n";
	$avp = get_plugins();
	 
	$ap = get_option("active_plugins");
	foreach($ap as $v) {
		if(strtolower(substr($avp[$v][Name],0,13))=="really-static"){
			$pluginsrs.=$avp[$v][Name]." (v".$avp[$v][Version]."), ";
			if($avp[$v]["RS Info"]!="")$really_static_plugininfos.=$avp[$v]["RS Info"]."<br>";
			else $really_static_plugininfos.="<b>".$avp[$v][Name]." (v".$avp[$v][Version].")</b> by ".$avp[$v][Author]." <br>";
		}else $plugins.=$avp[$v][Name]." (v".$avp[$v][Version]."), ";
	}
 
 
if(function_exists("get_blog_details"))$bd=get_blog_details(get_current_blog_id());	

$debugstrip=get_option ( 'rs_debugstrip' );
#versionnumbers
#serversettings
#urls
#obscureurl
#paths
#wordpresssettings
#otherplugins
#logentries
 
$uurrllss="\nhome: ".get_option ( 'home' )."\n"
. "site_url: ".get_option ( 'siteurl' )."\n"
. "Local: ".loaddaten ( "rs_localurl", 'reallystatic' )."\n"
. "Remote: ".loaddaten ( "rs_remoteurl", 'reallystatic' )."\n"
. "Local Design: ".loaddaten ( "rs_designlocal", 'reallystatic' ) ."\n"
. "Remote Design: ".loaddaten ( "rs_designremote", 'reallystatic' )."\n";
$ppaatthhss="\npath to RS:".REALLYSTATICHOME."\n".
"storeroutine: ".loaddaten ( "rs_save" )."\n";
if(loaddaten ( "rs_save" )=="local")$ppaatthhss.="localpath: ".get_option ( 'rs_lokalerspeicherpfad')."\n";

if($debugstrip[3]==1){
$uurrllss=str_replace(array(get_option ( 'home' ),get_option ( 'siteurl' )),array("##home##","##site##"),$uurrllss);
$ppaatthhss=str_replace(array($_SERVER["HOME"]),array("##shome##"),$ppaatthhss);

}
 

$menu[]=array("id"=>"rs_debug","name"=>__('Debug', 'reallystatic'),"content"=>
  __("If you think there is a bug or you got any questions feel free to use this form, or use the <a href='http://really-static-support.php-welt.net/' taget='_blank'>offical support forum</a>. <br>Debugdata:", 'reallystatic')."<br>"
.' 

<div  style="float:right; width:200px;"><form method="post">
<b>show:</b><br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[0],2,"checked").' value="1"> versionnumbers<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[1],2,"checked").' value="2"> serversettings<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[2],2,"checked").' value="3"> urls<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[4],2,"checked").' value="5"> paths<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[5],2,"checked").' value="6"> wordpresssettings<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[6],2,"checked").' value="7"> otherplugins<br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[7],2,"checked").' value="8"> logentries<br><br>
<b>obscure:</b><br>
<input type="checkbox" name="debugstrip[]" '.ison($debugstrip[3],2,"checked").' value="4"> urls<br>
<br><input type="hidden" name="strid2" value="rs_debug" /><input type="hidden" name="strid" value="rs_debug" />'.reallystatic_settingssubmitbutton(__('Save', 'reallystatic'),2,'',"logdata").'</form></div>
<div style="width:400px">
'











. '<form method="post"><textarea name="debug" cols="140" rows="25">'
.ison($debugstrip[0],2,"\nFileversion: ". date ("F d Y / H:i:s",$reallystaticfile)."\n".$tmp."Language: ".$ll."\n"
."Wordpressversion: ".get_bloginfo("version")."\n")

.ison($debugstrip[1],2,"\nCURL: ".ison(function_exists("curl_init"),1,"available","not available")."\n"
. "file_get_contents: ".ison(function_exists("file_get_contents"),1,"available","not available")."\n"
. "allow_url_fopen: ".ison(ini_get('allow_url_fopen'),2,"active","not active")."\n"
. "Host-OS: ".PHP_OS."\n")

.ison($debugstrip[2],2,$uurrllss)

 .ison($debugstrip[4],2,$ppaatthhss)
  .ison($debugstrip[5],2, "\nPermastructure: ".get_option ( 'permalink_structure' )."\n"
. "Multisite: ".ison(is_multisite(),1,"active","deactive")."\n"
. "Multimainsite: ".ison(is_main_site(),1,"active","deactive")."\n"
 
 .(is_multisite() ? "Multisiteurl:".$bd->siteurl."\n" : "")
 .(is_multisite() ? "Multidomain:".$bd->domain."\n" : "")
 .(is_multisite() ? "Multipath:".$bd->path."\n" : ""))

 .ison($debugstrip[6],2, "\nPlugins: ".substr($plugins,0,-2)."\n"
. "RSPlugins: ".substr($pluginsrs,0,-2)."\n")
 .ison($debugstrip[7],2, "
 Last 40 Logfileentrys:
=====================\n $merk")


. '</textarea><br>'
. __('You can send me your Debug by <a target="_blank" href="http://erik.sefkow.net/impressum.html">Email</a> or with this form.', 'reallystatic')
. '<br><LABEL ACCESSKEY=U>Your emailaddress: <INPUT TYPE=text NAME=mail SIZE=8 value="'.$_POST[mail].'"></LABEL><br>'
. '<LABEL ACCESSKEY=U>'.__("Describe your Problem (please only in german or english):", 'reallystatic').'<br><textarea name="comment" cols="140" rows="15">'.$_POST[comment].'</textarea></LABEL><br>'
. '<input type="hidden" name="strid2" value="rs_debug" /><input type="hidden" name="strid" value="rs_debug" />'.reallystatic_settingssubmitbutton(__('Send this debug information to the developer', 'reallystatic')).'</form>'
.'</div>
');
 



#####################
#echo '<link href="' . REALLYSTATICURLHOME . 'sonstiges/admin.css" rel="stylesheet" type="text/css" />';
echo '<style type="text/css">
		
		
h1.reallystatic {
	background: #fff url('.REALLYSTATICURLHOME.'/sonstiges/pluginbild.jpg) right center no-repeat;
	padding: 16px 2px;
	margin: 25px 0;
	border: 1px solid #ddd;
	-moz-border-radius: 3px;
	-webkit-border-radius: 3px;
	border-radius: 3px;
} 

ul#rstatic_tabs {
	list-style-type: none;
	margin: 0;
	padding: 0;
	height: 1%;
}

ul#rstatic_tabs li {
	float: left;
	border-top: 1px solid #EAF3FA;
	border-left: 1px solid #EAF3FA;
	margin: 0;
	padding: 0;
}

ul#rstatic_tabs li.last {
	border-right: 1px solid #EAF3FA;
}

ul#rstatic_tabs li a {
	font-family: "Lucida Grande","Lucida Sans Unicode",Tahoma,Verdana;
	font-weight: bold;
	font-size: 1.1em;
	float: left;
	border: none;
	color: #00019B;
	padding: 5px 8px;
	outline: none;
	text-decoration: none;
	-moz-outline: none;
}

ul#rstatic_tabs li a:hover {
	background: #F8F8F8;
	text-decoration: underline;
}

ul#rstatic_tabs li.rstatic_active a {
	background: #EAF3FA;
}
div#refreshallinfo {
	padding: 10px;
	background: #EAF3FA;
}
span#refreshallinfog {
 
	background: #c0c0c0;
}

div.rstatic_panel {
	clear: both;
	height: 1%;
	background: #EAF3FA;
	padding: 10px;
}
		</style>
';
echo "\n";
echo '<script type="text/javascript" src="' . REALLYSTATICURLHOME . 'sonstiges/admin.js"></script>';
echo '<script type="text/javascript" src="' . REALLYSTATICURLHOME . 'sonstiges/admin_2.js"></script>';
echo "\n";


echo'<h1 class="reallystatic">'.__("Really Static Settings", 'reallystatic').'</h1>';
global $rs_messsage;

if(is_array($rs_messsage[e]))foreach ($rs_messsage[e] as $v)reallystatic_configerror ( 0,$v );#errormessages
if(is_array($rs_messsage[o]))foreach ($rs_messsage[o] as $v)reallystatic_configok ( $v, 1 ); # okmessages
if(is_array($rs_messsage[m]))foreach ($rs_messsage[m] as $v)reallystatic_configok ( $v, 1 ); # js messages
unset($rs_messsage);



if(really_static_demodetect())$startid="rs_start";
else $startid="rs_source";
echo '	<form method="post" id="rstatic_option-form">

<script type="text/javascript">
//
     var url =document.URL;
	 var u=url.split("/");
	 url=u[u.length-1];
	 u=url.split("#");
	 if(u[1]!="")var strID="#"+u[1];
	 else var strID="#'.$startid.'";
	if(strID=="#undefined")  strID="#'.$startid.'";
	';

if($rewritestrID!="")echo 'strID="#'.$rewritestrID.'";';

echo '
 
	function hideshowupload(a){
	';
$dest=apply_filters ( "rs-adminmenu-transport",array());
foreach ($dest as $v)echo 'if(a.id=="fp'.$v[name].'")document.getElementById("shower'.$v[name].'").style.display="inline";
else document.getElementById("shower'.$v[name].'").style.display="none";';
echo '
			
	
	}
	
</script>




 <div class="tabs" id="tabs" style="max-width:1000px;display: none">
	   <ul class="tabNavigation" id="rstatic_tabs">';
     
     
     
$menu=apply_filters ( "rs-adminmenu-show",$menu);
$text="";
$rs_hide_adminpannel=get_option('rs_hide_adminpannel');
for($i = 0; $i < count($menu); $i++){
	if(isset($rs_hide_adminpannel[$menu[$i][id]]))continue;
if($menu[$i][name]!="")echo '<li id="'.$menu[$i][id].'"><a href="#'.$menu[$i][id].'">'.$menu[$i][name].'</a></li>';
else echo '<li id="'.$menu[$i][id].'"></li>';
$text.= '<div class="rstatic_panel" id="'.$menu[$i][id].'">'.$menu[$i][content].'</div>';	
}

#RS_LOG(loaddaten("rs_donationid"));
#RS_LOG(multiloaddaten("rs_donationid"));
echo '<li id="rs_donate" class="last"><a href="#rs_donate">';
if(loaddaten("rs_donationid")!="-" and loaddaten("rs_donationid")!="")echo __('About', 'reallystatic');
else{
if(loaddaten("rs_donationid")=="")echo __('<font color="red">please</font> ', 'reallystatic');
 echo ''.__('Donate', 'reallystatic').'';
 }
echo '</a></li></ul>';
echo '<div class="rstatic_panel" id="rs_donate">';
global $reallystaticsystemmessage;
if(loaddaten("rs_donationid")=="")echo '<form method="post"><input type="hidden" name="strid2" value="rs_donate" /><font color="red">'.$reallystaticsystemmessage.'</font>
  My plugins for Wordpress are "donationware". I develop, release, and maintain them for free, and you can use them for free, but I hope you find them worthy of a donation of thanks or encouragement. Registration costs one cent (a PayPal fee). If you choose to make a payment of $0.01 (or whatever minimum PayPal allows for your currency), PayPal takes that as a fee and I receive nothing, and that`s perfectly acceptable.
  <br><br>
  Your PayPal transaction ID is a fully valid registration code: <input name="donate" type="text" />'.reallystatic_settingssubmitbutton(__('Submit', 'reallystatic')).'</form>
  <form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="9157614">
<input type="image" src="https://www.paypal.com/'.$ll.'/i/btn/btn_donate_LG.gif" border="0" name="submit" alt="Jetzt einfach, schnell und sicher online bezahlen mit PayPal.">
<img alt="" border="0" src="https://www.paypal.com/'.$ll.'/i/scr/pixel.gif" width="1" height="1">
</form>

  '.versionupdate();
  elseif(get_option("rs_donationid")!="-")echo "<h2>License</h2>".loaddaten("rs_donationid");
else{
	echo "Thank you, for supporting really static!";
}
echo "<h2>Languagepack</h2>".__("This languagepack is written by <a target='_blank' href='http://erik.sefkow.net'>Erik Sefkow</a> please <a target='_blank' href='http://really-static-support.php-welt.net/why-donate--t8.html'>donate me</a>", 'reallystatic');
echo "<h2>Really-Static Plugins</h2>";
echo $really_static_plugininfos;
echo '</div>';
#echo str_replace("</form>",' <input type="hidden" id="rs_expertmodehidden" name="rs_expertmode" value="'.loaddaten ( "rs_expertmode").'"></form>',$text);
echo $text;

echo '<form method="post" name="expert"><input type="hidden" name="setexpert" value="1" /><input type="hidden" name="strid" value="'.$_POST[strid].'" /><input type="checkbox" name="rs_expertmode" onclick="document.expert.submit();" id="showexpertid" value="1" '.ison(loaddaten ( "rs_expertmode"),3,'checked ','',1).'> <label for="showexpertid">show Expertsettings</label></form></div>
<script type="text/javascript">showexpert(document.getElementById("showexpertid"));hideshowupload(document.getElementById("fp'.loaddaten ( "rs_save").'"));</script>';
/*if(loaddaten("rs_donationid")=="")echo '<br><br><center><!-- Facebook Badge START --><a href="http://www.facebook.com/pages/really-static-Wordpress-Plugin/177723978808" title="really static Wordpress Plugin" target="_TOP"><img src="http://badge.facebook.com/badge/177723978808.4655.159374760.png" width="360" height="101" style="border: 0px;" /></a><!-- Facebook Badge END --></center>';*/
 
 
 if (isset ( $_POST ["hideme"] )) {
	 

 echo '<div id="refreshallinfo">Really-static is refreshing your whole static filecache. This will take some time, according to the size of your blog. Inside the yellow box above, you see what really-static is doing right now. 
 At <span id="refreshallinfog"></span> really-static did the last thing. If this Time doesnt change for a while( > 60 seconds), there maybe a timeout. In this case please use the following button and really-static will restart with this process on the last known position.
 
  <form  method="post" id="my_fieldset"><input type="hidden" name="strid2" value="rs_refresh" /><input type="hidden" name="hideme" value="hidden" /><input type="hidden" name="pos" value="3" /><input type="submit" value="restart rebuild process"></form>

 </div>';
 

 really_static_rebuildentireblog();
	reallystatic_configok ( __ ( "done scanning for files", 'reallystatic' ), 2 );	
}	  else{

echo("<script type='text/javascript'> document.getElementById('tabs').style.display = 'block';	</script>");
 
}

#$transport = apply_filters ( "rs-transport", array () );
#$array=array();	 
#foreach ( $transport as $v)	$array=array_merge($array,call_user_func_array ( $v [9], array ()  ));
#rs_loga($array);










 





?>