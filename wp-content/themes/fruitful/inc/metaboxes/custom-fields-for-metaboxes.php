<?php 

/*Adding cutom metabox filed*/
/*Made by fruitful*/
	
add_action( 'cmb_render_custom_layout_sidebars', 'fruitful_custom_layout_sidebars', 10, 2 );
function fruitful_custom_layout_sidebars( $field, $meta ) {
	$layout = 0;
	$layout = $meta ? $meta : $field['default'];
    ?>
		<ul class="list-layouts">
			<li>
				<input type="radio" id="full-width" value="1" name="<?php echo $field['id'];?>"  <?php checked( $layout, '1' ); ?>/>
				<img src="<?php echo CMB_META_BOX_URL . 'images/full.png'; ?>" alt="" />
			</li>
			<li>
				<input type="radio" id="right-sidebar" value="2" name="<?php echo $field['id'];?>"  <?php checked( $layout, '2' ); ?>/>
				<img src="<?php echo CMB_META_BOX_URL . 'images/left.png'; ?>" alt="" />
			</li>
			<li>
				<input type="radio" id="left-sidebar" value="3" name="<?php echo $field['id'];?>"  <?php checked( $layout, '3' ); ?>/>
				<img src="<?php echo CMB_META_BOX_URL . 'images/right.png'; ?>" alt="" />
			</li>
		</ul>
		<p class="cmb_metabox_description"><?php echo esc_attr($field['desc']); ?></p>
	<?php
}


add_action( 'admin_enqueue_scripts', 'fruitful_custom_layout_sidebars_script' );
function fruitful_custom_layout_sidebars_script($hook) {
	wp_register_script( 'cmb-layouts', CMB_META_BOX_URL . 'js/layout/layout.js'  );
	wp_register_style ( 'cmb-layouts', CMB_META_BOX_URL . 'js/layout/layout.css' );
	
	if ( ($hook == 'post.php') || ($hook == 'post-new.php') || ($hook == 'page-new.php') || ($hook == 'page.php') ) {
		wp_enqueue_script( 'cmb-layouts' );
		wp_enqueue_style ( 'cmb-layouts' );
	}
}

function ed_metabox_include_front_page( $display, $meta_box ) {
    if ( 'front-page' !== $meta_box['show_on']['key'] ) return $display;
    
    if ( isset( $_GET['post'] ) ) {
        $post_id = $_GET['post'];
    } elseif ( isset( $_POST['post_ID'] ) ) {
        $post_id = $_POST['post_ID'];
    }

    if( !isset( $post_id ) ) return false;
    $front_page = get_option('page_on_front');

    if ( $post_id == $front_page ) {
        return $display;
    }

}
add_filter( 'cmb_show_on', 'ed_metabox_include_front_page', 10, 2 );