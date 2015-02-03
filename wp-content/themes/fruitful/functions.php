<?php
/**
 * Fruitful theme functions and definitions
 *
 * @package Fruitful theme
 * @since Fruitful theme 1.0
 */

 /**
 * Set the content width based on the theme's design and stylesheet.
 *
 * @since Fruitful theme 1.0
 */
if ( ! isset( $content_width ) )
	   $content_width = 960; /* pixels */

/*woocommerce theme support*/
add_theme_support( 'woocommerce' );

if ( ! function_exists( 'fruitful_setup' ) ):
/**
 * Sets up theme defaults and registers support for various WordPress features.
 *
 * Note that this function is hooked into the after_setup_theme hook, which runs
 * before the init hook. The init hook is too late for some features, such as indicating
 * support post thumbnails.
 *
 * @since Fruitful theme 1.0
 */


 /**
 * Implement the Custom Header feature
 */

 /*require get_template_directory() . '/inc/custom-header.php';*/

 /**
 * Custom template tags for this theme.
 */
require get_template_directory() . '/inc/template-tags.php';
require get_template_directory() . '/inc/widgets.php';


 /**
 * Adding recommended plugins for Fruitful Theme.
 */
require_once('inc/func/plugins-included.php');


/**
 * Custom functions that act independently of the theme templates
 */
require get_template_directory() . '/inc/tweaks.php';
require get_template_directory() . '/inc/func/fruitful-function.php';
require get_template_directory() . '/inc/func/comment-inline-error.php';
require get_template_directory() . '/inc/metaboxes/init-for-objestcs-mb.php';

/**
 * Custom Theme Options
 */
require get_template_directory() . '/inc/theme-options/theme-options.php';

function fruitful_fonts_url() {
	$fonts_url = '';

	/* Translators: If there are characters in your language that are not
	 * supported by Source Sans Pro, translate this to 'off'. Do not translate
	 * into your own language.
	 */
	$source_sans_pro = _x( 'on', 'Source Sans Pro font: on or off', 'fruitful' );

	/* Translators: If there are characters in your language that are not
	 * supported by Bitter, translate this to 'off'. Do not translate into your
	 * own language.
	 */
	$bitter = _x( 'on', 'Bitter font: on or off', 'fruitful' );

	if ( 'off' !== $source_sans_pro || 'off' !== $bitter ) {
		$font_families = array();

		if ( 'off' !== $source_sans_pro )
			$font_families[] = 'Source Sans Pro:300,400,700,300italic,400italic,700italic';

		if ( 'off' !== $bitter )
			$font_families[] = 'Bitter:400,700';

		$query_args = array(
			'family' => urlencode( implode( '|', $font_families ) ),
			'subset' => urlencode( 'latin,latin-ext' ),
		);
		$fonts_url = add_query_arg( $query_args, "//fonts.googleapis.com/css" );
	}

	return $fonts_url;
}

function fruitful_setup() {
	/**
	 * Make theme available for translation
	 * Translations can be filed in the /languages/ directory
	 * If you're building a theme based on Fruitful theme, use a find and replace
	 * to change 'fruitful' to the name of your theme in all the template files
	 */
	load_theme_textdomain( 'fruitful', get_template_directory() . '/languages' );
	/**
	 * Add default posts and comments RSS feed links to head
	 */
	add_theme_support( 'automatic-feed-links' );
	/**
	 * Enable support for Post Thumbnails
	 */
	add_theme_support( 'post-thumbnails' );
	add_theme_support( 'html5', array( 'search-form', 'comment-form', 'comment-list' ) );

	add_theme_support( 'post-formats', array(
		'aside', 'audio', 'chat', 'gallery', 'image', 'link', 'quote', 'status', 'video'
	) );

	/**
	 * This theme uses wp_nav_menu() in one location.
	 */
	register_nav_menus( array(
		'primary' => __( 'Primary Menu', 'fruitful' ),
	) );


	add_theme_support( 'post-thumbnails' );
	set_post_thumbnail_size( 604, 270, true );
	add_image_size( 'slider-thumb', 608, 300, true );
	add_image_size( 'main-slider', 1920, 900, true );

	add_editor_style( array( 'css/editor-style.css', 'fonts/genericons.css', fruitful_fonts_url() ) );

	$defaults = array(
		'default-color'          => 'fff',
		'default-image'          => '',
		'wp-head-callback'       => '_custom_background_cb',
		'admin-head-callback'    => '',
		'admin-preview-callback' => ''
		);
	/*add_theme_support( 'custom-background', $defaults );*/

	add_filter( 'use_default_gallery_style', '__return_false' );

}
endif;
add_action( 'after_setup_theme', 'fruitful_setup' );
// fruitful_setup

if ( ! function_exists( 'fruitful_wp_title' ) ) {
function fruitful_wp_title( $title, $sep ) {
	global $paged, $page;
	if ( is_feed() ) return $title;

	$title .= get_bloginfo( 'name' );
	$site_description = get_bloginfo( 'description', 'display' );
	if ( $site_description && ( is_home() || is_front_page() ) )
 		 $title = "$title $sep $site_description";
	if ( $paged >= 2 || $page >= 2 )
		$title = "$title $sep " . sprintf( __( 'Page %s', 'fruitful' ), max( $paged, $page ) );
	return $title;
}
	add_filter( 'wp_title', 'fruitful_wp_title', 10, 2 );
}

/**
 * Register widgetized area and update sidebar with default widgets
 *
 * @since Fruitful theme 1.0
 */
 if ( ! function_exists( 'fruitful_widgets_init' ) ) {
function fruitful_widgets_init() {
	register_widget( 'Fruitful_Widget_News_Archive' );

	register_sidebar( array(
		'name' => __( 'Main Sidebar', 'fruitful' ),
		'id' => 'sidebar-1',
		'before_widget' => '<aside id="%1$s" class="widget %2$s">',
		'after_widget' => '</aside>',
		'before_title' => '<h3 class="widget-title">',
		'after_title' => '</h3>',
	) );

	register_sidebar( array(
		'name' => __( 'Blog Sidebar', 'fruitful' ),
		'id' => 'sidebar-2',
		'before_widget' => '<aside id="%1$s" class="widget %2$s">',
		'after_widget' => '</aside>',
		'before_title' => '<h3 class="widget-title">',
		'after_title' => '</h3>',
	) );

	register_sidebar( array(
		'name' => __( 'Single Post Sidebar', 'fruitful' ),
		'id' => 'sidebar-3',
		'before_widget' => '<aside id="%1$s" class="widget %2$s">',
		'after_widget' => '</aside>',
		'before_title' => '<h3 class="widget-title">',
		'after_title' => '</h3>',
	) );

	register_sidebar( array(
		'name' => __( 'Homepage Sidebar', 'fruitful' ),
		'id' => 'sidebar-4',
		'before_widget' => '<aside id="%1$s" class="widget %2$s">',
		'after_widget' => '</aside>',
		'before_title' => '<h3 class="widget-title">',
		'after_title' => '</h3>',
	) );

	if (class_exists('woocommerce')){
		register_sidebar( array(
			'name' => __( 'Shop Page Sidebar', 'fruitful' ),
			'id' => 'sidebar-5',
			'before_widget' => '<aside id="%1$s" class="widget %2$s">',
			'after_widget' => '</aside>',
			'before_title' => '<h3 class="widget-title">',
			'after_title' => '</h3>',
		) );

		register_sidebar( array(
			'name' => __( 'Product Page Sidebar', 'fruitful' ),
			'id' => 'sidebar-6',
			'before_widget' => '<aside id="%1$s" class="widget %2$s">',
			'after_widget' => '</aside>',
			'before_title' => '<h3 class="widget-title">',
			'after_title' => '</h3>',
		) );
	}
}

add_action( 'widgets_init', 'fruitful_widgets_init' );
}

/**
 * Enqueue scripts and styles
 */
if ( ! function_exists( 'fruitful_scripts' ) ) {
function fruitful_scripts() {
	global $post;
	$prefix 	   = '_fruitful_';
	$slider_layout = false;
	$theme_options  = fruitful_ret_options("fruitful_theme_options");
	$front_page_id  = get_option('page_on_front');
	$blog_page_id   = get_option('page_for_posts ');


	if (is_page() && !is_front_page() && !is_home()) {
		$slider_layout  = get_post_meta( $post->ID, $prefix . 'slider_layout', true);
	} elseif(!is_front_page() && is_home() && ($blog_page_id != 0)) {
		/*Only for blog posts loop*/
		$slider_layout  = get_post_meta( $blog_page_id, $prefix . 'slider_layout', true);
	} elseif (is_front_page()) {
		$slider_layout  = get_post_meta( $front_page_id, $prefix . 'slider_layout', true);
	}

	if ($slider_layout) {
		if (isset($theme_options['select_slider'])) {
			  if ($theme_options['select_slider'] == "1") {
					wp_enqueue_style( 'flex-slider', 			get_template_directory_uri() . '/js/flex_slider/slider.css');
					wp_enqueue_script('flex-fitvid-j',			get_template_directory_uri() . '/js/flex_slider/jquery.flexslider-min.js', array( 'jquery' ), '20130930', false );
					wp_enqueue_script('flex-froogaloop-j',		get_template_directory_uri() . '/js/flex_slider/froogaloop.js', 	array( 'jquery' ), '20130930', false );
					wp_enqueue_script('flex-easing-j', 			get_template_directory_uri() . '/js/flex_slider/jquery.easing.js', 	array( 'jquery' ), '20130930', false );
					wp_enqueue_script('flex-fitvid-j',			get_template_directory_uri() . '/js/flex_slider/jquery.fitvid.js', 	array( 'jquery' ), '20130930', false);
					wp_enqueue_script('flex-mousewheel-j',		get_template_directory_uri() . '/js/flex_slider/jquery.mousewheel.js', array( 'jquery' ), '20130930', false );
					wp_enqueue_script('flex-modernizr-j',		get_template_directory_uri() . '/js/flex_slider/modernizr.js', array( 'jquery' ), '20130930', false );
				} else if ($theme_options['select_slider'] == "2") {
					wp_enqueue_style( 'nivo-bar-skin', 		get_template_directory_uri() . '/js/nivo_slider/skins/bar/bar.css');
					wp_enqueue_style( 'nivo-dark-skin', 	get_template_directory_uri() . '/js/nivo_slider/skins/dark/dark.css');
					wp_enqueue_style( 'nivo-default-skin', 	get_template_directory_uri() . '/js/nivo_slider/skins/default/default.css');
					wp_enqueue_style( 'nivo-light-skin', 	get_template_directory_uri() . '/js/nivo_slider/skins/light/light.css');
					wp_enqueue_style( 'nivo-style', 		get_template_directory_uri() . '/js/nivo_slider/nivo-slider.css');
					wp_enqueue_script('nivo-slider',		get_template_directory_uri() . '/js/nivo_slider/jquery.nivo.slider.pack.js', array( 'jquery' ), '20130930', false );
				}
		}
	}

	/*add woocommerce styles for ie*/
	wp_enqueue_style( 'ie-style',		get_template_directory_uri() . '/woocommerce/ie.css');

	/*add fancybox*/
	wp_enqueue_script('fn-box',				get_template_directory_uri() . '/js/fnBox/jquery.fancybox.pack.js',   array( 'jquery' ), '20140525', false );
	wp_enqueue_style( 'fn-box-style',		get_template_directory_uri() . '/js/fnBox/jquery.fancybox.css');

	wp_enqueue_script('resp-dropdown',		get_template_directory_uri() . '/js/mobile-dropdown.min.js', 	array( 'jquery' ), '20130930', false );
	wp_enqueue_script('init',				get_template_directory_uri() . '/js/init.min.js', array( 'jquery' ), '20130930', false );

	$is_fixed_header = -1;
	if (isset($theme_options['is_fixed_header']) && ($theme_options['is_fixed_header'] == 'on')) {
		$is_fixed_header = 1;
	}

	wp_localize_script( 'init', 'ThGlobal', 	array( 'ajaxurl' 			=> admin_url( 'admin-ajax.php' ),
													   'is_fixed_header' 	=> $is_fixed_header ) );

	wp_enqueue_script('small-menu-select', get_template_directory_uri() . '/js/small-menu-select.js', array( 'jquery' ), '20130930', false );
	if ( is_singular() && comments_open() && get_option( 'thread_comments' ) ) {
		wp_enqueue_script( 'comment-reply' );
	}

	if ( is_singular() && wp_attachment_is_image() ) {
		wp_enqueue_script( 'keyboard-image-navigation', get_template_directory_uri() . '/js/keyboard-image-navigation.js', array( 'jquery' ), '20120202' );
	}
}
}
add_action( 'wp_enqueue_scripts', 'fruitful_scripts' );

