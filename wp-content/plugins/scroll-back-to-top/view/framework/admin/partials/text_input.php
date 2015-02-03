<?php
$plugin_option_name = isset( $plugin_option_name ) ? $plugin_option_name : '';
$field_key          = isset( $field_key ) ? $field_key : '';
$option_value       = isset( $option_value ) ? $option_value : '';
$input_type         = isset( $input_type ) ? $input_type : '';
$extra_attributes   = isset( $extra_attributes ) ? $extra_attributes : '';
$units              = isset( $units ) ? $units : '';
$options            = isset( $options ) ? $options : '';
?>

<input
	class="<?php echo $plugin_option_name; ?> <?php echo $field_key; ?>"
	type="<?php echo $input_type != '' ? $input_type : 'text'; ?>"
	id="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	name="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	value="<?php echo $option_value; ?>"
	<?php echo $extra_attributes; ?>
><small class="units"><?php echo $units; ?></small>

<?php if ( $options == 'color-picker' ) : ?>
	<script> jQuery(document).ready(function($){ $('.<?php echo $field_key; ?>').wpColorPicker(); }); </script>
<?php endif; ?>