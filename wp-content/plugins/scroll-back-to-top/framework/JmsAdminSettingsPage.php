<?php
/**
 * Admin Settings Page class
 * This is an abstract implementation of an admin menu settings page
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage JMS Plugin Framework
 * @version 1.3
 * @uses JmsController
 * @uses JmsUserOptionsCollection
 */
if ( !class_exists( 'JmsAdminSettingsPage' ) ){
	abstract class JmsAdminSettingsPage extends JmsController {

    /**
     * @var float
     */
    protected $version = 1.3;

		/**
		 * @var string
		 */
		protected $settingsPageTitle = '';

		/**
		 * @var string
		 */
		protected $settingsMenuTitle = '';

		/**
		 * @var string
		 */
		protected $settingsMenuSlug = '';

		/**
		 * @var JmsUserOptionsCollection
		 */
		protected $options = '';

		/**
		 * @var string
		 */
		protected $icon = '';

		/**
		 * @var string
		 */
		protected $menuPosition = null;

		/**
		 * @var string
		 */
		protected $manageSettingsCapability = 'manage_options';

    /**
     * constructor
     *
     * @param string $pluginFile
     * @param string $pluginName
     */
    public function __construct( $pluginFile, $pluginName = ''  ) {

      parent::__construct( $pluginFile, $pluginName = ''  );

      add_filter('contextual_help', array( $this, 'contextualHelp' ), 10, 3);
    }

		/**
		 * settings page title
		 *
		 * @return string
		 */
		public function settingsPageTitle() {

			return $this->settingsPageTitle;
		}

		/**
		 * settings menu title
		 *
		 * @return string
		 */
		public function settingsMenuTitle() {

			return $this->settingsMenuTitle;
		}

		/**
		 * settings menu slug
		 *
		 * @return string
		 */
		public function settingsMenuSlug() {

			return $this->settingsMenuSlug;
		}

		/**
		 * manage settings capability
		 *
		 * @return string
		 */
		public function manageSettingsCapability() {

			return $this->manageSettingsCapability;
		}

		/**
		 * init admin settings page
		 *
		 * @param string $settingsPageTitle
		 * @param string $settingsMenuTitle
		 * @param JmsUserOptionsCollection $options
		 * @param string $menuMethod
		 */
		protected function _initAdminSettingsPage( $settingsPageTitle, $settingsMenuTitle, JmsUserOptionsCollection $options, $menuMethod ){

			add_action( 'admin_enqueue_scripts', array( $this, 'enqueueAdminSettingsPageScripts' ) );
			add_action( 'admin_enqueue_scripts', array( $this, 'enqueueAdminSettingsPageStyles' ) );

			add_action( 'admin_init', array( $this, 'registerSettings' ) );

			$this->settingsPageTitle = $settingsPageTitle;
			$this->settingsMenuTitle = $settingsMenuTitle;
			$this->options           = $options;

			$this->settingsMenuSlug = sanitize_title( $settingsMenuTitle );

			$actionName = $menuMethod . 'Action';
			add_action( 'admin_menu', array( $this, $actionName ) );
		}

		/**
		 * enqueue admin settings page scripts
		 */
		public function enqueueAdminSettingsPageScripts() {

			wp_enqueue_script( 'jquery' );
			wp_enqueue_script( 'wp-color-picker' );
		}

		/**
		 * enqueue admin settings page styles
		 */
		public function enqueueAdminSettingsPageStyles() {

			wp_enqueue_style( 'wp-color-picker' );
		}

		/**
		 * allows a user to call $this->addXxxxxPage( string $pageTitle, string $menuTitle, JmsUserOptionsCollection $options )
		 * internally handles calls like $this->addXxxxxPageAction() and adds the appropriate menu page
		 *
		 * @param string $method
		 * @param array $args
		 * @return boolean|void
		 */
		public function __call( $method, $args ){

			// first letters are 'add', last letters are 'Page'
			if ( $this->_callMethodHasPrefixAndSuffix( $method, 'add', 'Page' ) ) {
				// must be a valid menu type
				$menuType = $this->_getCallMethodTypeFromCalledMethod( $method, 'add', 'Page' );
				if ( !$this->_isValidMenuType( $menuType ) && !$this->_isValidSubMenuType( $menuType ) ) {

					return false;
				}

				// must have correct arguments to initialize
				if ( $this->_calledMethodWithCorrectArguments( $args ) ) {

					$this->_initAdminSettingsPage( $args[0], $args[1], $args[2], $method );
				}

				return true;

			// first letters are 'add', last letters are 'PageAction'
			} elseif ( $this->_callMethodHasPrefixAndSuffix( $method, 'add', 'PageAction' ) ) {

				// get method name
				$menuType = $this->_getCallMethodTypeFromCalledMethod( $method, 'add', 'PageAction' );
				if ( !$this->_isValidMenuType( $menuType ) && !$this->_isValidSubMenuType( $menuType ) ) {

					return false;
				}

				$this->_callAddMenuPageMethod( $menuType );

				return true;
			}

			// bad method call
			return false;
		}

		/**
		 * __call method has a prefix and suffix
		 *
		 * @param string $method
		 * @param string $prefix
		 * @param string $suffix
		 * @return boolean
		 */
		protected function _callMethodHasPrefixAndSuffix( $method, $prefix, $suffix ){

			return substr( $method, 0, 3 ) == $prefix &&
				substr( $method, ( strlen( $method ) - strlen( $suffix ) ), strlen( $method ) ) == $suffix;
		}

		/**
		 * __call'ed method with correct parameters
		 *
		 * @param array $args
		 * @return boolean
		 */
		protected function _calledMethodWithCorrectArguments( $args ){

			return isset( $args[0] )  &&
				isset( $args[1] ) &&
				isset( $args[2] ) &&
				is_string( $args[0] ) &&
				is_string( $args[1] ) &&
				$args[2] instanceof JmsUserOptionsCollection;
		}

		/**
		 * get method type from the called method name
		 *
		 * @param string $method
		 * @param string $prefix
		 * @param string $suffix
		 * @return string
		 */
		protected function _getCallMethodTypeFromCalledMethod( $method, $prefix, $suffix ){

			$method = substr( $method, ( strlen( $prefix ) ), ( strlen( $method ) - strlen( $suffix ) - strlen( $prefix ) ) );
			$method = strtolower( $method );

			return $method;
		}

		/**
		 * is a valid menu type
		 *
		 * @param string $menuType
		 * @return bool
		 */
		protected function _isValidMenuType( $menuType ){

			$menuPageMethods = $this->_getMenuPageMethods();

			return isset( $menuPageMethods[$menuType] );
		}

		/**
		 * is a valid sub menu type
		 *
		 * @param string $menuType
		 * @return bool
		 */
		protected function _isValidSubMenuType( $menuType ) {

			$subMenuPageMethods = $this->_getSubMenuPageMethods();

			return isset($subMenuPageMethods[$menuType] );
		}

		/**
		 * get the correct add_xxxx_menu() menu page method
		 *
		 * @param string $menuType
		 * @return string|null
		 */
		protected function _getAddMenuPageMethod( $menuType ){

			$menuPageMethods = $this->_getMenuPageMethods();

			return isset( $menuPageMethods[$menuType] ) ? $menuPageMethods[$menuType] : null;
		}

		/**
		 * get the correct add_xxxx_menu() submenu page method
		 *
		 * @param string $menuType
		 * @return string|null
		 */
		protected function _getAddSubMenuPageMethod( $menuType ){

			$subMenuPageMethods = $this->_getSubMenuPageMethods();

			return isset( $subMenuPageMethods[$menuType] ) ? $subMenuPageMethods[$menuType] : null;
		}

		/**
		 * returns an array of menu page methods
		 *
		 * @return array
		 * @see http://codex.wordpress.org/Administration_Menus
		 */
		protected function _getMenuPageMethods(){

			// menu pages
			return array(
				'menu'    => 'add_menu_page',
				'object'  => 'add_object_page',
				'utility' => 'add_utility_page',
			);
		}

		/**
		 * returns an array of sub menu page methods
		 *
		 * @return array
		 * @see http://codex.wordpress.org/Administration_Menus
		 */
		protected function _getSubMenuPageMethods(){

			// submenu pages
			return array(
				'dashboard'  => 'add_dashboard_page',
				'posts'      => 'add_posts_page',
				'media'      => 'add_media_page',
				'links'      => 'add_links_page',
				'pages'      => 'add_pages_page',
				'comments'   => 'add_comments_page',
				'theme'      => 'add_theme_page',
				'plugins'    => 'add_plugins_page',
				'users'      => 'add_users_page',
				'management' => 'add_management_page',
				'options'    => 'add_options_page',
			);
		}

		/**
		 * calls the add menu/submenu page method with the appropriate arguments
		 *
		 * @param string $menuType
		 */
		protected function _callAddMenuPageMethod( $menuType ){

      global $my_plugin_hook;

			// add menu page
			if ( $this->_isValidMenuType( $menuType ) ) {

				$method = $this->_getAddMenuPageMethod( $menuType );
        $my_plugin_hook = $method(
					__( $this->settingsPageTitle(), $this->textDomain() ),
					__( $this->settingsMenuTitle(), $this->textDomain() ),
					$this->manageSettingsCapability(),
					$this->settingsMenuSlug(),
					array( $this, 'renderAdminPage' )
				);

			// add admin submenu page
			} else {

				$method = $this->_getAddSubMenuPageMethod( $menuType );

				// it's a pretty big pain to determine default menu positions, only add that param
				// if we actually have a menu position set
				if ( $this->menuPosition ) {

          $my_plugin_hook = $method(
						__( $this->settingsPageTitle(), $this->textDomain() ),
						__( $this->settingsMenuTitle(), $this->textDomain() ),
						$this->manageSettingsCapability(),
						$this->settingsMenuSlug(),
						array( $this, 'renderAdminPage' ),
						$this->icon,
						$this->menuPosition
					);
				} else {
          $my_plugin_hook = $method(
						__( $this->settingsPageTitle(), $this->textDomain() ),
						__( $this->settingsMenuTitle(), $this->textDomain() ),
						$this->manageSettingsCapability(),
						$this->settingsMenuSlug(),
						array( $this, 'renderAdminPage' ),
						$this->icon
					);
				}
			}
		}

		/**
		 * render admin page
		 */
		public function renderAdminPage() {

			if ( !current_user_can( $this->manageSettingsCapability() ) ) {
				wp_die( __( 'You do not have sufficient permissions to access this page.', $this->textDomain() ) );
			}

			$args = array(
				'settings_page_slug' => $this->settingsMenuSlug(),
				'option_group'       => $this->options->optionsGroup(),
				'option_key'         => $this->options->optionsKey(),
			);

			$this->render( 'framework:admin:settings', $args );
		}

		/**
		 * admin settings init
		 */
		public function registerSettings() {

			$map = $this->options->optionMap();

			register_setting(
				$this->options->optionsGroup(),
				$this->options->optionsKey(),
				array( $this, 'validateFields' )
			);

			// map is broken up into sections, parse each one
			foreach ( $map as $sectionKey => $sectionDetails ) {
				$this->_addSettingsSection( $sectionKey, $sectionDetails );
			}
		}

		/**
		 * add settings section
		 *
		 * @param string $sectionKey
		 * @param string $sectionDetails
		 * @return bool|void
		 */
		protected function _addSettingsSection( $sectionKey, $sectionDetails ) {

			if ( !isset( $sectionDetails['label'] ) || !isset( $sectionDetails['fields'] ) ) {
				return false;
			}

			add_settings_section(
				$sectionKey,
				$sectionDetails['label'],
				array( $this, 'renderSectionText' ),
				$this->settingsMenuSlug()
			);

			foreach ( $sectionDetails['fields'] as $fieldKey => $fieldDetails ) {

				$this->_addSettingsField( $fieldKey, $sectionKey );
			}
		}

		/**
		 * add settings field
		 *
		 * @param string $fieldKey
		 * @param string $sectionKey
		 */
		protected function _addSettingsField( $fieldKey, $sectionKey ) {

			$fieldLabel = $this->options->fieldOption( $fieldKey, 'label' );
			$fieldType  = $this->options->fieldOption( $fieldKey, 'type' );
			$required   = $this->options->fieldOption( $fieldKey, 'required' );

			if ( $required === true ) {
				$fieldLabel .= $this->requiredIndicator();
			}

			$callback = $this->_getInputCallback( $fieldType );

			add_settings_field(
				$fieldKey,
				apply_filters( 'jms_admin_settings_option_field_label', $fieldLabel ),
				array( $this, $callback ),
				$this->settingsMenuSlug(),
				$sectionKey,
				array(
					'field_key' => $fieldKey,
				)
			);
		}

		/**
		 * get input callback function
		 *
		 * @param string $type
		 * @return string
		 */
		protected function _getInputCallback( $type ){

			switch ($type) {
				case 'radio':
					return 'renderRadioInput';
				case 'checkbox':
					return 'renderCheckboxInput';
				case 'textarea':
					return 'renderTextareaInput';
				case 'text':
				default:
					return 'renderTextInput';
			}
		}

		/**
		 * render section text
		 *
		 * @param array $sectionDetails
		 */
		public function renderSectionText( $sectionDetails ) {

			$key = isset( $sectionDetails['id'] ) ? $sectionDetails['id'] : null;

			if ( $key ) {
				echo apply_filters( 'jms_admin_settings_section_text', $this->options->sectionOption( $key, 'description' ) );
			}
		}

		/**
		 * render text input
		 *
		 * @param array $args
		 */
		public function renderTextInput( $args ) {
			$fieldKey = $args['field_key'];

			$dataType = $this->options->fieldOption( $fieldKey, 'data_type' );

			if ( $dataType == 'integer' || $dataType == 'float' ) {
				$inputType = 'number';
			} else {
				$inputType = 'text';
			}

			$args = array(
				'plugin_option_name' => $this->options->optionsKey(),
				'field_key'          => $fieldKey,
				'option_value'       => $this->options->getOption( $fieldKey ),
				'input_type'         => $inputType,
				'extra_attributes'   => $this->_extraAtts( $fieldKey, $inputType ),
				'units'              => $this->options->fieldOption( $fieldKey, 'units' ),
				'options'            => $this->options->fieldOption( $fieldKey, 'options' ),
			);

			$this->render( apply_filters( 'jms_admin_settings_text_input_template', 'framework:admin:partials:text_input' ), $args );
		}

		/**
		 * render checkbox input
		 *
		 * @param array $args
		 */
		public function renderCheckboxInput( $args ) {
			$fieldKey  = $args['field_key'];

			$args = array(
				'plugin_option_name' => $this->options->optionsKey(),
				'field_key'          => $fieldKey,
				'field_label'        => $this->options->fieldOption( $fieldKey, 'label' ),
				'option_value'       => $this->options->getOption( $fieldKey ),
				'extra_attributes'   => $this->_extraAtts( $fieldKey, 'checkbox' ),
			);

			$this->render( apply_filters( 'jms_admin_settings_checkbox_input_template', 'framework:admin:partials:checkbox_input' ), $args );
		}

		/**
		 * render radio input
		 *
		 * @param array $args
		 */
		public function renderRadioInput( $args ) {
			$fieldKey  = $args['field_key'];

			$args = array(
				'plugin_option_name' => $this->options->optionsKey(),
				'field_key'          => $fieldKey,
				'option_value'       => $this->options->getOption( $fieldKey ),
				'extra_attributes'   => $this->_extraAtts( $fieldKey, 'radio' ),
			);

			foreach ( $this->options->fieldOption( $fieldKey, 'choices' ) as $choiceKey => $choiceLabel ) {

				$args['choice_key']   = $choiceKey;
				$args['choice_label'] = $choiceLabel;

				$this->render(  apply_filters( 'jms_admin_settings_radio_input_template', 'framework:admin:partials:radio_input' ), $args );
			}
		}

		/**
		 * render textarea input
		 *
		 * @param array $args
		 */
		public function renderTextareaInput( $args ) {
			$fieldKey = $args['field_key'];

			$args = array(
				'plugin_option_name' => $this->options->optionsKey(),
				'field_key'          => $fieldKey,
				'option_value'       => $this->options->getOption( $fieldKey ),
				'extra_attributes'   => $this->_extraAtts( $fieldKey, 'textarea' ),
			);

			$this->render(  apply_filters( 'jms_admin_settings_textarea_input_template', 'framework:admin:partials:textarea_input' ), $args );
		}

		/**
		 * get extra html attributes
		 *
		 * @param string $fieldKey
		 * @param string $inputType
		 * @return string
		 */
		protected function _extraAtts( $fieldKey, $inputType ){

			$atts = array();

			$dataType = $this->options->fieldOption( $fieldKey, 'data_type' );

			switch ( $inputType ) {
				case 'number':
					if ( $dataType == 'integer' ) {
						$atts[] = 'step="1"';
						$atts[] = 'pattern="\d+"';
					} elseif( $dataType == 'float') {
						$atts[] = 'step="any"';
					}

					$min = $this->options->fieldOption( $fieldKey, 'min' );
					$max = $this->options->fieldOption( $fieldKey, 'max' );

					if ( is_int( $min ) ) {
						$atts[] = 'min="' . $min . '"';
					}
					if ( is_int( $max ) ) {
						$atts[] = 'max="' . $max . '"';
					}
					$atts = apply_filters( 'jms_admin_settings_number_input_field_extra_attributes', $atts );
					break;
				case 'textarea':
					$columns = $this->options->fieldOption( $fieldKey, 'columns' );
					$rows    = $this->options->fieldOption( $fieldKey, 'rows' );

					if ( is_int( $columns ) ) {
						$atts[] = 'cols="' . $columns . '"';
					}
					if ( is_int( $rows ) ) {
						$atts[] = 'rows="' . $rows . '"';
					}
					$atts = apply_filters( 'jms_admin_settings_textarea_input_field_extra_attributes', $atts );
					break;
				case 'text':
					$atts = apply_filters( 'jms_admin_settings_text_input_field_extra_attributes', $atts );
					break;
				case 'radio':
					$atts = apply_filters( 'jms_admin_settings_radio_input_field_extra_attributes', $atts );
					break;
				case 'checkbox':
					$atts = apply_filters( 'jms_admin_settings_checkbox_input_field_extra_attributes', $atts );
					break;
				default:
					break;
			}

			if ( $this->options->fieldOption( $fieldKey, 'required' ) === true ) {
				$atts[] = 'required="required"';
			}

			return  apply_filters( 'jms_admin_settings_input_field_extra_attributes', implode( ' ', $atts ) );
		}

		/**
		 * validate form fields
		 *
		 * @param array $fields
		 * @return array
		 */
		public function validateFields( $fields ) {

			// check for restore to defaults
			if ( isset( $fields['restore'] ) ) {
				return $this->options->defaultOptions();
			}

			$options = $this->options->fields();

			// make sure no extra options were posted.
			foreach ( $fields as $fieldKey => $fieldVal ) {
				if ( !isset( $fieldKey, $fields ) ) {
					add_settings_error(
						$fieldKey,
						'error',
						apply_filters( 'jms_admin_settings_error_invalid', __( $fieldKey . ' is not a valid option', $this->textDomain() ) ),
						'error'
					);
				}
			}

			// process each option in the options map since we may not have been posted every option.
			foreach ( $options as $optionKey => $optionLabel ) {

				$this->_validateField( $fields, $optionKey, $optionLabel );
			}

			return $fields;
		}

		/**
		 * validate field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateField( &$fields, $optionKey, $optionLabel ) {

			$dataType = $this->options->fieldOption( $optionKey, 'data_type' );

			// if the option wasn't posted it may be a boolean false, or may be a value the user wants to remove
			if ( isset( $fields[$optionKey] ) ) {
				if ( ( $dataType == 'integer' || $dataType == 'float' ) ) {
					$this->_validateNumberField( $fields, $optionKey, $optionLabel );
				} elseif( $dataType == 'string' ) {
					$this->_validateStringField( $fields, $optionKey, $optionLabel );
				} elseif ( $dataType == 'choice' ) {
					$this->_validateChoiceField( $fields, $optionKey, $optionLabel );
				} elseif ( $dataType == 'boolean' ) {
					$this->_validateBooleanField( $fields, $optionKey, $optionLabel );
				}
			} elseif( $dataType == 'boolean' ) {
				$fields[$optionKey] = 0;
			}
		}

		/**
		 * validate numeric field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateNumberField( &$fields, $optionKey, $optionLabel ) {

			if ( !is_numeric( $fields[$optionKey] ) ) {
				$fields[$optionKey] = $this->options->getOption( $optionKey );
				add_settings_error(
					$optionKey,
					'error',
					apply_filters( 'jms_admin_settings_error_invalid_number', __( $optionLabel . ' must be a numeric value', $this->textDomain() ) ),
					'error'
				);
			}

			$min = $this->options->fieldOption( $optionKey, 'min' );
			if ( $fields[$optionKey] < $min ) {
				$fields[$optionKey] = $this->options->getOption( $optionKey );
				add_settings_error(
					$optionKey,
					'error',
					apply_filters( 'jms_admin_settings_error_invalid_number', sprintf( __( $optionLabel . ' cannot be less than %s', $this->textDomain() ), $min ) ),
					'error'
				);
			}

			$max = $this->options->fieldOption( $optionKey, 'max' );
			if ( $fields[$optionKey] > $max ) {
				$fields[$optionKey] = $this->options->getOption( $optionKey );
				add_settings_error(
					$optionKey,
					'error',
					apply_filters( 'jms_admin_settings_error_invalid_number', sprintf( __( $optionLabel . ' cannot be greater than %s', $this->textDomain() ), $max ) ),
					'error'
				);
			}

			$dataType = $this->options->fieldOption( $optionKey, 'data_type' );
			if ( $dataType == 'integer' ) {
				$fields[$optionKey] = (int)$fields[$optionKey];
			}
			if ( $dataType == 'float' ) {
				$fields[$optionKey] = (float)$fields[$optionKey];
			}
		}

		/**
		 * validate string field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateStringField( &$fields, $optionKey, $optionLabel ) {
			// sanitize strings
			$fields[$optionKey] = filter_var( $fields[$optionKey], FILTER_SANITIZE_STRING );

			// check color
			if ( $this->options->fieldOption( $optionKey, 'options' ) == 'color-picker' ) {
				$this->_validateColorField( $fields, $optionKey, $optionLabel );
			}
		}

		/**
		 * validate color field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateColorField( &$fields, $optionKey, $optionLabel ) {

			$colorHex = $fields[$optionKey];
			if ( substr( $colorHex, 0, 1 ) == '#' ) {
				$colorHex = substr( $colorHex, 1 );
			}

			if ( !ctype_xdigit( $colorHex ) ) {
				$fields[$optionKey] = $this->options->getOption( $optionKey );
				add_settings_error(
					$optionKey,
					'error',
					apply_filters( 'jms_admin_settings_error_invalid_color', __( $optionLabel . ' must be a valid hexadecimal color value', $this->textDomain() ) ),
					'error'
				);
			}
		}

		/**
		 * validate choice field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateChoiceField( &$fields, $optionKey, $optionLabel ) {

			$choices = $this->options->fieldOption( $optionKey, 'choices' );
			if ( !isset( $choices[$fields[$optionKey]] ) ) {
				$fields[$optionKey] = $this->options->getOption( $optionKey );
				add_settings_error(
					$optionKey,
					'error',
					apply_filters( 'jms_admin_settings_error_invalid_choice', __( $optionLabel . ' is not a valid selection', $this->textDomain() ) ),
					'error'
				);
			}
		}

		/**
		 * validate boolean field
		 *
		 * @param array $fields
		 * @param string $optionKey
		 * @param string $optionLabel
		 */
		protected function _validateBooleanField( &$fields, $optionKey, $optionLabel ) {
			$fields[$optionKey] = (bool)$fields[$optionKey];
		}

		/**
		 * required indicator
		 *
		 * @return string
		 */
		public function requiredIndicator(){

			return $this->render( apply_filters( 'jms_admin_settings_required_indicator_template', 'framework:admin:partials:required_indicator' ), array(), false );
		}

		/**
		 * @param string $icon
		 */
		public function setIcon( $icon )
		{
			$this->icon = $icon;
		}

		/**
		 * @return string
		 */
		public function getIcon()
		{
			return $this->icon;
		}

		/**
		 * @param string $manageSettingsCapability
		 */
		public function setManageSettingsCapability( $manageSettingsCapability )
		{
			$this->manageSettingsCapability = $manageSettingsCapability;
		}

		/**
		 * @return string
		 */
		public function getManageSettingsCapability()
		{
			return $this->manageSettingsCapability;
		}

		/**
		 * @param string $menuPosition
		 */
		public function setMenuPosition( $menuPosition )
		{
			$this->menuPosition = $menuPosition;
		}

		/**
		 * @return string
		 */
		public function getMenuPosition()
		{
			return $this->menuPosition;
		}

		/**
		 * @param \JmsUserOptionsCollection $options
		 */
		public function setOptions( $options )
		{
			$this->options = $options;
		}

		/**
		 * @return \JmsUserOptionsCollection
		 */
		public function getOptions()
		{
			return $this->options;
		}

		/**
		 * @param string $settingsPageTitle
		 */
		public function setSettingsPageTitle( $settingsPageTitle )
		{
			$this->settingsPageTitle = $settingsPageTitle;
		}

		/**
		 * @return string
		 */
		public function getSettingsPageTitle()
		{
			return $this->settingsPageTitle;
		}

		/**
		 * @param string $settingsMenuSlug
		 */
		public function setSettingsMenuSlug( $settingsMenuSlug )
		{
			$this->settingsMenuSlug = $settingsMenuSlug;
		}

		/**
		 * @return string
		 */
		public function getSettingsMenuSlug()
		{
			return $this->settingsMenuSlug;
		}

		/**
		 * @param string $settingsMenuTitle
		 */
		public function setSettingsMenuTitle( $settingsMenuTitle )
		{
			$this->settingsMenuTitle = $settingsMenuTitle;
		}

		/**
		 * @return string
		 */
		public function getSettingsMenuTitle()
		{
			return $this->settingsMenuTitle;
		}

    /**
     * Contextual help
     *
     * @param string $contextual_help
     * @param string $screen_id
     * @param WP_Screen $screen
     * @return string
     */
    public function contextualHelp( $contextual_help, $screen_id, $screen ){
      global $my_plugin_hook;

      if ($screen_id == $my_plugin_hook) {
        $contextual_help = $this->_renderContextualHelp($screen);
      }

      return $contextual_help;
    }

    /**
     * Render contextual help menu for an admin page
     *
     * @param WP_Screen $screen
     * @return string
     */
    protected function _renderContextualHelp(WP_Screen $screen) {
      return '';
    }
	}
}