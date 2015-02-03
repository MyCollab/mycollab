<?php
/**
 * Custom functions that act independently of the theme templates
 *
 * Eventually, some of the functionality here could be replaced by core features
 *
 * @package WordPress
 * @subpackage Fruitful theme
 * @since Fruitful theme 1.0
 */

/**
 * Get our wp_nav_menu() fallback, wp_page_menu(), to show a home link.
 *
 * @since Fruitful theme 1.0
 */
function fruitful_page_menu_args( $args ) {
	$args['show_home'] = true;
	return $args;
}
add_filter( 'wp_page_menu_args', 'fruitful_page_menu_args' );

/**
 * Adds custom classes to the array of body classes.
 *
 * @since Fruitful theme 1.0
 */
function fruitful_body_classes( $classes ) {
	// Adds a class of group-blog to blogs with more than 1 published author
	if ( is_multi_author() ) {
		$classes[] = 'group-blog';
	}
	
	if (class_exists('Woocommerce')) {
		if (is_shop()) $classes[] = 'shop-page ';
	} 
	
	$theme_options = fruitful_ret_options("fruitful_theme_options");
	if (isset($theme_options['responsive']) && 
			 ($theme_options['responsive'] == 'on')) {
		$classes[] = 'responsive';
	}

	return $classes;
}
add_filter( 'body_class', 'fruitful_body_classes' );

/**
 * Filter in a link to a content ID attribute for the next/previous image links on image attachment pages
 *
 * @since Fruitful theme 1.0
 */
function fruitful_enhanced_image_navigation( $url, $id ) {
	if ( ! is_attachment() && ! wp_attachment_is_image( $id ) )
		return $url;

	$image = get_post( $id );
	if ( ! empty( $image->post_parent ) && $image->post_parent != $id )
		$url .= '#main';

	return $url;
}
add_filter( 'attachment_link', 'fruitful_enhanced_image_navigation', 10, 2 );