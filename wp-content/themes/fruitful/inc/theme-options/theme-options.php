<?php
/**
 * Fruitful theme Theme Options
 *
 * @package Fruitful theme
 * @since Fruitful theme 1.0
 */

/**
 * Register the form setting for our fruitful_options array.
 *
 * This function is attached to the admin_init action hook.
 *
 * This call to register_setting() registers a validation callback, fruitful_theme_options_validate(),
 * which is used when the option is saved, to ensure that our option values are properly
 * formatted, and safe.
 *
 * @since Fruitful theme 1.0
 */
function fruitful_theme_options_init() {
	register_setting(
		'fruitful_options', 		// Options group, see settings_fields() call in fruitful_theme_options_render_page()
		'fruitful_theme_options', 	// Database option, see fruitful_get_theme_options()
		'' 							// The sanitization callback, see fruitful_theme_options_validate()
	);
	$is_demo_content_installed = get_option('fruitful_demo_content');
	
	// Register our settings field group
	add_settings_section('general',			'',  '__return_false', 'theme_options' );
	add_settings_section('header',			'',  '__return_false', 'theme_options' );
	add_settings_section('background',		'',  '__return_false', 'theme_options' );
	add_settings_section('logo', 			'',  '__return_false', 'theme_options' );
	add_settings_section('colors', 			'',  '__return_false', 'theme_options' );
	add_settings_section('fonts', 			'',  '__return_false', 'theme_options' );
	add_settings_section('slider', 			'',  '__return_false', 'theme_options' );
	add_settings_section('links', 			'',  '__return_false', 'theme_options' );
	add_settings_section('footer', 			'',  '__return_false', 'theme_options' );
	add_settings_section('css', 			'',  '__return_false', 'theme_options' );
	add_settings_section('woocommerce', 	'',  '__return_false', 'theme_options' );
	
	if (!$is_demo_content_installed) {
		//add_settings_field( 'general_idc', 		__( 'Install Home Page', 	'fruitful' ),	'fruitful_demo_content',	'theme_options',  'general', array('info' => __( 'Setup home page with dummy data, like on demo. Option can be used once.', 'fruitful' )));
	}
	
	add_settings_field( 'general_rs', 		__( 'Layout', 	'fruitful' ),	'fruitful_get_responsive_design',	'theme_options',  'general', array('info' => __( 'Theme supported 2 types of html layout. Default responsive  setting which adapt for mobile devices and static page with fixed width. Uncheck arrow below if you need static website display. ', 'fruitful' )));
	add_settings_field( 'general_cm',		__( 'Comments', 'fruitful' ), 	'fruitful_get_general_comment',  	'theme_options',  'general', array('info' => __( 'If you want to display comments on your post page or page, select options below.', 'fruitful' )));
	add_settings_field( 'general_ds',		__( 'Default theme styles',  'fruitful' ),'fruitful_get_style_theme', 'theme_options',  'general', array('info' => __( 'Default CSS. Theme option for styling is not working, if this option enable.', 'fruitful' )));
	add_settings_field( 'general_lp',		__( 'Front page template with latest posts',    'fruitful' ),'fruitful_latest_posts_template','theme_options',  'general', array('info' => __( 'Settings > Reading > Front page displays > Your latest posts', 'fruitful' )));
	add_settings_field( 'general_cp',		__( 'Custom wordpress pages layout',   'fruitful' ),   'fruitful_custom_pages_layout','theme_options',  'general',   array('info' => __( 'Set template for custom pages.', 'fruitful' )));
	add_settings_field( 'general_sf',		__( 'Show Featured image on single post', 'fruitful' ),'fruitful_show_featured_single_post','theme_options',  'general', array('info' => __( 'Select option below for show featured image on single post page.', 'fruitful' )));
	if(function_exists('icl_get_languages')){ // if WPML is activated
		add_settings_field( 'general_wpml',		__( 'Multilingual Switch in Header (WPML)', 'fruitful' ), 'fruitful_wpml_ready',  'theme_options',  'general', array('info' => __( 'If you wish to show Language Switch in header, select option below. ', 'fruitful' )));
	}
	add_settings_field( 'general_rb',		__( 'Reset options', 'fruitful' ), 'fruitful_reset_btn',  'theme_options',  'general', array('info' => __( 'All theme options will be reset to default. ', 'fruitful' )));
	
	add_settings_field( 'header_hd',		__( 'Sticky  header', 	 		 'fruitful' ), 	'fruitful_get_general_header', 	'theme_options',  'header', array('info' => __( 'Options relating to the website header', 'fruitful' )));
	add_settings_field( 'header_hi',		__( 'Background for header', 	 'fruitful' ), 	'fruitful_get_header_img', 		'theme_options',  'header', array('info' => __( 'Upload image with full width for background in header area. (Supported files .png, .jpg, .gif)  ', 'fruitful' )));
	add_settings_field( 'header_hs',		__( 'Background image size', 	 'fruitful' ), 	'fruitful_get_header_img_size', 'theme_options',  'header', array('info' => __( 'Choose size for background image - full width or only for content area.', 'fruitful' )));
	add_settings_field( 'header_hh',		__( 'Height for header area', 	 'fruitful' ), 	'fruitful_get_header_height', 	'theme_options',  'header', array('info' => __( 'Minimum height in pixels', 'fruitful' )));
	add_settings_field( 'header_mp',		__( 'Menu Position', 			 'fruitful' ), 	'fruitful_set_menu_position', 	'theme_options',  'header', array('info' => __( 'Set menu position.', 'fruitful' )));
	add_settings_field( 'header_rt',		__( 'Type of Responsive menu', 	 'fruitful' ), 	'fruitful_get_menu_type_resp', 	'theme_options',  'header', array('info' => __( 'Set type of responsive menu.', 'fruitful' )));
		
	add_settings_field( 'background_image', __( 'Background Image', 'fruitful' ),  'fruitful_get_background_img',   'theme_options',  'background', array('info' => __( 'Upload your background image for site background. (Supported files .png, .jpg, .gif)', 'fruitful' )));
	add_settings_field( 'background_color', __( 'Background Color ', 'fruitful' ), 'fruitful_get_background_color', 'theme_options',  'background', array('info' => __( 'Choose color for body background', 'fruitful' )));
	add_settings_field( 'content_background_color', __( 'Background color for content  ', 'fruitful' ), 'fruitful_get_container_background_color', 'theme_options',  'background', array('info' => __( 'Choose color for main content area', 'fruitful' )));
		
	add_settings_field( 'logo_image', 		__( 'Logo image', 'fruitful' ), 	'fruitful_get_logo_img', 		'theme_options', 'logo',  	array('info' => __( 'Upload logo image for your website. Size is original (Supported files .png, .jpg, .gif)', 'fruitful' )));
  //add_settings_field( 'logo_size', 		__( 'Logo Size', 'fruitful' ), 		'fruitful_get_logo_wh',	 		'theme_options', 'logo',  	array('info' => __( 'Specify resolution for your logo image. Our theme will crop (timthumb) your image for need size.', 'fruitful' )) );
	add_settings_field( 'fav_icon', 		__( 'Favicon', 'fruitful' ), 		'fruitful_get_fav_icon', 		'theme_options', 'logo',  	array('info' => __( 'Upload needed image for site favicon. (Supported files .ico (16x16))', 'fruitful' )));
	add_settings_field( 'logo_position', 	__( 'Logo Position', 'fruitful' ), 	'fruitful_set_logo_position', 	'theme_options', 'logo',  	array('info' => __( 'Set Logo Position', 'fruitful' )));
	
	
	add_settings_field( 'menu_style',			__( 'Main menu color', 'fruitful' ),		'fruitful_menu_style_color', 			'theme_options', 'colors', array('info' => __( 'Choose your colors for main menu in header', 'fruitful' ), 'newrow' => true) );
	add_settings_field( 'dropdown_menu_style',	__( 'Dropdown menu color', 'fruitful' ),	'fruitful_dropdown_menu_style_color', 	'theme_options', 'colors', array('info' => __( 'Choose your colors for dropdown menu in header', 'fruitful' )) );
	add_settings_field( 'font_style',			__( 'General font color', 'fruitful' ),		'fruitful_font_style_color',			'theme_options', 'colors', array('info' => __( 'Choose your colors for text and links', 'fruitful' ), 'newrow' => true) );
	add_settings_field( 'separator_style',		__( 'Color for lines', 'fruitful' ),		'fruitful_sep_style_color',				'theme_options', 'colors', array('info' => __( 'Choose your colors for lines and separators', 'fruitful' )) );
	add_settings_field( 'button_style',			__( 'Color for buttons', 'fruitful' ),		'fruitful_but_style_color',				'theme_options', 'colors', array('info' => __( 'Choose your colors for buttons', 'fruitful' ), 'newrow' => true) );
	add_settings_field( 'social_style',			__( 'Color for social icons', 'fruitful' ),	'fruitful_soc_icon_style_color',		'theme_options', 'colors', array('info' => __( 'Choose your colors for social icons', 'fruitful' )) );
	if (class_exists('Woocommerce')) { 
		add_settings_field( 'woocommerce_style',	__( 'WooCommerce colors', 'fruitful' ),		'fruitful_woo_colors',					'theme_options', 'colors', array('info' => __( 'Choose your colors for WooCommerce', 'fruitful' ), 'newrow' => true) );
	}	
	
	
	add_settings_field( 'fonts_options', 	__( 'Fonts', 'fruitful' ), 	'fruitful_fonts_options',	'theme_options', 'fonts', 		array('info' => __( 'Popular web safe font collection, select and use for your needs.', 'fruitful' )) );
	
	add_settings_field( 'fonts_headers', 	__( 'Headers', 'fruitful' ),'fruitful_fonts_headers',	'theme_options', 'fonts', array('info' => __( 'Choose font-family for all headlines.', 'fruitful' )) );
	add_settings_field( 'fonts_menu', 		__( 'Menu',    'fruitful' ),'fruitful_fonts_menu',		'theme_options', 'fonts', array('info' => __( 'Choose font-family for primary menu.', 'fruitful' )) );
	add_settings_field( 'fonts_content', 	__( 'Body', 'fruitful' ),   'fruitful_fonts_content',	'theme_options', 'fonts', array('info' => __( 'Choose font-family for content.', 'fruitful' )) );
	
	add_settings_field( 'fonts_size', 		__( 'Font size', 'fruitful' ), 		'fruitful_fonts_size',		'theme_options', 'fonts',  array('info' => __( 'Choose font size for specific html elements. Set size as number, without px.', 'fruitful' )) );
	add_settings_field( 'slider_select',	__( 'Slider', 'fruitful' ),	'fruitful_slider_select',	'theme_options', 'slider', array('info' => __( 'Select a slider type that will be used by default.', 'fruitful' )) );
	add_settings_field( 'slider_options',	__( 'Slider Options', 'fruitful' ),	'fruitful_slider_options',	'theme_options', 'slider', array('info' => __( 'Choose needed options for slider: animation type, sliding direction, speed of animations, etc', 'fruitful' )) );
	add_settings_field( 'slider_image',		__( 'Slides', 'fruitful' ), 		'fruitful_slider_images',	'theme_options', 'slider', array('info' => __( 'Add images to slider (.png, .jpg, .gif). Drag and drop to rearrange slide order. Slider will display images in original size that you load to media gallery. Please upload images of the same size, to get the best look on the page. To display slider use home page slider settings. Current theme version supports only one slider per website. ', 'fruitful' )) );
	
	add_settings_field( 'socials_links_position', 	__( 'Socials Links Position', 'fruitful' ), 	'fruitful_settings_field_socials_links_position', 'theme_options', 'links', array('info' => __( 'Choose place where social links will be displayed.', 'fruitful' )) );
	add_settings_field( 'socials_links', 	__( 'Socials Links', 'fruitful' ), 	'fruitful_settings_field_socials_links', 'theme_options', 'links', array('info' => __( 'Add link to your social media profiles. Icons with link will be display in header or footer.', 'fruitful' )) );
	add_settings_field( 'footer_text_copy',	__( 'Footer options', 'fruitful' ), 'fruitful_settings_field_footer_text', 'theme_options',   'footer', array('info' => __( 'Replace default theme copyright information and links', 'fruitful' )) );
	
	add_settings_field( 'custom_css',		__( 'Custom CSS', 'fruitful' ), 'fruitful_settings_field_custom_css', 'theme_options', 'css' , array('info' => __( 'Theme has two css files style.css and fixed-style.css which use default styles for front-end responsive and static layout. Do not edit theme default css files, use textarea editor below for overwriting all css styles.', 'fruitful' )) );
	
	if (class_exists('Woocommerce')) { 
		add_settings_field( 'general_sc',		 __( 'Show cart in header',  'fruitful' ),'fruitful_show_cart_theme', 'theme_options',  'woocommerce', array('info' => __( 'If you want to display cart link in header select options below.', 'fruitful' )));
		add_settings_field( 'woo_shop_sidebar',	 __( 'Woocommerce Shop Sidebar', 'fruitful' ), 	'fruitful_woo_shop_sidebar', 'theme_options', 'woocommerce', 		array('info' => __( 'Show or hide sidebar', 'fruitful' )));
		add_settings_field( 'woo_product_sidebar', __( 'Woocommerce Product Sidebar', 'fruitful' ), 	'fruitful_woo_product_sidebar', 'theme_options', 'woocommerce', 		array('info' => __( 'Show or hide sidebar', 'fruitful' )));
		add_settings_field( 'shop_num_row',		 __( 'Woocommerce pages products per row', 'fruitful' ), 	'fruitful_set_shop_num_row', 'theme_options', 'woocommerce', 		array('info' => __( 'Choose number of products', 'fruitful' )));
		add_settings_field( 'woo_shop_num_prod', __( 'Number of products on Shop pages', 'fruitful' ), 	'fruitful_woo_shop_prod', 'theme_options', 'woocommerce', 		array('info' => __( 'Choose number of products. Write -1 for show all products on one page', 'fruitful' )));
	}
	
	if(!get_option( 'fruitful_theme_options' )) {
		add_option( 'fruitful_theme_options', fruitful_get_theme_options());
	}
}
	
