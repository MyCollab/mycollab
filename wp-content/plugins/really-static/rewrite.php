<?php
add_filter ( 'rewrite_rules_array', 'really_static_insert_rewrite_rules' ,1);
add_filter ( 'query_vars', 'really_static_insert_query_vars' );
add_action ( 'wp_loaded', 'really_static_rewrite_flush_rules' );


 
/**
 *
 *
 * löscht bei jedem aufruf die rewrite regeln
 * 
 * @todo sollte in zukunft nur noch am anfang aufgerufen werden
 *      
 */
function really_static_rewrite_flush_rules() {
	$rules = get_option ( 'rewrite_rules' );
	global $wp_rewrite;
	$wp_rewrite->flush_rules ();
	global $eigenerools, $eigeneroolsrev, $eigeneroolsrev2;
	$eigeneroolsrev = apply_filters ( "rs-rewriterules-wp2s", $eigeneroolsrev );
	$eigeneroolsrev2 = apply_filters ( "rs-rewriterules-s2wp", $eigeneroolsrev2 );
	$eigenerools = apply_filters ( "rs-rewriterules-main", $eigenerools );
}
add_filter ( 'get_comments_pagenum_link', 'really_static_rewrite_comments_pagenum_link' );
function really_static_rewrite_comments_pagenum_link($a) {
	#if (  really_static_selfdetect ())
	#	return $a;
	$a = preg_replace ( '&/([^/]+)(.html)/comment-page-([0-9]+)(#.*?|)$&i', '/comment-page-$3/$1$2', $a );
	// a=substr(str_replace( get_option ( 'home' ) .
	// "/","",$a),0,strlen("/comment-page-/".$matches[1])*-1);
	$a = preg_replace ( "!(|&)page(/|=)(\d+)/!i", '', $a );
	return $a;
}
add_filter ( 'get_pagenum_link', 'really_static_rewrite_pagenum' );
/**
 *
 *
 * schreibt allgemeine seitenlinks um
 * 
 * @param unknown_type $a        	
 *
 */
function really_static_rewrite_pagenum($a) {
	global $eigeneroolsrev2;
	$matches = null;
	if (! rs_nonpermanent) {
		$returnValue = preg_match ( '#.html/page/([0-9]+)$#i', $a, $matches );
		if ($returnValue != true) {
			$matches [1] = 1;
			if (substr ( $a, - 1 ) != "/")
				$a = $a . "/page/1";
			else
				$a = $a . "page/1";
		}
		$a = substr ( str_replace ( get_option ( 'home' ) . "/", "", $a ), 0, strlen ( "/page/" . $matches [1] ) * - 1 );
	} else {
		$returnValue = preg_match ( '#.html&paged=([0-9]+)$#i', $a, $matches );
		if ($returnValue != true) {
			$matches [1] = 1;
			if (substr ( $a, - 1 ) != "&")
				$a = $a . "&paged=1";
			else
				$a = $a . "paged=1";
		}
		$a = substr ( str_replace ( get_option ( 'home' ) . "/", "", $a ), 0, strlen ( "&paged=" . $matches [1] ) * - 1 );
	}
	foreach ( $eigeneroolsrev2 as $k => $v ) {
		if (preg_match ( '#' . $k . '#i', $a ))
			break;
		else
			$v = false;
	}
	if ($v !== false)
		$b = preg_replace ( "#\%PAGE\=([0-9]+)\%#", $matches [1], $v );
	else
		$b = $a;
	return really_static_rewrite1 ( get_option ( 'home' ) . "/" . $b );
}
 
/**
 *
 *
 * Adding a new rule
 * 
 * @param unknown_type $rules        	
 */
function really_static_insert_rewrite_rules($rules) {

	
	global $eigenerools;
	if(!is_array($eigenerools))return $rules;
	$newrules = array ();
	foreach ( $eigenerools as $k => $v ) {
		$newrules [$k] =$v;
	}
	#RS_LOGA($rules);
#RS_LOGA($newrules);
#RS_LOG("################");
	#RS_LOGA($rules+$newrules);
	
	#return $rules+$newrules;// + ;
 
	
	#if ( is_multisite() && !is_subdomain_install() && is_main_site() ){ //suche nach parse_request
	#return $newrules+$rules;
	#}else
	if(is_multisite() && !is_main_site()){
	$cb=get_blog_details(get_current_blog_id());
	$cbp=substr($cb->path,1,-1);
		foreach ( $newrules+$rules as $k => $v ) {
			if($v!="")$rr [$cbp."/".$k] =$v;
			else $rr [$cbp.$k] ="";
		}

		return $rr;// + ;
	}else return $newrules+$rules;// + ;
	
}