if ( ! function_exists( 'fruitful_get_link_url' ) ) {
	function fruitful_get_link_url() {
		$content = get_the_content();
		$has_url = get_url_in_content( $content );

		return ( $has_url ) ? $has_url : apply_filters( 'the_permalink', get_permalink() );
	}
}

/*get cart button html*/
if ( ! function_exists( 'fruitful_get_cart_button_html' ) ) {
	function fruitful_get_cart_button_html() {
		$btn_cart = '';
		$theme_options = fruitful_ret_options("fruitful_theme_options");

		if (class_exists('Woocommerce')) {
			global $woocommerce;
			if (!empty($theme_options['showcart']) && (esc_attr($theme_options['showcart']) == 'on')) {
					$btn_cart = '<div class="cart-button">
						<a href="'.get_permalink( woocommerce_get_page_id( 'cart' ) ).'" class="cart-contents">
							<div class="cart_image"></div>
							<span class="num_of_product_cart">'.$woocommerce->cart->cart_contents_count.'</span>
						</a>
					</div>';
			}
		}
		echo $btn_cart;
	}
}


/*function for including google fonts*/
if ( ! function_exists( 'fruitful_add_custom_fonts' ) ) {
function fruitful_add_custom_fonts() {
    $font_url = array();
	$http_ = 'http://';
	if (is_ssl()) {
		$http_ = 'https://';
	}

	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,800,700,600,300&subset=latin,latin-ext';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Josefin+Slab:400,100,100italic,300,300italic,400italic,600,600italic,700,700italic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Lobster&subset=cyrillic-ext,latin-ext,latin,cyrillic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Arvo:400,400italic,700,700italic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Lato:400,100,100italic,300,300italic,400italic,700,700italic,900,900italic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Vollkorn:400,400italic,700,700italic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Abril+Fatface';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Ubuntu:400,300italic,400italic,500,500italic,700,700italic,300&subset=latin,greek,latin-ext,cyrillic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=PT+Sans:400,400italic,700,700italic&subset=latin,cyrillic';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Old+Standard+TT:400,400italic,700';
	$font_url[] = $http_ .'fonts.googleapis.com/css?family=Droid+Sans:400,700';

	foreach ($font_url as $font) {
		$unq_id = uniqid('custom_fonts_');
		wp_register_style($unq_id, $font);
		wp_enqueue_style($unq_id);
	}
}
}


/*Slider*/
if (!function_exists('fruitful_get_slider_layout_flex')) {
	function fruitful_get_slider_layout_flex() {
		global $post;
		$out = "";
		$prefix = '_fruitful_';
		$slider_layout = false;
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		$front_page_id  = get_option('page_on_front');
		$blog_page_id   = get_option('page_for_posts ');


		if (is_page() && !is_front_page() && !is_home()) {
			$slider_layout  = get_post_meta( $post->ID, $prefix . 'slider_layout', true);
		} elseif(!is_front_page() && is_home() && ($blog_page_id != 0)) {
			/*Only for blog posts loop*/
			$slider_layout  = get_post_meta( $blog_page_id, $prefix . 'slider_layout', true);
		} elseif (is_front_page()) {
			$slider_layout  = get_post_meta( $front_page_id, $prefix . 'slider_layout', true);
		}

		if(!empty($slider_layout) && ($slider_layout)) {
			$out .= '$(".flexslider").flexslider({' . "\n";
			$out .= 'animation: "'			. esc_attr($theme_options['s_animation'])		.'",' . "\n";
			$out .= 'direction: "'  		. esc_attr($theme_options['s_direction'])   	.'",' . "\n";
			$out .= 'reverse: '				. esc_attr($theme_options['s_reverse']) 		. ',' . "\n";
			$out .= 'slideshow: ' 		  	. esc_attr($theme_options['s_slideshow']) 		. ',' . "\n";
			$out .= 'slideshowSpeed: ' 		. esc_attr($theme_options['s_slideshowSpeed']) 	. ',' . "\n";
			$out .= 'animationSpeed: ' 		. esc_attr($theme_options['s_animationSpeed']) 	. ',' . "\n";
			$out .= 'controlNav: ' 			. esc_attr($theme_options['s_controlnav']) 		. ',' . "\n";

			if (isset($theme_options['s_initDelay'])) {
				$out .= 'initDelay: ' . $theme_options['s_initDelay'] .',' . "\n";
			}
			$out .= 'randomize: '	. $theme_options['s_randomize'] . "\n";
			$out .= '});' . "\n";
		}

		return $out;
	}
}

if (!function_exists('fruitful_get_slider_layout_nivo')) {
	function fruitful_get_slider_layout_nivo() {
		global $post;
		$out = "";
		$prefix = '_fruitful_';
		$slider_layout = false;
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		$front_page_id  = get_option('page_on_front');
		$blog_page_id   = get_option('page_for_posts ');


		if (is_page() && !is_front_page() && !is_home()) {
			$slider_layout  = get_post_meta( $post->ID, $prefix . 'slider_layout', true);
		} elseif(!is_front_page() && is_home() && ($blog_page_id != 0)) {
			/*Only for blog posts loop*/
			$slider_layout  = get_post_meta( $blog_page_id, $prefix . 'slider_layout', true);
		} elseif (is_front_page()) {
			$slider_layout  = get_post_meta( $front_page_id, $prefix . 'slider_layout', true);
		}

		if(!empty($slider_layout) && ($slider_layout)){
			$out .= '$(".nivoSlider").nivoSlider({' . "\n";
			$out .= 'effect: "'				. esc_attr($theme_options['nv_animation'])		. '",' . "\n";
			$out .= 'slices: '				. esc_attr($theme_options['nv_slice'])			.  ',' . "\n";
			$out .= 'boxCols: '				. esc_attr($theme_options['nv_boxCols'])		.  ',' . "\n";
			$out .= 'boxRows: '				. esc_attr($theme_options['nv_boxRows'])		.  ',' . "\n";
			$out .= 'animSpeed: '			. esc_attr($theme_options['nv_animSpeed'])		.  ',' . "\n";
			$out .= 'pauseTime: '			. esc_attr($theme_options['nv_pauseTime'])		.  ',' . "\n";
			$out .= 'startSlide:' . (isset($theme_options['nv_startSlide']) ? $theme_options['nv_startSlide'] : 0) . ',' . "\n";
			$out .= 'directionNav: '		. esc_attr($theme_options['nv_directionNav'])		.  ',' . "\n";
			$out .= 'controlNav: '			. esc_attr($theme_options['nv_controlNav'])			.  ',' . "\n";
			$out .= 'controlNavThumbs: '	. esc_attr($theme_options['nv_controlNavThumbs'])	.  ',' . "\n";
			$out .= 'pauseOnHover: '		. esc_attr($theme_options['nv_pauseOnHover'])	.  ',' . "\n";
			$out .= 'manualAdvance: '		. esc_attr($theme_options['nv_manualAdvance'])	.  ',' . "\n";
			$out .= 'prevText: "'			. esc_attr($theme_options['nv_prevText'])		.  '",' . "\n";
			$out .= 'nextText: "'			. esc_attr($theme_options['nv_nextText'])		.  '",' . "\n";
			$out .= 'randomStart: '			. esc_attr($theme_options['nv_randomStart']) . "\n";
			$out .= '});';
		}

		return $out;
	}
}

