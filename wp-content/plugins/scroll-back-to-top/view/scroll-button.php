<div class="scroll-back-to-top-wrapper">
	<span class="scroll-back-to-top-inner">
		<?php if ( isset( $label_type ) && $label_type == 'text' && isset( $label_text ) ) : ?>
			<?php echo $label_text; ?>
		<?php elseif( isset( $label_type ) && isset( $icon_size )) : ?>
			<i class="fa <?php echo $icon_size; ?> <?php echo $label_type; ?>"></i>
		<?php endif; ?>
	</span>
</div>