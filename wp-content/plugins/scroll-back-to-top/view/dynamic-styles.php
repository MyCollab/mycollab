
<style>
<?php $media_query = false; ?>
<?php if ( isset( $min_resolution ) || isset( $max_resolution ) ) : ?>
<?php $media_query = true; ?>
@media <?php if ( isset( $min_resolution ) ) : ?>(min-width: <?php echo $min_resolution; ?>)
  <?php if ( isset( $max_resolution ) ) : ?>
    and
  <?php endif; ?>
<?php endif; ?>
<?php if ( isset( $max_resolution ) ) : ?>(max-width: <?php echo $max_resolution; ?>)<?php endif; ?> {
<?php endif; ?>
.scroll-back-to-top-wrapper {
    position: fixed;
	opacity: 0;
	visibility: hidden;
	overflow: hidden;
	text-align: center;
	z-index: 99999999;
<?php if ( isset( $label_type ) && $label_type == 'text' && isset( $font_size ) && $font_size != '0px' ) : ?>
	font-size: <?php echo $font_size; ?>;
<?php endif; ?>
<?php if ( isset( $color_background ) ) : ?>
    background-color: <?php echo $color_background; ?>;
<?php endif; ?>
<?php if ( isset( $color_foreground ) ) : ?>
	color: <?php echo $color_foreground; ?>;
<?php endif; ?>
<?php if ( isset( $size_w ) ) : ?>
	width: <?php echo $size_w; ?>;
<?php endif; ?>
<?php if ( isset( $size_h ) ) : ?>
	height: <?php echo $size_h; ?>;
	line-height: <?php echo $size_h; ?>;
<?php endif; ?>
<?php if ( isset( $margin_top ) ) : ?>
	margin-top: <?php echo $margin_top; ?>;
<?php endif; ?>
<?php if ( isset( $margin_right ) ) : ?>
	margin-right: <?php echo $margin_right; ?>;
<?php endif; ?>
<?php if ( isset( $margin_bottom ) ) : ?>
	margin-bottom: <?php echo $margin_bottom; ?>;
<?php endif; ?>
<?php if ( isset( $margin_left ) ) : ?>
	margin-left: <?php echo $margin_left; ?>;
<?php endif; ?>
<?php if ( isset( $css_top ) ) : ?>
	top: <?php echo $css_top; ?>;
<?php endif; ?>
<?php if ( isset( $css_right ) ) : ?>
	right: <?php echo $css_right; ?>;
<?php endif; ?>
<?php if ( isset( $css_bottom ) ) : ?>
	bottom: <?php echo $css_bottom; ?>;
<?php endif; ?>
<?php if ( isset( $css_left ) ) : ?>
	left: <?php echo $css_left; ?>;
<?php endif; ?>
<?php if ( isset( $padding_top ) ) : ?>
	padding-top: <?php echo $padding_top; ?>;
<?php endif; ?>
<?php if ( isset( $border_radius_top_left ) ) : ?>
	border-top-left-radius: <?php echo $border_radius_top_left; ?>;
<?php endif; ?>
<?php if ( isset( $border_radius_top_right ) ) : ?>
	border-top-right-radius: <?php echo $border_radius_top_right; ?>;
<?php endif; ?>
<?php if ( isset( $border_radius_bottom_right ) ) : ?>
	border-bottom-right-radius: <?php echo $border_radius_bottom_right; ?>;
<?php endif; ?>
<?php if ( isset( $border_radius_bottom_left ) ) : ?>
	border-bottom-left-radius: <?php echo $border_radius_bottom_left; ?>;
<?php endif; ?>
<?php if ( isset( $fade_duration ) ) : ?>
	-webkit-transition: all <?php echo $fade_duration; ?>s ease-in-out;
	-moz-transition: all <?php echo $fade_duration; ?>s ease-in-out;
	-ms-transition: all <?php echo $fade_duration; ?>s ease-in-out;
	-o-transition: all <?php echo $fade_duration; ?>s ease-in-out;
	transition: all <?php echo $fade_duration; ?>s ease-in-out;
<?php endif; ?>
}
.scroll-back-to-top-wrapper:hover {
<?php if ( isset( $color_hover ) ) : ?>
	background-color: <?php echo $color_hover; ?>;
<?php endif; ?>
<?php if ( isset( $color_foreground_hover ) ) : ?>
  color: <?php echo $color_foreground_hover; ?>;
<?php endif; ?>
}
.scroll-back-to-top-wrapper.show {
    visibility:visible;
    cursor:pointer;
<?php if ( isset( $opacity ) ) : ?>
	opacity: <?php echo $opacity; ?>;
<?php endif; ?>
}
.scroll-back-to-top-wrapper i.fa {
	line-height: inherit;
}
.scroll-back-to-top-wrapper .fa-lg {
	vertical-align: 0;
}
<?php if ( isset( $extra_css ) ) : ?>
<?php echo $extra_css; ?>
<?php endif; ?>
<?php if ( $media_query ) : ?>
}
<?php endif; ?>
</style>