<?php
/**
 * Admin Settings Page
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage JMS Plugin Framework
 * @version 1.2
 */
?>

<style type="text/css">
	input:disabled { background:rgba(255, 13, 13, 0.23); }
</style>

<div class="wrap">

	<?php screen_icon( 'plugins' ); ?>
	<h2><?php echo esc_html( get_admin_page_title() ); ?></h2>

	<?php if ( isset( $option_group ) && isset( $settings_page_slug ) && isset( $option_key ) ) : ?>
		<form action="options.php" method="post" id="sbtt_options_form">
			<?php settings_fields( $option_group ); ?>
			<?php do_settings_sections( $settings_page_slug ); ?>
			<div>
				<?php submit_button( null, 'primary', 'submit', false ); ?>
				<?php submit_button( 'Resore Default Settings', 'secondary', $option_key . '[restore]', false ); ?>

			</div>
		</form>

		<script>
			jQuery(document).ready(function($){
				$('[name="<?php echo $option_key . '[restore]'; ?>"]').on('click', function(e){
					if(!confirm("<?php _e('Are you sure you want to restore defualt settings?  This will erase all current settings', 'scroll-back-to-top' ); ?>")) {
						e.preventDefault();
					}
				});
			});
		</script>

	<?php endif; ?>
</div>


