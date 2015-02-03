<?php
$plugin_option_name = isset( $plugin_option_name ) ? $plugin_option_name : '';
$field_key          = isset( $field_key ) ? $field_key : '';
$field_label        = isset( $field_label ) ? $field_label : '';
$option_value       = isset( $option_value ) ? $option_value : '';
$extra_attributes   = isset( $extra_attributes ) ? $extra_attributes : '';
?>

<label
	for="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	style="margin-right:15px;"
	>
<input
	class="<?php echo $plugin_option_name; ?> <?php echo $field_key; ?>"
	type="checkbox"
	id="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	name="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	value="1"
	<?php checked( $option_value, 1 ); ?>
	<?php echo $extra_attributes; ?>
	><?php echo $field_label; ?>
</label>