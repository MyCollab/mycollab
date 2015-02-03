<?php
/*
	Plugin Name: Maintenance
	Plugin URI: http://wordpress.org/plugins/maintenance/
	Description: Take your website for maintenance away from public view. Use maintenance plugin if your website is in development or you need to change a few things, run an upgrade. Make it only accessible by login and password. Plugin has a options to add a logo, background, headline, message, colors, login, etc. Extended PRO with more features version is available for purchase.
	Version: 2.4
	Author: fruitfulcode
	Author URI: http://fruitfulcode.com
	License: GPL2
*/
/*  Copyright 2013  Fruitful Code  (email : mail@fruitfulcode.com)

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

class maintenance {
		function __construct() {
			global	$maintenance_variable;
			$maintenance_variable = new stdClass;

			add_action( 'plugins_loaded', array( &$this, 'constants'), 	1);
			add_action( 'plugins_loaded', array( &$this, 'lang'),		2);
			add_action( 'plugins_loaded', array( &$this, 'includes'), 	3);
			add_action( 'plugins_loaded', array( &$this, 'admin'),	 	4);

			
			register_activation_hook  ( __FILE__, array( &$this,  'activation' ));
			register_deactivation_hook( __FILE__, array( &$this,'deactivation') );
			
			add_action('wp', array( &$this, 'mt_template_redirect'), 1);
			add_action('wp_logout',	array( &$this, 'mt_user_logout'));
			add_action('init', array( &$this, 'mt_admin_bar'));
		}
		
		function constants() {
			define( 'MAINTENANCE_VERSION', '2.0.0' );
			define( 'MAINTENANCE_DB_VERSION', 1 );
			define( 'MAINTENANCE_WP_VERSION', get_bloginfo( 'version' ));
			define( 'MAINTENANCE_DIR', trailingslashit( plugin_dir_path( __FILE__ ) ) );
			define( 'MAINTENANCE_URI', trailingslashit( plugin_dir_url( __FILE__ ) ) );
			define( 'MAINTENANCE_INCLUDES', MAINTENANCE_DIR . trailingslashit( 'includes' ) );
			define( 'MAINTENANCE_LOAD',     MAINTENANCE_DIR . trailingslashit( 'load' ) );
		}
		
		function lang() {
			load_plugin_textdomain( 'maintenance', false, dirname( plugin_basename( __FILE__ ) ) . '/languages/' );		
		}	
		
		function includes() {
			require_once( MAINTENANCE_INCLUDES . 'functions.php' ); 
			require_once( MAINTENANCE_INCLUDES . 'update.php' ); 
			require_once( MAINTENANCE_DIR 	   . 'load/functions.php' ); 
		}
		
		function admin() {
			if ( is_admin() ) {
				require_once( MAINTENANCE_INCLUDES . 'admin.php' );
			}	
		}
		
		function activation() {
		}
		function deactivation() {
			delete_option('maintenance_options');
			delete_option('maintenance_db_version');
		}
		
		function mt_user_logout() { 
			wp_safe_redirect(get_bloginfo('url'));
			exit; 
		}
		
		function mt_template_redirect() {
			load_maintenance_page();
		}
		
		function mt_admin_bar() {
			add_action('admin_bar_menu', 'maintenance_add_toolbar_items', 100);
			if (!is_admin() ) {
				$mt_options = mt_get_plugin_options(true);
				if (isset($mt_options['admin_bar_enabled']) && is_user_logged_in()) { 
					add_filter('show_admin_bar', '__return_true');  																	 
				} else {
					add_filter('show_admin_bar', '__return_false');  																	 
				}
			}	
		}
}

$maintenance = new maintenance();
?>