add_action( 'admin_init', 'fruitful_theme_options_init' );

add_action( 'admin_enqueue_scripts', 'add_admin_options_and_styles' );
function add_admin_options_and_styles($hook) {
	if( 'appearance_page_theme_options' != $hook ) return;
	fruitful_add_jquery_script();
	fruitful_add_admin_style();
} 

/**
 * Change the capability required to save the 'fruitful_options' options group.
 *
 * @see fruitful_theme_options_init() First parameter to register_setting() is the name of the options group.
 * @see fruitful_theme_options_add_page() The edit_theme_options capability is used for viewing the page.
 *
 * @param string $capability The capability used for the page, which is manage_options by default.
 * @return string The capability to actually use.
 */
function fruitful_option_page_capability( $capability ) {
	return 'edit_theme_options';
}
add_filter( 'option_page_capability_fruitful_options', 'fruitful_option_page_capability' );

/**
 * Add our theme options page to the admin menu.
 *
 * This function is attached to the admin_menu action hook.
 *
 * @since Fruitful theme 1.0
 */
add_action( 'admin_menu', 'fruitful_theme_options_add_page' );
 function fruitful_theme_options_add_page() {
	$theme_page = add_theme_page(
		__( 'Fruitful Theme Options', 'fruitful' ),   	// Name of page
		__( 'Theme Options', 'fruitful' ),   			// Label in menu
			'edit_theme_options',          	 			// Capability required
			'theme_options',                     		// Menu slug, used to uniquely identify the page
			'fruitful_theme_options_render_page' 		// Function that renders the options page
	);
}


