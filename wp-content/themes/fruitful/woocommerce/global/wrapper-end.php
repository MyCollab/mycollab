<?php
/**
 * Content wrappers
 *
 * @author 		Fruitful
 * @package 	WooCommerce/Templates
 * @version     1.6.4
 */


$template    = strtolower(get_option( 'template' ));
$woo_sidebar = fruitful_get_woo_sidebar();

switch( $template ) {
	case 'twentyeleven' :
		echo '</div></div>';
		break;
	case 'twentytwelve' :
		echo '</div></div>';
		break;
	case 'twentythirteen' :
		echo '</div></div>';
		break;
	case 'twentyfourteen' :
		echo '</div></div></div>';
		get_sidebar( 'content' );
		break;
	case 'fruitful' :
		if ($woo_sidebar == 1) {
			echo '</div></div></div>';
		} elseif($woo_sidebar == 2) {
			echo '</div></div></div>';
			echo '<div class="five columns alpha woo-loop-sidebar">';
				if (is_shop() || is_product_category()) {
					get_sidebar( 'shop' );
				} else {
					get_sidebar( 'product' );
				}
			echo '</div>';
		} else {
			echo '</div></div></div>';
			echo '<div class="five columns omega woo-loop-sidebar">';
				if (is_shop() || is_product_category()){
					get_sidebar( 'shop' );
				} else {
					get_sidebar( 'product' );
				}
			echo '</div>';
		}
		break;
	default :
		echo '</div></div>';
		break;
}