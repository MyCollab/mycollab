<?php get_header(); ?>
This is the home.php file.
<?php if ( have_posts() ) : while ( have_posts() ) : the_post(); ?>

  <h3><?php the_title(); ?></h3>

  <?php the_content(); ?>
  <?php wp_link_pages(); ?>
  <?php edit_post_link(); ?>

<?php endwhile; ?>

<?php
if ( get_next_posts_link() ) {
  next_posts_link();
}
?>
<?php
if ( get_previous_posts_link() ) {
  previous_posts_link();
}
?>

<?php else: ?>

  <p>No posts found. :(</p>

<?php endif; ?>

<?php get_footer(); ?>