if (!function_exists('fruitful_get_slider')) {
	function fruitful_get_slider() {
		if (is_404()) return;
		global $post;
		$prefix 	= '_fruitful_';
		$slider_  	= $slider_layout = '';
		$id 		= 'fruitful-slider-'.rand(1, 250);

		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		$front_page_id  = get_option('page_on_front');
		$blog_page_id   = get_option('page_for_posts ');


		if (is_page() && !is_front_page() && !is_home()) {
			$slider_layout  = get_post_meta( $post->ID, $prefix . 'slider_layout', true);
		} elseif(!is_front_page() && is_home() && ($blog_page_id != 0)) {
			/*Only for blog posts loop*/
			$slider_layout  = get_post_meta( $blog_page_id, $prefix  . 'slider_layout', true);
		} elseif (is_front_page()) {
			$slider_layout  = get_post_meta( $front_page_id, $prefix . 'slider_layout', true);
		}

		/*Full Backend Options*/
		if(isset($theme_options['slides']) && (count($theme_options['slides']) > 0)) {
			foreach ($theme_options['slides'] as $key=>$slide) {
				$path_to_img = $val = '';
				$val = wp_get_attachment_image_src( esc_attr($slide['attach_id']), 'main-slider');
				$path_to_img = esc_url_raw($val[0]);
			}

			if ($path_to_img){
				if ($theme_options['select_slider'] == "1") {

						if ($slider_layout == 1) {
							$slider_ .= '<div class="main-slider-container fullwidth">';
						} else {
							$slider_ .= '<div class="main-slider-container">';
						}
							$slider_ .= '<section class="slider">';
								$slider_ .= '<div class= "flexslider" id="' . $id . '">';
									$slider_ .= '<ul class="slides">';
									foreach ($theme_options['slides'] as $key=>$slide) {
										$val = wp_get_attachment_image_src( esc_attr($slide['attach_id']), 'main-slider');
										$path_to_img = esc_url_raw($val[0]);
										$slider_ .= '<li>';
											if (!empty($slide['link'])) {
												if (!empty($slide['is_blank'])) {
													$slider_ .= '<a href="'.esc_url($slide['link']).'" target="_blank">';
												} else {
													$slider_ .= '<a href="'.esc_url($slide['link']).'">';
												}
													$slider_ .= '<img src="'.$path_to_img.'" />';
												$slider_ .= '</a>';
											} else {
												$slider_ .= '<img src="'.$path_to_img.'" />';
											}
										$slider_ .= '</li>';
									}
									$slider_ .= '</ul></div></section></div>';

				} else if ($theme_options['select_slider'] == "2") {

						if ($slider_layout == 1) {
							$slider_ .= '<div class="main-slider-container slider-wrapper fullwidth '. $theme_options['nv_skins'] .'">';
						} else {
							$slider_ .= '<div class="main-slider-container slider-wrapper '. $theme_options['nv_skins'] .'">';
						}

							$slider_ .= '<div id="nivo-slider-'. $id . '" class="nivoSlider">';
							foreach ($theme_options['slides'] as $key=>$slide) {
								$val = wp_get_attachment_image_src( esc_attr($slide['attach_id']), 'main-slider');
								$path_to_img = esc_url_raw($val[0]);
								if (!empty($slide['link'])) {
									if (!empty($slide['is_blank'])) {
										$slider_ .= '<a href="'.esc_url($slide['link']).'" target="_blank">';
									} else {
										$slider_ .= '<a href="'.esc_url($slide['link']).'">';
									}
										$slider_ .= '<img src="'. $path_to_img .'" data-thumb="'. $path_to_img .'" alt="" />';
									$slider_ .= '</a>';
								} else {
									$slider_ .= '<img src="'. $path_to_img .'" data-thumb="'. $path_to_img .'" alt="" />';
								}
							}
							$slider_ .= '</div>';
						$slider_ .= '</div>';
				}
			} else {
				$slider_ = '<div class="main-slider-container">';
					$slider_ .= '<section class="slider"><h3 class="no-slider-text">'. __('Please add images for slider in theme options!', 'fruitful') .'</h3></section>';
				$slider_ .= '</div>';
			}
		} else {
			$slider_ = '<div class="main-slider-container">';
				$slider_ .= '<section class="slider"><h3 class="no-slider-text">'. __('Please add images for slider in theme options!', 'fruitful') .'</h3></section>';
			$slider_ .= '</div>';
		}

		if (!empty($slider_layout)) {
			if ($slider_layout != 1) {
				$slider_ = '<div class="container"><div class="sixteen columns">' . $slider_ . '</div></div>';
			}
			echo '<div id="slider-container">'.$slider_.'</div>';
		}
	}
}

/*Get logo img*/
if (!function_exists('fruitful_get_logo')) {
	function fruitful_get_logo () {
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		$url_logo = '';

		if (!empty($theme_options['logo_img'])) { $url_logo_id	= esc_attr($theme_options['logo_img']); } else { $url_logo_id 	= ''; }

		/*Full Backend Options*/
		$description  = $name = '';
		$description  = esc_attr(get_bloginfo('description'));
		$name  		  = esc_attr(get_bloginfo('name'));

		if ($url_logo_id != "") {
			$url_logo = wp_get_attachment_image_src($url_logo_id, 'full');
			$url_logo = esc_url_raw($url_logo[0]);
			echo  '<a href="' . esc_url( home_url( '/' ) ) . '" title="' . $description .'" rel="home"><img class="logo" src="'. $url_logo  .'" alt="' . $description . '"/></a>';
		} else {
			echo  '<a class="logo-description" href="' . esc_url( home_url( '/' ) ) . '" title="' . $description .'" rel="home"><h1 class="site-title">'. $name .'</h1><h2 class="site-description">'. $description .'</h2></a>';
		}
	}
}

/*Get Favicon*/
if (!function_exists('fruitful_get_favicon')) {
	function fruitful_get_favicon () {
		$out_fav_html = '';
		$theme_options  = fruitful_ret_options("fruitful_theme_options");

		if (isset($theme_options['fav_icon'])) {
			$url_favicon = esc_attr($theme_options['fav_icon']);
			$url_favicon = wp_get_attachment_image_src($url_favicon, 'full');
			$url_favicon = esc_url_raw($url_favicon[0]);
		} else {
			$url_favicon = '';
		}

		if ($url_favicon != "") {
			$out_fav_html .=  '<link rel="shortcut icon" href="'. $url_favicon .'">';
			$out_fav_html .=  '<link rel="apple-touch-icon-precomposed" sizes="16x16" href="'. $url_favicon .'">';
		} else {
			/*Default favicon file*/
		}
		echo $out_fav_html;
	}
}

/*Get footer text*/
if (!function_exists('fruitful_get_footer_text')) {
	function fruitful_get_footer_text () {
		$out_footer_text = $footer_text = '';
		$theme_options   = fruitful_ret_options("fruitful_theme_options");
		if (!empty($theme_options['footer_text'])) {
			$footer_text = fruitful_kses_data(stripslashes($theme_options['footer_text']));

			if (is_home() || is_front_page()) {
				$out_footer_text .= $footer_text;
			} else {
				$out_footer_text .= '<nofollow>';
					$out_footer_text .= $footer_text;
				$out_footer_text .= '</nofollow>';

			}
		echo $out_footer_text;
		}
	}
}

/*Get position for social icons*/
if (!function_exists('fruitful_is_social_header')) {
	function fruitful_is_social_header () {
		$pos = false;
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		if(!empty($theme_options['sl_position']))	{ $pos =  esc_attr($theme_options['sl_position']); }
		return $pos;
	}
}

/*Get footer social icons*/
if (!function_exists('fruitful_get_socials_icon')) {
	function fruitful_get_socials_icon () {
		$out = '';
		$theme_options  = fruitful_ret_options("fruitful_theme_options");

		if(!empty($theme_options['facebook_url'])) 		{ $out .= '<a class="facebook" 	 title="facebook"	href="'	.	esc_url($theme_options['facebook_url']) 	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['twitter_url']))		{ $out .= '<a class="twitter" 	 title="twitter"	href="'	.	esc_url($theme_options['twitter_url']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['linkedin_url'])) 		{ $out .= '<a class="linkedin" 	 title="linkedin"	href="'	.	esc_url($theme_options['linkedin_url']) 	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['myspace_url'])) 		{ $out .= '<a class="myspace" 	 title="myspace"	href="'	.	esc_url($theme_options['myspace_url']) 		. '" target="_blank"></a>'; }
		if(!empty($theme_options['googleplus_url'])) 	{ $out .= '<a class="googleplus" title="google+"	href="'	.	esc_url($theme_options['googleplus_url']) 	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['dribbble_url'])) 	 	{ $out .= '<a class="dribbble" 	 title="dribbble"	href="'	.	esc_url($theme_options['dribbble_url']) 	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['skype_link'])) 		{ $out .= '<a class="skype" 	 title="skype"		href="skype:'.esc_attr($theme_options['skype_link'])	. '?call"><i class="fa"></i></a>'; }
		if(!empty($theme_options['flickr_link'])) 		{ $out .= '<a class="flickr" 	 title="flickr"		href="' 	.esc_url($theme_options['flickr_link']) 	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['youtube_url'])) 		{ $out .= '<a class="youtube" 	 title="youtube"	href="'	.	esc_url($theme_options['youtube_url']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['vimeo_url'])) 		{ $out .= '<a class="vimeo" 	 title="vimeo"		href="'	.	esc_url($theme_options['vimeo_url']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['rss_link'])) 			{ $out .= '<a class="rss" 		 title="rss"		href="'	.	esc_url($theme_options['rss_link']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['vk_link'])) 			{ $out .= '<a class="vk" 		 title="vk"			href="'	.	esc_url($theme_options['vk_link'])			. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['instagram_url']))		{ $out .= '<a class="instagram"	 title="instagram"	href="'	.	esc_url($theme_options['instagram_url'])	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['pinterest_url']))		{ $out .= '<a class="pinterest"	 title="pinterest"	href="'	.	esc_url($theme_options['pinterest_url'])	. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['yelp_url']))			{ $out .= '<a class="yelp"		 title="yelp"		href="'	.	esc_url($theme_options['yelp_url'])			. '" target="_blank"></a>'; }
		if(!empty($theme_options['email_link'])) 		{ $out .= '<a class="email" 	 title="email"		href="mailto:'.sanitize_email($theme_options['email_link']). '"><i class="fa"></i></a>'; }
		if(!empty($theme_options['github_link'])) 		{ $out .= '<a class="github" 	 title="github"		href="'	.	esc_url($theme_options['github_link']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['tumblr_link'])) 		{ $out .= '<a class="tumblr" 	 title="tumblr"		href="'	.	esc_url($theme_options['tumblr_link']) 		. '" target="_blank"><i class="fa"></i></a>'; }
		if(!empty($theme_options['soundcloud_link'])) 	{ $out .= '<a class="soundcloud" title="soundcloud" href="'	.	esc_url($theme_options['soundcloud_link']) 	. '" target="_blank"><i class="fa"></i></a>'; }

		echo '<div class="social-icon">' . $out . '</div>';
	}
}


/*Enable Comment*/
if ( ! function_exists( 'fruitful_state_post_comment' ) ) {
	function fruitful_state_post_comment () {
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		if (!empty($theme_options['postcomment'])) {
			return ($theme_options['postcomment'] == "on");
		} else {
			return false;
		}
	}
}

if ( ! function_exists( 'fruitful_state_page_comment' ) ) {
	function fruitful_state_page_comment () {
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		if (!empty($theme_options['pagecomment'])) {
			return ($theme_options['pagecomment'] == "on");
		} else {
			return false;
		}
	}
}


/*Compress code*/
if ( ! function_exists( 'fruitful_compress_code' ) ) {
	function fruitful_compress_code($code) {
		$code = preg_replace('!/\*[^*]*\*+([^/][^*]*\*+)*/!', '', $code);
		$code = str_replace(array("\r\n", "\r", "\n", "\t", '  ', '    ', '    '), '', $code);

		return $code;
	}
}