add_action( 'admin_bar_menu', 'fruitful_add_custom_link_options', 1000 );
function fruitful_add_custom_link_options() {
    global $wp_admin_bar, $wpdb;
		if ( !is_super_admin() || !is_admin_bar_showing() )
		return;

	/* Add the main siteadmin menu item */
    $wp_admin_bar->add_menu( array( 'id' => 'fruitfultheme_options', 'title' => __( 'Theme Options', 'fruitful' ), 'href' => admin_url('admin.php?page=theme_options')));	
}


/**
 * Returns the options array for Fruitful theme.
 *
 * @since Fruitful theme 1.0
 */
function fruitful_get_theme_options() {
	$saved = (array) get_option( 'fruitful_theme_options' );
	$defaults = fruitful_get_default_array();
	$defaults = apply_filters( 'fruitful_default_theme_options', $defaults );
	$options  = wp_parse_args( $saved, $defaults );
	$options = array_intersect_key( $options, $defaults );
	return $options;
}

function fruitful_demo_content() {
	?>	
	<div class="box-option">
		<input type="button" id="btn_idc" class="btn_idc button button-primary" value="<?php _e('Dummy data', 'fruitful');?>"/>
	</div>	
<?php }

function fruitful_get_responsive_design() { 
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<label for="responsive_ch"><input type="checkbox" name="fruitful_theme_options[responsive]" id="responsive_ch" <?php checked( 'on', $options['responsive']); ?> />
			<?php _e( 'Responsive', 'fruitful' ); ?>
		</label>
	</div>
<?php }

function fruitful_show_featured_single_post(){
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<label for="show_featured_single"><input type="checkbox" name="fruitful_theme_options[show_featured_single]" id="show_featured_sf" <?php checked( 'on', $options['show_featured_single']); ?> />
			<?php _e( 'Show featured image', 'fruitful' ); ?>
		</label>
	</div>
<?php }

function fruitful_get_general_comment() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-option">
			<label for="postcomment_ch"><input type="checkbox" name="fruitful_theme_options[postcomment]" id="postcomment_ch" <?php checked( 'on', $options['postcomment']); ?> />
			<?php _e( 'Display comment on posts page', 'fruitful' ); ?>
			</label>
		</div>
		<div class="box-option">
			<label for="pagecomment_ch"><input type="checkbox" name="fruitful_theme_options[pagecomment]" id="pagecomment_ch" <?php checked( 'on', $options['pagecomment']); ?> />
			<?php _e( 'Display comment on page', 'fruitful' ); ?>
			</label>
		</div>
	<?php	
}

function fruitful_get_general_header() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-option">
			<label for="is_fixed_header_ch"><input type="checkbox" name="fruitful_theme_options[is_fixed_header]" id="is_fixed_header_ch" <?php checked( 'on', $options['is_fixed_header']); ?> />
			<?php _e( 'Enabled', 'fruitful' ); ?>
			</label>
		</div>
	<?php
}

function fruitful_get_style_theme() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-option">
			<label for="style_theme"><input type="checkbox" name="fruitful_theme_options[styletheme]" id="style_theme" <?php checked( 'on', $options['styletheme']); ?> />
			<?php _e( 'Enable', 'fruitful' ); ?>
			</label>
		</div>
	<?php	
}

function fruitful_show_cart_theme() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-option">
			<label for="show_cart"><input type="checkbox" name="fruitful_theme_options[showcart]" id="show_cart" <?php checked( 'on', $options['showcart']); ?> />
			<?php _e( 'Enable', 'fruitful' ); ?>
			</label>
		</div>
	<?php	
}

function fruitful_reset_btn() { 
	?>
	<div class="box-option">
		<input name="reset" class="button-primary reset-btn" value="<?php esc_attr_e('Reset Defaults', 'fruitful'); ?>" />		
	</div>	
	<?php	
}

function fruitful_wpml_ready() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-option">
			<label for="is_wpml_ready_ch"><input type="checkbox" name="fruitful_theme_options[is_wpml_ready]" id="is_wpml_ready_ch" <?php checked( 'on', $options['is_wpml_ready']); ?> />
			<?php _e( 'Enable', 'fruitful' ); ?>
			</label>
		</div>
		<?php 
}

function fruitful_settings_field_socials_links_position() {
	$options = fruitful_get_theme_options();
	?>
	<div class="soc_links_positions">
		<?php fruitful_get_select_fields('sl_position', $options, fruitful_social_links_positions_list(), 'select-position'); ?>
	</div>
	<?php
}	

