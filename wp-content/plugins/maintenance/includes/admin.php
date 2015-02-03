<?php
	add_action( 'admin_menu', 'maintenance_admin_setup' );
	
	function maintenance_admin_setup() {
		global	$maintenance_variable;
				$maintenance_variable->options_page = add_menu_page('Maintenance', 'Maintenance', 'manage_options', 'maintenance', 'manage_options',  MAINTENANCE_URI . '/images/icon-small.png');
		add_action( "admin_init", 'mt_register_settings');
		add_action( "admin_head-{$maintenance_variable->options_page}", 'maintenance_metaboxes_scripts' );
		add_action( "admin_print_styles-{$maintenance_variable->options_page}",  'admin_print_custom_styles');
		add_action( "load-{$maintenance_variable->options_page}", 'maintenance_page_add_meta_boxes' );
	}	
	
	function maintenance_page_add_meta_boxes() {
		global	$maintenance_variable;
		do_action('add_meta_boxes', $maintenance_variable->options_page);
		
	}
	
	function mt_register_settings() {
		if ( !empty($_POST['lib_options']) && check_admin_referer('maintenance_edit_post','maintenance_nonce') ) {
			if (!isset($_POST['lib_options']['state'])) { $_POST['lib_options']['state'] = 0; } 
			else {	   $_POST['lib_options']['state'] = 1; }
			
			if (isset($_POST['lib_options']['htmlcss'])) {
				$_POST['lib_options']['htmlcss'] = wp_kses_post(stripslashes($_POST['lib_options']['htmlcss']));
			}
			
			if (isset($_POST['lib_options'])) {
			    update_option( 'maintenance_options',  $_POST['lib_options']);
			}	
		}
	}
	
	function admin_print_custom_styles() {	
			if( function_exists( 'wp_enqueue_media' ) ){
				wp_enqueue_media();
			} else {
				wp_enqueue_script('media-upload');
				wp_enqueue_script('thickbox');
				wp_enqueue_style ('thickbox');
			}
			
			wp_enqueue_script( 'common' );
			wp_enqueue_script( 'wp-lists' );
			wp_enqueue_script( 'postbox' );
			
			wp_enqueue_style  ('arvo', 'http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700|Arvo:400,400italic,700,700italic' );		
			wp_enqueue_style  ('wp-color-picker' );
			
			wp_enqueue_script ('select2',    MAINTENANCE_URI .'js/select2/select2.min.js' );
			wp_enqueue_style  ('select2',    MAINTENANCE_URI .'js/select2/select2.css' );	
			
			wp_enqueue_script ('uplaods_',    MAINTENANCE_URI .'js/uploads_.min.js' );
			wp_enqueue_script ('maintenance', MAINTENANCE_URI .'js/init.min.js', array( 'wp-color-picker' ), false, true );
			wp_enqueue_style  ('maintenance', MAINTENANCE_URI .'css/admin.css' );	
	}
	
	function manage_options()  {
		generate_plugin_page();
	}
	
	function generate_plugin_page() {
		global	$maintenance_variable;
		$mt_option = mt_get_plugin_options(true);
	?>
		<div id="maintenance-options" class="wrap">	
			<h2></h2>						
			<form method="post" action="" enctype="multipart/form-data" name="options-form">
				<?php wp_nonce_field('maintenance_edit_post','maintenance_nonce'); ?>
				<?php wp_nonce_field('meta-box-order',  'meta-box-order-nonce', false ); ?>
				<?php wp_nonce_field('closedpostboxes', 'closedpostboxesnonce', false ); ?>
				<?php screen_icon(); ?>
				<h2>Maintenance</h2>						
				<input type="checkbox" id="state" name="lib_options[state]" <?php checked($mt_option['state'], 1); ?> />
				<div class="clear"></div>
				<div id="poststuff">
					 <div class="metabox-holder">
						 <div id="all-fileds" class="postbox-container column-1 normal">
							<?php do_meta_boxes($maintenance_variable->options_page,'normal',null); ?>
							<?php do_meta_boxes($maintenance_variable->options_page,'advanced',null); ?>
						</div>
						
						 <div id="promo" class="postbox-container column-2 normal">
							<?php do_meta_boxes($maintenance_variable->options_page,'side',null); ?>
						</div>
						 
					</div>
					<?php submit_button(__('Save changes', 'maintenance'), 'primary'); ?>
				</div>	
			</form>
		</div>
	<?php
	}