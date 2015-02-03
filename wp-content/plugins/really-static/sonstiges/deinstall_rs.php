<?php
global $wpdb;
RS_LOG("Init Really-static deinstall");
RS_LOG("remove Database");
$querystr = "DROP TABLE IF EXISTS `".REALLYSTATICDATABASE."`;";
$wpdb->get_results($querystr, OBJECT );
RS_LOG("remove defaults");

foreach(rs_options() as $v)delete_option($v);

$transport = apply_filters ( "rs-transport", array () );
foreach ( $transport as $v)	call_user_func_array ( $v [12],array(get_option ( 'rs_firstTime'))  );
RS_LOG("deinstallation done");
?>