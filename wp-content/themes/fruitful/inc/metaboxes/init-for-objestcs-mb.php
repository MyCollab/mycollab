<?php
/**
 * Include and setup custom metaboxes and fields.
 *
 * @category Fruitful
 * @package  Metaboxes
 * @license  http://www.opensource.org/licenses/gpl-license.php GPL v2.0 (or later)
 * @link     https://github.com/webdevstudios/Custom-Metaboxes-and-Fields-for-WordPress
 */

 
add_action( 'init', 'cmb_initialize_cmb_meta_boxes', 999 );
function cmb_initialize_cmb_meta_boxes() {
	if ( ! class_exists( 'cmb_Meta_Box' ) ) {
		require_once dirname(__FILE__) . '/init.php';
		require_once dirname(__FILE__) . '/custom-fields-for-metaboxes.php';
	}	
	
}

add_filter( 'cmb_meta_boxes', 'fruitful_all_metaboxes', 10, 2 );
/**
 * Define the metabox and field configurations.
 *
 * @param  array $meta_boxes
 * @return array
 */
function fruitful_all_metaboxes( array $meta_boxes ) {
	// Start with an underscore to hide fields from custom fields list
	$prefix = '_fruitful_';
	
	$meta_boxes['fruitful_slider_settings'] = array(
		'id'         => 'fruitful_slider_settings',
		'title'      => __( 'Slider settings', 'fruitful' ),
		'context'    => 'normal',
		'priority'   => 'low',
		'show_names' => true, 
		'pages'      => array( 'page'), 
		//'show_on'	 =>	array( 'key'=>'front-page', 'value' => '' ),
		'fields'     => array(
				array(
					'name' 		=> __('Slider display', 'fruitful'),
					'id' 		=> $prefix . 'slider_layout',
					'subname'   => __('Select the option to display the corresponding slider', 'fruitful'),
					'std' 		=> '0',
					'options' 	=> array(
										'0' => __('Disable slider', 'fruitful'),
										'1' => __('Full width slider', 'fruitful'),
										'2' => __('Boxed slider', 'fruitful'),
										),
					'type' => 'select'
				),
		),
	);
	
	// Only with sidebar enabled
	$meta_boxes['fruitful_page_general_settings'] = array(
		'id'         => 'fruitful_page_general_settings',
		'title'      => __( 'Page settings', 'fruitful' ),
		'pages'      => array( 'page'), 
		'context'    => 'normal',
		'priority'   => 'low',
		'show_names' => true, 
		'fields'     => array(
			array(
				'name' 		=> __( 'Layout', 'fruitful' ),
				'subname' 	=> __( 'Select a specific layout for this page.', 'fruitful' ),
				'id'      	=> $prefix . 'page_layout',
				'type' 	  	=> 'custom_layout_sidebars',
				'default' 	=> '1'
			),
		),
	);
	
	
	$meta_boxes['fruitful_post_general_settings'] = array(
		'id'         => 'fruitful_post_general_settings',
		'title'      => __( 'Post settings', 'fruitful' ),
		'pages'      => array( 'post'), 
		'context'    => 'normal',
		'priority'   => 'low',
		'show_names' => true, 
		'fields'     => array(
			array(
				'name' 		=> __( 'Layout', 'fruitful' ),
				'subname' 	=> __( 'Select a specific layout for this post.', 'fruitful' ),
				'id' 		=> $prefix . 'page_layout',
				'type' 		=> 'custom_layout_sidebars',
				'default' 	=> '2'
			),
		),
	);
	
	
	return $meta_boxes;
}	