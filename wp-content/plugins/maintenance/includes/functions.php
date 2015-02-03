<?php
	global $standart_fonts;
	$standart_fonts = array(
        "Arial, Helvetica, sans-serif" 			=> "Arial, Helvetica, sans-serif",
        "Arial Black, Gadget, sans-serif" 		=> "Arial Black, Gadget, sans-serif",
        "Bookman Old Style, serif" 				=> "Bookman Old Style, serif",
        "Comic Sans MS, cursive" 				=> "Comic Sans MS, cursive",
        "Courier, monospace" 					=> "Courier, monospace",
        "Garamond, serif" 						=> "Garamond, serif",
        "Georgia, serif" 						=> "Georgia, serif",
        "Impact, Charcoal, sans-serif" 			=> "Impact, Charcoal, sans-serif",
        "Lucida Console, Monaco, monospace" 	=> "Lucida Console, Monaco, monospace",
        "Lucida Sans Unicode, Lucida Grande, sans-serif" => "Lucida Sans Unicode, Lucida Grande, sans-serif",
        "MS Sans Serif, Geneva, sans-serif" 	=> "MS Sans Serif, Geneva, sans-serif",
        "MS Serif, New York, sans-serif" 		=> "MS Serif, New York, sans-serif",
        "Palatino Linotype, Book Antiqua, Palatino, serif" => "Palatino Linotype, Book Antiqua, Palatino, serif",
        "Tahoma,Geneva, sans-serif" 			=> "Tahoma, Geneva, sans-serif",
        "Times New Roman, Times,serif" 			=> "Times New Roman, Times, serif",
        "Trebuchet MS, Helvetica, sans-serif" 	=> "Trebuchet MS, Helvetica, sans-serif",
        "Verdana, Geneva, sans-serif" 			=> "Verdana, Geneva, sans-serif",
    );
		
	
	function mt_get_plugin_options($is_current = false) {
		$saved	  = (array) get_option('maintenance_options');
		if (!$is_current) {
			$defaults = mt_get_default_array();
			$defaults = apply_filters('mt_plugin_default_options', $defaults );
			$options  = wp_parse_args($saved, $defaults );
			$options  = array_intersect_key( $options, $defaults );
		} else {
			$options  = $saved;
		}
		
		return $options;
	}
	
	function generate_input_filed($title, $id, $name, $value, $placeholder = '') {
		$out_filed = '';
		$out_filed .= '<tr valign="top">';
		$out_filed .= '<th scope="row">' . esc_attr($title) .'</th>';
			$out_filed .= '<td>';
				$out_filed .= '<filedset>';
					$out_filed .= '<input type="text" id="'.esc_attr($id).'" name="lib_options['.$name.']" value="'. wp_kses_post(stripslashes($value)) .'" placeholder="'.$placeholder.'"/>';
				$out_filed .= '</filedset>';
			$out_filed .= '</td>';
		$out_filed .= '</tr>';			
		echo $out_filed;
	}	
	
	
	function generate_textarea_filed($title, $id, $name, $value) {
		$out_filed = '';
		$out_filed .= '<tr valign="top">';
		$out_filed .= '<th scope="row">' . esc_attr($title) .'</th>';
			$out_filed .= '<td>';
				$out_filed .= '<filedset>';
					$out_filed .= '<textarea name="lib_options['.$name.']" id="'.esc_attr($id).'" cols="30" rows="10">'. wp_kses_post(stripslashes($value)) .'</textarea>';
				$out_filed .= '</filedset>';
			$out_filed .= '</td>';
		$out_filed .= '</tr>';			
		echo $out_filed;
	}	
	
	function generate_check_filed($title, $label, $id, $name, $value) {
		$out_filed = '';
		$out_filed .= '<tr valign="top">';
		$out_filed .= '<th scope="row">' . esc_attr($title) .'</th>';
			$out_filed .= '<td>';
				$out_filed .= '<filedset>';
					$out_filed .= '<label for='.esc_attr($id).'>';
						$out_filed .= '<input type="checkbox"  id="'.esc_attr($id).'" name="lib_options['.$name.']" value="1" '. checked( true, $value, false ) .'/>';
						$out_filed .= $label;
					$out_filed .= '</label>';
				$out_filed .= '</filedset>';
			$out_filed .= '</td>';
		$out_filed .= '</tr>';			
		echo $out_filed;		
	}			
	
	function generate_image_filed($title, $id, $name, $value, $class, $name_btn, $class_btn) {
		$out_filed = '';
		
		$out_filed .= '<tr valign="top">';
		$out_filed .= '<th scope="row">' . esc_attr($title) .'</th>';
			$out_filed .= '<td>';
				$out_filed .= '<filedset>';
					$out_filed .= '<input type="hidden" id="'.esc_attr($id).'" name="lib_options['.$name.']" value="'.esc_attr($value).'" />';
					$out_filed .= '<div class="img-container">';
						$url = ''; 
						if($value != '') { 
							$image = wp_get_attachment_image_src( $value, 'full'); 
							$url   = esc_url($image[0]);
						} 
					
						$out_filed .= '<div class="'.esc_attr($class).'" style="background-image:url('.$url.')">';
							if ( $value ) { 
								$out_filed .= '<input class="button button-primary delete-img remove" type="button" value="x" />';
							}	
						$out_filed .= '</div>';
						$out_filed .= '<input type="button" class="'.esc_attr($class_btn).'" value="'.esc_attr($name_btn).'"/>';
						
					$out_filed .= '</div>';
				$out_filed .= '</filedset>';
			$out_filed .= '</td>';
		$out_filed .= '</tr>';
		echo $out_filed;		
	}
	
	function get_color_field($title, $id, $name, $value, $default_color) {
			$out_filed = '';
			$out_filed .= '<tr valign="top">';
					$out_filed .= '<th scope="row">'. esc_attr($title) .'</th>';
					$out_filed .= '<td>';
						$out_filed .= '<filedset>';
							$out_filed .= '<input type="text" id="'.esc_attr($id).'" name="lib_options['.$name.']" data-default-color="'.esc_attr($default_color).'" value="'. wp_kses_post(stripslashes($value)) .'" />';
						$out_filed .= '<filedset>';
					$out_filed .= '</td>';	
				$out_filed .= '</tr>';
			echo $out_filed;
	}		
	
	function mt_get_google_font($font = null) {	
		$font_params = $full_link = $gg_fonts = '';
		
		if (file_exists(MAINTENANCE_INCLUDES .'fonts/googlefonts.json')) {
			$gg_fonts = json_decode(file_get_contents(MAINTENANCE_INCLUDES .'fonts/googlefonts.json'));
		}	
			
		if (property_exists ($gg_fonts, $font)) {
			$curr_font = $gg_fonts->{$font};
			if (!empty($curr_font)) {
				$name_font = str_replace(' ','+',$font);
				foreach ($curr_font->variants as $values) {
					$font_params .= $values->id . ',';
				}
			
				$font_params = trim($font_params, ",");
				$full_link = $name_font . ':' . $font_params;
				$full_link = 'http'. ( is_ssl() ? 's' : '' ) .'://fonts.googleapis.com/css?family=' . $full_link;
			}	
		}	
		
		return $full_link;
	}

    function get_fonts_field($title, $id, $name, $value) {
			global $standart_fonts;
			$out_items = $gg_fonts = '';
			
			if (file_exists(MAINTENANCE_INCLUDES .'fonts/googlefonts.json')) {
				$gg_fonts = json_decode(file_get_contents(MAINTENANCE_INCLUDES .'fonts/googlefonts.json'));
			}	
			
			$out_filed = '';
			$out_filed .= '<tr valign="top">';
					$out_filed .= '<th scope="row">'. esc_attr($title) .'</th>';
					$out_filed .= '<td>';
						$out_filed .= '<filedset>';
						if(!empty($standart_fonts)) {
								$out_items .= '<optgroup label="' . __('Standard Fonts', 'anaglyph-framework') . '">';
								foreach ($standart_fonts as $key => $options) {
									$out_items .= '<option value="'.$key.'" '. selected( $value, $key, false ) .'>'.$options.'</option>';
								}
						}	
						
						if (!empty($gg_fonts)) {
								$out_items .= '<optgroup label="' . __('Google Web Fonts', 'anaglyph-framework') . '">';
							foreach ($gg_fonts as $key => $options) {
								$out_items .= '<option value="'.$key .'" '. selected( $value, $key, false ) .'>'.$key.'</option>';
							}
						}
						
						if (!empty($out_items)) {
							$out_filed .= '<select class="select2_customize" name="lib_options['.$name.']" id="'.esc_attr($id).'">';
								$out_filed .= $out_items;
							$out_filed .= '</select>';
						}	
						$out_filed .= '<filedset>';
					$out_filed .= '</td>';	
				$out_filed .= '</tr>';
			return $out_filed;
	}		
	
	function maintenance_page_create_meta_boxes() {
		global $maintenance_variable;
		add_meta_box( 'maintenance-general', __( 'General Settings', 'maintenance' ),  'add_data_fields', $maintenance_variable->options_page, 'normal', 'default');
		add_meta_box( 'maintenance-css', 	 __( 'Custom CSS', 'maintenance' ),        'add_css_fields', $maintenance_variable->options_page, 'normal', 'default');
		add_meta_box( 'maintenance-excludepages', 	 __( 'Exclude pages', 'maintenance' ), 'add_exclude_pages_fields', $maintenance_variable->options_page, 'normal', 'default');
	}
	add_action('add_meta_boxes', 'maintenance_page_create_meta_boxes', 10);
	
	function maintenance_page_create_meta_boxes_widget_pro() {
		global $maintenance_variable;
		add_meta_box( 'promo-extended',   	 __( 'Pro version', 'maintenance' ),  'maintenanace_extended_version',  $maintenance_variable->options_page, 'side',   'default' );
	}
	add_action('add_meta_boxes', 'maintenance_page_create_meta_boxes_widget_pro', 11);
	

	function maintenance_page_create_meta_boxes_our_themes() {
		global $maintenance_variable;
		add_meta_box( 'promo-our-themes',   	 __( 'Fruitful Code projects',  'maintenance' ),  'maintenanace_our_themes',   $maintenance_variable->options_page, 'side',   'default' );
	}	
	add_action('add_meta_boxes', 'maintenance_page_create_meta_boxes_our_themes', 12);	
	
	function maintenance_page_create_meta_boxes_widget_support() {
		global $maintenance_variable;
		add_meta_box( 'promo-content',   	 __( 'Support',  'maintenance' ),  'maintenanace_contact_support',   $maintenance_variable->options_page, 'side',   'default' );
	}	
	add_action('add_meta_boxes', 'maintenance_page_create_meta_boxes_widget_support', 13);	
	
	function add_data_fields ($object, $box) {
		$mt_option = mt_get_plugin_options(true);
		$is_blur   = false; 
		
		/*Deafult Variable*/
		$page_title = $heading = $description = '';
		
		
		if (isset($mt_option['page_title'])) $page_title = wp_kses_post($mt_option['page_title']);
		if (isset($mt_option['heading']))  $heading = wp_kses_post($mt_option['heading']);
		if (isset($mt_option['description'])) $description = wp_kses_post($mt_option['description']);
		if (isset($mt_option['footer_text'])) $footer_text = wp_kses_post($mt_option['footer_text']);
		
		?>
		<table class="form-table">
			<tbody>
		<?php	
				generate_input_filed(__('Page title', 'maintenance'), 'page_title', 'page_title', $page_title);
				generate_input_filed(__('Headline', 'maintenance'),	'heading', 'heading', $heading);
				generate_textarea_filed(__('Description', 'maintenance'), 'description', 'description', $description);
				generate_input_filed(__('Footer Text', 'maintenance'),	'footer_text', 'footer_text', 	$footer_text);
				generate_image_filed(__('Logo', 'maintenance'), 'logo', 'logo', intval($mt_option['logo']), 'boxes box-logo', __('Upload Logo', 'maintenance'), 'upload_logo upload_btn button');
				do_action('maintenance_background_field');
				do_action('maintenance_color_fields');
				do_action('maintenance_font_fields');
				generate_check_filed(__('Admin bar', 'maintenance'), __('Show admin bar', 'maintenance'), 'admin_bar_enabled', 'admin_bar_enabled', isset($mt_option['admin_bar_enabled']));
				generate_check_filed(__('503', 'maintenance'), __('Service temporarily unavailable', 'maintenance'), '503_enabled', '503_enabled',  isset($mt_option['503_enabled']));
				
				$gg_analytics_id = '';
				if (!empty($mt_option['gg_analytics_id'])) {
					$gg_analytics_id = esc_attr($mt_option['gg_analytics_id']);
				}
				
				generate_input_filed(__('Google Analytics ID',  'maintenance'), 'gg_analytics_id', 'gg_analytics_id', $gg_analytics_id,  __('UA-XXXXX-X', 'maintenance'));
				generate_input_filed(__('Blur intensity',  'maintenance'), 'blur_intensity', 'blur_intensity', intval($mt_option['blur_intensity']));

				if (isset($mt_option['is_blur'])) {
					if ($mt_option['is_blur']) $is_blur = true; 
				} 
				
				generate_check_filed(__('Background blur', 'maintenance'), __('Apply a blur', 'maintenance'), 'is_blur', 'is_blur', $is_blur);
				generate_check_filed(__('Login On / Off', 'maintenance'),  '', 'is_login', 'is_login', isset($mt_option['is_login']));
		?>		
			</tbody>
		</table>
		<?php
	}	
	
	
	function add_css_fields() {
		$mt_option = mt_get_plugin_options(true);
		echo '<table class="form-table">';
			echo '<tbody>';
				generate_textarea_filed(__('CSS Code', 'maintenance'), 'custom_css', 'custom_css', wp_kses_stripslashes($mt_option['custom_css']));
			echo '</tbody>';
		echo '</table>';	
	}
	
	function add_exclude_pages_fields() {
		$mt_option = mt_get_plugin_options(true);
		$out_filed = '';
		
		$post_types = get_post_types(array('show_ui' => true, 'public' => true), 'objects' );
		
		$out_filed .= '<table class="form-table">';
			$out_filed .= '<tbody>';
			$out_filed .= '<tr valign="top">';	
				$out_filed .= '<th colspan="2" scope="row">' . __('Select the page to be displayed:', 'maintenance') .'</th>';
			$out_filed .= '</tr>';
						
			foreach ($post_types as $post_slug => $type) {
					
					if (($post_slug == 'attachment') || 
						($post_slug == 'revision') || 
						($post_slug == 'nav_menu_item')
						) continue;
					
					$out_filed .= '<tr valign="top">';	
						$out_filed .= '<th scope="row">' . $type->labels->name .'</th>';
						
						$out_filed .= '<filedset>';	
						$out_filed .= '<td>';	
						
						$args = array();
						$args = array(
									'posts_per_page'   => -1,
									'orderby'          => 'NAME',
									'order'            => 'ASC',
									'post_type'        => $post_slug,
									'post_status'      => 'publish'); 
	
						$posts_array = get_posts( $args );
						$db_pages_ex = array();
						
						/*Exclude pages from maintenance mode*/
						if (!empty($mt_option['exclude_pages']) && isset($mt_option['exclude_pages'][$post_slug])) { $db_pages_ex = $mt_option['exclude_pages'][$post_slug]; }
						
						if (!empty($posts_array)) {
							$out_filed .= '<select id="exclude-pages" name="lib_options[exclude_pages]['.$post_slug.'][]" style="width:100%;" class="exclude-pages multiple-select-mt" multiple="multiple">';
							
							foreach ($posts_array as $post_values) {
								$current = '';
								if (!empty($db_pages_ex)) {
									if (in_array($post_values->ID, $db_pages_ex)) {
										$current = $post_values->ID;
									}
								}	
								$selected = selected($current, $post_values->ID, false);
								$out_filed .= '<option value="'.$post_values->ID.'" '.$selected .'>'.$post_values->post_title.'</option>';
							}
							
							$out_filed .= '</select>';	
						} else {
							$out_filed .= '<h3>'.__('Not available object.', 'maintenance').'</h3>';
						}
					$out_filed .= '</filedset>';	
				$out_filed .= '</td>';	
			$out_filed .= '</tr>';						
		}
		
			$out_filed .= '</tbody>';
		$out_filed .= '</table>';	
		
		echo $out_filed;
	}
	
	function get_background_fileds_action() {
		$mt_option = mt_get_plugin_options(true);
		generate_image_filed(__('Background image', 'maintenance'), 'body_bg', 'body_bg', esc_attr($mt_option['body_bg']), 'boxes box-bg', __('Upload Background', 'maintenance'), 'upload_background upload_btn button');
	}
	add_action ('maintenance_background_field', 'get_background_fileds_action', 10);
	
	function get_color_fileds_action() {
		$mt_option = mt_get_plugin_options(true);
		get_color_field(__('Background color', 'maintenance'), 'body_bg_color', 'body_bg_color', esc_attr($mt_option['body_bg_color']), '#1111111');
		get_color_field(__('Font color', 'maintenance'), 'font_color', 'font_color', esc_attr($mt_option['font_color']), 	  '#ffffff');
	}	
	add_action ('maintenance_color_fields', 'get_color_fileds_action', 10);
	
	
	function get_font_fileds_action() {
		$mt_option = mt_get_plugin_options(true);
		echo get_fonts_field(__('Font family', 'maintenance'), 'body_font_family', 'body_font_family', esc_attr($mt_option['body_font_family'])); 	
	}	
	add_action ('maintenance_font_fields', 'get_font_fileds_action', 10);
	
	
	function maintenanace_contact_support() {
		$promo_text  = '';
		$promo_text .= '<div class="sidebar-promo" id="sidebar-promo">';
			$promo_text .= '<h4 class="support">'. __('Have any questions?','maintenance'). '</h3>';
			$promo_text .= '<p>'. sprintf(__('You may find answers to your questions at <a target="_blank" href="http://wordpress.org/support/plugin/maintenance">support forum</a><br>You may  <a target="_blank" href="mailto:mail@fruitfulcode.com?subject=Maintenance plugin">contact us</a> with customization requests and suggestions.<br> Please visit our website to learn about us and our services <a href="%1$s" title="%2$s">%2$s</a>', 'maintenance'), 
											 'http://fruitfulcode.com',
											 'fruitfulcode.com'
										 ).'</p>';
		$promo_text .= '</div>';		
		echo $promo_text;
	}

	function maintenanace_our_themes() {
		$promo_text  = '';
		$promo_text .= '<div class="sidebar-promo" id="sidebar-themes">';
			$promo_text .= '<h4 class="themes">'. __('Premium WordPress themes','maintenance'). '</h3>';
			
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
			
			
			$promo_text .= '<p>'. sprintf ('<a target="_blank" class="%1s" href="%2s" title="%3s"></a>', $class, $link, $title ) . '</p>';
			
		$promo_text .= '</div>';		
		echo $promo_text;
	}
	
	function maintenanace_extended_version() {
		$promo_text  = '';
		$promo_text .= '<div class="sidebar-promo worker" id="sidebar-promo">';
			$promo_text .= '<h4 class="star">'. __('Extended functionality','maintenance') .'</h3>';
			$promo_text .= '<p>' . sprintf(__('Purchase <a href="http://codecanyon.net/item/maintenance-wordpress-plugin/2781350?ref=fruitfulcode" target="_blank">PRO</a> version  with extended functionality. %1$s If you like our plugin please <a target="_blank" href="http://wordpress.org/support/view/plugin-reviews/maintenance?filter=5">rate it</a>, <a title="leave feedbacks" href="%2$s" target="_blank">leave feedbacks</a>.', 'maintenance'), 
										   '<br />',
										   'http://wordpress.org/support/view/plugin-reviews/maintenance') .'</p>';
			$promo_text .= sprintf('<a class="button button-primary" title="%1$s" href="%2$s" target="_blank">%1$s</a>', 
							__('Demo website', 'maintenance'),
							'http://plugins.fruitfulcode.com/maintenance/'
							);
		$promo_text .= '</div>';	
		echo $promo_text;
	}
	
	function mt_curPageURL() {
		$pageURL = 'http';
		if (isset($_SERVER["HTTPS"])) {$pageURL .= "s";}
			$pageURL .= "://";
		if ($_SERVER["SERVER_PORT"] != "80") {
			$pageURL .= $_SERVER["SERVER_NAME"].":".$_SERVER["SERVER_PORT"].$_SERVER["REQUEST_URI"];
		} else {
			$pageURL .= $_SERVER["SERVER_NAME"].$_SERVER["REQUEST_URI"];
		}
		return $pageURL;
	}
	
	function mtCheckExclude() {
		global $mt_options;
		$mt_options = mt_get_plugin_options(true);
		$is_skip = false;
		$curUrl = mt_curPageURL();
		
		if (isset($mt_options['exclude_pages']) && !empty($mt_options['exclude_pages'])) {
			$exlude_objs = $mt_options['exclude_pages'];
			
			foreach ($exlude_objs as $objs_id) {
				foreach ($objs_id as $obj_id) {
					if ( $curUrl == get_the_permalink($obj_id)) {
						 $is_skip = true;
						 break;
					}
				}	
			}
		}
		
        return $is_skip;
	}
	
	
	function load_maintenance_page() {
		global $mt_options;
		
		$vCurrDate_start = $vCurrDate_end = $vCurrTime = '';
		
		$vdate_start = $vdate_end = date_i18n( 'Y-m-d', strtotime( current_time('mysql', 0) )); 
		$vtime_start = date_i18n( 'h:i a', strtotime( '12:00 am')); 
		$vtime_end 	 = date_i18n( 'h:i a', strtotime( '12:00 pm')); 
					
		$mt_options	= mt_get_plugin_options(true);
			if (!is_user_logged_in()) {
				if ($mt_options['state']) {
					
					if (!empty($mt_options['expiry_date_start']))
						$vdate_start = $mt_options['expiry_date_start'];
					if (!empty($mt_options['expiry_date_end']))
						$vdate_end = $mt_options['expiry_date_end'];
						
					if (!empty($mt_options['expiry_time_start']))
						$vtime_start = $mt_options['expiry_time_start'];
					if (!empty($mt_options['expiry_time_end']))
						$vtime_end = $mt_options['expiry_time_end'];
					 
						$vCurrTime 		 = strtotime(current_time('mysql', 0));
						$vCurrDate_start = strtotime($vdate_start . ' ' . $vtime_start); 
						$vCurrDate_end 	 = strtotime($vdate_end . ' ' . $vtime_end); 
						
						if (mtCheckExclude()) return true;
						
						if ($vCurrTime < $vCurrDate_start) return true;
						if ($vCurrTime >= $vCurrDate_end) {
							if (!empty($mt_options['is_down'])) return true;
						}
				} else {
					return true;		
				}				
				
				if ( file_exists (MAINTENANCE_LOAD . 'index.php')) {
				  	 include_once MAINTENANCE_LOAD . 'index.php';
					 exit;
				}	
				
				
			}
	}
	
	
	function maintenance_metaboxes_scripts() {
		global $maintenance_variable; 
	?>
		<script type="text/javascript">
		//<![CDATA[
		jQuery(document).ready( function() {
			jQuery('.if-js-closed').removeClass('if-js-closed').addClass('closed');
			postboxes.add_postbox_toggles( '<?php echo esc_js($maintenance_variable->options_page); ?>' );
		});
		//]]>
		</script>
	<?php }	
	
	function maintenance_add_toolbar_items(){
		global $wp_admin_bar, $wpdb;
			   $mt_options	= mt_get_plugin_options(true);
			   $check = '';
		if ( !is_super_admin() || !is_admin_bar_showing() ) return;		
		$url_to = admin_url( 'admin.php?page=maintenance');
		
		if ($mt_options['state']) { 
			$check = 'On';
		} else {
			$check = 'Off';
		}
		$wp_admin_bar->add_menu( array( 'id' => 'maintenance_options', 'title' => __( 'Maintenance', 'maintenance' ) . ' is ' . $check, 'href' => $url_to, 'meta'  => array( 'title' => __( 'Maintenance', 'maintenance' ) . ' is ' . $check)));	
	} 
	
	
	function maintenance_hex2rgb($hex) {
		$hex = str_replace("#", "", $hex);

		if(strlen($hex) == 3) {
			$r = hexdec(substr($hex,0,1).substr($hex,0,1));
			$g = hexdec(substr($hex,1,1).substr($hex,1,1));
			$b = hexdec(substr($hex,2,1).substr($hex,2,1));
		} else {
			$r = hexdec(substr($hex,0,2));
			$g = hexdec(substr($hex,2,2));
			$b = hexdec(substr($hex,4,2));
		}
		$rgb = array($r, $g, $b);
		return implode(",", $rgb); 
	}
		
		
	function mt_insert_attach_sample_files() {
		global $wpdb;
		$title = '';
		$attach_id   = 0;
		$is_attach_exists = $wpdb->get_results( "SELECT p.ID FROM $wpdb->posts p WHERE  p.post_title LIKE '%mt-sample-background%'", OBJECT );
		if (!empty($is_attach_exists)) {
			$attach_id = current($is_attach_exists)->ID;
		} else {
			require_once(ABSPATH . 'wp-admin/includes/image.php');
			$upload_dir  = wp_upload_dir();
			$image_url 	 = MAINTENANCE_URI . 'images/mt-sample-background.jpg';
			$file_name   = basename( $image_url );
			$upload      = wp_upload_bits( $file_name, null, file_get_contents($image_url), current_time( 'mysql', 0));
				
			if ($upload['error'] == '') {
				$title = preg_replace('/\.[^.]+$/', '', basename($image_url));
						
				$wp_filetype = wp_check_filetype(basename($upload['file']), null );
				$attachment = array(
						'guid' 			 => $upload['url'], 
						'post_mime_type' => $wp_filetype['type'],
						'post_title' 	 => $title,
						'post_content' 	 => '',
						'post_status' 	 => 'inherit'
					);
					
				$attach_id   = wp_insert_attachment($attachment, $upload['file']);
				$attach_data = wp_generate_attachment_metadata($attach_id, $upload['file']);
				wp_update_attachment_metadata($attach_id, $attach_data);
				
			}
		}	

		if (!empty($attach_id)) {
			return $attach_id;
		} else {
			return '';
		}
	}
	
	
	function mt_get_default_array() {
		return array(
			'state'		  		=> true,
			'page_title'  		=> __('Website is under construction', 'maintenance'),
			'heading'	  		=> __('Maintenance mode is on', 'maintenance'),	
			'description' 		=> __('Website will be available soon', 'maintenance'),
			'footer_text'		=> '&copy; ' . get_bloginfo( 'name' ) . ' ' . date('Y'),
			'logo'		  		=> '',
			'body_bg'	  		=> mt_insert_attach_sample_files(),
			'body_bg_color'    	=> '#111111',
			'font_color' 		=> '#ffffff',
			'body_font_family' 	=> 'Open Sans',
			'is_blur'			=> false,
			'blur_intensity'	=> 5,	
			'admin_bar_enabled' => true,
			'503_enabled'		=> true,
			'gg_analytics_id'   => '',
			'is_login'			=> true,
			'custom_css'		=> '',
			'exclude_pages'		=> ''
		);
	}