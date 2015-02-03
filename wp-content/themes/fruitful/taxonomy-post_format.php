<?php
/**
 * The template for displaying Post Format pages.
 *
 * Used to display archive-type pages for posts with a post format.
 * If you'd like to further customize these Post Format views, you may create a
 * new template file for each specific one.
 *
 * Learn more: http://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Fruitful theme
 * @since Fruitful theme 1.0
 */

get_header(); ?>
	<header class="archive-header">
		<h1 class="archive-title"><?php printf( __( '%s Archives', 'fruitful' ), '<span>' . get_post_format_string( get_post_format() ) . '</span>' ); ?></h1>
	</header><!-- .archive-header -->
	<div class="eleven columns alpha">		
		<div id="primary" class="content-area">
			<div id="content" class="site-content" role="main">

			<?php if ( have_posts() ) : ?>
				<?php //fruitful_content_nav( 'nav-above' ); ?>
				<?php /* The loop */ ?>
				<?php while ( have_posts() ) : the_post(); ?>
					<?php get_template_part( 'content', get_post_format() ); ?>
				<?php endwhile; ?>

				<?php fruitful_content_nav( 'nav-below' ); ?>

			<?php else : ?>
				<?php get_template_part( 'content', 'none' ); ?>
			<?php endif; ?>
			</div><!-- #content -->
		</div><!-- #primary -->
	</div>	

	<div class="five columns omega">
		<?php get_sidebar('Main Sidebar'); ?>
	</div>	
<?php get_footer(); ?>