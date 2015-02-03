<?php
/* -------------------------------------
 *      MISSING WORDPRESS FUNCTIONS
 * ------------------------------------- */

/**
 * Retrieve the date that the last page was published.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * Variation of get_lastpostdate defined in wp-includes/post.php since 0.71
 *
 * @uses apply_filters() Calls 'get_lastpagedate' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the last post.

if( !function_exists('get_lastpagedate') ) {
 function get_lastpagedate($timezone = 'server') {	
	return apply_filters( 'get_lastpagedate', _get_time( $timezone, 'date', 'page' ), $timezone );
 }
} */

/**
 * Retrieve last page modified date depending on timezone.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is just when the last post was modified. The
 * 'gmt' is when the last post was modified in GMT time.
 *
 * Variation of get_lastpostmodified defined in wp-includes/post.php since 1.2.0
 *
 * @uses apply_filters() Calls 'get_lastpagemodified' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date the post was last modified.

if( !function_exists('get_lastpagemodified') ) {
 function get_lastpagemodified($timezone = 'server') {
	$lastpagemodified = _get_time( $timezone, 'modified', 'page' );

	$lastpagedate = get_lastpagedate($timezone);
	if ( $lastpagedate > $lastpagemodified )
		$lastpagemodified = $lastpagedate;

	return apply_filters( 'get_lastpagemodified', $lastpagemodified, $timezone );
 }
} */

/**
 * Retrieve the date that the first post was published.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * Reverse of get_lastpostdate defined in wp-includes/post.php since 0.71
 *
 * @uses apply_filters() Calls 'get_firstpostdate' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the last post.

if( !function_exists('get_firstpostdate') ) {
 function get_firstpostdate($timezone = 'server') {
	return apply_filters( 'get_firstpostdate', _get_time( $timezone, 'date', 'post', 'first' ), $timezone );
 }
} */

/**
 * Retrieve the date that the first page was published.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * Adaptation of get_firstpostdate defined in this file
 *
 * @uses apply_filters() Calls 'get_firstpagedate' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the last post.

if( !function_exists('get_firstpagedate') ) {
 function get_firstpagedate($timezone = 'server') {
	return apply_filters( 'get_firstpagedate', _get_time( $timezone, 'date', 'page', 'first' ), $timezone );
 }
} */

/**
 * Retrieve first post modified date depending on timezone.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * Reverse of get_lastpostmodified defined in wp-includes/post.php since WP 1.2.0
 *
 * @uses apply_filters() Calls 'get_firstpostmodified' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the oldest modified post.

if( !function_exists('get_firstpostmodified') ) {
 function get_firstpostmodified($timezone = 'server') {
	$firstpostmodified = _get_time( $timezone, 'modified', 'post', 'first' );

	$firstpostdate = get_firstpostdate($timezone);
	if ( $firstpostdate > $firstpostmodified )
		$firstpostmodified = $firstpostdate;

	return apply_filters( 'get_firstpostmodified', $firstpostmodified, $timezone );
 }
} */

/**
 * Retrieve first page modified date depending on timezone.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * Variation of get_firstpostmodified defined in this file
 *
 * @uses apply_filters() Calls 'get_firstpagemodified' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the oldest modified page.

if( !function_exists('get_firstpagemodified') ) {
 function get_firstpagemodified($timezone = 'server') {
	$firstpagemodified = _get_time( $timezone, 'modified', 'page', 'first' );

	$firstpagedate = get_firstpagedate($timezone);
	if ( $firstpagedate > $firstpagemodified )
		$firstpagemodified = $firstpagedate;

	return apply_filters( 'get_firstpagemodified', $firstpagemodified, $timezone );
 }
} */

/**
 * Retrieve the date that the first post/page was published.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * @uses apply_filters() Calls 'get_firstdate' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @param string $post_type Post type to check.
 * @return string The date of the last post.
 */
if( !function_exists('get_firstdate') ) {
 function get_firstdate($timezone = 'server', $post_type = 'any') {
	return apply_filters( 'get_firstdate', _get_time( $timezone, 'date', $post_type, 'first' ), $timezone );
 }
}

/**
 * Retrieve first post/page modified date depending on timezone.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * @uses apply_filters() Calls 'get_firstmodified' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the oldest modified post or page.

if( !function_exists('get_firstmodified') ) {
 function get_firstmodified($timezone = 'server') {
	$firstmodified = _get_time( $timezone, 'modified', 'any', 'first' );

	$firstdate = get_firstdate($timezone);
	if ( $firstdate > $firstmodified )
		$firstmodified = $firstdate;

	return apply_filters( 'get_firstmodified', $firstmodified, $timezone );
 }
} */

/**
 * Retrieve the date that the last post/page was published.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * @uses apply_filters() Calls 'get_lastdate' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @param $post_types The post type(s). Can be string or array.
 * @return string The date of the last post.
 */
if( !function_exists('get_lastdate') ) {
 function get_lastdate($timezone = 'server', $post_types = 'any', $m = false) {

 	if (!is_array($post_types))
 		$post_types = array($post_types);
 	
 	$lastmodified = array();
	foreach ($post_types as $post_type)
		$lastmodified[] = _get_time( $timezone, 'date', $post_type, 'last', $m );

	sort($lastmodified);
	
	return apply_filters( 'get_lastdate', array_shift(array_filter($lastmodified)), $timezone );
 }
}