/**
 *
 *
 * Adding the id var so that WP recognizes it
 * 
 * @param unknown_type $vars        	
 */
function really_static_insert_query_vars($vars) {
	array_push ( $vars, 'id' );
	return $vars;
}

add_filter ( "home_url", "home_urldddd" );
function home_urldddd($url, $path = "", $orig_scheme = "", $blog_id = "") {
	if (! really_static_selfdetect ())
		return $url;
 
	if ((get_option ( 'home' ) . "/") != rs_remoteurl())
		return str_replace ( loaddaten ( "rs_localurl", 'reallystatic' ), loaddaten ( "rs_remoteurl", 'reallystatic' ), $url );
	else
		return $url;
}
add_filter ( "redirect_canonical", "really_static_rewrite2" );
function really_static_rewrite2($url) {
	return false;
}

add_filter ( "year_link", "really_static_rewrite1" );
add_filter ( "month_link", "really_static_rewrite1" );
add_filter ( "day_link", "really_static_rewrite1" );
add_filter ( "category_link", "really_static_rewrite1" );
add_filter ( "tag_link", "really_static_rewrite1" );
add_filter ( "author_link", "really_static_rewrite1" );
add_filter ( "page_link", "really_static_rewrite3" );
add_filter ( "post_link", "really_static_rewrite3" );
add_filter ( "get_comment_link", "really_static_rewrite_commentlink", 10, 3 );
add_filter ( "get_comment_auhtor_link", "really_static_rewrite1" );
add_filter ( "attachment_link", "really_static_rewrite4" );

/**
 * ersetzt galerylink
 */
function really_static_rewrite4($url1) {
global $dontwrite;$dontwrite=1;
 
if(substr($url1,(strlen(loaddaten ( "remoteurl" ))-1))==$_SERVER[REQUEST_URI])return $url1;

 

	global $arbeitsliste, $arbeitetrotzdem;
	$arbeitetrotzdem = true;
	$url2 = really_static_rewrite1 ( $url1 );
	
	$l = strlen ( loaddaten ( "remoteurl" ) );
	
	$arbeitsliste [update] [substr ( $url2, $l )] = loaddaten ( "localurl" ) . substr ( $url1, $l );
	
	return $url2;
}
/**
 *
 *
 * schreibt links zu kommentarseiten um
 * 
 * @param String $url        	
 * @param unknown_type $cc        	
 * @param unknown_type $args        	
 */
function really_static_rewrite_commentlink($url, $cc = "", $args = "") {
	global $wpdb;
	$allowedtypes = array (
			'comment' => '',
			'pingback' => 'pingback',
			'trackback' => 'trackback' 
	);
	$comtypewhere = ('all' != $args ['type'] && isset ( $allowedtypes [$args ['type']] )) ? " AND comment_type = '" . $allowedtypes [$args ['type']] . "'" : '';
	
	$seitenanzahl = ceil ( $wpdb->get_var ( $wpdb->prepare ( "SELECT COUNT(comment_ID) FROM $wpdb->comments WHERE comment_post_ID = %d AND comment_parent = 0 AND comment_approved = '1' " . $comtypewhere, $cc->comment_post_ID ) ) ) / get_option ( "comments_per_page" );
	
	if ($args [per_page] > 0)
		$commentseitenanzahl = $seitenanzahl / $args [per_page]; // per_page
	else
		$commentseitenanzahl = 1;
	if (ceil ( $commentseitenanzahl ) == $args [page])
		$url = str_replace ( '/comment-page-' . $args [page], '', $url );
	return really_static_rewrite1 ( $url );
}
/**
 */
