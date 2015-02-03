<?php 

class ffsSettingsPage   {
    public function __construct() {
        add_action( 'admin_menu', array( $this, 'ffsPluginPage' ) );
        add_action( 'admin_init', array( $this, 'ffsPageInit' ) );
    }

    public function ffsPluginPage() {
        
        add_options_page(
            'Fruitful Shortcodes', 
            'Fruitful Shortcodes', 
            'manage_options', 
            'ffsAdminSettings', 
            array( $this, 'ffsCreateOptionsPage' )
        );
    }

    public function ffsCreateOptionsPage() {
        $this->fruitful_options = get_option( 'fruitful_options_plugin' );
    ?>
        <div class="wrap">
            <?php screen_icon(); ?>
            <h2>Shortcode Settings</h2>           
            <form method="post" action="options.php">
            <?php
                settings_fields( 'ffsAdminGroup' );   
                do_settings_sections( 'ffsAdminSettings' );
                submit_button(); 
            ?>
            </form>
        </div>
        <?php
    }

    
    public function ffsPageInit() {        
        register_setting(
            'ffsAdminGroup', 		// Option group
            'fruitful_options_plugin', 	// Option name
            array( $this, 'fruitful_sanitize' ) // Sanitize
        );

        add_settings_section(
            'ffsSettingSection', 	// ID
            '', 	// Title
            null, // Callback
            'ffsAdminSettings' // Page
        );  

        add_settings_field(
            'fruitful_post_types', 	// ID
            'Display On', 		// Title 
            array( $this, 'fruitful_get_post_types_call' ), // Callback
            'ffsAdminSettings', 	// Page
            'ffsSettingSection' 	// Section           
        );      
        
    }

    /**
     * Sanitize each setting field as needed
     *
     * @param array $input Contains all settings fields as array keys
     */
    public function fruitful_sanitize( $elements ) {
		
        $new_elements = array();
        if( isset( $elements['fruitful_post_types'] ) ) $new_elements['fruitful_post_types'] = $elements['fruitful_post_types'];

        return $new_elements;
    }

    
    protected function fruitful_getPostTypes() {
        return get_post_types(array('public' => true));
    }
	
    public function fruitful_get_post_types_call() {
        $pst_arr = ($pst_arr = $this->fruitful_options['fruitful_post_types']) ? ($pst_arr) : array('page', 'post');
		
        foreach ($this->fruitful_getPostTypes() as $post_type) {
                 $checked = (in_array($post_type, $pst_arr)) ? ' checked="checked"' : '';
                ?>
                <label>
                    <input type="checkbox"<?php echo $checked; ?> value="<?php echo $post_type; ?>" id="fruitful_post_types_<?php echo $post_type; ?>" name="fruitful_options_plugin[fruitful_post_types][]" />
                    <?php echo $post_type; ?>
                </label><br>
                <?php }
        ?>
        <p class="description indicator-hint">
			<?php _e("Select for which content types...", "ff_shortcodes"); ?>
		</p>
        <?php
    }

}

if( is_admin() ) $my_settings_page = new ffsSettingsPage();