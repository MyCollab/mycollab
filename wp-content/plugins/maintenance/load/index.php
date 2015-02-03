<?php 
	$mess_arr = array();
	$ebody_class = '';
	$mess_arr = get_custom_login_code(); 
	if (!empty($mess_arr[0])) $ebody_class = 'error';
	$mt_options = mt_get_plugin_options(true);
	
?>
<!DOCTYPE html>
<!--[if IE 7]>
<html class="ie ie7" <?php language_attributes(); ?>>
<![endif]-->
<!--[if IE 8]>
<html class="ie ie8" <?php language_attributes(); ?>>
<![endif]-->
<!--[if !(IE 7) | !(IE 8)  ]><!-->
<html <?php language_attributes(); ?>>
<!--<![endif]-->
<head>
	<meta charset="<?php bloginfo( 'charset' ); ?>" />
	<?php get_page_title(esc_attr($mess_arr[0])); ?>
	<meta name="viewport" content="width=device-width, user-scalable=no, maximum-scale=1, initial-scale=1, minimum-scale=1">
	<link rel="profile" href="http://gmpg.org/xfn/11" />
	<link rel="pingback" href="<?php bloginfo( 'pingback_url' ); ?>" />
	<?php do_action('load_custom_scripts'); ?>
	<?php do_action('options_style'); ?>
	<?php do_action('add_single_backstretch_background'); ?>
	<?php do_action('add_gg_analytics_code'); ?>
</head>
<body <?php body_class('maintenance ' . $ebody_class); ?>>
	  <?php do_action('before_main_container'); ?>
	<div class="main-container">
		<?php do_action('before_content_section'); ?>
		<div id="wrapper">
			<div class="center">
				<header>
					<?php do_action('logo_box'); ?>
				</header>
			</div>
		
			<div id="content" class="site-content">
				<div class="center">
					<?php do_action('content_section'); ?>
				</div>	
			</div>
		
		</div> <!-- end wrapper -->		
		<footer role="contentinfo">
			<div class="center">
				<?php do_action('footer_section'); ?>
			</div>
		</footer>
		<?php do_action('after_content_section'); ?>
		<?php do_action('user_content_section'); ?>
	</div>
	<?php do_action('after_main_container'); ?>
	<?php if (isset($mt_options['is_login'])) { ?>
		
		<div class="login-form-container">
			<?php do_login_form(esc_attr($mess_arr[3]), esc_attr($mess_arr[1]), esc_attr($mess_arr[2])); ?>
			<?php do_button_login_form(); ?>
		</div>	
	<?php } ?>	
	
</body>
</html>