function really_static_rewrite3($url) {
 
	
	
	
	global $seitenlinktransport;
	#RS_LOG($url);
	$url = really_static_urlfix1 ( $url );
	#RS_LOG($url);
	#RS_LOG("DEMO".really_static_selfdetect ());
	#RS_LOG("really_static_rewrite3  ".really_static_selfdetect());
	if (!really_static_selfdetect ())	return $url;
	#RS_LOG("really_static_rewrite3  ! $seitenlinktransport !".really_static_selfdetect());
	$url = str_replace ( get_option ( 'home' ) . "/", get_option ( 'home' ) . $seitenlinktransport . "/", $url );
	unset ( $seitenlinktransport );
	global $eigeneroolsrev;
	if (count ( $eigeneroolsrev ) == 0)
		return $url;
	 
	foreach ( $eigeneroolsrev as $k => $v ) {
		// uu=$url;
		$url = preg_replace ( '!' . $k . '!is', $v, $url );
		// S_LOG("$uu=>$url $k => $v");
	}
 
	return $url;
}
/**
 * Rewriting URLs
 * 
 * @param String $url        	
 * @return String
 */
function really_static_rewrite1($url) { 
	$url = really_static_urlfix1 ( $url );

		// S_LOG("#####$url");
	global $seitenlinktransport;
	$url = $url . $seitenlinktransport;
	unset ( $seitenlinktransport );
	if (! really_static_selfdetect ())
		return $url;	
	
	
	global $eigeneroolsrev;
	if (count ( $eigeneroolsrev ) == 0)
		return $url;
		// $uuu=$url;
 
	foreach ( $eigeneroolsrev as $k => $v ) {
		
		$url = preg_replace ( '!' . $k . '!is', $v, $url );
	}
		//RS_LOG("out $url");
	// RS_LOG("##2###$url");
	
	return $url;
}
global $eigenerools, $eigeneroolsrev, $eigeneroolsrev2;

// Biegt wordpress intern links um so das wordpress neue links annimmt
;
// eigenerools['monat/seite-([0-9]+).html$'] =
// 'index.php?year=2011&monthnum=5&paged=$matches[1]';
// eigenerools['monat/(seite-1|index).html$'] =
// 'index.php?year=2011&monthnum=5';

// eigeneroolsrev['2011/05$'] = 'monat/index.html';
// eigeneroolsrev['2011/05/page/1$'] = 'monat/index.html';
// eigeneroolsrev['2011/05/page/([0-9]+)?$'] = 'monat/seite-$1.html';

