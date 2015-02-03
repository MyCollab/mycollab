<?php
	if ( ! defined( 'ABSPATH' ) ) exit;
	
	add_action( 'admin_init', 'fruitful_tinymce_button' );
	add_action( 'admin_print_scripts-post.php', 'get_pp', 20 );
	add_action( 'admin_print_scripts-post-new.php', 'get_pp', 20 );
	
	function fruitful_tinymce_button() {
		if ( current_user_can( 'edit_posts' ) && current_user_can( 'edit_pages' ) )  {
			 add_filter( 'mce_buttons_3', 'fruitful_register_tinymce_elements' );
		}
	}
			
	function get_pp() {
		$fruitful_options = get_option ('fruitful_options_plugin');

		if (empty($fruitful_options['fruitful_post_types'])) {
			$enable_post_types = array('post', 'page');
		} else {
			$enable_post_types = $fruitful_options['fruitful_post_types'];
		}
			
		if( in_array(get_post_type(), $enable_post_types)) {
			add_filter( 'mce_external_plugins', 'fruitful_add_tinymce_elements' );
		}
	}
	
	function fruitful_register_tinymce_elements( $buttons )  {
		array_push(	$buttons, 
     				'fruitful_horizontal_tabs',
     				'fruitful_vertical_tabs', 
     				'fruitful_accordion_tabs', 
					'fruitful_dbox', 
					'fruitful_one_half_column', 
					'fruitful_one_third_column', 
					'fruitful_two_third_column', 
					'fruitful_one_fourth_column', 
					'fruitful_three_fourth_column', 
					'fruitful_one_fifth_column', 
					'fruitful_sep', 
					'fruitful_alerts', 
					'fruitful_pbar',
					'fruitful_btn'
					);  
		return $buttons;
	}

	function fruitful_add_tinymce_elements( $plugin_array )  {
		$plugin_array['fruitful_horizontal_tabs']   = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_vertical_tabs'] 	= plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_accordion_tabs']    = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_dbox']   		    = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_one_half_column']   = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_one_third_column']  = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_two_third_column']  = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_one_fourth_column'] = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_three_fourth_column'] = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_one_fifth_column'] = plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_sep'] 		= plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_alerts'] 	= plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_pbar'] 		= plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		$plugin_array['fruitful_btn'] 		= plugins_url('/tinymce', __FILE__) . '/ffs.tinymce.js';
		
	
		return $plugin_array;
	}

	foreach( array('post.php','post-new.php') as $hook ) add_action( "admin_head-$hook", 'fruitful_admin_head' );
	 
	function fruitful_admin_head()  {
		$plugin_url = plugins_url( '/', __FILE__ ); ?>
		<script type='text/javascript'> var my_plugin = { 'url': '<?php echo $plugin_url; ?>' }; </script>
	<?php
}