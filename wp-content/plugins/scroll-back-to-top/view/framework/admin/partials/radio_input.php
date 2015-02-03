<?php
$plugin_option_name = isset( $plugin_option_name ) ? $plugin_option_name : '';
$field_key          = isset( $field_key ) ? $field_key : '';
$choice_key         = isset( $choice_key ) ? $choice_key : '';
$choice_label       = isset( $choice_label ) ? $choice_label : '';
$option_value       = isset( $option_value ) ? $option_value : '';
$extra_attributes   = isset( $extra_attributes ) ? $extra_attributes : '';
?>

<label
	for="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>].<?php echo $choice_key; ?>"
	style="margin-right:15px;"
	>
<input
	class="<?php echo $plugin_option_name; ?> <?php echo $field_key; ?>"
	type="radio"
	id="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>].<?php echo $choice_key; ?>"
	name="<?php echo $plugin_option_name; ?>[<?php echo $field_key; ?>]"
	value="<?php echo $choice_key; ?>"
	<?php checked( $option_value, $choice_key ); ?>
	<?php echo $extra_attributes; ?>
	><?php echo $choice_label; ?>
</label>