function fruitful_settings_field_socials_links() {
	$options = fruitful_get_theme_options();
	?>
	<div class="socials">
		<h4><?php _e('Facebook', 'fruitful'); ?></h4>		<input id="facebook_url" 	class="text-input" name="fruitful_theme_options[facebook_url]" 	type="text"   value="<?php echo esc_url( $options['facebook_url'] ); ?>"/>
		<h4><?php _e('Twitter', 'fruitful'); ?></h4>		<input id="twitter_url" 	class="text-input" name="fruitful_theme_options[twitter_url]" 	type="text"   value="<?php echo esc_url( $options['twitter_url'] ); ?>"/>
		<h4><?php _e('LinkedIn', 'fruitful'); ?></h4>		<input id="linkedin_url" 	class="text-input" name="fruitful_theme_options[linkedin_url]" 	type="text"   value="<?php echo esc_url( $options['linkedin_url'] ); ?>"/>
		<h4><?php _e('MySpace', 'fruitful'); ?></h4>		<input id="myspace_url" 	class="text-input" name="fruitful_theme_options[myspace_url]" 	type="text"   value="<?php echo esc_url( $options['myspace_url'] ); ?>"/>
		<h4><?php _e('Google Plus+', 'fruitful'); ?></h4>	<input id="googleplus_url" 	class="text-input" name="fruitful_theme_options[googleplus_url]" type="text"  value="<?php echo esc_url( $options['googleplus_url'] ); ?>"/>
		<h4><?php _e('Dribbble', 'fruitful'); ?></h4>		<input id="dribbble_url" 	class="text-input" name="fruitful_theme_options[dribbble_url]" 	type="text"   value="<?php echo esc_url( $options['dribbble_url'] ); ?>"/>
		<h4><?php _e('Skype', 'fruitful'); ?></h4>			<input id="skype_link" 		class="text-input" name="fruitful_theme_options[skype_link]" 	type="text"   value="<?php echo esc_attr( $options['skype_link'] ); ?>"/>
		<h4><?php _e('Flickr', 'fruitful'); ?></h4>			<input id="flickr_link" 	class="text-input" name="fruitful_theme_options[flickr_link]" 	type="text"   value="<?php echo esc_url( $options['flickr_link'] ); ?>"/>
		<h4><?php _e('You Tube', 'fruitful'); ?></h4>		<input id="youtube_url" 	class="text-input" name="fruitful_theme_options[youtube_url]"	type="text"   value="<?php echo esc_url( $options['youtube_url'] ); ?>"/>
		<h4><?php _e('Vimeo', 'fruitful'); ?></h4>			<input id="viemo_url" 		class="text-input" name="fruitful_theme_options[vimeo_url]"		type="text"   value="<?php echo esc_url( $options['vimeo_url'] ); ?>"/>
		<h4><?php _e('RSS', 'fruitful'); ?></h4>			<input id="rss_link" 		class="text-input" name="fruitful_theme_options[rss_link]" 		type="text"   value="<?php echo esc_url( $options['rss_link'] ); ?>"/>
		<h4><?php _e('Vk.com', 'fruitful'); ?></h4>			<input id="vk_link" 		class="text-input" name="fruitful_theme_options[vk_link]" 		type="text"   value="<?php echo esc_url( $options['vk_link'] ); ?>"/>
		<h4><?php _e('Instagram', 'fruitful'); ?></h4>		<input id="instagram_url"	class="text-input" name="fruitful_theme_options[instagram_url]"	type="text"   value="<?php echo esc_url( $options['instagram_url'] ); ?>"/>
		<h4><?php _e('Pinterest', 'fruitful'); ?></h4>		<input id="pinterest_url"	class="text-input" name="fruitful_theme_options[pinterest_url]"	type="text"   value="<?php echo esc_url( $options['pinterest_url'] ); ?>"/>
		<h4><?php _e('Yelp', 'fruitful'); ?></h4>			<input id="yelp_url"		class="text-input" name="fruitful_theme_options[yelp_url]"		type="text"   value="<?php echo esc_url( $options['yelp_url'] ); ?>"/>
		<h4><?php _e('E-mail', 'fruitful'); ?></h4>			<input id="email_link" 		class="text-input" name="fruitful_theme_options[email_link]" 	type="text"   value="<?php echo sanitize_email( $options['email_link'] ); ?>"/>
		<h4><?php _e('Github', 'fruitful'); ?></h4>			<input id="github_link"		class="text-input" name="fruitful_theme_options[github_link]" 	type="text"   value="<?php echo esc_url( $options['github_link'] ); ?>"/>
		<h4><?php _e('Tumblr', 'fruitful'); ?></h4>			<input id="tumblr_link"		class="text-input" name="fruitful_theme_options[tumblr_link]" 	type="text"   value="<?php echo esc_url( $options['tumblr_link'] ); ?>"/>
		<h4><?php _e('Soundcloud', 'fruitful'); ?></h4>		<input id="soundcloud_link"	class="text-input" name="fruitful_theme_options[soundcloud_link]" type="text" value="<?php echo esc_url( $options['soundcloud_link'] ); ?>"/>
	</div>
	<?php
}	
	
function fruitful_settings_field_footer_text() {
	$options = fruitful_get_theme_options();
	?>
	<h4><?php _e( 'Copyright section', 'fruitful' ); ?></h4><textarea class="large-text" name="fruitful_theme_options[footer_text]" id="footer-text" cols="50" rows="20" /><?php echo stripslashes($options['footer_text']); ?></textarea>
<?php
}

function fruitful_settings_field_custom_css() {
	$options = fruitful_get_theme_options();
	?>
	<h4><?php _e( 'Styles editor', 'fruitful' ); ?></h4><textarea class="large-text" name="fruitful_theme_options[custom_css]" id="css-text" cols="50" rows="30" /><?php echo stripslashes($options['custom_css']); ?></textarea>
<?php
}

function fruitful_get_background_img () {
	$options = fruitful_get_theme_options();
	$upload  = $options['backgroung_img'];
	
	echo fruitful_get_box_upload_image($upload, 'backgroung_img'); 
	?>

	<div class="box-option">
		<label for="bg_checkbox">
			<input type="checkbox" name="fruitful_theme_options[bg_repeating]" id="bg_checkbox" <?php checked( 'on', $options['bg_repeating'] ); ?> />
			<?php _e( 'Background  repeat', 'fruitful' ); ?>
		</label>
	</div>
	
	<?php
	}

function fruitful_get_header_img () {
	$options = fruitful_get_theme_options();
	$upload  = intval($options['header_img']);

	echo fruitful_get_box_upload_image($upload, 'header_img', 'upload_btn', 'reset_btn', 'headerbackground', 'headerimgbackground');
	?>
	<div class="box-option">
		<h4><?php _e( 'Header background-color', 'fruitful' ); ?></h4>
		<input type="text" id="header_bg_color" class="colorPicker" name="fruitful_theme_options[header_bg_color]" value="<?php echo esc_attr($options['header_bg_color']); ?>" data-default-color="#ffffff"/>
	</div>
	<?php
}

function fruitful_get_header_img_size () {
	$options = fruitful_get_theme_options();
?>
	<div class="box-option">
		<?php fruitful_get_select_fields('header_img_size', $options, fruitful_get_header_img_sizes(), 'headerimgbackground'); ?>		
	</div>
<?php 
}

function fruitful_get_header_height() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<input type="text" name="fruitful_theme_options[header_height]" id="header_height" class="header_height small_input text-input" value="<?php echo intval($options['header_height']); ?>"/>
	</div>	
<?php	
}

function fruitful_latest_posts_template() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('latest_posts_templ', $options, fruitful_custom_layouts(), 'latest_posts_template'); ?>		
	</div>	
<?php	
}

function fruitful_custom_pages_layout() {
	$options = fruitful_get_theme_options();
	?>
	
	<div class="box-option"><?php fruitful_get_select_fields('layout_archive_templ', $options, fruitful_custom_layouts(), 'layout_archive_template', 	__('Archive:', 'fruitful' )); ?></div>	
	<div class="box-option"><?php fruitful_get_select_fields('layout_author_templ',	 $options, fruitful_custom_layouts(), 'layout_author_template', 	__('Author:', 'fruitful' )); ?></div>	
	<div class="box-option"><?php fruitful_get_select_fields('layout_cat_templ',	 $options, fruitful_custom_layouts(), 'layout_cat_template', 		__('Category:', 'fruitful' )); ?></div>	
	<div class="box-option"><?php fruitful_get_select_fields('layout_tag_templ',	 $options, fruitful_custom_layouts(), 'layout_tag_template', 		__('Tags:', 'fruitful' )); ?></div>	
	<div class="box-option"><?php fruitful_get_select_fields('layout_404_templ',	 $options, fruitful_custom_layouts(), 'layout_404_template', 		__('404:', 'fruitful' )); ?></div>	
	<div class="box-option"><?php fruitful_get_select_fields('layout_search_templ',  $options, fruitful_custom_layouts(), 'layout_search_template', 	__('Search:', 'fruitful' )); ?></div>	
<?php	
}

function fruitful_set_menu_position() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('menu_position', $options, fruitful_elem_position(), 'menu-position'); ?>		
	</div>	
<?php	
}


function fruitful_get_menu_type_resp() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('menu_type_responsive', $options, fruitful_get_menu_type_select(), 'menu-type-responsive'); ?>		
	</div>	
<?php	
}

function fruitful_woo_shop_sidebar() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('woo_shop_sidebar', $options, fruitful_woo_shop_sidebar_list(), 'woocommerce'); ?>		
	</div>	
