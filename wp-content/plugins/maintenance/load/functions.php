<?php
 
function get_custom_login_code() {
	global $wp_query, 
		   $error; 
	$mt_options = mt_get_plugin_options(true);
	$user_connect = false;
    if (!is_array($wp_query->query_vars)) $wp_query->query_vars = array();	   
	$error_message  = 	$user_login = $user_pass = $error = '';
	$is_role_check  = false;
	$class_login 	= "user-icon";
	$class_password = "pass-icon";
	$using_cookie = false;
	
	if(isset($_POST['is_custom_login'])) {
		$user_login = esc_attr($_POST['log']);
		$user_login = sanitize_user( $user_login );
		$user_pass  = $_POST['pwd'];
		$access = array();
		$access['user_login'] 	 = esc_attr($user_login);
		$access['user_password'] = $user_pass;
		$access['remember']  	 = true;
					
		$user = null;
		$user = new WP_User(0, $user_login);
		$current_role = current($user->roles);
					 
		if (!empty($mt_options['roles_array'])) {
			foreach (array_keys($mt_options['roles_array']) as $key) {
				if ($key == $current_role) { $is_role_check = true; }	
			}
		}  else {
			$is_role_check = true;
		}
					 
		if ( !$is_role_check) {
			  $error_message  	= __('Permission access denied!', 'maintenance');
			  $class_login 		= 'error';
			  $class_password 	= 'error';
		} else {
			
			$user_connect = wp_signon( $access, false);
			if ( is_wp_error($user_connect) )  {
				if ($user_connect->get_error_code() == 'invalid_username') {
					$error_message  =  __('You entered your login are incorrect!', 'maintenance');;
					$class_login 	= 'error';
					$class_password = 'error';
				} else if ($user_connect->get_error_code() == 'incorrect_password') {
					$error_message  =  __('You entered your password are incorrect!', 'maintenance');
					$class_password = 'error';
				} else {
					$error_message  =  __('You entered your login and password are incorrect!', 'maintenance');
					$class_login = 'error';
					$class_password = 'error';
				}
			} else {
				wp_redirect(home_url('/'));
				exit;
			}
		}	
	} 	
	
	if (!$user_connect) {
		get_headers_503();
	}	
		
		return array($error_message, $class_login, $class_password, $user_login);
	}			

	function add_custom_style() {
		global $wp_styles;
		$mt_options = mt_get_plugin_options(true);
		
		if (!empty($mt_options['body_font_family'])) {
			$font_link = '';
			$font_link = mt_get_google_font(esc_attr($mt_options['body_font_family']));
			if ($font_link != '') {
				wp_register_style('_custom_fonts', $font_link);
				$wp_styles->do_items('_custom_fonts');		
			}	
		}
		
		wp_register_style('_iconstyle', MAINTENANCE_URI .'load/images/fonts-icon/icons.style.css');
		wp_register_style('_style', 	MAINTENANCE_URI .'load/style.css');
		$wp_styles->do_items('_iconstyle');
		
		/*Add inline custom style*/
		get_options_style();
		$wp_styles->do_items('_style');
	}	
		
	function add_custom_scripts() {
		global $wp_scripts;
		wp_register_script( '_placeholder', 	MAINTENANCE_URI  .'load/js/jquery.placeholder.js', 	   'jquery');
		wp_register_script( '_backstretch', 	MAINTENANCE_URI  .'load/js/jquery.backstretch.min.js', 'jquery');
		wp_register_script( '_frontend', 		MAINTENANCE_URI  .'load/js/jquery.frontend.min.js', 'jquery');
		wp_register_script( '_blur',			MAINTENANCE_URI  .'load/js/jquery.blur.min.js', 'jquery');
		
		$wp_scripts->do_items('jquery');
		$wp_scripts->do_items('_placeholder');
		$wp_scripts->do_items('_backstretch');		
		$wp_scripts->do_items('_blur');
		$wp_scripts->do_items('_frontend');
	}
	
	add_action ('load_custom_scripts', 'add_custom_style',   5);
	add_action ('load_custom_scripts', 'add_custom_scripts', 15);
	
	function get_page_title($error_message) {
		$mt_options = mt_get_plugin_options(true);
		$title = $options_title = '';
		if (empty($mt_options['page_title'])) {
			$options_title = wp_title( '|', false);
		} else {
			$options_title = $mt_options['page_title'];
		}
		
		if ($error_message != '') {
		    $title =  $options_title . ' - ' . $error_message;
		} else {
		    $title =  $options_title;
		}
		echo "<title>$title</title>";
	}
	
	function get_options_style() {
		$mt_options = mt_get_plugin_options(true);
		$options_style = '';
		if ( !empty($mt_options['body_bg_color']) ) {
			  $options_style .= 'body {background-color: '. esc_attr($mt_options['body_bg_color']) .'}';
			  $options_style .= '.preloader {background-color: '. esc_attr($mt_options['body_bg_color']) .'}';
		}
		
		if (!empty($mt_options['body_font_family'])) { 
			$options_style .=  'body {font-family: '. esc_attr($mt_options['body_font_family']) .'; }';
		}
		
		
		if ( !empty($mt_options['font_color']) ) {
			 $font_color = esc_attr($mt_options['font_color']);
			 $options_style .= '.site-title, .preloader i, .login-form, .login-form a.lost-pass, .btn-open-login-form, .site-content, .user-content-wrapper, .user-content, footer {color: '. $font_color .';} ';
			 $options_style .= '.ie7 .login-form input[type="text"], .ie7 .login-form input[type="password"], .ie7 .login-form input[type="submit"]  {color: '. $font_color .'} ';
			 $options_style .= 'a.close-user-content, #mailchimp-box form input[type="submit"], .login-form input#submit.button  {border-color:'. $font_color .'} ';
			 $options_style .= '.ie7 .company-name {color: '. $font_color .'} ';
		}
		
		if (!empty($mt_options['custom_css'])) {
			$options_style .= wp_kses_stripslashes($mt_options['custom_css']);
		}
		
		wp_add_inline_style( '_style', $options_style );
	}
	//add_action('options_style', 'get_options_style', 10);
	
	function get_logo_box() {
		$mt_options = mt_get_plugin_options(true);
		$out_html = '';
			
			if ( !empty($mt_options['logo']) ) { 
				$logo = wp_get_attachment_image_src( $mt_options['logo'], 'full'); 
				$out_html = '<a class="logo" rel="home" href="'.esc_url(site_url('')) .'" style="width:'.$logo[1].'px">';
					$out_html .= '<img src="'. esc_url($logo[0]) .'" alt="logo"/>';
				$out_html .= '</a>'; 
			} else { 
				$out_html = '<a class="logo istext" rel="home" href="'.esc_url(site_url('')) .'">';
					$out_html .= '<h1 class="site-title">'. get_bloginfo( 'name' ) .'</h1>';
			} 
			$out_html .= '</a>'; 
			
		echo $out_html;
	}
	add_action ('logo_box', 'get_logo_box', 10);
	
	
	function get_content_section() {
		$mt_options  = mt_get_plugin_options(true);
		$out_content = '';
		if (!empty($mt_options['heading'])) {
			$out_content .= '<h2 class="heading font-center">'     . wp_kses_post(stripslashes($mt_options['heading'])) .'</h2>';
		}
		
		if (!empty($mt_options['description'])) {
			$out_content .= '<h3 class="description font-center">' . wp_kses_post(stripslashes($mt_options['description'])) .'</h3>';
		}
		
		echo $out_content;
	}
	add_action('content_section', 'get_content_section', 10);
	
	
	function add_single_background() {
		$out_ = '';
		$mt_options  = mt_get_plugin_options(true);
		$intensity = 5;
		if (!empty($mt_options['body_bg'])) {
			if (empty($mt_options['gallery_array']['attachment_ids'])) {
			$out_ .= '<script type="text/javascript">'. "\r\n";
				$out_ .= 'jQuery(document).ready(function() { ' . "\r\n";
					if (!empty($mt_options['body_bg'])) {
						$bg    =  wp_get_attachment_image_src( $mt_options['body_bg'], 'full');
						$out_ .= 'if (jQuery(window).height() < 768) {'. "\r\n" ;
							$out_ .= 'jQuery("body").backstretch("'. esc_url($bg[0]) .'");' . "\r\n" ;
						$out_ .= '}	else {'. "\r\n" ;
							$out_ .= 'jQuery(".main-container").backstretch("'. esc_url($bg[0]) .'");' . "\r\n" ;
						$out_ .= '}'. "\r\n" ;
					}
					
					if (!empty($mt_options['is_blur'])) {
						/*Blur image background*/
						if (!empty($mt_options['blur_intensity'])) {
						    $intensity = absint($mt_options['blur_intensity']);
						}				
						$out_ .= 'var vblur = jQuery(".backstretch").Vague({intensity:'.$intensity.'}); vblur.blur()' . "\r\n";
					}
				
				$out_ .= '});';
			$out_ .= '</script>';
			}
		echo $out_;
		}
	}	
	add_action ('add_single_backstretch_background', 'add_single_background', 10);
	
	function get_footer_section() {
		$mt_options  = mt_get_plugin_options(true);
		
		$out_ftext = '';
		$out_ftext .= '<a class="company-name" rel="footer" href="'.esc_url(site_url('')) .'">';
			if (isset($mt_options['footer_text']) && !empty($mt_options['footer_text'])) {
				$out_ftext .= $mt_options['footer_text'];
			} else {			
				$out_ftext .= '&copy; ' . get_bloginfo( 'name' ) . ' ' . date('Y');
			}	
		$out_ftext .= '</a>';
		echo $out_ftext;
	}
	add_action('footer_section', 'get_footer_section', 10);
	
	
	function do_button_login_form($error = -1) {
		?>
			<div id="btn-open-login-form" class="btn-open-login-form">
				<i class="foundicon-lock"></i>
			</div>
		<?php
	
	}
	
	function do_login_form($user_login, $class_login, $class_password) {
		$mt_options  = mt_get_plugin_options(true);
		$out_login_form = $form_error = '';
		if (($class_login == 'error') || ($class_password == 'error')) {
			 $form_error = ' active error';
		}
		
		$out_login_form .= '<form name="login-form" id="login-form" class="login-form'.$form_error.'" method="post">';
				$out_login_form .= '<label for="">'. __('User Login', 'maintenance') .'</label>';
				$out_login_form .= '<span class="licon '.$class_login.'"><input type="text" name="log" id="log" value="'.  $user_login .'" size="20"  class="input username" placeholder="'. __('Username', 'maintenance') .'"/></span>';
				$out_login_form .= '<span class="picon '.$class_password.'"><input type="password" name="pwd" id="login_password" value="" size="20"  class="input password" placeholder="'. __('Password', 'maintenance') .'" /></span>';
				$out_login_form .= '<a class="lost-pass" href="'.esc_url(wp_lostpassword_url()).'" title="'.__('Lost Password', 'maintenance') .'">'.__('Lost Password', 'maintenance') .'</a>';
				$out_login_form .= '<input type="submit" class="button" name="submit" id="submit" value="'.__('Login','maintenance') .'" tabindex="4" />';
				$out_login_form .= '<input type="hidden" name="is_custom_login" value="1" />';
		$out_login_form .= '</form>';
		
		if (isset($mt_options['is_login'])) {
			echo $out_login_form;
		}	
	}
	
	function get_preloader_element() {
		$out = '';
		$out = '<div class="preloader"><i></i></div>';
		echo $out; 
	}
	add_action('before_content_section', 'get_preloader_element', 5);
	
	function maintenance_gg_analytics_code() {
		$mt_options  = mt_get_plugin_options(true);
			if (!isset($mt_options['503_enabled']) && (isset($mt_options['gg_analytics_id']))) {
		?>
		<script type="text/javascript">
			var _gaq = _gaq || [];
				_gaq.push(['_setAccount', '<?php echo esc_attr($mt_options['gg_analytics_id']); ?>']);
				_gaq.push(['_trackPageview']);
			(function() {
				var ga = document.createElement('script'); 
					ga.type = 'text/javascript'; 
					ga.async = true;
					ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
					var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			})();
		</script>
		<?php 
		}
  }
  add_action('add_gg_analytics_code', 'maintenance_gg_analytics_code');
  
	function get_headers_503() {
		$mt_options  = mt_get_plugin_options(true);
		nocache_headers();
		if (isset($mt_options['503_enabled'])) {
			$protocol = "HTTP/1.0";
			if ( "HTTP/1.1" == $_SERVER["SERVER_PROTOCOL"] )
				$protocol = "HTTP/1.1";
				header( "$protocol 503 Service Unavailable", true, 503 );
				
				$vCurrDate_end = '';
				$vdate_end = date_i18n( 'Y-m-d', strtotime( current_time('mysql', 0) )); 
				$vtime_end = date_i18n( 'h:i a', strtotime( '12:00 pm')); 

				if (!empty($mt_options['expiry_date_end']))
					$vdate_end = $mt_options['expiry_date_end'];
				if (!empty($mt_options['expiry_time_end']))
					$vtime_end = $mt_options['expiry_time_end'];		   
		
				if (($mt_options['state']) && (!empty($mt_options['expiry_date_end']) && !empty($mt_options['expiry_time_end']))) {
						$date_concat = $vdate_end . ' ' . $vtime_end;
						$vCurrDate = strtotime($date_concat); 	
						if ( strtotime( current_time('mysql', 0)) < $vCurrDate ) {
							header( "Retry-After: " . gmdate("D, d M Y H:i:s", $vCurrDate) );
						} else {
							header( "Retry-After: 3600" );
						}
				} else {
					header( "Retry-After: 3600" );
				}	
		}
	}