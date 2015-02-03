<?php

get_template_part('inc/func/plugin-activation');

add_action( 'tgmpa_register', 'fruitful_register_required_plugins' );
/**
 * Register the required plugins for Fruitful theme.
 */
function fruitful_register_required_plugins() {

	$plugins = array(
		array(
			'name'    => 'Maintenance',
			'slug'    => 'maintenance',
			'required'  => false,
		),

		array(
			'name'    => 'Fruitful Shortcodes',
			'slug'    => 'fruitful-shortcodes',
			'required'  => true,
		),
		
	);

	$config = array(
		'domain'          => 'fruitful',          
		'default_path'    => '',                         
		'parent_menu_slug'  => 'themes.php',        
		'parent_url_slug'   => 'themes.php',        
		'menu'            => 'install-required-plugins',
		'has_notices'       => true,                    
		'is_automatic'      => true,         
		'message'       => '<br />1. Select all plugins checkbox to the left of "Plugin" <br />2. Click "Bulk Actions" and then Install <br />3. Click "Apply" button',              
		'strings'         => array(
			'page_title'                            => __( 'Fruitful Plugin Integration', 'fruitful' ),
			'menu_title'                            => __( 'Plugin Integration', 'fruitful' ),
			'installing'                            => __( 'Installing Plugin: %s', 'fruitful' ), // %1$s = plugin name
			'oops'                                  => __( 'Something went wrong with the plugin API.', 'fruitful' ),
			'notice_can_install_required'           => __( 'Fruitful theme requires the following plugin: %1$s.', 'fruitful' ), // %1$s = plugin name(s)
			'notice_can_install_recommended'      	=> __( 'Fruitful theme recommends the following plugin: %1$s.', 'fruitful' ),  // %1$s = plugin name(s)
			'notice_cannot_install'          	 	=> __( 'Sorry, but you do not have the correct permissions to install the %s plugin. Contact the administrator of this site for help on getting the plugin installed.',  'fruitful' ),  // %1$s = plugin name(s)
			'notice_can_activate_required'          => __( 'The following required plugin is currently inactive: %1$s.',  'fruitful' ),  // %1$s = plugin name(s)
			'notice_can_activate_recommended'     	=> __( 'The following recommended plugin is currently inactive: %1$s.', 'fruitful' ),  // %1$s = plugin name(s)
			'notice_cannot_activate'          		=> __( 'Sorry, but you do not have the correct permissions to activate the %s plugin. Contact the administrator of this site for help on getting the plugin activated.','fruitful' ),  // %1$s = plugin name(s)
			'notice_ask_to_update'           		=> __( 'The following plugin needs to be updated to its latest version to ensure maximum compatibility with Fruitful theme: %1$s.', 'fruitful' ), // %1$s = plugin name(s)
			'notice_cannot_update'            		=> __( 'Sorry, but you do not have the correct permissions to update the %s plugin. Contact the administrator of this site for help on getting the plugin updated.', 'fruitful' ), // %1$s = plugin name(s)
			'install_link'                  		=> __( 'Begin installing plugin', 'fruitful' ),
			'activate_link'                 		=> __( 'Activate installed plugin', 'fruitful' ),
			'return'                                => __( 'Return to Required Plugins Installer', 'fruitful' ),
			'plugin_activated'                      => __( 'Plugin activated successfully.', 'fruitful' ),
			'complete'                  			=> __( 'All plugins installed and activated successfully. %s', 'fruitful' ), // %1$s = dashboard link
			'nag_type'                 				=> 'updated' 
		)
	); 
	

  tgmpa( $plugins, $config );
}

?>