// eigeneroolsrev2['monat/(seite-1|index).html$'] = '2011/05';
// eigeneroolsrev2['monat/seite-([0-9]+).html$'] = '2011/05/page/%PAGE=$1%';
$seite = "page/";
if (! rs_nonpermanent) {
	// galery
	$eigeneroolsrev ['.html/([^/.]+)$'] = '/bild-$1.html';
	$eigeneroolsrev2 ['/bild-([^/.]*).html$'] = '.html/$1';
	// ######################## JAHR
	$eigenerools ['([0-9]{4})/' . $seite . '([0-9]+).html$'] = 'index.php?year=$matches[1]&paged=$matches[2]';
	$eigenerools ['([0-9]{4})/(' . $seite . '1|index).html$'] = 'index.php?year=$matches[1]';
	
	$eigeneroolsrev ['([0-9]{4})$'] = '$1/index.html';
	$eigeneroolsrev ['([0-9]{4})/page/1$'] = '$1/index.html';
	$eigeneroolsrev ['([0-9]{4})/page/([0-9]+)?$'] = '$1/' . $seite . '$2.html';
	
	$eigeneroolsrev2 ['([0-9]{4})/(' . $seite . '1|index).html$'] = '$1';
	$eigeneroolsrev2 ['([0-9]{4})/' . $seite . '([0-9]+).html$'] = '$1/page/%PAGE=$2%';
	// ######################## MONAT
	$eigenerools ['([0-9]{4})/([0-9]{1,2})/' . $seite . '([0-9]+).html$'] = 'index.php?year=$matches[1]&monthnum=$matches[2]&paged=$matches[3]';
	$eigenerools ['([0-9]{4})/([0-9]{1,2})/(' . $seite . '1|index).html$'] = 'index.php?year=$matches[1]&monthnum=$matches[2]';
	
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})$'] = '$1/$2/index.html';
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})/page/1$'] = '$1/$2/index.html';
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})/page/([0-9]+)?$'] = '$1/$2/' . $seite . '$3.html';
	
	$eigeneroolsrev2 ['([0-9]{4})/([0-9]{1,2})/(' . $seite . '1|index).html$'] = '$1/$2';
	$eigeneroolsrev2 ['([0-9]{4})/([0-9]{1,2})/' . $seite . '([0-9]+).html$'] = '$1/$2/page/%PAGE=$3%';
	// ######################## DatumTAG
	$eigenerools ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/' . $seite . '([0-9]+).html$'] = 'index.php?year=$matches[1]&monthnum=$matches[2]&day=$matches[3]&paged=$matches[4]';
	$eigenerools ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/(' . $seite . '1|index).html$'] = 'index.php?year=$matches[1]&monthnum=$matches[2]&day=$matches[3]';
	
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})$'] = '$1/$2/$3/index.html';
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/page/1$'] = '$1/$2/$3/index.html';
	$eigeneroolsrev ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/page/([0-9]+)?$'] = '$1/$2/$3/' . $seite . '$4.html';
	
	$eigeneroolsrev2 ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/(' . $seite . '1|index).html$'] = '$1/$2/$3';
	$eigeneroolsrev2 ['([0-9]{4})/([0-9]{1,2})/([0-9]{1,2})/' . $seite . '([0-9]+).html$'] = '$1/$2/$3/page/%PAGE=$4%';
	
	// ######################## CATEGORY
	$eigenerools ['category/(.+?)/' . $seite . '([0-9]+).html$'] = 'index.php?category_name=$matches[1]&paged=$matches[2]';
	$eigenerools ['category/(.+?)/(' . $seite . '1|index).html$'] = 'index.php?category_name=$matches[1]';
	
	// eigeneroolsrev['category/(.+?)$'] = 'category/$1/index.html';
	$eigeneroolsrev ['category/([^(\.|\/)]*)$'] = 'category/$1/index.html';
	$eigeneroolsrev ['category/(.+?)/page/1$'] = 'category/$1/index.html';
	$eigeneroolsrev ['category/(.+?)/page/([0-9]+)?$'] = 'category/$1/' . $seite . '$2.html';
	
	$eigeneroolsrev2 ['category/(.+?)/(' . $seite . '1|index).html$'] = 'category/$1';
	$eigeneroolsrev2 ['category/(.+?)/' . $seite . '([0-9]+).html$'] = 'category/$1/page/%PAGE=$2%';
	// ######################## TAGS
	$eigenerools ['tag/(.+?)/' . $seite . '([0-9]+).html$'] = 'index.php?tag_name=$matches[1]&paged=$matches[2]';
	$eigenerools ['tag/(.+?)/(' . $seite . '1|index).html$'] = 'index.php?tag_name=$matches[1]';
	
	$eigeneroolsrev ['tag/([^(\.|\/)]*)$'] = 'tag/$1/index.html';
	$eigeneroolsrev ['tag/(.+?)/page/1$'] = 'tag/$1/index.html';
	$eigeneroolsrev ['tag/(.+?)/page/([0-9]+)?$'] = 'tag/$1/' . $seite . '$2.html';
	
	$eigeneroolsrev2 ['tag/(.+?)/(' . $seite . '1|index).html$'] = 'tag/$1';
	$eigeneroolsrev2 ['tag/(.+?)/' . $seite . '([0-9]+).html$'] = 'tag/$1/page/%PAGE=$2%';
	
	// ######################## author
