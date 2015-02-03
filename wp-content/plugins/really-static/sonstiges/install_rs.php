<?php
global $wpdb;
RS_LOG("Init Really-static install");
RS_LOG("init Database");
$querystr = "DROP TABLE IF EXISTS `".REALLYSTATICDATABASE."`;CREATE TABLE `" .REALLYSTATICDATABASE."` (`url` varchar(1000) CHARACTER SET utf8 NOT NULL ,	`content` CHAR( 32 ) NOT NULL,`datum` INT(11) NOT NULL ) ;";
   require_once(ABSPATH . 'wp-admin/includes/upgrade.php');
   dbDelta($querystr);
   
   
#RS_LOGA($wpdb->get_results($querystr, OBJECT ));
RS_LOG("init defaults");
add_option('rs_fileextensions', array('.jpg' =>1,'.png'=>1 ,'.jpeg'=>1 ,'.gif'=>1 ,'.swf'=>1 ,'.gz'=>1,'.tar'=>1 ,'.zip'=>1 ,'.pdf'=>1 ), '', 'yes' );
add_option("rs_expertmode",0, '', 'yes' );
add_option("rs_stupidfilereplaceA",false, '', 'yes' );
add_option("rs_stupidfilereplaceB",false, '', 'yes' );
add_option("rs_stupidfilereplaceC",false, '', 'yes' );
add_option("rs_stupidfilereplaceD",false, '', 'yes' );
add_option("rs_counter",0, '', 'yes' );
add_option("rs_firstTime",RSVERSION.RSSUBVERSION, '', 'yes' );
add_option('rs_makestatic_a1', array(array("%indexurl%","")), '', 'yes' );
add_option('rs_makestatic_a2', array(array("%tagurl%","")), '', 'yes' );
add_option('rs_makestatic_a3', array(array("%caturl%","")), '', 'yes' );
add_option('rs_makestatic_a4', array(array("%authorurl%","")), '', 'yes' );
add_option('rs_makestatic_a5', array(array("%dateurl%","")), '', 'yes' );
add_option('rs_makestatic_a6', array(array("%commenturl%","")), '', 'yes' );
add_option('rs_makestatic_a7', array(), '', 'yes' );
add_option('rs_posteditcreatedelete',array(array("%postname%","")),'','');
add_option('rs_urlrewriteinto', array(), '', 'yes' );
add_option('rs_lokalerspeicherpfad', REALLYSTATICHOME.'static/', '', 'yes' );
if(get_option('permalink_structure')=="")add_option('rs_nonpermanent',1,'','');
else add_option('rs_nonpermanent',0,'','');


add_option('rs_localpath', '', '', 'yes' );
add_option('rs_subpfad', '', '', 'yes' );
add_option('rs_remoteurl', REALLYSTATICURLHOME."static/", '', 'yes' );

add_option('rs_differentpaths', false, '', 'yes' );






add_option('rs_save', "local", '', 'yes' );
add_option('rs_designlocal', get_bloginfo('template_directory')."/", '', 'yes' );
add_option('rs_designremote', get_bloginfo('template_directory')."/", '', 'yes' );
add_option('rs_everytime',array(),'','');
add_option('rs_pageeditcreatedelete',array(),'','');
add_option('rs_commenteditcreatedelete',array(),'','');
add_option('rs_everyday',array(),'','');

add_option('rs_donationid',"",'','');
add_option('rs_debugstrip',"11101111",'','');

add_option('rs_maketagstatic',1,'','');
add_option('rs_makecatstatic',1,'','');
add_option('rs_makeauthorstatic',1,'','');
add_option('rs_makedatestatic',1,'','');
add_option("rs_makedatetagstatic",1,'','');
add_option("rs_makedatemonatstatic",1,'','');
add_option("rs_makedatejahrstatic",1,'','');

add_option('rs_makeindexstatic',1,'','');

add_option('rs_hide_adminpannel',array(),'','');
add_option( 'rs_showokmessage',array());
add_option( 'rs_logfile',true);
add_option( 'rs_ftpsaveroutine',1);
add_option( 'rs_allrefreshcache',array());
add_option( 'rs_rule',array());
add_option( 'rs_rulerw',array());
add_option( 'rs_rulecron',array());
$transport = apply_filters ( "rs-transport", array () );
foreach ( $transport as $v)	call_user_func_array ( $v [11],array(get_option ( 'rs_firstTime'))  );
RS_LOG("init defaults done");
 $buffer="";
$fh = @fopen ( REALLYSTATICHOME ."settings.dat", "r" );
   while (($b  = @fgets($fh, 4096)) !== false) {
        $buffer.=$b;
    }
@fclose ( $fh );
if($buffer!=""){
$b=unserialize($buffer);
}
if($buffer!="" && $b[autoinstall]==1)importsettings($buffer);
 else 	rs_addmessage( 0, sprintf ( __ ( "You need to configure your Really-Static plugin. Use our <a href='%s'>quick setup</a> or click <a href='%s'>here</a> to jump into the settingsmenu.", 'reallystatic' ), "options-general.php?page=" . REALLYSTATICBASE . "&menu=123", "options-general.php?page=" . REALLYSTATICBASE ) ,1,"page",REALLYSTATICBASE);

 

?>