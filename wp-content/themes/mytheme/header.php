<!DOCTYPE html>
<html>
<head>
  <meta charset="<?php bloginfo( 'charset' ); ?>">
  <title><?php wp_title( '|', true, 'right' ); ?></title>
  <link rel="stylesheet" href="<?php echo esc_url( get_stylesheet_uri() ); ?>" type="text/css" />
  <?php wp_head(); ?>
</head>
<body>
  <h1><?php bloginfo( 'name' ); ?></h1>
  <h2><?php bloginfo( 'description' ); ?></h2>