<?php	
}

function fruitful_woo_product_sidebar() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('woo_product_sidebar', $options, fruitful_woo_shop_sidebar_list(), 'woocommerce'); ?>		
	</div>	
<?php	
}

function fruitful_set_shop_num_row() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('shop_num_row', $options, fruitful_number_per_row(), 'woocommerce'); ?>		
	</div>	
<?php	
}

function fruitful_woo_shop_prod() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<input type="text" name="fruitful_theme_options[woo_shop_num_prod]" id="woo_shop_num_prod" class="woo_shop_num_prod small_input text-input" value="<?php echo intval($options['woo_shop_num_prod']); ?>"/>
	</div>	
<?php	
}

function fruitful_get_logo_img () {
	$options = fruitful_get_theme_options();
	$upload  = intval($options['logo_img']);

	echo fruitful_get_box_upload_image($upload, 'logo_img', 'upload_btn', 'reset_btn', 'logo'); 
}


function fruitful_get_fav_icon () {
	$options = fruitful_get_theme_options();
	$upload  = intval($options['fav_icon']);

	echo fruitful_get_box_upload_image($upload, 'fav_icon', 'upload_btn', 'reset_btn', 'favicon');
}




function fruitful_set_logo_position() {
	$options = fruitful_get_theme_options();
	?>
	<div class="box-option">
		<?php fruitful_get_select_fields('logo_position', $options, fruitful_elem_position(), 'logo-position'); ?>		
	</div>	
<?php	
}


