<?php
$plugin_option_name = isset( $plugin_option_name ) ? $plugin_option_name : '';
$field_key          = isset( $field_key ) ? $field_key : '';
$option_value       = isset( $option_value ) ? $option_value : '';
$extra_attributes   = isset( $extra_attributes ) ? $extra_attributes : '';
?>

<textarea
	class="<?php echo $plugin_option_name; ?> <?php echo $field_key; ?>"
	id="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	name="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	<?php echo $extra_attributes; ?>
	><?php echo $option_value; ?></textarea>