/**
 * Retrieve last post/page modified date depending on timezone.
 *
 * The server timezone is the default and is the difference between GMT and
 * server time. The 'blog' value is the date when the last post was posted. The
 * 'gmt' is when the last post was posted in GMT formatted date.
 *
 * @uses apply_filters() Calls 'get_lastmodified' filter
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @return string The date of the oldest modified post.
 */
if( !function_exists('get_lastmodified') ) {
 function get_lastmodified($timezone = 'server', $post_type = 'any', $m = false) {
	//$lastmodified = _get_time( $timezone, 'modified', $post_type, 'last', $m );

	//$lastdate = get_lastdate($timezone, $post_type, $m);
	//if ( $lastdate > $lastmodified )
	//	$lastmodified = $lastdate;

	return apply_filters( 'get_lastmodified', _get_time( $timezone, 'modified', $post_type, 'last', $m ), $timezone );
 }
}

/**
 * Retrieve first or last post type date data based on timezone.
 * Variation of function _get_last_post_time
 *
 * @access private
 *
 * @param string $timezone The location to get the time. Can be 'gmt', 'blog', or 'server'.
 * @param string $field Field to check. Can be 'date' or 'modified'.
 * @param string $post_type Post type to check. Defaults to 'any'.
 * @param string $which Which to check. Can be 'first' or 'last'. Defaults to 'last'.
 * @return string The date.
 */
if( !function_exists('_get_time') ) {
 function _get_time( $timezone, $field, $post_type = 'any', $which = 'last', $m = 0 ) {
	global $wpdb;

	if ( !in_array( $field, array( 'date', 'modified' ) ) )
		return false;

	$timezone = strtolower( $timezone );
	
	$order = ( $which == 'last' ) ? 'DESC' : 'ASC';

	$key = ( $post_type == 'any' ) ? "{$which}post{$field}{$m}:$timezone" : "{$which}posttype{$post_type}{$field}{$m}:$timezone";

	$date = wp_cache_get( $key, 'timeinfo' );

	if ( !$date ) {
		$add_seconds_server = date('Z');

		if ( $post_type == 'any' ) {
			$post_types = get_post_types( array( 'public' => true ) );
			array_walk( $post_types, array( &$wpdb, 'escape_by_ref' ) );
			$post_types = "'" . implode( "', '", $post_types ) . "'";
		} elseif ( is_array($post_type) ) {
			$types = get_post_types( array( 'public' => true ) );
			foreach ( $post_type as $type )
				if ( !in_array( $type, $types ) )
					return false;
			array_walk( $post_type, array( &$wpdb, 'escape_by_ref' ) );
			$post_types = "'" . implode( "', '", $post_type ) . "'";
		} else {
			if ( !in_array( $post_type, get_post_types( array( 'public' => true ) ) ) )
				return false;
			$post_types = "'" . addslashes($post_type) . "'";
		}

                $where = "$wpdb->posts.post_status='publish' AND $wpdb->posts.post_type IN ({$post_types}) AND $wpdb->posts.post_date_gmt ";
                // If a month is specified in the querystring, load that month
		$m = preg_replace('|[^0-9]|', '', $m);
		if ( !empty($m) ) {
			$where .= " AND YEAR($wpdb->posts.post_date)=" . substr($m, 0, 4);
			if ( strlen($m) > 5 )
				$where .= " AND MONTH($wpdb->posts.post_date)=" . substr($m, 4, 2);
		}

		switch ( $timezone ) {
			case 'gmt':
				$date = $wpdb->get_var("SELECT post_{$field}_gmt FROM $wpdb->posts WHERE $where ORDER BY $wpdb->posts.post_{$field}_gmt {$order} LIMIT 1");
				break;
			case 'blog':
				$date = $wpdb->get_var("SELECT post_{$field} FROM $wpdb->posts WHERE $where ORDER BY $wpdb->posts.post_{$field}_gmt {$order} LIMIT 1");
				break;
			case 'server':
				$date = $wpdb->get_var("SELECT DATE_ADD(post_{$field}_gmt, INTERVAL '$add_seconds_server' SECOND) FROM $wpdb->posts WHERE $where ORDER BY $wpdb->posts.post_{$field}_gmt {$order} LIMIT 1");
				break;
		}


		if ( $date )
			wp_cache_set( $key, $date, 'timeinfo' );
	}

	return $date;
 }
}

/* By gunter [dot] sammet [at] gmail [dot] com http://www.php.net/manual/en/function.htmlentities.php#88169 */
$entity_custom_from = false; 
$entity_custom_to = false;
function html_entity_decode_encode_rss($data) {
	global $entity_custom_from, $entity_custom_to;
	
	if(!is_array($entity_custom_from) || !is_array($entity_custom_to)) {
		$array_position = 0;
		foreach (get_html_translation_table(HTML_ENTITIES) as $key => $value) {
			switch ($value) {
				case '&nbsp;':
					break;
				case '&gt;':
				case '&lt;':
				case '&quot;':
				case '&apos;':
				case '&amp;':
					$entity_custom_from[$array_position] = $key; 
					$entity_custom_to[$array_position] = $value; 
					$array_position++; 
					break; 
				default: 
					$entity_custom_from[$array_position] = $value; 
					$entity_custom_to[$array_position] = $key; 
					$array_position++; 
			} 
		}
	}
	return str_replace($entity_custom_from, $entity_custom_to, $data); 
}

