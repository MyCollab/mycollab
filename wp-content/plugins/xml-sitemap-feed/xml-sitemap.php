<?php
/*
Plugin Name: XML Sitemap & Google News feeds
Plugin URI: http://status301.net/wordpress-plugins/xml-sitemap-feed/
Description: Feed the  hungry spiders in compliance with the XML Sitemap and Google News protocols. Happy with the results? Please leave me a <strong><a href="https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ravanhagen%40gmail%2ecom&item_name=XML%20Sitemap%20Feed&item_number=4%2e0&no_shipping=0&tax=0&bn=PP%2dDonationsBF&charset=UTF%2d8&lc=us">tip</a></strong> for continued development and support. Thanks :)
Text Domain: xml-sitemap-feed
Version: 4.3.2
Author: RavanH
Author URI: http://status301.net/
*/

/*  Copyright 2013 RavanH http://status301.net/ email: ravanhagen@gmail.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
*/

/* --------------------
 *  AVAILABLE HOOKS
 * --------------------
 *
 * FILTERS
 *	xml_sitemap_url		-> Filters the URL used in the sitemap reference in robots.txt
 *	(deprecated)			(receives an ARRAY and MUST return one; can be multiple urls) 
 *					and for the home URL in the sitemap (receives a STRING and MUST
 *					return one) itself. Useful for multi language plugins or other 
 *					plugins that affect the blogs main URL... See pre-defined filter
 *					XMLSitemapFeed::qtranslate() in XMLSitemapFeed.class.php as an
 *					example.
 *      xmlsf_defaults		-> Filters the default array values for different option groups.
 *      xmlsf_allowed_domain	-> Filters the response when checking the url against allowed domains. 
 *					Can be true or false.
 *	the_title_xmlsitemap	-> Filters the Google News publication name, title and keywords 
 					and Image title and caption tags
 
 * ACTIONS
 *	[ none at this point, but feel free to request, suggest or submit one :) ]
 *	
 */

if(!empty($_SERVER['SCRIPT_FILENAME']) && 'xml-sitemap.php' == basename($_SERVER['SCRIPT_FILENAME']))
	die('You may not access this file directly!');

/* --------------------
 *      CONSTANTS
 * -------------------- */

	define('XMLSF_VERSION', '4.3.2');

if ( file_exists ( dirname(__FILE__).'/xml-sitemap-feed' ) )
	define('XMLSF_PLUGIN_DIR', dirname(__FILE__) . '/xml-sitemap-feed');
else
	define('XMLSF_PLUGIN_DIR', dirname(__FILE__));

define('XMLSF_PLUGIN_BASENAME', plugin_basename(__FILE__));

/* The following constants can be used to change plugin defaults by defining them in wp-config.php */

/* 
 * XMLSF_POST_TYPE 
 * 
 * Comma seperated list of post types.
 * default: 'any'
 *
 * example:
 * define('XMLSF_POST_TYPE', 'post,page');
 */
 
/* 
 * XMLSF_NAME 
 * 
 * Pretty permalink name for the main sitemap (index)
 */
if ( !defined('XMLSF_NAME') )
	define('XMLSF_NAME', 'sitemap.xml');

/* 
 * XMLSF_POST_TYPE_NEWS_TAGS 
 * 
 * Post types to append sitemap news tags to in regular sitemaps.
 * Does not have effect when News sitemap is switched of in site settings.
 * default: 'post'
 *
 * example:
 * define('XMLSF_POST_TYPE_NEWS_TAGS', 'post,mycustomtype');
 */


/* 
 * XMLSF_NEWS_NAME 
 * 
 * Pretty permalink name for the news sitemap
 */
if ( !defined('XMLSF_NEWS_NAME') )
	define('XMLSF_NEWS_NAME', 'sitemap-news.xml');
	
/* 
 * XMLSF_NEWS_POST_TYPE 
 * 
 * Post types to include in dedicated news sitemap
 */
if ( !defined('XMLSF_NEWS_POST_TYPE') )
	define('XMLSF_NEWS_POST_TYPE', 'post');

/*
 * XMLSF_GOOGLE_NEWS_TITLE
 *
 * Google News name, if different than site name
 * TODO
 */



/* -------------------------------------
 *      MISSING WORDPRESS FUNCTIONS
 * ------------------------------------- */

include_once(XMLSF_PLUGIN_DIR . '/hacks.php');

/* ----------------------
 *     INSTANTIATE
 * ---------------------- */

if ( class_exists('XMLSitemapFeed') || include_once( XMLSF_PLUGIN_DIR . '/includes/core.php' ) )
	$xmlsf = new XMLSitemapFeed();