if ( ! function_exists( 'fruitful_hex2rgb' ) ) {
	function fruitful_hex2rgb( $colour ) {
		if ( $colour[0] == '#' ) {
			 $colour = substr( $colour, 1 );
		}
		if ( strlen( $colour ) == 6 ) {
			list( $r, $g, $b ) = array( $colour[0] . $colour[1], $colour[2] . $colour[3], $colour[4] . $colour[5] );
		} elseif ( strlen( $colour ) == 3 ) {
			list( $r, $g, $b ) = array( $colour[0] . $colour[0], $colour[1] . $colour[1], $colour[2] . $colour[2] );
		} else {
			return false;
		}
		$r = hexdec( $r );
		$g = hexdec( $g );
		$b = hexdec( $b );
		return array( 'red' => $r, 'green' => $g, 'blue' => $b );
	}
}

if ( ! function_exists( 'fruitful_get_responsive_style' ) ) {
	function fruitful_get_responsive_style () {
		$style_ = $back_style = $woo_style_ = '';
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		fruitful_add_custom_fonts();
		if (isset($theme_options['responsive']) && ($theme_options['responsive'] == 'on')) {
			if (class_exists('woocommerce')){wp_enqueue_style( 'woo-style', get_template_directory_uri() . '/woocommerce/woo.css');}
			if (!class_exists('ffs')){
				wp_enqueue_style('fontawesome-style',  get_stylesheet_directory_uri() . '/css/font-awesome.min.css');
			}
			wp_enqueue_style('main-style',  get_stylesheet_uri());
		} else {
			if (class_exists('woocommerce')){wp_enqueue_style( 'woo-style', get_template_directory_uri() . '/woocommerce/woo-fixed.css');}
			if (!class_exists('ffs')){
				wp_enqueue_style('fontawesome-style',  get_stylesheet_directory_uri() . '/css/font-awesome.min.css');
			}
			wp_enqueue_style('main-style',  get_stylesheet_directory_uri()  .'/fixed-style.css');
		}

		if (!empty($theme_options['styletheme'])) {
			if ($theme_options['styletheme'] == 'off') {
				$style_ .= 'h1 {font-size : '.esc_attr($theme_options['h1_size']) .'px; }' . "\n";
				$style_ .= 'h2 {font-size : '.esc_attr($theme_options['h2_size']) .'px; }' . "\n";
				$style_ .= 'h3 {font-size : '.esc_attr($theme_options['h3_size']) .'px; }' . "\n";
				$style_ .= 'h4 {font-size : '.esc_attr($theme_options['h4_size']) .'px; }' . "\n";
				$style_ .= 'h5 {font-size : '.esc_attr($theme_options['h5_size']) .'px; }' . "\n";
				$style_ .= 'h6 {font-size : '.esc_attr($theme_options['h6_size']) .'px; }' . "\n";

				$style_ .= 'h1, h2, h3, h4, h5, h6 {font-family : '. esc_attr($theme_options['h_font_family']) .'; } ' . "\n";
				$style_ .= '.main-navigation a     {font-family : '. esc_attr($theme_options['m_font_family']) .'; color : '.esc_attr($theme_options['menu_font_color']). '; } ' . "\n";
				$style_ .= '.main-navigation ul:not(.sub-menu) > li > a, .main-navigation ul:not(.sub-menu) > li:hover > a   { font-size : '.esc_attr($theme_options['m_size']) .'px;    } ' . "\n";


				if (!empty($theme_options['menu_bg_color']))   { $style_ .= '.main-navigation {background-color : ' .esc_attr($theme_options['menu_bg_color']) . '; }' . "\n";  }

				$style_ .= '#header_language_select a {font-family : '.  esc_attr($theme_options['m_font_family']) .';} ' . "\n";
				$style_ .= 'body {font-size : '. esc_attr($theme_options['p_size']) .'px; font-family : ' . esc_attr($theme_options['p_font_family']) . '; }' . "\n";


				if(!empty($theme_options['background_color']))  { $back_style .= ' background-color : '. esc_attr($theme_options['background_color']) .'; '; }
				if(!empty($theme_options['backgroung_img']))    {
					$bg_url = array();
					$bg_url = wp_get_attachment_image_src(intval($theme_options['backgroung_img']), 'full');
					$bg_url = esc_url_raw($bg_url[0]);

					if(isset($theme_options['bg_repeating']) && ($theme_options['bg_repeating'] == 'on')) {
						$back_style .= 'background-image : url(' .$bg_url .'); background-repeat : repeat; ';
					} else {
						$back_style .= 'background-image : url(' .$bg_url .'); background-repeat : no-repeat; background-size:100% 100%; background-size:cover; background-attachment:fixed; ';
					}
				}

				$style_ .= 'body {'. $back_style .'}' . "\n";

				if(!empty($theme_options['container_bg_color']))  {
					$style_ .= '.page-container .container {background-color : '. esc_attr($theme_options['container_bg_color']) . '; } ' . "\n";
				}

				/*Header styles*/
				if (!empty($theme_options['header_bg_color']))   { $style_ .= '.head-container, .head-container.fixed  {background-color : ' .esc_attr($theme_options['header_bg_color']) . '; }' . "\n";  }
				if (!empty($theme_options['header_img']))    {
					$header_url = wp_get_attachment_image_src(intval($theme_options['header_img']), 'full');
					$header_url = esc_url_raw($header_url[0]);
					$style_ .= '.head-container {background-image : url(' .esc_attr($header_url) . '); } ' . "\n";

					if (!empty($theme_options['header_img_size'])){
						if ($theme_options['header_img_size'] == 'full'){
							$style_ .= '.head-container {background-size :cover; background-position:center center;} ' . "\n";
						} else {
							$style_ .= '@media only screen and (max-width:480px){'."\n";
								$style_ .= '.head-container {background-size :300px; background-position:top center;} ' . "\n";
							$style_ .= '}'."\n";
							$style_ .= '@media only screen and (min-width:481px) and (max-width:767px){'."\n";
								$style_ .= '.head-container {background-size :420px; background-position:top center;} ' . "\n";
							$style_ .= '}'."\n";
							$style_ .= '@media only screen and (min-width:768px) and (max-width:959px){'."\n";
								$style_ .= '.head-container {background-size :768px; background-position:top center;} ' . "\n";
							$style_ .= '}'."\n";
							$style_ .= '@media only screen and (min-width:960px){'."\n";
								$style_ .= '.head-container {background-size :960px; background-position:top center;} ' . "\n";
							$style_ .= '}'."\n";
						}
					}
				}
				if (!empty($theme_options['header_height'])) {
					$style_ .= '.head-container {min-height : '.esc_attr($theme_options['header_height']).'px; }' . "\n";
				}
				if (!empty($theme_options['is_fixed_header'])) {
					if (isset($theme_options['is_fixed_header']) && ($theme_options['is_fixed_header'] == 'on')) {
						$style_ .= '.head-container {position : fixed; }' . "\n";
					} else {
						$style_ .= '.head-container {position : relative; }' . "\n";
					}
				}
				/*end of header styles*/


				if (!empty($theme_options['menu_btn_color']))    { $style_ .= '.main-navigation ul li.current_page_item a, .main-navigation ul li.current-menu-ancestor a, .main-navigation ul li.current-menu-item a, .main-navigation ul li.current-menu-parent a, .main-navigation ul li.current_page_parent a {background-color : '.esc_attr($theme_options['menu_btn_color']) . '; }' . "\n";  }
				if (!empty($theme_options['menu_hover_color']))  { $style_ .= '.main-navigation ul li.current_page_item a, .main-navigation ul li.current-menu-ancestor a, .main-navigation ul li.current-menu-item a, .main-navigation ul li.current-menu-parent a, .main-navigation ul li.current_page_parent a {color : '.esc_attr($theme_options['menu_hover_color']) . '; } ' . "\n";  }

				$style_ .= '.main-navigation ul > li:hover>a {' . "\n";
					if (!empty($theme_options['menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				/*styles for dropdown menu*/
				$style_ .= '#masthead .main-navigation ul > li > ul > li > a {' . "\n";
					if (!empty($theme_options['dd_menu_bg_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_bg_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_font_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_font_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				$style_ .= '#masthead .main-navigation ul > li > ul > li:hover > a {' . "\n";
					if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				$style_ .= '#masthead .main-navigation ul > li ul > li.current-menu-item > a {' . "\n";
					if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				$style_ .= '#masthead div .main-navigation ul > li > ul > li > ul a {' . "\n";
					if (!empty($theme_options['dd_menu_bg_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_bg_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_font_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_font_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				$style_ .= '#masthead div .main-navigation ul > li > ul > li > ul li:hover a {' . "\n";
					if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= ' } ' . "\n";

				$style_ .= '#lang-select-block li ul li a{'. "\n";
					if (!empty($theme_options['dd_menu_bg_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_bg_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_font_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_font_color']) . ';  ' . "\n"; }
				$style_ .= '}' . "\n";

				$style_ .= '#lang-select-block li ul li a:hover{'. "\n";
					if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= '}' . "\n";

				$style_ .= '#lang-select-block li ul li.active a{'. "\n";
					if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
					if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
				$style_ .= '}' . "\n";
				/*end of styles for dropdown menu*/

				/*styles for responsive full width menu*/
				if (!empty($theme_options['menu_type_responsive']) && ($theme_options['menu_type_responsive'] == 'full_width')) {
					$style_ .= '.resp_full_width_menu .site-header .menu_wrapper{'. "\n";
						if (!empty($theme_options['dd_menu_bg_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_bg_color']) . '; ' . "\n"; }
					$style_ .= '}' . "\n";
					$style_ .= '.resp_full_width_menu .site-header .menu_wrapper .menu li a{'. "\n";
						if (!empty($theme_options['dd_menu_font_color']))	{ $style_ .= 'color : '.esc_attr($theme_options['dd_menu_font_color']) . ';  ' . "\n"; }
					$style_ .= '}' . "\n";
					$style_ .= '.resp_full_width_menu .site-header .menu_wrapper .menu li.current-menu-item>a,'. "\n";
					$style_ .= '.resp_full_width_menu .site-header .menu_wrapper .menu li.current_page_item>a,'. "\n";
					$style_ .= '.resp_full_width_menu .site-header .menu_wrapper .menu a:hover{'. "\n";
						if (!empty($theme_options['dd_menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['dd_menu_btn_color']) . '; ' . "\n"; }
						if (!empty($theme_options['dd_menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['dd_menu_hover_color']) . ';  ' . "\n"; }
					$style_ .= '}' . "\n";
				}
				/*end of styles for responsive full width menu*/

				$style_ .= '#header_language_select ul li.current > a { color : '.esc_attr($theme_options['menu_font_color']). '; } ' . "\n";
				if (!empty($theme_options['menu_bg_color'])) { $style_ .= '#header_language_select { background-color : '.esc_attr($theme_options['menu_bg_color']) . '; } ' . "\n";  }

				$style_ .= '#header_language_select ul li.current:hover > a { ' . "\n";
					if (!empty($theme_options['menu_btn_color']))    { $style_ .= 'background-color : '. esc_attr($theme_options['menu_btn_color']) . ';' . "\n"; }
					if (!empty($theme_options['menu_hover_color']))  { $style_ .= 'color : '.esc_attr($theme_options['menu_hover_color']) . ';' . "\n"; }
				$style_ .= '} ' . "\n";

				/*Add Custom Colors to theme*/
				if (!empty($theme_options['p_font_color']))  	    { $style_ .= 'body {color : '. esc_attr($theme_options['p_font_color']) .'; } ' . "\n"; }
				if (!empty($theme_options['widgets_sep_color']))  {
					$style_ .= '#page .container #secondary .widget h3.widget-title, #page .container #secondary .widget h1.widget-title, header.post-header .post-title  {border-color : '. esc_attr($theme_options['widgets_sep_color']) .'; } ' . "\n";
					$style_ .= 'body.single-product #page .related.products h2  {border-bottom-color : '. esc_attr($theme_options['widgets_sep_color']) .'; } ' . "\n";
				}
				if (!empty($theme_options['a_font_color']))   		{

					$a_font_color = esc_attr($theme_options['a_font_color']);

					$style_ .= 'a {color : '.$a_font_color.'; }';
					$style_ .= '#page .container #secondary>.widget_nav_menu>div>ul>li ul>li>a:before {color : '.$a_font_color.'; }';
					$style_ .= '#page .container #secondary .widget ul li.cat-item a:before {color : '.$a_font_color.'; }';
					$style_ .= 'html[dir="rtl"] #page .container #secondary>.widget_nav_menu>div>ul>li ul>li>a:after {color : '. $a_font_color .'; }';
					$style_ .= 'html[dir="rtl"] #page .container #secondary .widget ul li.cat-item a:after {color : '. $a_font_color .'; }';
				}

				if (!empty($theme_options['a_hover_font_color']))   {
					$style_ .= 'a:hover   {color : '. esc_attr($theme_options['a_hover_font_color']) .'; } '  . "\n";

					$style_ .= '#page .container #secondary>.widget_nav_menu li.current-menu-item>a {color : '. esc_attr($theme_options['a_hover_font_color']) .'; } ';

					$style_ .= '#page .container #secondary>.widget_nav_menu>div>ul>li ul>li>a:hover:before,
								#page .container #secondary>.widget_nav_menu>div>ul>li ul>li.current-menu-item>a:before,
								#page .container #secondary>.widget_nav_menu>div>ul>li ul>li.current-menu-item>a:hover:before{color : '. esc_attr($theme_options['a_hover_font_color']) .'; }';

					$style_ .= '#page .container #secondary .widget ul li.current-cat>a,
								#page .container #secondary .widget ul li.cat-item ul li.current-cat a:before,
								#page .container #secondary .widget ul li.cat-item a:hover:before{color : '. esc_attr($theme_options['a_hover_font_color']) .'; }';

					$style_ .= 'html[dir="rtl"] #page .container #secondary>.widget_nav_menu>div>ul>li ul>li>a:hover:after,';
					$style_ .= 'html[dir="rtl"] #page .container #secondary>.widget_nav_menu>div>ul>li ul>li.current-menu-item>a:after,';
					$style_ .= 'html[dir="rtl"] #page .container #secondary>.widget_nav_menu>div>ul>li ul>li.current-menu-item>a:hover:after{color : '. esc_attr($theme_options['a_hover_font_color']) .'; } '  . "\n";

					$style_ .= 'html[dir="rtl"] #page .container #secondary .widget ul li.current-cat>a,
								html[dir="rtl"] #page .container #secondary .widget ul li.current-cat>a:after,
								html[dir="rtl"] #page .container #secondary .widget ul li.cat-item a:hover:after{color : '. esc_attr($theme_options['a_hover_font_color']) .'; } ';
				}

				if (!empty($theme_options['a_focus_font_color']))   { $style_ .= 'a:focus   {color : '. esc_attr($theme_options['a_focus_font_color']) .'; } '  . "\n"; }
				if (!empty($theme_options['a_active_font_color']))  { $style_ .= 'a:active  {color : '. esc_attr($theme_options['a_active_font_color']) .'; } ' . "\n"; }

				if (!empty($theme_options['date_of_post_b_color']))  {
					$style_ .= '.blog_post .date_of_post  {background : none repeat scroll 0 0 '. esc_attr($theme_options['date_of_post_b_color']) .'; } ' . "\n";
				}

				if (!empty($theme_options['date_of_post_f_color']))  {
					$style_ .= '.blog_post .date_of_post  {color : '. esc_attr($theme_options['date_of_post_f_color']) .'; } ' . "\n";
				}

				$woo_style_ .= '.num_of_product_cart {border-color: '. esc_attr($theme_options['menu_btn_color']) . '; }  ' . "\n";

				if (!empty($theme_options['btn_color'])) {
					$btn_color = esc_attr($theme_options['btn_color']);

					$style_		 .= 'button, input[type="button"], input[type="submit"], input[type="reset"]{background-color : '.$btn_color.' !important; } ';
					$style_		 .= 'body a.btn.btn-primary, body button.btn.btn-primary, body input[type="button"].btn.btn-primary , body input[type="submit"].btn.btn-primary {background-color : '.$btn_color.' !important; }';
					$woo_style_  .= '.woocommerce table.my_account_orders .order-actions .button, .woocommerce-page table.my_account_orders .order-actions .button{background-color : '.$btn_color.' !important; }';
					$style_ 	 .= '.nav-links.shop .pages-links .page-numbers, .nav-links.shop .nav-next a, .nav-links.shop .nav-previous a{background-color : '.$btn_color.' !important; }';
				}

				if (!empty($theme_options['btn_active_color'])) {
					$btn_active_color = esc_attr($theme_options['btn_active_color']);

					$style_ .= 'button:hover, button:active, button:focus{background-color : '.$btn_active_color.' !important; }';
					$style_ .= 'input[type="button"]:hover, input[type="button"]:active, input[type="button"]:focus{background-color : '.$btn_active_color.' !important; }';
					$style_ .= 'input[type="submit"]:hover, input[type="submit"]:active, input[type="submit"]:focus{background-color : '.$btn_active_color.' !important; }';
					$style_ .= 'input[type="reset"]:hover, input[type="reset"]:active, input[type="reset"]:focus{background-color : '.$btn_active_color.' !important; }';
					$style_	.= 'body a.btn.btn-primary:hover, body button.btn.btn-primary:hover, body input[type="button"].btn.btn-primary:hover , body input[type="submit"].btn.btn-primary:hover {background-color : '.$btn_active_color.' !important; }';
					$woo_style_  .= '.woocommerce table.my_account_orders .order-actions .button:hover, .woocommerce-page table.my_account_orders .order-actions .button:hover{background-color : '.$btn_active_color.' !important; }';
					$style_ .= '.nav-links.shop .pages-links .page-numbers:hover, .nav-links.shop .nav-next a:hover, .nav-links.shop .nav-previous a:hover, .nav-links.shop .pages-links .page-numbers.current{background-color : '.$btn_active_color.' !important; }';
				}

				/*social icons styles*/
				if (!empty($theme_options['soc_icon_bg_color'])) {
					$style_ .= '.social-icon>a>i{background:'.$theme_options['soc_icon_bg_color'].'}' . "\n";
				}
				if (!empty($theme_options['soc_icon_color'])) {
					$style_ .= '.social-icon>a>i{color:'.$theme_options['soc_icon_color'].'}' . "\n";
				}


				/*Woocommerce styles*/
				if (class_exists('woocommerce')){

					if (!empty($theme_options['woo_shop_sidebar'])){
						$shop_sidebar_template = $theme_options['woo_shop_sidebar'];
						if ($shop_sidebar_template == 3){	/*right sidebar template*/
							$woo_style_ .= '#page .container .woo-loop-content{float:left}'."\n";
							$woo_style_ .= '#page .container .woo-loop-sidebar{float:right}'."\n";
							$woo_style_ .= '#page .container .woo-loop-sidebar #secondary{float:right}'."\n";
							$woo_style_ .= '.woocommerce .woocommerce-ordering, .woocommerce-page .woocommerce-ordering{float:left}'."\n";
						} else {
							$woo_style_ .= '#page .container .woo-loop-content{float:right}'."\n";
							$woo_style_ .= '#page .container .woo-loop-sidebar{float:left}'."\n";
							$woo_style_ .= '#page .container .woo-loop-sidebar #secondary{float:left}'."\n";
							$woo_style_ .= '.woocommerce .woocommerce-ordering, .woocommerce-page .woocommerce-ordering{float:right}'."\n";
						}
					}

					if (!empty($theme_options['woo_product_sidebar'])){
						$product_sidebar_template = $theme_options['woo_product_sidebar'];
						if ($product_sidebar_template == 3){	/*right sidebar template*/
							$woo_style_ .= '.single-product #page .container .woo-loop-content{float:left}'."\n";
							$woo_style_ .= '.single-product #page .container .woo-loop-sidebar{float:right}'."\n";
							$woo_style_ .= '.single-product #page .container .woo-loop-sidebar #secondary{float:right}'."\n";
						} else {
							$woo_style_ .= '.single-product #page .container .woo-loop-content{float:right}'."\n";
							$woo_style_ .= '.single-product #page .container .woo-loop-sidebar{float:left}'."\n";
							$woo_style_ .= '.single-product #page .container .woo-loop-sidebar #secondary{float:left}'."\n";
						}
					}

					/*price color*/
					if (!empty($theme_options['a_hover_font_color']))   {
						$woo_style_ .= '.woocommerce ul.products li.product .price ,
										.woocommerce-page ul.products li.product .price,
										body.woocommerce div.product span.price,
										body.woocommerce-page div.product span.price,
										body.woocommerce #content div.product span.price,
										body.woocommerce-page #content div.product span.price,
										body.woocommerce div.product p.price,
										body.woocommerce-page div.product p.price,
										body.woocommerce #content div.product p.price,
										body.woocommerce-page #content div.product p.price{color : '. esc_attr($theme_options['a_hover_font_color']) .'; }';
					}

					/*buttons color*/
					if (!empty($theme_options['btn_color'])) {
						$btn_color = esc_attr($theme_options['btn_color']);

						$woo_style_ .= '.woocommerce .woocommerce-message, .woocommerce-page .woocommerce-message{border-top:3px solid '.$btn_color.';}';
						$woo_style_ .= '.woocommerce .woocommerce-info, .woocommerce-page .woocommerce-info{border-top:3px solid '.$btn_color.';}';
						$woo_style_ .= '.woocommerce .woocommerce-message:before, .woocommerce-page .woocommerce-message:before{background-color:'.$btn_color.';}';
						$woo_style_ .= '.woocommerce .woocommerce-info:before, .woocommerce-page .woocommerce-info:before{background-color:'.$btn_color.';}';
						$woo_style_ .= '.single-product .woocommerce-message .button{background-color:'.$btn_color.';}';
					}

					/*buttons hover color*/
					if (!empty($theme_options['btn_active_color']))
					$woo_style_ .= '.single-product .woocommerce-message .button:hover{background-color:'.esc_attr($theme_options['btn_active_color']).';}';

					if (!empty($theme_options['woo_sale_price_color'])) {
						$color_rgba = fruitful_hex2rgb($theme_options['woo_sale_price_color']);
						$color = $color_rgba['red'] . ',' . $color_rgba['green'] . ',' . $color_rgba['blue'];
						$woo_style_ .= '.woocommerce ul.products li.product .price del, .woocommerce-page ul.products li.product .price del {color:rgba('.$color.',.5); }';
					}

					if (!empty($theme_options['woo_rating_color_regular'])) {
						$woo_style_ .= '.woocommerce .star-rating, .woocommerce-page .star-rating,
										.woocommerce p.stars a.star-1,
										.woocommerce p.stars a.star-2,
										.woocommerce p.stars a.star-3,
										.woocommerce p.stars a.star-4,
										.woocommerce p.stars a.star-5,
										.woocommerce-page p.stars a.star-1,
										.woocommerce-page p.stars a.star-2,
										.woocommerce-page p.stars a.star-3,
										.woocommerce-page p.stars a.star-4,
										.woocommerce-page p.stars a.star-5 {
											color:' .esc_attr($theme_options['woo_rating_color_regular']). '; }';
					}


					if (!empty($theme_options['woo_rating_color_active'])) {
						$woo_style_ .= '.woocommerce p.stars a.star-1:hover,
										.woocommerce p.stars a.star-2:hover,
										.woocommerce p.stars a.star-3:hover,
										.woocommerce p.stars a.star-4:hover,
										.woocommerce p.stars a.star-5:hover,
										.woocommerce-page p.stars a.star-1:hover,
										.woocommerce-page p.stars a.star-2:hover,
										.woocommerce-page p.stars a.star-3:hover,
										.woocommerce-page p.stars a.star-4:hover,
										.woocommerce-page p.stars a.star-5:hover,
										.woocommerce .star-rating:hover, .woocommerce-page .star-rating:hover { color:' .esc_attr($theme_options['woo_rating_color_active']). '; }';
					}

				}

				if (class_exists('BuddyPress')){
					if (!empty($theme_options['btn_color'])) {
						$style_ .= '#buddypress input[type=submit]{background-color : '.esc_attr($theme_options['btn_color']).' !important; } ' . "\n";
					}
					if (!empty($theme_options['btn_active_color'])) {
						$style_ .= '#buddypress input[type=submit]:hover, #buddypress input[type=submit]:active, #buddypress input[type=submit]:focus{background-color : '.esc_attr($theme_options['btn_active_color']).' !important; } ' . "\n";
					}
				}

			} else {
				$style_ .= 'body {font-family:Open Sans, sans-serif}' . "\n";
			}
		}

		if (!empty($theme_options['custom_css'])) {
			$style_ .= wp_kses_stripslashes($theme_options['custom_css']) . "\n";
		}

		wp_add_inline_style( 'main-style', fruitful_compress_code($style_));
		if ($woo_style_ != '') {
			wp_add_inline_style( 'woo-style', fruitful_compress_code($woo_style_));
		}
	}
	add_action('wp_enqueue_scripts', 'fruitful_get_responsive_style', 99);
}

if ( ! function_exists( 'fruitful_get_sliders' ) ) {
	function fruitful_get_sliders() {
		global $post;
		$prefix = '_fruitful_';
		$theme_options  = fruitful_ret_options("fruitful_theme_options");
		$front_page_id  = get_option('page_on_front');
		$blog_page_id   = get_option('page_for_posts ');


		if (is_page() && !is_front_page() && !is_home()) {
			$slider_layout  = get_post_meta( $post->ID, $prefix . 'slider_layout', true);
		} elseif(!is_front_page() && is_home() && ($blog_page_id != 0)) {
			/*Only for blog posts loop*/
			$slider_layout  = get_post_meta( $blog_page_id, $prefix . 'slider_layout', true);
		} elseif (is_front_page()) {
			$slider_layout  = get_post_meta( $front_page_id, $prefix . 'slider_layout', true);
		}

		if ($slider_layout){
			if (!empty($theme_options['select_slider'])) {
				if ($theme_options['select_slider'] == "1") {
					echo fruitful_get_slider_layout_flex();
				} else if ($theme_options['select_slider'] == "2") {
					echo fruitful_get_slider_layout_nivo();
				}
			}
		}
	}
}

/* Woocommerce functions */
if (class_exists('Woocommerce')) {
	/*change number of products per row shop page*/
	add_filter('loop_shop_columns', 'fruitful_loop_columns');
	if (!function_exists('fruitful_loop_columns')) {
		function fruitful_loop_columns() {
			$theme_options = fruitful_ret_options("fruitful_theme_options");
			if (!empty($theme_options['shop_num_row'])){
				return esc_attr($theme_options['shop_num_row']);
			} else {
				return '4';
			}
		}
	}

	/*change number of products per page shop page*/
	add_filter( 'loop_shop_per_page', 'fruitful_loop_shop_per_page', 20);
	if (!function_exists('fruitful_loop_shop_per_page')) {
		function fruitful_loop_shop_per_page(){
			$theme_options 	   = fruitful_ret_options("fruitful_theme_options");
			$woo_shop_num_prod = get_option('posts_per_page');
			if (!empty($theme_options['woo_shop_num_prod'])) $woo_shop_num_prod  = esc_attr($theme_options['woo_shop_num_prod']);
			return $woo_shop_num_prod;
		}
	}

	/*remove sidebar from all woocommerce pages except shop page*/
	add_action( 'wp', 'init' );
	function init() {
		if ( !is_shop() && !is_product_category()) {
			remove_action( 'woocommerce_sidebar', 'woocommerce_get_sidebar', 10);
		}
	}

	/*remove woocommerce sidebar on some pages*/
	add_action('template_redirect', 'fruitful_remove_woo_sidebar');
	if (!function_exists('fruitful_remove_woo_sidebar')) {
		function fruitful_remove_woo_sidebar() {
			if (fruitful_get_woo_sidebar() == 1){
				remove_action('woocommerce_sidebar', 'woocommerce_get_sidebar');
			}
		}
	}

	/*check is woocommerce sidebar will be hidden*/
	if (!function_exists('fruitful_get_woo_sidebar')) {
		function fruitful_get_woo_sidebar() {
			$woo_sidebar = 2;


			if ( is_shop() || is_product_category() ) {
				$theme_options = fruitful_ret_options("fruitful_theme_options");
				if (!empty($theme_options['woo_shop_sidebar'])){
					$woo_sidebar =  esc_attr($theme_options['woo_shop_sidebar']);
				}
			}
			if ( is_product() ) {
				$theme_options = fruitful_ret_options("fruitful_theme_options");
				if (!empty($theme_options['woo_product_sidebar'])){
					$woo_sidebar =  esc_attr($theme_options['woo_product_sidebar']);
				}
			}
			return $woo_sidebar;
		}
	}

	/*rewrite pagenavi for woocommerce*/
	remove_action('woocommerce_pagination', 'woocommerce_pagination', 10);
	add_action( 'woocommerce_pagination', 'woocommerce_pagination', 10);
	if ( ! function_exists( 'woocommerce_pagination' ) ) {
		function woocommerce_pagination() {
			fruitful_wp_corenavi();
		}
	}

	/*change title in tabs on single product page*/
	add_filter('woocommerce_product_description_heading','fruitful_product_description_heading');
	function fruitful_product_description_heading() {
	   return '';
	}

	/*4 cross products for cart*/
	remove_action( 'woocommerce_cart_collaterals', 'woocommerce_cross_sell_display' );
	add_action( 'woocommerce_cart_collaterals', 'fruitful_woocommerce_cross_sell_display' );
	if ( ! function_exists( 'fruitful_woocommerce_cross_sell_display' ) ) {
		function fruitful_woocommerce_cross_sell_display() {

			if ( ! defined( 'ABSPATH' ) ) exit; // Exit if accessed directly
			global $woocommerce_loop, $woocommerce, $product;
			$crosssells = $woocommerce->cart->get_cross_sells();
			if ( sizeof( $crosssells ) == 0 ) return;
			$meta_query = $woocommerce->query->get_meta_query();
			$args = array(
				'post_type'           => 'product',
				'ignore_sticky_posts' => 1,
				'posts_per_page'      => apply_filters( 'woocommerce_cross_sells_total', 4 ),
				'no_found_rows'       => 1,
				'orderby'             => 'rand',
				'post__in'            => $crosssells,
				'meta_query'          => $meta_query
			);
			$products = new WP_Query( $args );
			$woocommerce_loop['columns'] 	= apply_filters( 'woocommerce_cross_sells_columns', 4 );
			if ( $products->have_posts() ) : ?>
				<div class="cross-sells">
					<h2><?php _e( 'You may be interested in&hellip;', 'woocommerce' ) ?></h2>
					<?php woocommerce_product_loop_start(); ?>
						<?php while ( $products->have_posts() ) : $products->the_post(); ?>
							<?php woocommerce_get_template_part( 'content', 'product' ); ?>
						<?php endwhile; // end of the loop. ?>
					<?php woocommerce_product_loop_end(); ?>
				</div>
			<?php endif;
			wp_reset_query();
		}
	}

	/*4 of related products per row*/
	remove_action( 'woocommerce_after_single_product_summary', 'woocommerce_output_related_products', 20 );
	add_action( 'woocommerce_after_single_product_summary', 'fruitful_after_single_product_summary', 20 );
	if ( ! function_exists( 'fruitful_after_single_product_summary' ) ) {
		function fruitful_after_single_product_summary() {
			fruitful_woocommerce_related_products(4, 4);
		}
	}

	/*function for change posts per row and number of related products on single product page*/
	if ( ! function_exists( 'fruitful_woocommerce_related_products' ) ) {
		function fruitful_woocommerce_related_products($posts_per_page = 2, $columns = 2, $orderby = false){
			$args = array(
				'posts_per_page' => $posts_per_page,
				'columns'        => $columns,
				'orderby'        => $orderby,
			);
			$defaults = array(
				'posts_per_page' => 2,
				'columns'        => 2,
				'orderby'        => 'rand'
			);
			$args = wp_parse_args( $args, $defaults );
			wc_get_template( 'single-product/related.php', $args );
		}
	}

	/*Update cart contents update when products are added to the cart via AJAX */
	add_filter('add_to_cart_fragments', 'fruitful_woocommerce_header_add_to_cart_fragment');
	if ( ! function_exists( 'fruitful_woocommerce_header_add_to_cart_fragment' ) ) {
		function fruitful_woocommerce_header_add_to_cart_fragment( $fragments ) {
			global $woocommerce;
			ob_start();
			?>
			<a href="<?php echo get_permalink( woocommerce_get_page_id( 'cart' ) ); ?>" class="cart-contents">
				<div class="cart_image"></div>
				<span class="num_of_product_cart"><?php global $woocommerce;
				 echo sprintf(_n('%d ', '%d ', $woocommerce->cart->cart_contents_count, 'fruitful'), $woocommerce->cart->cart_contents_count); ?> </span>
			</a>
			<?php
			$fragments['a.cart-contents'] = ob_get_clean();
			return $fragments;
		}
	}
}

if ( ! function_exists( 'fruitful_custom_css_and_slider_scripts' ) ) {
function fruitful_custom_css_and_slider_scripts() {
	echo '<script type="text/javascript">';
		echo 'jQuery(document).ready(function($) { ';
				fruitful_get_sliders();
		echo '});';
	echo '</script>';
}
	add_action('wp_head', 'fruitful_custom_css_and_slider_scripts', 25);
}

if ( ! function_exists( 'fruitful_entry_meta' ) ) {
function fruitful_entry_meta() {
?>
	<span class="author-link author"><a href="<?php print esc_url( get_author_posts_url( get_the_author_meta( 'ID' ))); ?>"><?php print get_the_author(); ?></a></span>
	<?php if ( 'post' == get_post_type() ) : // Hide category and tag text for pages on Search ?>
	<?php
		/* translators: used between list items, there is a space after the comma */
		 $categories_list = get_the_category_list( __( ', ', 'fruitful' ) );
	if ( $categories_list && fruitful_categorized_blog() ) : ?>
		<span class="cat-links">
			<?php printf( __( 'Posted in %1$s', 'fruitful' ), $categories_list ); ?>
		</span>
	<?php endif; // End if categories ?>

	<?php
		/* translators: used between list items, there is a space after the comma */
		$tags_list = get_the_tag_list( '', __( ', ', 'fruitful' ) );
		if ( $tags_list ) :
	?>
		<span class="tag-links">
			<?php // printf( __( 'Tagged %1$s', 'fruitful' ), $tags_list ); ?>
			<?php echo $tags_list; ?>
		</span>
		<?php endif; // End if $tags_list ?>
	<?php endif; // End if 'post' == get_post_type() ?>
<?php
}
}

if ( ! function_exists( 'fruitful_entry_date' ) ) {
function fruitful_entry_date( $echo = true ) {
	if ( has_post_format( array( 'chat', 'status' ) ) )
		$format_prefix = _x( '%1$s on %2$s', '1: post format name. 2: date', 'fruitful' );
	else
		$format_prefix = '%2$s';

	$date = sprintf( '<span class="date"><a href="%1$s" title="%2$s" rel="bookmark"><time class="entry-date" datetime="%3$s">%4$s</time></a></span>',
		esc_url( get_permalink() ),
		esc_attr( sprintf( __( 'Permalink to %s', 'fruitful' ), the_title_attribute( 'echo=0' ) ) ),
		esc_attr( get_the_date( 'c' ) ),
		esc_html( sprintf( $format_prefix, get_post_format_string( get_post_format() ), get_the_date() ) )
	);

	if ($echo ) echo $date;
	return $date;
}
}

if ( ! function_exists( 'fruitful_customize_register' ) ) {
function fruitful_customize_register( $wp_customize ) {
	class Fruitful_Theme_Options_Button_Control extends WP_Customize_Control {
		public $type = 'button_link_control';

		public function render_content() {
			?>
				<label>
					<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
					<input class="button button-primary save link_to_options" type="button" value="Theme Options" onclick="javascript:location.href='<?php echo esc_url(admin_url('admin.php?page=theme_options')); ?>'"/>
				</label>
			<?php
		}
	}

	$wp_customize->get_setting( 'blogname' )->transport         = 'postMessage';
	$wp_customize->get_setting( 'blogdescription' )->transport  = 'postMessage';
	$wp_customize->get_setting( 'header_textcolor' )->transport = 'postMessage';

	$wp_customize->remove_section( 'colors');
	$wp_customize->remove_section( 'header_image');
	$wp_customize->remove_section( 'background_image');

	$wp_customize->add_section('fruitful_themeoptions_link', array(
							   'title' => __('Fruitful Theme Options', 'fruitful'),
							   'priority' => 10,
							));

	$wp_customize->add_setting( 'themeoptions_button_control', array('sanitize_callback'=>'fruitful_theme_options_validate') );
	$wp_customize->add_control(
		new Fruitful_Theme_Options_Button_Control (
			$wp_customize,
			'button_link_control',
			array(
				'label' 	=> __('Advanced theme settings', 'fruitful'),
				'section' 	=> 'fruitful_themeoptions_link',
				'settings' 	=> 'themeoptions_button_control',
			)
		)
	);
}
add_action( 'customize_register', 'fruitful_customize_register' );
}

if ( ! function_exists( 'fruitful_theme_options_validate' ) ) {
function fruitful_theme_options_validate($value) {
	return $value;
}
}

if ( ! function_exists( 'fruitful_customize_preview_js' ) ) {
function fruitful_customize_preview_js() {
	wp_enqueue_script( 'fruitful-customizer', get_template_directory_uri() . '/js/theme-customizer.js', array( 'customize-preview' ), '20130226', true );
}
add_action( 'customize_preview_init', 'fruitful_customize_preview_js' );
}

if ( ! function_exists( 'fruitful_metadevice' ) ) {
function fruitful_metadevice() {
	$browser = '';
	$browser_ip	= strpos($_SERVER['HTTP_USER_AGENT'],"iPhone");
	$browser_an	= strpos($_SERVER['HTTP_USER_AGENT'],"Android");
	$browser_ipad = strpos($_SERVER['HTTP_USER_AGENT'],"iPad");
	if ($browser_ip  	== true) { $browser = 'iphone';  }
	if ($browser_an		== true) { $browser = 'android'; }
	if ($browser_ipad 	== true) { $browser = 'ipad'; }

	if($browser == 'iphone') 	{ echo '<meta name="viewport" content="width=480, maximum-scale=1, user-scalable=0"/>';  }
    if($browser == 'android') 	{ echo '<meta name="viewport" content="target-densitydpi=device-dpi, width=device-width" />'; }
	if($browser == 'ipad') 		{ echo '<meta name="viewport" content="width=768px, minimum-scale=1.0, maximum-scale=1.0" />'; }
}
}
add_action( 'wp_head', 'fruitful_metadevice' );

if ( ! function_exists( 'fruitful_esc_content_pbr' ) ) {
	function fruitful_esc_content_pbr($content = null) {
		 $content = preg_replace( '%<p>&nbsp;\s*</p>%', '', $content );
		 $Old     = array( '<br />', '<br>' );
		 $New     = array( '','' );
		 $content = str_replace( $Old, $New, $content );
		 return $content;
	}
}

if ( ! function_exists( 'fruitful_get_class_pos' ) ) {
	function fruitful_get_class_pos($index)  {
		if ($index == 0) { 		$pos_class = 'left-pos'; 	}
		else if ($index == 1) {	$pos_class = 'center-pos';	}
		else {  $pos_class = 'right-pos'; }

		return esc_attr($pos_class);
	}
}

if ( ! function_exists( 'fruitful_kses_data' ) ) {
function fruitful_kses_data($text = null) {
	$allowed_tags = wp_kses_allowed_html( 'post' );
	return wp_kses($text, $allowed_tags);
}
}

if ( ! function_exists( 'fruitful_get_languages_list' ) ) {
	function fruitful_get_languages_list(){
		$theme_options = fruitful_ret_options("fruitful_theme_options");
		if( function_exists('icl_get_languages') && $theme_options['is_wpml_ready'] == 'on' ){
			$languages = icl_get_languages('skip_missing=0');
			if(!empty($languages)){
				echo '<div id="header_language_select"><ul id="lang-select-block">';
				foreach($languages as $l){
					if($l['active']) {
						echo '<li class="current">';
							echo '<a class="'.$l['language_code'].'" href="'.$l['url'].'" onclick="return false">';
								echo $l['language_code'];
							echo '</a>';
						echo '<ul id="lang-select-popup">';

							echo '<li class="active">';
								echo '<a class="'.$l['language_code'].'" href="'.$l['url'].'" onclick="return false">';
									echo $l['native_name'];
								echo '</a>';
							echo '</li>';
					}

				}
				foreach($languages as $l){
					if(!($l['active'])) {
							echo '<li class="unactive">';
							echo '<a class="'.$l['language_code'].'" href="'.$l['url'].'">';
								echo $l['native_name'];
							echo '</a></li>';
					}
				}
						echo '</ul>';
					echo '</li>';
				echo '</ul></div>';
			}
		}
	}
}

if ( ! function_exists( 'fruitful_wp_corenavi' ) ) {
	function fruitful_wp_corenavi() {
		global $wp_query,
				$wp_rewrite;
		$next_label = $prev_label = '';
		if (wp_is_mobile()) {
			$next_label = __(' &laquo; ','fruitful');
			$prev_label = __(' &raquo; ','fruitful');
		} else {
			$next_label = __('&laquo; Previous Page','fruitful');
			$prev_label = __('Next Page &raquo;','fruitful');
		}

		$pages = '';
		$max = $wp_query->max_num_pages;
		if (!$current = get_query_var('paged')) {
			$current = 1;
		}

		$a['base']    = str_replace(999999999, '%#%', get_pagenum_link(999999999));
		$a['total']   = $max;
		$a['current'] = $current;

		$total = 0;    //1 - display the text "Page N of N", 0 - not display
		$a['mid_size'] = 2;  //how many links to show on the left and right of the current
		$a['end_size'] = 1;  //how many links to show in the beginning and end
		$a['prev_text'] = '';  //text of the "Previous page" link
		$a['next_text'] = '';  //text of the "Next page" link

		if  ($max > 1) {
			echo '<div class="pagination nav-links shop aligncenter">';
		}
		if  ($total == 1 && $max > 1) {
			$pages = '<span class="pages">Page ' . $current . ' of ' . $max . '</span>'."\r\n";
		}
		echo '<div class="nav-previous ">'; previous_posts_link($next_label); echo '</div>';
			echo '<div class="pages-links">';
				echo $pages . paginate_links($a);
			echo '</div>';
		echo '<div class="nav-next">';  next_posts_link($prev_label); echo '</div>';
		if ($max > 1) {
			echo '</div>';
		}
	}
}

/*rewrite get_product_search_form() function*/
if ( ! function_exists( 'fruitful_get_product_search_form' ) ) {
	function fruitful_get_product_search_form(){
		?>
		<form role="search" method="get" id="searchform" action="<?php echo esc_url( home_url( '/'  ) ); ?>">
			<div>
				<input type="text" value="<?php echo get_search_query(); ?>" name="s" id="s" placeholder="<?php _e( 'Search for products', 'woocommerce' ); ?>" />
				<input type="submit" id="searchsubmit" value="<?php echo esc_attr__( 'Search' ); ?>" />
				<input type="hidden" name="post_type" value="product" />
			</div>
		</form>
		<?php
	}
}

if ( ! function_exists( 'fruitful_is_woo_sidebar' ) ) {
	function fruitful_is_woo_sidebar() {
		$is_sidebar = true;
		if (class_exists('Woocommerce')) {
			if (is_cart() || is_checkout() || is_account_page()) {
				 $is_sidebar = false;
			}
		}
		return $is_sidebar;
	}
}

if(!function_exists('fruitful_is_blog')){
	function fruitful_is_blog () {
		global  $post;
		$posttype = get_post_type($post );
		return ( ((is_archive()) || (is_author()) || (is_category()) || (is_home()) || (is_single()) || (is_tag())) && ( $posttype == 'post')  ) ? true : false ;
	}
}

if(!function_exists('fruitful_is_latest_posts_page')){
	function fruitful_is_latest_posts_page () {
		global  $post;
		$blog_id = get_option('page_for_posts', true);
		return ( is_home() && is_front_page() && ($post->ID != $blog_id) ) ? true : false ;
	}
}

if ( ! function_exists( 'fruitful_get_content_with_custom_sidebar' ) ) {
	function fruitful_get_content_with_custom_sidebar($curr_sidebar = null) {
		global $post;
		function get_content_part() {
			global $post;
			?>
			<div id="primary" class="content-area">
				<div id="content" class="site-content" role="main">
			<?php
				/* Start the Loop */
				$page_on_front  = get_option('page_on_front');
				$page_for_posts = get_option('page_for_posts');

				if (is_page() && !empty($page_on_front) &&  !empty($page_for_posts) && ($page_on_front == $page_for_posts)) {
					echo '<div class="alert alert-danger"><strong>'.__("Front page displays Error.", 'fruitful').'</strong> '.__('Select different pages!', 'fruitful').'</div>';
				} else {
					if (!is_archive()) {
						if (is_home()) {
							if ( have_posts() ) :
								/* The loop */
								while ( have_posts() ) : the_post();
									get_template_part( 'content', get_post_format() );
								endwhile;
								fruitful_content_nav( 'nav-below' );
							else :
								get_template_part( 'no-results', 'index' );
							endif;
						} else {
							if ( have_posts() ) {
								while ( have_posts() ) : the_post();
									if (is_page() && !is_front_page() && !is_home()) {
										get_template_part( 'content', 'page' );

										if (fruitful_state_page_comment()) {
											comments_template( '', true );
										}
									} else if (is_single()) {
										get_template_part( 'content', get_post_format() );
										fruitful_content_nav( 'nav-below' );

										if (fruitful_state_post_comment()) {
											if ( comments_open() || '0' != get_comments_number() ) comments_template();
										}
									} else if (is_front_page())	{
										get_template_part( 'content', 'page' );
									}
							   endwhile;
							}
						}
					} else {
						?>
							<section id="primary" class="content-area">
								<div id="content" class="site-content" role="main">

								<?php if ( have_posts() ) : ?>
										<header class="page-header">
											<h1 class="page-title">
												<?php
													if ( is_category() ) {
														printf( __( 'Category Archives: %s', 'fruitful' ), '<span>' . single_cat_title( '', false ) . '</span>' );

													} elseif ( is_tag() ) {
														printf( __( 'Tag Archives: %s', 'fruitful' ), '<span>' . single_tag_title( '', false ) . '</span>' );

													} elseif ( is_author() ) {
														the_post();
														printf( __( 'Author Archives: %s', 'fruitful' ), '<span class="vcard"><a class="url fn n" href="' . get_author_posts_url( get_the_author_meta( "ID" ) ) . '" title="' . esc_attr( get_the_author() ) . '" rel="me">' . get_the_author() . '</a></span>' );
														rewind_posts();

													} elseif ( is_day() ) {
														printf( __( 'Daily Archives: %s', 'fruitful' ), '<span>' . get_the_date() . '</span>' );

													} elseif ( is_month() ) {
														printf( __( 'Monthly Archives: %s', 'fruitful' ), '<span>' . get_the_date( 'F Y' ) . '</span>' );

													} elseif ( is_year() ) {
														printf( __( 'Yearly Archives: %s', 'fruitful' ), '<span>' . get_the_date( 'Y' ) . '</span>' );

													} else {
														_e( 'Archives', 'fruitful' );

													}
												?>
											</h1>
											<?php
												if ( is_category() ) {
													$category_description = category_description();
													if ( ! empty( $category_description ) )
														echo apply_filters( 'category_archive_meta', '<div class="taxonomy-description">' . $category_description . '</div>' );

												} elseif ( is_tag() ) {
													$tag_description = tag_description();
													if ( ! empty( $tag_description ) )
														echo apply_filters( 'tag_archive_meta', '<div class="taxonomy-description">' . $tag_description . '</div>' );
												}
											?>
										</header><!-- .page-header -->

										<?php /* Start the Loop */
										while ( have_posts() ) : the_post();
											get_template_part( 'content', get_post_format() );
										endwhile;
										fruitful_content_nav( 'nav-below' );

									else :

										get_template_part( 'no-results', 'archive' );

									endif; ?>

								</div><!-- #content .site-content -->
							</section><!-- #primary .content-area -->
						<?php
					}
				}
			?>
				</div>
			</div>
		<?php
		}

		function get_html_custom_post_template($content_class, $sidebar_class, $curr_sidebar, $content_type) {
			global $post;
			$is_sidebar = true;
			$is_sidebar = fruitful_is_woo_sidebar();

			if ($content_type == 0) { ?>
				<?php get_content_part(); ?>
		<?php } else if ($content_type == 1) { ?>

				<div class="eleven columns <?php echo $content_class;?>"><?php get_content_part(); ?> </div>

				<?php if ($is_sidebar && is_page()) { ?>
					<div class="five columns <?php echo $sidebar_class;?>"> <?php get_sidebar($curr_sidebar); ?> </div>
				<?php } else { ?>
					<div class="five columns <?php echo $sidebar_class;?>"> <?php get_sidebar($curr_sidebar); ?> </div>
				<?php } ?>

		<?php } else if ($content_type == 2) { ?>

				<div class="eleven columns <?php echo $content_class;?>"> <?php get_content_part(); ?> </div>

				<?php if ($is_sidebar && is_page()) { ?>
					<div class="five columns <?php echo $sidebar_class;?>"> <?php get_sidebar($curr_sidebar); ?> </div>
				<?php } else { ?>
					<div class="five columns <?php echo $sidebar_class;?>"> <?php get_sidebar($curr_sidebar); ?> </div>
				<?php } ?>

		<?php }
		}

		$curr_template = '';
		$options = fruitful_get_theme_options();

		if (fruitful_is_latest_posts_page()) {
			$curr_template = esc_attr($options['latest_posts_templ']);
		} elseif (is_archive()) {
			$curr_template = esc_attr($options['layout_archive_templ']);
		} elseif (is_404()) {
			$curr_template = esc_attr($options['layout_404_templ']);
		} elseif (is_search()) {
			$curr_template = esc_attr($options['layout_search_templ']);
		} elseif (is_category()) {
			$curr_template = esc_attr($options['layout_cat_templ']);
		} elseif (is_tag()) {
			$curr_template = esc_attr($options['layout_tag_templ']);
		} elseif (is_author()) {
			$curr_template = esc_attr($options['layout_author_templ']);
		} else {
			$default_blog_template = (get_post_meta( get_option('page_for_posts', true), '_fruitful_page_layout', true ))?(get_post_meta( get_option('page_for_posts', true), '_fruitful_page_layout', true )-1) : 1;

			$default_post_template = (get_post_meta( $post->ID , '_fruitful_page_layout', true ))?(get_post_meta(  $post->ID , '_fruitful_page_layout', true )-1):1;
			$default_page_template = (get_post_meta( $post->ID , '_fruitful_page_layout', true ))?(get_post_meta(  $post->ID , '_fruitful_page_layout', true )-1):0;
			if (!fruitful_is_blog()) {
				if (is_archive()) {
					$curr_template = $default_blog_template;
				} else {

					if (class_exists('BuddyPress')){
						$bp_pages = get_option('bp-pages');			//possible pages - activity, members, register, activate
						foreach ($bp_pages as $bp_page_slug => $bp_page_id){
							if (bp_is_current_component($bp_page_slug)){
								$curr_template = (get_post_meta( $bp_page_id , '_fruitful_page_layout', true ))?(get_post_meta( $bp_page_id , '_fruitful_page_layout', true )-1):0;
							} else {
								$curr_template = $default_page_template;
							}
						}
					} else {
						$curr_template = $default_page_template;
					}

				}
			} else {
				if (is_single()) {
					$curr_template = $default_post_template;
				} else {
					$curr_template = $default_blog_template;
				}
			}
		}

		if ($curr_template == 0) {
			get_html_custom_post_template('alpha', 'omega', $curr_sidebar, $curr_template);
		} else if ($curr_template == 1) {
			get_html_custom_post_template('alpha', 'omega', $curr_sidebar, $curr_template);
		} else if ($curr_template == 2) {
			get_html_custom_post_template('omega', 'alpha', $curr_sidebar, $curr_template);
		} else {
			if (is_home()) {
				$curr_template = 1;
			}
			get_html_custom_post_template('alpha', 'omega', $curr_sidebar, $curr_template);
		}
	}
}

if (!class_exists('ffs')){
	function fruitful_shortcodes_admin_notice(){
		echo '<div class="updated"><p>';
			echo __('Attention! Fruitful theme version 2.0 got major updates. You may have a problems with display content in shortcodes, because we created this part as plugin. You need install ','fruitful');
			echo '<a href="http://wordpress.org/plugins/fruitful-shortcodes/" target="_blank">Fruitful Shortcodes</a>';
			echo __(' and use them.', 'fruitful');
		echo '</p></div>';
	}
	add_action('admin_notices', 'fruitful_shortcodes_admin_notice');
}
