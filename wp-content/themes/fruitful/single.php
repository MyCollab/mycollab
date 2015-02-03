<?php
/**
 * The Template for displaying all single posts.
 *
 * @package WordPress
 * @subpackage Fruitful theme
 * @since Fruitful theme 1.0
 */

get_header(); ?>

	<?php fruitful_get_content_with_custom_sidebar('single-post'); ?>

<?php get_footer(); ?>