/*	$eigenerools ['author/(.+?)/' . $seite . '([0-9]+).html$'] = 'index.php?author_name=$matches[1]&paged=$matches[2]';
	$eigenerools ['author/(.+?)/(' . $seite . '1|index).html$'] = 'index.php?author_name=$matches[1]';
	
	$eigeneroolsrev ['author/(.+?)(/index.html|)$'] = 'author/$1/index.html';
	$eigeneroolsrev ['author/(.+?)/page/1(/index.html|)$'] = 'author/$1/index.html';
	$eigeneroolsrev ['author/(.+?)/page/([0-9]+)?(/index.html|)$'] = 'author/$1/' . $seite . '$2.html';
	
	$eigeneroolsrev2 ['author/(.+?)/(' . $seite . '1|index).html$'] = 'author/$1';
	$eigeneroolsrev2 ['author/(.+?)/' . $seite . '([0-9]+).html$'] = 'author/$1/page/%PAGE=$2%';
	
	*/
	
	
	
	$eigenerools ['author/(.+?)/' . $seite . '([0-9]+).html$'] = 'index.php?author_name=$matches[1]&paged=$matches[2]';
	$eigenerools ['author/(.+?)/(' . $seite . '1|index).html$'] = 'index.php?author_name=$matches[1]';
	$eigeneroolsrev ['author/([^(\.|\/)]*)$'] = 'author/$1/index.html';
	$eigeneroolsrev ['author/(.+?)/page/1$'] = 'author/$1/index.html';
	$eigeneroolsrev ['author/(.+?)/page/([0-9]+)?$'] = 'author/$1/' . $seite . '$2.html';
	$eigeneroolsrev2 ['author/(.+?)/(' . $seite . '1|index).html$'] = 'author/$1';
	$eigeneroolsrev2 ['author/(.+?)/' . $seite . '([0-9]+).html$'] = 'author/$1/page/%PAGE=$2%';
	
	
	
	
	
	
	
	
	
	
	
	// ######################## page_id statische seite
	
	// och unbearbeitet
	// $eigenerools[''.$seite.'(.+?)/index.html$'] =
	// 'index.php?&paged=$matches[1]';
	// eigeneroolsrev['page/([0-9]+)$'] = ''.$seite.'$1/index.html';
	// eigeneroolsrev2[''.$seite.'([0-9]+)/index.html$'] = 'page/%PAGE=$1%';
	
	/**
	 * $eigeneroolsrev: von Wordpress zu statisch
	 * $eigeneroolsrev2 und $eigenerools: von statisch zurück zu wordpress
	 */
	
	// ###########################
	// [page/?([0-9]{1,})/?$] => index.php?&paged=$matches[1]
	$eigenerools ['' . $seite . '(.+?)/index.html$'] = 'index.php?&paged=$matches[1]';
	$eigeneroolsrev ['page/([0-9]+)$'] = '' . $seite . '$1/index.html';
	$eigeneroolsrev2 ['' . $seite . '([0-9]+)/index.html$'] = 'page/%PAGE=$1%';
	
	$eigeneroolsrev2 ['index.html$'] = '';
	$eigeneroolsrev2 ['comment-page-(\d+)/(.+?).html$'] = '$2.html/comment-page-$1';
	
	$eigeneroolsrev2 ['page/(\d+)/(.+?).html$'] = '$2.html/$1';
	
	// eigeneroolsrev2['2011/05/page/([0-9]+)?$'] = '2011/05/page/%PAGE%';
	
	// # # fff/3/index.html =>page/2/fff.html
	
	// ttp://teststat.sefkow.net/4.html/comment-page-2#comment-518
	
	$eigeneroolsrev ['([^/]*).html/comment-page-(\d+)\#comment-(\d+)$'] = 'comment-page-$2/$1.html#comment-$3';
	
	// page
	$eigeneroolsrev ['^([^\.]+)$'] = '$1/index.html';
	// ttp://test.sefkow.net/page/2/gfgf/
	$eigeneroolsrev2 ['page/(\d+)/([^/.]*)/$'] = '$2/$1';
	
	// $eigeneroolsrev['(.*?)/([^/]*)/([0-9]+)$'] = ''.$seite.'$3/$2';
	
		$eigenerools ['$'] = '';
 	$eigenerools ['\/\/$'] = '/';
	$eigeneroolsrev ['\/\/$'] = '/';
	$eigeneroolsrev2 ['\/\/$'] = '/';	
 
	
	
} else { // # fuer ? links
       // author=1&paged=2
       
	// #author
	$eigeneroolsrev ['\?author=(.+?)&(|amp;)paged=1$'] = 'author=$1/index.html';
	$eigeneroolsrev ['\?author=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'author=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?author=(.+?)$'] = 'author=$1/index.html';
	
	$eigeneroolsrev2 ['author=(.+?)/(' . $seite . '1|index).html$'] = '?author=$1';
	$eigeneroolsrev2 ['author=(.+?)/' . $seite . '([0-9]+).html$'] = '?author=$1&paged=%PAGE=$2%';
	// ###cat
	$eigeneroolsrev ['\?cat=(.+?)&(|amp;)paged=1$'] = 'cat=$1/index.html';
	$eigeneroolsrev ['\?cat=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'cat=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?cat=(.+?)$'] = 'cat=$1/index.html';
	
	$eigeneroolsrev2 ['cat=(.+?)/(' . $seite . '1|index).html$'] = '?cat=$1';
	$eigeneroolsrev2 ['cat=(.+?)/' . $seite . '([0-9]+).html$'] = '?cat=$1&paged=%PAGE=$2%';
	// ###tag
	$eigeneroolsrev ['\?tag=(.+?)&(|amp;)paged=1$'] = 'tag=$1/index.html';
	$eigeneroolsrev ['\?tag=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'tag=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?tag=(.+?)$'] = 'tag=$1/index.html';
	
	$eigeneroolsrev2 ['tag=(.+?)/(' . $seite . '1|index).html$'] = '?tag=$1';
	$eigeneroolsrev2 ['tag=(.+?)/' . $seite . '([0-9]+).html$'] = '?tag=$1&paged=%PAGE=$2%';
	
	// ###date
	$eigeneroolsrev ['\?m=(.+?)&(|amp;)paged=1$'] = 'm=$1/index.html';
	$eigeneroolsrev ['\?m=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'm=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?m=(.+?)$'] = 'm=$1/index.html';
	
	$eigeneroolsrev2 ['m=(.+?)/(' . $seite . '1|index).html$'] = '?m=$1';
	$eigeneroolsrev2 ['m=(.+?)/' . $seite . '([0-9]+).html$'] = '?m=$1&paged=%PAGE=$2%';
	// #seiten
	$eigeneroolsrev ['\?p=(.+?)&(|amp;)paged=1$'] = 'p=$1/index.html';
	$eigeneroolsrev ['\?p=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'p=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?p=(.+?)$'] = 'p=$1/index.html';
	
	$eigeneroolsrev2 ['p=(.+?)/(' . $seite . '1|index).html$'] = '?p=$1';
	$eigeneroolsrev2 ['p=(.+?)/' . $seite . '([0-9]+).html$'] = '?p=$1&paged=%PAGE=$2%';
	// #statische seiten
	$eigeneroolsrev ['\?page_id=(.+?)&(|amp;)paged=1$'] = 'page_id=$1/index.html';
	$eigeneroolsrev ['\?page_id=(.+?)&(|amp;)paged=([0-9]+)?$'] = 'page_id=$1/' . $seite . '$3.html';
	$eigeneroolsrev ['\?page_id=(.+?)$'] = 'page_id=$1/index.html';
	
	$eigeneroolsrev2 ['page_id=(.+?)/(' . $seite . '1|index).html$'] = '?page_id=$1';
	$eigeneroolsrev2 ['page_id=(.+?)/' . $seite . '([0-9]+).html$'] = '?page_id=$1&paged=%PAGE=$2%';
	
	// sonstiges
	$eigeneroolsrev ['\?paged=([0-9]+)$'] = '' . $seite . '$1/index.html';
	$eigeneroolsrev2 ['' . $seite . '([0-9]+)/index.html$'] = '?paged=%PAGE=$1%';
	$eigeneroolsrev2 ['index.html$'] = '';
}
function really_wp_url_make_to_static($url) {
	global $eigeneroolsrev;
	foreach ( $eigeneroolsrev as $k => $v ) {
		$url = preg_replace ( '#' . $k . '#is', $v, $url );
	}
	// $url = preg_replace ( "#\%PAGE\=([0-9]+)\%#", '$1', $url );
	//$url=str_replace("//","/",$url);
	$url =apply_filters ( "rs_wp2static_url",$url );
	return $url;
}
function really_static_make_to_wp_url($url) {
	$url=str_replace("//","/",$url);

	global $eigeneroolsrev2;
	foreach ( $eigeneroolsrev2 as $k => $v ) {
		
		// uuu=$url;
		$url = preg_replace ( '#' . $k . '#is', $v, $url );
		// S_LOG("$uuu=>$url $k => $v");
	}
	$url = preg_replace ( "#\%PAGE\=([0-9]+)\%#", '$1', $url );
	$url =apply_filters ( 'rs_static2wp_url',$url );
	return $url;
}
/**
 *
 *
 * Korigiert einen komischen fehler der bei gleichheit von wordpressurl und
 * siteurl auftritt
 * 
 * @param unknown_type $url        	
 */
function really_static_urlfix1($url) {
	if (! really_static_selfdetect ())
		return str_replace ( rs_remoteurl(), get_option ( 'home' ) . "/", $url );
	return $url;
	
	if (get_option ( 'home' ) != get_option ( 'siteurl' ))
		return $url;
	return str_replace ( rs_remoteurl(), get_option ( 'home' ) . "/", $url );
}

?>