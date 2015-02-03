<?php
/*
* 
*/
function rs_connect($everything_you_need_to_connect=""){
global $rs_connect;
return $rs_connect;
}
function rs_disconnect(){

}
function rs_writefile($destinationfile,$sourcefile){
//try  rs_connect();

}
function rs_deletefile($file){
//try  rs_connect();

}
function rs_writecontent($destinationfile,$content){
//try  rs_connect();

}
 
/**
* text= errortex
* type 1=just debug 2=error-> halt
*/
function rs_error($text,$type){


}
?>