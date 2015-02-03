<?php

function fruitful_get_header_img_sizes() {
	$num_row = array(
		'0' => array(
			'value' =>	   'full',
			'label' => __( 'Full width position', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'centered',
			'label' => __( 'Centered position', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_get_header_img_sizes', $num_row );
}

function fruitful_slide_skins_select() {
	$slide_skins = array(
		'0' => array(
			'value' =>	   'theme-default',
			'label' => __( 'default', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'theme-dark',
			'label' => __( 'dark', 'fruitful' )
		),
		'2' => array(
			'value' =>	   'theme-bar',
			'label' => __( 'bar', 'fruitful' )
		),
		'4' => array(
			'value' =>	   'theme-light',
			'label' => __( 'light', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_slide_skins_select', $slide_skins );
}

function fruitful_slide_select() {
	$slide_anim_options = array(
		'0' => array(
			'value' =>	   '1',
			'label' => __( 'FlexSlider', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '2',
			'label' => __( 'Nivo Slider', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_slide_select', $slide_anim_options );
}


function fruitful_elem_position() {
	$elem_pos = array(
		'0' => array(
			'value' =>	   '0',
			'label' => __( 'Left', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '1',
			'label' => __( 'Center', 'fruitful' )
		),
		'2' => array(
			'value' =>	   '2',
			'label' => __( 'Right', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_elem_position', $elem_pos );
}

function fruitful_custom_layouts() {
	$latest_posts_pos = array(
		'0' => array(
			'value' =>	   '0',
			'label' => __( 'Full width', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '1',
			'label' => __( 'Right sidebar', 'fruitful' )
		),
		'2' => array(
			'value' =>	   '2',
			'label' => __( 'Left sidebar', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_custom_layouts', $latest_posts_pos );
}

function fruitful_woo_shop_sidebar_list() {
	$num_row = array(
		'0' => array(
			'value' =>	   '1',
			'label' => __( 'Full width', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '2',
			'label' => __( 'Left sidebar', 'fruitful' )
		),
		'2' => array(
			'value' =>	   '3',
			'label' => __( 'Right sidebar', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_woo_shop_sidebar_list', $num_row );
}

function fruitful_number_per_row() {
	$num_row = array(
		'0' => array(
			'value' =>	   '2',
			'label' => __( '2 products', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '3',
			'label' => __( '3 products', 'fruitful' )
		),
		'2' => array(
			'value' =>	   '4',
			'label' => __( '4 products', 'fruitful' )
		),
		'3' => array(
			'value' =>	   '5',
			'label' => __( '5 products', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_number_per_row', $num_row );
}

function fruitful_flex_effect() {
	$flex_effects = array(
		'0' => array(
			'value' =>	   'random',
			'label' => __( 'random', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'sliceDownRight',
			'label' => __( 'sliceDownRight', 'fruitful' )
		),
		'2' => array(
			'value' =>	   'sliceDownLeft',
			'label' => __( 'sliceDownLeft', 'fruitful' )
		),
		'3' => array(
			'value' =>	   'sliceUpRight',
			'label' => __( 'sliceUpRight', 'fruitful' )
		),
		'4' => array(
			'value' =>	   'sliceUpDown',
			'label' => __( 'sliceUpDown', 'fruitful' )
		),
		'5' => array(
			'value' =>	   'sliceUpDownLeft',
			'label' => __( 'sliceUpDownLeft', 'fruitful' )
		),
		'6' => array(
			'value' =>	   'fold',
			'label' => __( 'fold', 'fruitful' )
		),
		'7' => array(
			'value' =>	   'fade',
			'label' => __( 'fade', 'fruitful' )
		),
		'8' => array(
			'value' =>	   'boxRandom',
			'label' => __( 'boxRandom', 'fruitful' )
		),
		'9' => array(
			'value' =>	   'boxRain',
			'label' => __( 'boxRain', 'fruitful' )
		),
		'10' => array(
			'value' =>	   'boxRainReverse',
			'label' => __( 'boxRainReverse', 'fruitful' )
		),
		'11' => array(
			'value' =>	   'boxRainGrow',
			'label' => __( 'boxRainGrow', 'fruitful' )
		),
		'12' => array(
			'value' =>	   'boxRainGrowReverse',
			'label' => __( 'boxRainGrowReverse', 'fruitful' )
		)
		
	);

	return apply_filters( 'fruitful_flex_effect', $flex_effects );
}

function fruitful_slide_anim_list() {
	$slide_anim_options = array(
		'0' => array(
			'value' =>	   'fade',
			'label' => __( 'fade', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'slide',
			'label' => __( 'slide', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_slide_anim_list', $slide_anim_options );
}

function fruitful_slide_direction_list() {
	$slide_direct_options = array(
		'0' => array(
			'value' =>	   'horizontal',
			'label' => __( 'horizontal', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'vertical',
			'label' => __( 'vertical', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_slide_direction_list', $slide_direct_options );
}

function fruitful_bool_list() {
	$font_bool_options = array(
		'0' => array(
			'value' =>	   'true',
			'label' => __( 'true', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'false',
			'label' => __( 'false', 'fruitful' )
		)
	);
	return apply_filters( 'fruitful_bool_list', $font_bool_options);
}

function fruitful_get_menu_type_select(){
	$menu_type_responsive = array(
		'0' => array(
			'value' =>	   'full_width',
			'label' => __( 'Button menu', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'inside_content',
			'label' => __( 'Select menu', 'fruitful' )
		)
	);
	return apply_filters( 'fruitful_get_menu_type_select', $menu_type_responsive);
}

function fruitful_fonts_list() {
	$font_family_options = array(
		'0' => array(
			'value' =>	   'Arial, Helvetica, sans-serif',
			'label' => __( 'Arial, Helvetica, sans-serif', 'fruitful' )
		),
		'1' => array(
			'value' =>	   'Arial Black, Gadget, sans-serif',
			'label' => __( 'Arial Black, Gadget, sans-serif', 'fruitful' )
		),
		'2' => array(
			'value' => 	   	'Comic Sans MS, Textile, cursive',
			'label' => __( 	'Comic Sans MS, Textile, cursive', 'fruitful' )
		),
		'3' => array(
			'value' => 	   'Courier New, Courier, monospace',
			'label' => __( 'Courier New, Courier, monospace', 'fruitful' )
		),
		'4' => array(
			'value' => 	   'Georgia, Times New Roman, Times, serif',
			'label' => __( 'Georgia, Times New Roman, Times, serif', 'fruitful' )
		),
		'5' => array(
			'value' => 	   'Impact, Charcoal, sans-serif',
			'label' => __( 'Impact, Charcoal, sans-serif', 'fruitful' )
		),
		'6' => array(
			'value' => 	   'Lucida Console, Monaco, monospace',
			'label' => __( 'Lucida Console, Monaco, monospace', 'fruitful' )
		),
		'7' => array(
			'value' => 	   'Lucida Sans Unicode, Lucida Grande, sans-serif',
			'label' => __( 'Lucida Sans Unicode, Lucida Grande, sans-serif', 'fruitful' )
		),
		'8' => array(
			'value' => 	   'Palatino Linotype, Book Antiqua, Palatino, serif',
			'label' => __( 'Palatino Linotype, Book Antiqua, Palatino, serif', 'fruitful' )
		),
		'9' => array(
			'value' => 	   'Tahoma, Geneva, sans-serif',
			'label' => __( 'Tahoma, Geneva, sans-serif', 'fruitful' )
		),
		'10' => array(
			'value' => 	   'Times New Roman, Times, serif',
			'label' => __( 'Times New Roman, Times, serif', 'fruitful' )
		),
		'11' => array(
			'value' => 	   'Trebuchet MS, Helvetica, sans-serif',
			'label' => __( 'Trebuchet MS, Helvetica, sans-serif', 'fruitful' )
		),
		'12' => array(
			'value' => 	   'Verdana, Geneva, sans-serif',
			'label' => __( 'Verdana, Geneva, sans-serif', 'fruitful' )
		),
		'13' => array(
			'value' => 	   'MS Sans Serif, Geneva, sans-serif',
			'label' => __( 'MS Sans Serif, Geneva, sans-serif', 'fruitful' )
		),
		'14' => array(
			'value' => 	   'MS Serif, New York, serif',
			'label' => __( 'MS Serif, New York, serif', 'fruitful' )
		),
		
		/*Google fonts*/	
		'15' => array(
			'value' => 	   'Open Sans, sans-serif',
			'label' => __( 'Open Sans, sans-serif', 'fruitful' )
		),
		'16' => array(
			'value' => 	   'Lobster, cursive',
			'label' => __( 'Lobster, cursive', 'fruitful' )
		),
		'17' => array(
			'value' => 	   'Josefin Slab, serif',
			'label' => __( 'Josefin Slab, serif', 'fruitful' )
		),
		'18' => array(
			'value' => 	   'Arvo, serif',
			'label' => __( 'Arvo, serif', 'fruitful' )
		),
		'19' => array(
			'value' => 	   'Lato, sans-serif',
			'label' => __( 'Lato, sans-serif', 'fruitful' )
		),
		'20' => array(
			'value' => 	   'Vollkorn, serif',
			'label' => __( 'Vollkorn, serif', 'fruitful' )
		),
		'21' => array(
			'value' => 	   'Abril Fatface, cursive',
			'label' => __( 'Abril Fatface, cursive', 'fruitful' )
		),
		'22' => array(
			'value' => 	   'Ubuntu, sans-serif',
			'label' => __( 'Ubuntu, sans-serif', 'fruitful' )
		),
		'23' => array(
			'value' => 	   'PT Sans, sans-serif',
			'label' => __( 'PT Sans, sans-serif', 'fruitful' )
		),
		'24' => array(
			'value' => 	   'Old Standard TT, serif',
			'label' => __( 'Old Standard TT, serif', 'fruitful' )
		),
		'25' => array(
			'value' => 	   'Droid Sans, sans-serif',
			'label' => __( 'Droid Sans, sans-serif', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_fonts_list', $font_family_options );
}

function fruitful_social_links_positions_list() {
	$links_position_options = array(
		'0' => array(
			'value' =>	   '1',
			'label' => __( 'Header', 'fruitful' )
		),
		'1' => array(
			'value' =>	   '0',
			'label' => __( 'Footer', 'fruitful' )
		)
	);

	return apply_filters( 'fruitful_social_links_positions_list', $links_position_options );
}

 function fruitful_custom_do_settings_sections($page) {
    global $wp_settings_sections, $wp_settings_fields;
	$id_=0;
	$optins = (array) get_option( 'fruitful_theme_options' );
    if ( !isset($wp_settings_sections) || !isset($wp_settings_sections[$page]) )
        return;
    foreach( (array) $wp_settings_sections[$page] as $section ) {

		if ($section['title'] != "") {
		   print "<h3>{$section['title']}</h3>";
		}
        call_user_func($section['callback'], $section);
        if ( !isset($wp_settings_fields) ||
             !isset($wp_settings_fields[$page]) ||
             !isset($wp_settings_fields[$page][$section['id']]) )
                continue;
        	 
			$name_id = "settings-section-" . $id_;
			 print '<div id="'. $name_id .'" class="settings-section">';
					fruitful_custom_do_settings_fields($page, $section['id']);
			 print '</div>';
		$id_++;		 
    }
}


function fruitful_custom_do_settings_fields($page, $section) {
    global $wp_settings_fields;
	$id_=0;

    if ( !isset($wp_settings_fields) ||
         !isset($wp_settings_fields[$page]) ||
         !isset($wp_settings_fields[$page][$section]) )
        return;
		
    foreach ( (array) $wp_settings_fields[$page][$section] as $field ) {
        if (!empty($field['args']['newrow'])) {
			print '<div id="set_form_row_' . $id_ .'" class="settings-form-row newrow">';
		} else {
			print '<div id="set_form_row_' . $id_ .'" class="settings-form-row">';
		}
		
        if ( !empty($field['args']['label_for']) )
            print '<h3 class="main-header-options">' . esc_attr($field['title']);
        else
            print '<h3 class="main-header-options">' . esc_attr($field['title']);
			print '</h3>';
				print '<span class="add_element_info">'. $field['args']['info'] .'</span>';
				print '<div class="box-options">';
				call_user_func($field['callback'], $field['args']);
		print '</div></div>';
			$id_++;		 
    }
}

function fruitful_add_admin_style() {
	if(is_rtl()){
		wp_enqueue_style('admin-style', 		get_template_directory_uri() . '/inc/css/admin-rtl.css');
	} else {
		wp_enqueue_style('admin-style', 		get_template_directory_uri() . '/inc/css/admin.css');
	}
	wp_enqueue_style('fonts-style', 		get_template_directory_uri() . '/inc/css/fonts-style.css');
	wp_enqueue_style('ch-style',			get_template_directory_uri() . '/inc/js/ch/ch.css');
	wp_enqueue_style('sl-style',			get_template_directory_uri() . '/inc/js/sl/jquery.formstyler.css');
	wp_enqueue_style('dialog', 				get_template_directory_uri() . '/inc/js/dialogBox/jquery-impromptu.css');
	wp_enqueue_style( 'wp-color-picker' );
}

function fruitful_add_jquery_script() {
	wp_enqueue_script('wp-color-picker');
	
	if( function_exists( 'wp_enqueue_media' ) ){
		wp_enqueue_media();
	} else {
		wp_enqueue_style ('thickbox');
		wp_enqueue_script('media-upload');
		wp_enqueue_script('thickbox');
	}
	
	wp_enqueue_script('chJq',				get_template_directory_uri() . "/inc/js/ch/ch.js", array('jquery'));
	wp_enqueue_script('slJq',				get_template_directory_uri() . "/inc/js/sl/jquery.formstyler.min.js", array('jquery'));
	wp_enqueue_script('dialog', 			get_template_directory_uri() . "/inc/js/dialogBox/jquery-impromptu.min.js",  array('jquery'));
	wp_enqueue_script('uploads_',			get_template_directory_uri() . "/inc/js/uploads_.js", array('jquery'));
	wp_enqueue_script('admin-jQuery-fruit',	get_template_directory_uri() . "/inc/js/main.js", array('jquery'));
}

function fruitful_get_box_upload_image($val, $field, $btnclassup = 'upload_btn', $btnclassr = 'reset_btn', $imgcontclass = '', $imgid = '') {
		 $out  = '';
		 $out .= '<div class="box-image">';
			if ($val != '') {
				$out .= '<div class="img-container '.$imgcontclass.'">';
					$image_attributes = wp_get_attachment_image_src( $val, 'full');
					if ($imgid != '') {
						$out .= '<img id="'.$imgid.'" src="'.esc_url_raw($image_attributes[0]).'" alt="" />';
					} else {
						$out .= '<img src="'.esc_url_raw($image_attributes[0]).'" alt="" />';
					}					
				$out .= '</div>	';
			}
			
			$out .= '<input class="of-input" name="fruitful_theme_options['. $field .']"   id="'. $field .'_upload" type="hidden" value="'. $val .'" />';
			$out .= '<div class="upload_button_div">';
				$out .= '<span data-imagetype="'.$imgcontclass.'" class="button '. $btnclassup .'" id="'. $field .'">'. __('Upload Image', 'fruitful') .'</span>';
			if(!empty($val)) {
				$none = '';
			} else { 
				$none = 'none';
			}
				$out .= '<span class="button ' . $btnclassr . ' ' . $none .'" id="reset_'. $field .'" title="' . $field . '">'.__('Remove', 'fruitful') .'</span>';
			$out .= '</div>';
		$out .= '</div>';
	return $out;
}


function fruitful_get_box_upload_slide($attach_id, $link_url, $is_blank, $ind, $btnclassup = 'upload_btn',  $btnclassr = 'reset_btn') {
	$out  = ''; 
	$out .= '<div class="box-image">';
	if ($attach_id != -1) {
		$out .= '<div class="img-container custom-slide">';
			$image_attributes = wp_get_attachment_image_src($attach_id, 'full');
			$out .= '<img src="'.esc_url_raw($image_attributes[0]).'" alt="" />';
		$out .= '</div>	';
				
	}
		/*Link out for Slider*/
		$out .= '<label for="slide-link-'.$ind.'">'. __('Link URL', 'fruitful') .'</label>';
		$out .= '<input type="text" name="fruitful_theme_options[slides][slide-'.$ind.'][link]" id="slide-link-'.$ind.'" class="slide-link-'.$ind.' text-input" value="'.esc_url($link_url).'"/>';
		$out .= '<div class="clear"></div>';

		$out .= '<label for="link-blank-'.$ind.'">';
		$out .= '<input type="checkbox" name="fruitful_theme_options[slides][slide-'.$ind.'][is_blank]" id="link-blank-'.$ind.'" class="link-target-'.$ind.'" '. checked( 'on', $is_blank, false) .'/>';
		$out .= __('Target "_blank"', 'fruitful') .'</label>';
	
		$out .= '<input class="of-input" name="fruitful_theme_options[slides][slide-'.$ind.'][attach_id]" id="attach-'.$ind.'" type="hidden" value="'. intval($attach_id) .'" />';
		$out .= '<div class="upload_button_div">';
			$out .= '<span data-imagetype="slide" class="button '. $btnclassup .'" id="add-slide-btn-'. $ind .'">Upload Image</span>';
		$out .= '</div>';
	$out .= '</div>';
	return $out;
}


function fruitful_get_select_fields($field_name, $options, $array_of_values, $class_name = "selected", $title = '') {
		$out = '';
		
		if (!empty($title)) $out .= '<h4><strong>'.$title.'</strong></h4>';
		$out .= '<select class="'. $class_name .'" name="fruitful_theme_options['.$field_name.']" id="options-'.$field_name.'">';
		$selected = $options[$field_name];
		$p = $r = '';
			foreach ( $array_of_values as $option ) {
				$label = $option['label'];
				if ( $selected == $option['value'] ) // Make default first in list
					$p = "\n\t<option style=\"padding-right: 10px;\" selected='selected' value='" . esc_attr( $option['value'] ) . "'>$label</option>";
				else
					$r .= "\n\t<option style=\"padding-right: 10px;\" value='" . esc_attr( $option['value'] ) . "'>$label</option>";
			}
			$out .= $p . $r;
	$out .= '</select>';
	echo $out;
}
	
function fruitful_ret_options ($name_options) {
   return $options = array_filter((array) get_option($name_options));
}

function fruitful_get_default_array() {
return array(
				/*General Settings*/
				'responsive'		=> 'on',
				'postcomment'		=> 'on',
				'pagecomment'		=> 'on',
				'is_fixed_header'	=> 'off',
				'styletheme'		=> 'off',
				'is_wpml_ready'		=> 'on',
				'latest_posts_templ'	=> '0',
				'layout_404_templ'		=> '0',
				'layout_search_templ'	=> '1',
				'layout_cat_templ'		=> '1',
				'layout_tag_templ'		=> '1',
				'layout_author_templ'	=> '1',
				'layout_archive_templ'	=> '1',
				
				'show_featured_single'=> 'off',

				/*Header image*/
				'header_bg_color'	=> '#ffffff',	
				'header_img' 		=> '',
				'header_img_size'	=> 'full',
				'header_height' 	=> '80',
				'menu_type_responsive'=> 'inside_content',
				
				/*Background Image*/
				'backgroung_img'    => '',
				'background_color'	=> '#ffffff', 
				'bg_repeating'		=> 'off',
				'container_bg_color' => '#ffffff', 
				
				/*logo*/
				'logo_img'			=> '',
				'fav_icon'			=> '',
				
				'logo_position'		=> '0',
				'menu_position'		=> '2',
				
				
				/*Color*/
				'menu_bg_color'		=> '#ffffff',
				'menu_btn_color'	=> '#F15A23',
				'menu_hover_color'	=> '#ffffff',
				'menu_font_color'	=> '#333333',		
				

				/*Dropdown Color*/
				'dd_menu_bg_color'		=> '#ffffff',
				'dd_menu_btn_color'		=> '#F15A23',
				'dd_menu_hover_color'	=> '#ffffff',
				'dd_menu_font_color'	=> '#333333',		
	
				/*General font colors*/
				'p_font_color'			=> '#333333',
				'a_font_color'			=> '#333333',
				'a_hover_font_color'	=> '#FF5D2A',
				'a_focus_font_color'	=> '#FF5D2A',
				'a_active_font_color'	=> '#FF5D2A',
				
				/*Color for lines*/
				'widgets_sep_color'		=> '#F15A23',	
				'date_of_post_b_color' 	=> '#F15A23',
				'date_of_post_f_color'	=> '#ffffff',
				
				/*Color for buttons*/
				'btn_color'				=> '#333333',	
				'btn_active_color'		=> '#F15A23',	
				
				/*Color for social icons*/
				'soc_icon_bg_color'		=>	'#333333',
				'soc_icon_color'		=>	'#ffffff',
				
				/*Woo Colors*/
				'woo_sale_price_color'	   => '#919191',
				'woo_rating_color_regular' => '#333333',
				'woo_rating_color_active'  => '#FF5D2A',
				
				
				/*fonts*/
				'h_font_family'		=> 'Open Sans, sans-serif',
				'h1_size'			=> '27',
				'h2_size'			=> '34',
				'h3_size'			=> '18',
				'h4_size'			=> '17',
				'h5_size'			=> '14',
				'h6_size'			=> '12',
				'm_font_family'		=> 'Open Sans, sans-serif',
				'm_size'			=> '14',
				'p_font_family'		=> 'Open Sans, sans-serif',
				'p_size'			=> '14',
				'select_slider'     => '1',
				
				
				/*Sliders*/
				
				//'s_width'			=> '960',
				//'s_height'		=> '520',
				
				/*slider flex*/
				's_animation'		=> 'fade', 
				's_direction'		=> 'horizontal',
				's_reverse'			=> 'false',
				's_slideshow'		=> 'true',
				's_slideshowSpeed'	=> '7000',
				's_animationSpeed'	=> '600',
				's_initDelay'		=> '0',
				's_randomize'		=> 'false',
				's_controlnav'		=> 'true',
				
				/*slider nivo*/
				'nv_skins'				=> 'theme-bar',
				'nv_animation' 			=> 'random',
				'nv_slice' 				=> '15',
				'nv_boxCols' 			=> '8',
				'nv_boxRows' 			=> '4',
				'nv_animSpeed' 			=> '500',
				'nv_pauseTime' 			=> '3000',
				'nv_startSlide'			=> '0',
				'nv_directionNav' 		=> 'true',
				'nv_controlNav' 		=> 'true',
				'nv_controlNavThumbs' 	=> 'false',
				'nv_pauseOnHover' 		=> 'true',
				'nv_manualAdvance' 		=> 'false',
				'nv_prevText' 			=> 'Prev',
				'nv_nextText' 			=> 'Next',
				'nv_randomStart' 		=> 'false',
				'slides'				=> '',
				
				/*End Sliders*/
				
				/*footer*/			 
				'footer_text'	 => esc_attr__( 'Fruitful theme by', 'fruitful' ) . ' <a href="' . esc_url(__('http://fruitfulcode.com','fruitful')) . '">' . esc_attr__( 'fruitfulcode', 'fruitful' ) . '</a> ' . esc_attr__( 'Powered by:', 'fruitful' ) . ' <a href="' . esc_url(__('http://wordpress.org','fruitful')) . '">' . esc_attr__( 'WordPress', 'fruitful' ) . '</a>', 
				
				/*socials*/
				'sl_position'		=> '0',
				'facebook_url' 		=> '',
				'twitter_url' 		=> '',
				'linkedin_url'		=> '',
				'myspace_url'		=> '',
				'googleplus_url'	=> '',
				'dribbble_url'		=> '',
				'skype_link'		=> '',
				'flickr_link'		=> '',
				'youtube_url'		=> '',
				'vimeo_url'			=> '',
				'rss_link'			=> '',
				'vk_link'			=> '',
				'instagram_url'		=> '',
				'pinterest_url'		=> '',
				'yelp_url'			=> '',
				'email_link'		=> '', 
				'github_link'		=> '', 
				'tumblr_link'		=> '',
				'soundcloud_link'	=> '',

				/*woocoommerce*/
				'showcart'  		=> 'on',
				'woo_shop_sidebar'	=> '2',
				'woo_product_sidebar'	=> '1',
				'shop_num_row'		=> '4',
				'woo_shop_num_prod' => '10',
				
				
				'custom_css'        => stripslashes('')
		);
}
?>