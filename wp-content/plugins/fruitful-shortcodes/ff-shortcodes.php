<?php
/*
	Plugin Name: Fruitful Shortcodes
	Plugin URI: http://plugins.fruitfulcode.com/shortcodes/
	Description: Add additional content shortcodes: horizontal tabs, vertical tabs, accordion, promo text, columns, infobox, separator, alert, progress bar, button, 
	Version: 1.3
	Author: fruitfulcode
	Author URI: http://fruitfulcode.com
	License: GPL2
*/
/*  Copyright 2014  Fruitful Code  (email : mail@fruitfulcode.com)

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License, version 2, as 
	published by the Free Software Foundation.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

class ffs {
	function __construct() {
		add_action( 'plugins_loaded', array( &$this, 'fruitful_shortcode_constants'), 	1);
		add_action( 'plugins_loaded', array( &$this, 'fruitful_shortcode_lang'),		2);
		add_action( 'plugins_loaded', array( &$this, 'fruitful_shortcode_includes'), 	3);
		add_action( 'plugins_loaded', array( &$this, 'fruitful_shortcode_admin_init'),	4);

		
		register_activation_hook  ( __FILE__, array( &$this,  'fruitful_shortcode_activation' ));
		register_deactivation_hook( __FILE__, array( &$this,'fruitful_shortcode_deactivation' ));
		
		add_action('init', array( &$this,'fruitful_init_shortcodes'));
	}
		
	function fruitful_shortcode_constants() {
		define( 'FRUITFUL_SHORTCODE_VERSION', '1.0.0' );
		define( 'FRUITFUL_SHORTCODE_WP_VERSION',   get_bloginfo( 'version' ));
		define( 'FRUITFUL_SHORTCODE_DIR', 	    trailingslashit( plugin_dir_path( __FILE__ ) ) );
		define( 'FRUITFUL_SHORTCODE_URI', 		trailingslashit( plugin_dir_url( __FILE__ ) ) );
		define( 'FRUITFUL_SHORTCODE_INCLUDES', 	FRUITFUL_SHORTCODE_DIR . trailingslashit( 'includes' ) );
		define( 'FRUITFUL_SHORTCODE_LOAD',     	FRUITFUL_SHORTCODE_DIR . trailingslashit( 'load' ) );
	}
		
	function fruitful_shortcode_lang() {
		//load_plugin_textdomain( 'ff_shortcodes', false, dirname( plugin_basename( __FILE__ ) ) . '/languages/' );		
	}	
		
	function fruitful_shortcode_includes() {
		//require_once( fruitful_INCLUDES . 'functions.php' ); 
	}
		
	function fruitful_shortcode_admin_init() {
		if ( is_admin() ) {
			require_once( FRUITFUL_SHORTCODE_INCLUDES . '/admin/admin-options.php' );
			require_once( FRUITFUL_SHORTCODE_INCLUDES . '/admin/admin-mce.php' );
		}	
	}
		
	function fruitful_shortcode_activation() {
		/*Nor Enough*/
	}
		
	function fruitful_shortcode_deactivation() {
		delete_option('ff_shortcodes_options');
		delete_option('ff_shortcodes_db_version');
	}
	
	function fruitful_init_shortcodes() {
		require_once FRUITFUL_SHORTCODE_INCLUDES . '/shortcodes/shortcodes.php';
	}
	
}

$ffs = new ffs();