function fruitful_get_logo_wh () {
	$options = fruitful_get_theme_options();
	$upload  = intval($options['logo_img']);
	?>
	<h4 class="full-width"><?php _e( 'Width', 'fruitful' ); ?></h4>
	<input type="text" name="fruitful_theme_options[logo_w]"  id="logo-w" class="text-input" value ="<?php echo intval($options['logo_w']); ?>"/>
	<h4 class="full-width"><?php _e( 'Height', 'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[logo_h]" id="logo-h"  class="text-input" value ="<?php echo intval($options['logo_h']); ?>"/>

	<?php }
	
function fruitful_fonts_options () {
	$options = fruitful_get_theme_options();
	/*No Options*/
	?>
	
	<?php
}



function fruitful_fonts_headers () {
	$options = fruitful_get_theme_options();
	?>
    <div class="text_fonts">
		<div id="header_sample_font" class="sample_text"><?php _e('Sample Font', 'fruitful'); ?></div>
		<?php fruitful_get_select_fields('h_font_family',$options, fruitful_fonts_list(), 'select-fonts'); ?>
	</div>

	<?php
}

function fruitful_fonts_menu () {
	$options = fruitful_get_theme_options();
	?>
    <div class="text_fonts">
		<div id="menu_sample_font" class="sample_text"><?php _e('Sample Font', 'fruitful'); ?></div>
		<?php fruitful_get_select_fields('m_font_family',$options, fruitful_fonts_list(), 'select-fonts'); ?>
	</div>
	<?php
}

function fruitful_fonts_content () {
	$options = fruitful_get_theme_options();
	?>
    <div class="text_fonts">
		<div id="content_sample_font" class="sample_text"><?php _e('Sample Font', 'fruitful'); ?></div>
		<?php fruitful_get_select_fields('p_font_family', $options, fruitful_fonts_list(), 'select-fonts'); ?>
	</div>		
	<?php
}

function fruitful_fonts_size () {
	$options = fruitful_get_theme_options();
	?>
    <h4><?php _e( 'H1',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h1_size]" id="h1-size" class="text-input" value ="<?php echo intval($options['h1_size']); ?>"/>
	<h4><?php _e( 'H2',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h2_size]" id="h2-size" class="text-input" value ="<?php echo intval($options['h2_size']); ?>"/>
	<h4><?php _e( 'H3',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h3_size]" id="h4-size" class="text-input" value ="<?php echo intval($options['h3_size']); ?>"/>
	<h4><?php _e( 'H4',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h4_size]" id="h4-size" class="text-input" value ="<?php echo intval($options['h4_size']); ?>"/>
	<h4><?php _e( 'H5',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h5_size]" id="h5-size" class="text-input" value ="<?php echo intval($options['h5_size']); ?>"/>
	<h4><?php _e( 'H6',   'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[h6_size]" id="h6-size" class="text-input" value ="<?php echo intval($options['h6_size']); ?>"/>	
	<h4><?php _e( 'Menu', 'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[m_size]"  id="m-size"  class="text-input" value ="<?php echo intval($options['m_size']); ?>"/>	
	<h4><?php _e( 'P', 	  'fruitful' ); ?></h4><input type="text" name="fruitful_theme_options[p_size]"  id="p-size"  class="text-input" value ="<?php echo intval($options['p_size']); ?>"/>
	<?php
}



function fruitful_slider_select() {
	$options = fruitful_get_theme_options();
	fruitful_get_select_fields('select_slider',$options, fruitful_slide_select(), 'select-slider'); 
 }


function fruitful_slider_options() {
	$options = fruitful_get_theme_options();
	?>
	<input type="button" id="view_all_options" class="button-secondary" value="<?php _e( 'View Options', 'fruitful' ); ?>" /> 
		<div id="slider_main_options" class="slider-main-options">
			<div class="no-slider-select">
				<div class="option_block"><h4><?php _e( 'No Slider Select!', 'fruitful' ); ?></h4></div>
			</div>	
			<div class="flex-slider">
				<div class="option_block"><h4><?php _e( 'Animation type', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_animation',$options, fruitful_slide_anim_list()); ?></div>	
				<div class="option_block"><h4><?php _e( 'Sliding direction, "horizontal" or "vertical"', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_direction',$options, fruitful_slide_direction_list()); ?></div>		
				<div class="option_block"><h4><?php _e( 'Reverse the animation direction', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_reverse',$options, fruitful_bool_list()); ?></div>		
				<div class="option_block"><h4><?php _e( 'Animate slider automatically', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_slideshow',$options, fruitful_bool_list()); ?></div>		
				<div class="option_block"><h4><?php _e( 'Set the speed of the slideshow cycling, in milliseconds', 'fruitful' ); ?></h4><input type="text" id="speed-slideshow" class="text-input" name="fruitful_theme_options[s_slideshowSpeed]" value="<?php echo esc_attr($options['s_slideshowSpeed']); ?>"/></div>		
				<div class="option_block"><h4><?php _e( 'Set the speed of animations, in milliseconds', 'fruitful' ); ?></h4><input type="text" id="speed-animation" class="text-input" name="fruitful_theme_options[s_animationSpeed]" value="<?php echo esc_attr($options['s_animationSpeed']); ?>"/></div>	
				<div class="option_block"><h4><?php _e( 'Set an initialization delay, in milliseconds', 'fruitful' ); ?></h4><input type="text" id="init-delay" class="text-input" name="fruitful_theme_options[s_initDelay]" value="<?php echo esc_attr($options['s_initDelay']); ?>"/></div>	
				<div class="option_block"><h4><?php _e( 'Randomize slide order', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_randomize',$options, fruitful_bool_list()); ?></div>	
				<div class="option_block"><h4><?php _e( 'Manual control usage', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('s_controlnav',$options, fruitful_bool_list()); ?></div>	
			</div>
			<div class="nivo-slider">
				<div class="option_block"><h4><?php _e( 'Slider Skins', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_skins',$options, fruitful_slide_skins_select()); ?></div>	
				<div class="option_block"><h4><?php _e( 'Effect', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_animation',$options, fruitful_flex_effect()); ?></div>	
				<div class="option_block"><h4><?php _e( 'For slice animations', 'fruitful' ); ?></h4><input type="text" id="nv-slice" class="text-input" name="fruitful_theme_options[nv_slice]" value="<?php echo esc_attr($options['nv_slice']); ?>"/></div>						
				<div class="option_block"><h4><?php _e( 'For box animations (Cols)', 'fruitful' ); ?></h4><input type="text" id="nv-boxCols" class="text-input" name="fruitful_theme_options[nv_boxCols]" value="<?php echo esc_attr($options['nv_boxCols']); ?>"/></div>						
				<div class="option_block"><h4><?php _e( 'For box animations (Rows)', 'fruitful' ); ?></h4><input type="text" id="nv-boxRows" class="text-input" name="fruitful_theme_options[nv_boxRows]" value="<?php echo esc_attr($options['nv_boxRows']); ?>"/></div>						
				<div class="option_block"><h4><?php _e( 'Slide transition speed', 'fruitful' ); ?></h4><input type="text" id="nv-animSpeed" class="text-input" name="fruitful_theme_options[nv_animSpeed]" value="<?php echo esc_attr($options['nv_animSpeed']); ?>"/></div>										
				<div class="option_block"><h4><?php _e( 'How long each slide will show', 'fruitful' ); ?></h4><input type="text" id="nv-pauseTime" class="text-input" name="fruitful_theme_options[nv_pauseTime]" value="<?php  echo esc_attr($options['nv_pauseTime']); ?>"/></div>										
				<div class="option_block"><h4><?php _e( 'Set starting Slide (0 index)', 'fruitful' ); ?></h4><input type="text" id="nv-startSlide" class="text-input" name="fruitful_theme_options[nv_startSlide]" value="<?php echo esc_attr($options['nv_startSlide']); ?>"/></div>										
				<div class="option_block"><h4><?php _e( 'Next & Prev navigation', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_directionNav',$options, fruitful_bool_list()); ?></div>										
				<div class="option_block"><h4><?php _e( '1,2,3... navigation', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_controlNav',$options, fruitful_bool_list()); ?></div>														
				<div class="option_block"><h4><?php _e( 'Use thumbnails for Control Nav', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_controlNavThumbs',$options, fruitful_bool_list()); ?></div>														
				<div class="option_block"><h4><?php _e( 'Stop animation while hovering', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_pauseOnHover',$options, fruitful_bool_list()); ?></div>														
				<div class="option_block"><h4><?php _e( 'Force manual transitions', 'fruitful' ); ?></h4><?php fruitful_get_select_fields('nv_manualAdvance',$options, fruitful_bool_list()); ?></div>														
				<div class="option_block"><h4><?php _e( 'Prev directionNav text',  'fruitful'); ?></h4><input type="text" id="nv-prevText" class="text-input" name="fruitful_theme_options[nv_prevText]" value="<?php echo esc_html($options['nv_prevText']); ?>"/></div>																		
				<div class="option_block"><h4><?php _e( 'Next directionNav text',  'fruitful'); ?></h4><input type="text" id="nv-nextText" class="text-input" name="fruitful_theme_options[nv_nextText]" value="<?php echo esc_html($options['nv_nextText']); ?>"/></div>																		
				<div class="option_block"><h4><?php _e( 'Start on a random slide', 'fruitful'); ?></h4><?php fruitful_get_select_fields('nv_randomStart',$options, fruitful_bool_list()); ?></div>																		
			</div>
	</div>
	
	<?php
}

function fruitful_get_slide($ind, $id, $link_url = null, $is_blank = 'off') {
	$out = '';
	$out .= '<li class="slide" id="slide-image-' . $ind . '">';
		$out .= '<h4 class="slide-header" id="slide-header-'. $ind .'">' . sprintf(__('Slide # %1$d', 'fruitful'),   $ind);
			$out .= '<span class="content-close-slide" id="content-slide-close_' . $ind . '"></span>';
				$out .= '<span class="remove-slide" id="remove-slide-'.$ind.'"></span>';
		$out .= '</h4>';
		
		$out .= '<div class="slide-content" id="slide-content-'. $ind .'">';
			$out .= fruitful_get_box_upload_slide($id, $link_url, $is_blank, $ind);
		$out .= '</div>';
	$out .= '</li>';
	return $out;
}	

function fruitful_slider_images() {
	$slides = get_option( 'fruitful_theme_options' );
	$vcount_slides = 0;
	if(!empty($slides['slides'])) {
		$vcount_slides  = count($slides['slides']); 
	}
	?>
		<div class="slides-btn">
			<span class="collapse_all"><?php _e('Collapse all', 'fruitful'); ?></span>
			<span class="expand_all"><?php _e('Expand all', 'fruitful'); ?></span>
		</div>
		<ul class="slides">
			<?php 
					/*Init First Slide for noraml work script*/
					if ($vcount_slides == 0) {
						echo fruitful_get_slide(1, -1); 
					} else {
						foreach ($slides['slides'] as $key=>$slide) {
							$slide_inndex = -1;
							$attach_id 	  = $slide['attach_id'];
							$link_url = null;
							$is_blank = 'off';
							
							$slide_inndex = trim(substr($key, strrpos($key, '-')+1, 5));
							if (isset($slide['link'])) { $link_url = $slide['link']; }
							if (isset($slide['is_blank'])) { $is_blank = $slide['is_blank']; }
							echo fruitful_get_slide($slide_inndex, $attach_id, esc_url($link_url), $is_blank); 
						}
					}
			?>
		</ul>
		<input type="button" class="button-primary add_new_btn" value="<?php _e('Add New Slide', 'fruitful'); ?>" />
<?php
}

function fruitful_menu_style_color () {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Background color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="menu_bg_color" class="colorPicker" name="fruitful_theme_options[menu_bg_color]" value="<?php echo esc_attr($options['menu_bg_color']); ?>" data-default-color="#ffffff" />
			</fieldset>
		</div>
		<div class="box-options">
			<h4><?php _e( 'Menu button color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="menu_btn_color" class="colorPicker" name="fruitful_theme_options[menu_btn_color]" value="<?php echo esc_attr($options['menu_btn_color']); ?>" data-default-color="#F15A23" />
			</fieldset>	
		</div>
		<div class="box-options">
			<h4><?php _e( 'Font color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="menu_font_color" class="colorPicker" name="fruitful_theme_options[menu_font_color]" value="<?php echo esc_attr($options['menu_font_color']); ?>" data-default-color="#333333" />
			</fieldset>		
		</div>
		<div class="box-options">
			<h4><?php _e( 'Font color (active and hover)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="menu_hover_color" class="colorPicker" name="fruitful_theme_options[menu_hover_color]" value="<?php echo esc_attr($options['menu_hover_color']); ?>" data-default-color="#ffffff" />
			</fieldset>	
		</div>
	<?php
}

function fruitful_dropdown_menu_style_color () {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Background color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="dd_menu_bg_color" class="colorPicker" name="fruitful_theme_options[dd_menu_bg_color]" value="<?php echo esc_attr($options['dd_menu_bg_color']); ?>" data-default-color="#ffffff" />
			</fieldset>
		</div>
		<div class="box-options">
			<h4><?php _e( 'Menu button color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="dd_menu_btn_color" class="colorPicker" name="fruitful_theme_options[dd_menu_btn_color]" value="<?php echo esc_attr($options['dd_menu_btn_color']); ?>" data-default-color="#F15A23" />
			</fieldset>	
		</div>
		<div class="box-options">
			<h4><?php _e( 'Font color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="dd_menu_font_color" class="colorPicker" name="fruitful_theme_options[dd_menu_font_color]" value="<?php echo esc_attr($options['dd_menu_font_color']); ?>" data-default-color="#333333" />
			</fieldset>		
		</div>
		<div class="box-options">
			<h4><?php _e( 'Font color (active and hover)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="dd_menu_hover_color" class="colorPicker" name="fruitful_theme_options[dd_menu_hover_color]" value="<?php echo esc_attr($options['dd_menu_hover_color']); ?>" data-default-color="#ffffff" />
			</fieldset>	
		</div>
		<div class="row"></div>
	<?php
}


function fruitful_font_style_color () {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Font color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="p_font_color" class="colorPicker" name="fruitful_theme_options[p_font_color]" value="<?php echo esc_attr($options['p_font_color']); ?>" data-default-color="#333333" />
			</fieldset>
		</div>
		
		<?php /*Link Color*/ ?>
		
		<div class="box-options">
			<h4><?php _e( 'Link color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="a_font_color" class="colorPicker" name="fruitful_theme_options[a_font_color]" value="<?php echo esc_attr($options['a_font_color']); ?>" data-default-color="#333333" />
			</fieldset>	
		</div>
		<div class="box-options">
			<h4><?php _e( 'Link color (hover)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="a_hover_font_color" class="colorPicker" name="fruitful_theme_options[a_hover_font_color]" value="<?php echo esc_attr($options['a_hover_font_color']); ?>" data-default-color="#FF5D2A" />
			</fieldset>		
		</div>
		<div class="box-options">
			<h4><?php _e( 'Link color (focus)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="a_focus_font_color" class="colorPicker" name="fruitful_theme_options[a_focus_font_color]" value="<?php echo esc_attr($options['a_focus_font_color']); ?>" data-default-color="#FF5D2A" />
			</fieldset>		
		</div>
		<div class="box-options">
			<h4><?php _e( 'Link color (active)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="a_active_font_color" class="colorPicker" name="fruitful_theme_options[a_active_font_color]" value="<?php echo esc_attr($options['a_active_font_color']); ?>" data-default-color="#FF5D2A" />
			</fieldset>		
		</div>
	<?php
}


function fruitful_sep_style_color() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Widget separator color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="widgets_sep_color" class="colorPicker" name="fruitful_theme_options[widgets_sep_color]" value="<?php echo esc_attr($options['widgets_sep_color']); ?>" data-default-color="#F15A23" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Blog post date color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="date_of_post_b_color" class="colorPicker" name="fruitful_theme_options[date_of_post_b_color]" value="<?php echo esc_attr($options['date_of_post_b_color']); ?>" data-default-color="#F15A23" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Date font color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="date_of_post_f_color" class="colorPicker" name="fruitful_theme_options[date_of_post_f_color]" value="<?php echo esc_attr($options['date_of_post_f_color']); ?>" data-default-color="#ffffff" />
			</fieldset>
		</div>
		<div class="row"></div>
	<?php 

}	

function fruitful_but_style_color() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Button background color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="btn_color" class="colorPicker" name="fruitful_theme_options[btn_color]" value="<?php echo esc_attr($options['btn_color']); ?>" data-default-color="#333333" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Button background color (hover, active, focus, current page - pagenavi)', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="btn_active_color" class="colorPicker" name="fruitful_theme_options[btn_active_color]" value="<?php echo esc_attr($options['btn_active_color']); ?>" data-default-color="#F15A23" />
			</fieldset>
		</div>
	<?php 

}	

function fruitful_soc_icon_style_color() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Social icons background color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="soc_icon_bg_color" class="colorPicker" name="fruitful_theme_options[soc_icon_bg_color]" value="<?php echo esc_attr($options['soc_icon_bg_color']); ?>" data-default-color="#333333" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Social icons color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="soc_icon_color" class="colorPicker" name="fruitful_theme_options[soc_icon_color]" value="<?php echo esc_attr($options['soc_icon_color']); ?>" data-default-color="#ffffff" />
			</fieldset>
		</div>
	<?php 

}	
	
function fruitful_woo_colors() {
	$options = fruitful_get_theme_options();
	?>
		<div class="box-options">
			<h4><?php _e( 'Sale price color', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="woo_sale_price_color" class="colorPicker" name="fruitful_theme_options[woo_sale_price_color]" value="<?php echo esc_attr($options['woo_sale_price_color']); ?>" data-default-color="#333333" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Rating color (regular) ', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="woo_rating_color_regular" class="colorPicker" name="fruitful_theme_options[woo_rating_color_regular]" value="<?php echo esc_attr($options['woo_rating_color_regular']); ?>" data-default-color="#333333" />
			</fieldset>
		</div>
		
		<div class="box-options">
			<h4><?php _e( 'Rating color (hover, active) ', 'fruitful' ); ?></h4>
			<fieldset>
				<input type="text" id="woo_rating_color_active" class="colorPicker" name="fruitful_theme_options[woo_rating_color_active]" value="<?php echo esc_attr($options['woo_rating_color_active']); ?>" data-default-color="#ff5d2a" />
			</fieldset>
		</div>
		
	<?php 

}	
	
function fruitful_get_background_color () {
	$options = fruitful_get_theme_options();
	?>
		<fieldset>
			<input type="text" id="background_color" class="colorPicker" name="fruitful_theme_options[background_color]" value="<?php echo esc_attr($options['background_color']); ?>"  data-default-color="#ffffff" />
		</fieldset>
	<?php
}

function fruitful_get_container_background_color () {
	$options = fruitful_get_theme_options();
	?>
		<fieldset>
			<input type="text" id="container_bg_color" class="colorPicker" name="fruitful_theme_options[container_bg_color]" value="<?php echo esc_attr($options['container_bg_color']); ?>" data-default-color="#ffffff" />
		</fieldset>	
	<?php
}

/**
 * Renders the Theme Options administration screen.
 *
 * @since Fruitful theme 1.0
 */
function fruitful_theme_options_render_page() {
	?>
	<div class="wrap">
	<h2></h2> 
	<form method="post" action="/" enctype="multipart/form-data"  class="form-admin-fruitful" id="form-admin-fruitful">
		<div id="save_options" class="save-options"></div>	
		<div class="header">
			<h2 class="title_theme"><?php _e( 'Theme Options', 'fruitful' ); ?></h2> 
			<?php submit_button(__( 'Save', 'fruitful' )); ?>
			<?php settings_errors(); ?>
		</div>
			<div class="content">
				<div class="menu-options">
					<ul>
						<li class="current"><a  id="item_0" href="javascript:void(0)" 	title="General "><span class="menu-img" id="menu_img_0"></span><span class="menu-text"><?php _e( 'General', 'fruitful' ); ?></span></a></li>
						<li><a  id="item_1" href="javascript:void(0)" title="<?php _e( 'Header', 	 'fruitful' ); ?>"><span class="menu-img" id="menu_img_1"></span><span class="menu-text"><?php _e( 'Header',	 'fruitful' ); ?></span></a></li>
						<li><a  id="item_2" href="javascript:void(0)" title="<?php _e( 'Background', 'fruitful' ); ?>"><span class="menu-img" id="menu_img_2"></span><span class="menu-text"><?php _e( 'Background', 'fruitful' ); ?></span></a></li>
						<li><a  id="item_3" href="javascript:void(0)" title="<?php _e( 'Logo',		 'fruitful' ); ?>"><span class="menu-img" id="menu_img_3"></span><span class="menu-text"><?php _e( 'Logo',		 'fruitful' ); ?></span></a></li>
						<li><a  id="item_4" href="javascript:void(0)" title="<?php _e( 'Colors',	 'fruitful' ); ?>"><span class="menu-img" id="menu_img_4"></span><span class="menu-text"><?php _e( 'Colors',	 'fruitful' ); ?></span></a></li>
						<li><a  id="item_5" href="javascript:void(0)" title="<?php _e( 'Fonts', 	 'fruitful' ); ?>"><span class="menu-img" id="menu_img_5"></span><span class="menu-text"><?php _e( 'Fonts', 	 'fruitful' ); ?></span></a></li>
						<li><a  id="item_6" href="javascript:void(0)" title="<?php _e( 'Slider', 	 'fruitful' ); ?>"><span class="menu-img" id="menu_img_6"></span><span class="menu-text"><?php _e( 'Slider', 	 'fruitful' ); ?></span></a></li>
						<li><a  id="item_7" href="javascript:void(0)" title="<?php _e( 'Social Links','fruitful'); ?>"><span class="menu-img" id="menu_img_7"></span><span class="menu-text"><?php _e( 'Social Links','fruitful'); ?></span></a></li>
						<li><a  id="item_8" href="javascript:void(0)" title="<?php _e( 'Footer', 	 'fruitful' ); ?>"><span class="menu-img" id="menu_img_8"></span><span class="menu-text"><?php _e( 'Footer', 	 'fruitful' ); ?></span></a></li>
						<li><a  id="item_9" href="javascript:void(0)" title="<?php _e( 'Custom CSS', 'fruitful' ); ?>"><span class="menu-img" id="menu_img_9"></span><span class="menu-text"><?php _e( 'Custom CSS', 'fruitful' ); ?></span></a></li>
						<?php if (class_exists('woocommerce')){ ?>
						<li><a  id="item_10" href="javascript:void(0)" title="<?php _e( 'Woocommerce','fruitful' ); ?>"><span class="menu-img" id="menu_img_10"></span><span class="menu-text"><?php _e( 'Woocommerce','fruitful'); ?></span></a></li>
						<?php } ?>
					</ul>
				</div> 	
				<?php
					settings_fields( 'fruitful_options' );
					fruitful_custom_do_settings_sections( 'theme_options');
				?>
		
		</div>
		<div class="footer">
			<?php submit_button(__( 'Save', 'fruitful' )); ?>
		</div>
		  <input type="hidden" name="action"    value="fruitful_theme_options_action" />
		  <input type="hidden" name="security" value="<?php echo wp_create_nonce('fruitful_theme_data'); ?>" />
		</form>
		
		<div id="sidebar-promo" class="sidebar-promo">
			<div class="sidebar-promo-widget promo-support">
				<h3><?php _e('Support', 'fruitful'); ?></h3>
				<p class="sidebar-promo-content"><?php 
						_e('If You faced with problems or find error or bug, please','fruitful'); 
						echo ' <a target="_blank" href="http://support.fruitfulcode.com/hc/en-us/requests/new">'; _e('submit request.','fruitful'); echo '</a> ';
						_e('On official ','fruitful'); echo ' <a target="_blank" href="http://wordpress.org/support/theme/fruitful">'; _e('Support forum','fruitful'); echo '</a> ';
						_e('You may find answers on Your questions.','fruitful'); 
				?></p>
			</div>
			<div class="sidebar-promo-widget promo-customization">
				<h3><?php _e('Additional customization', 'fruitful'); ?></h3>
				<p class="sidebar-promo-content"><?php 
						_e('Our team is available for any type of WordPress development. ','fruitful'); 
						_e('If You want customize theme or add new features, You can','fruitful'); 
						echo ' <a target="_blank" href="http://fruitfulcode.com/order/">'; _e('submit order','fruitful'); echo '</a> ';
						_e('on our website','fruitful'); 
						echo ' <a target="_blank" href="http://fruitfulcode.com"> fruitfulcode.com </a> ';
				?></p>
			</div>
			<div class="sidebar-promo-widget promo-themes">
				<h3><?php _e('Premium WordpPess themes', 'fruitful'); ?></h3>
				<?php
					$rand_banner = rand(0, 2);
			
					$class ="anaglyph-theme";
					$link = "http://themeforest.net/item/anaglyph-one-page-multi-page-wordpress-theme/7874320?ref=fruitfulcode";
					$title = __('ANAGLYPH - One page / Multi Page WordPress Theme', 'maintenance');
			
					if ($rand_banner == 1) {
						$class ="lovely-theme";
						$link = "http://themeforest.net/item/lovely-simple-elegant-wordpress-theme/8428221?ref=fruitfulcode";
						$title = __('Love.ly - Simple & Elegant WordPress theme', 'maintenance');
					}
			
					if ($rand_banner == 2) {
						$class ="zoner-theme";
						$link = "http://themeforest.net/item/zoner-real-estate-wordpress-theme/9099226?ref=fruitfulcode";
						$title = __('Zoner - Real Estate WordPress theme', 'maintenance');
					}
			
				
				?>
				<div>
					<a class="<?php echo $class; ?>" target="_blank" title="<?php echo esc_attr($title); ?>" href="<?php echo esc_url($link); ?>"></a>
				</div>
			</div>
		</div>
	</div>
	<?php
}


add_action('wp_ajax_fruitful_reset_btn', 'fruitful_reset_action');
function fruitful_reset_action() {
	delete_option('fruitful_theme_options', '');
	die();
}


add_action('wp_ajax_fruitful_add_new_slide_action', 'fruitful_new_slide');
function fruitful_new_slide() {
	$slides = (array) get_option( 'fruitful_theme_slides_options' );
	$data 	 = $_POST['data'];
	echo fruitful_get_slide($data, -1, ''); 
	die();
}


add_action('wp_ajax_run_import_dummy_data', 'fruitful_run_import_dummy_data');
function fruitful_run_import_dummy_data() {
	$vIsUpdate = false;
	$vIsUpdate = fruitful_create_home_page();
	echo $vIsUpdate;
	die();
}

add_action('wp_ajax_fruitful_theme_options_action', 'fruitful_data_save');
function fruitful_data_save() {
	$data = $_POST['fruitful_theme_options'];
	
	if (!isset($data['responsive'])) 	  {$data['responsive'] 		= 'off'; }
	if (!isset($data['postcomment'])) 	  {$data['postcomment'] 	= 'off'; }
	if (!isset($data['pagecomment'])) 	  {$data['pagecomment'] 	= 'off'; }
	if (!isset($data['is_fixed_header'])) {$data['is_fixed_header'] = 'off'; }
	if (!isset($data['styletheme'])) 	  {$data['styletheme'] 		= 'off'; }
	if (!isset($data['showcart'])) 	 	  {$data['showcart'] 		= 'off'; }
	if (!isset($data['is_wpml_ready']))   {$data['is_wpml_ready']	= 'off'; }
	if (!isset($data['bg_repeating'])) 	  {$data['bg_repeating'] 	= 'off'; }
	
	if(!empty($data)) {
	   if(update_option('fruitful_theme_options', $data)) {
	           die('1');
	        } else {
	           die('0');
	        }
	    } else {
	           die('1');  
	    }
}