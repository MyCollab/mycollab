<?php
/**
 * Content wrappers
 *
 * @author 		Fruitful
 * @package 	WooCommerce/Templates
 * @version     1.6.4
 */

if ( ! defined( 'ABSPATH' ) ) exit; // Exit if accessed directly

$template = strtolower(get_option( 'template' ));
$woo_sidebar = fruitful_get_woo_sidebar();

switch( $template ) {
	case 'twentyeleven' :
		echo '<div id="primary"><div id="content" role="main">';
		break;
	case 'twentytwelve' :
		echo '<div id="primary" class="site-content"><div id="content" role="main">';
		break;
	case 'twentythirteen' :
		echo '<div id="primary" class="site-content"><div id="content" role="main" class="entry-content twentythirteen">';
		break;
	case 'twentyfourteen' :
		echo '<div id="primary" class="content-area"><div id="content" role="main" class="site-content twentyfourteen"><div class="tfwc">';
		break;
	case 'fruitful' :
		$theme_options = fruitful_ret_options("fruitful_theme_options");
		$prod_num_row_class = '';
		if (is_shop() || is_product_category()) {
			if (!empty($theme_options['shop_num_row'])){
				$prod_num_row = $theme_options['shop_num_row'];
				$prod_num_row_class = 'prod_num_row-'.$prod_num_row;
			}
		}
		if ($woo_sidebar == 1){
			echo '<div class="woo-loop-content alpha omega '.$prod_num_row_class.'"><div id="container"><div id="content" role="main">';
		} elseif($woo_sidebar == 2) {
			echo '<div class="eleven columns woo-loop-content omega '.$prod_num_row_class.'"><div id="container"><div id="content" role="main">';
		} else {
			echo '<div class="eleven columns woo-loop-content alpha '.$prod_num_row_class.'"><div id="container"><div id="content" role="main">';
		}
		break;
	default :
		echo '<div id="container"><div id="content" role="main">';
		break;
}