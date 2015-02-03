<?php
/**
 * The Shop Sidebar containing Widget areas for Shop Page.
 *
 * @package WordPress
 * @subpackage Fruitful theme
 * @since Fruitful theme 1.0
 */
?>
		<div id="secondary" class="widget-area" role="complementary">
			<?php do_action( 'before_sidebar' ); ?>
			<?php if ( ! dynamic_sidebar( 'sidebar-5' ) ) : ?>
					
					<aside id="woocommerce_product_search-2" class="widget woocommerce widget_product_search">
						<h3 class="widget-title">Search Products</h3>
						<?php get_product_search_form(); ?>
					</aside>
					<aside id="woocommerce_product_categories-4" class="widget woocommerce widget_product_categories">
						<h3 class="widget-title">Product Categories</h3>
						<?php 
							$args = array('hierarchical' => true);
							the_widget('WC_Widget_Product_Categories', $args); 
						?>
					</aside>
					
			<?php endif; // end sidebar widget area ?>
		</div><!-- #secondary .widget-area -->
