<?php
/**
 * User Options Collection class
 * This class manages plugin options that a user would change.
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage JMS Plugin Framework
 * @version 1.3
 */
if ( !class_exists( 'JmsPluginUserOptionsCollection' ) ){
	class JmsUserOptionsCollection {

    /**
     * @var float
     */
    protected $version = 1.3;

		/**
		 * @var string
		 */
		protected $optionsGroup = 'jms-framework-options';

		/**
		 * @var string
		 */
		protected $optionsKey = 'jms-framework';

		/**
		 * @var array
		 */
		protected $optionMap = array();

		/**
		 * get options group
		 *
		 * @return string
		 */
		public function optionsGroup(){

			return $this->optionsGroup;
		}

		/**
		 * get options key
		 *
		 * @return string
		 */
		public function optionsKey(){

			return $this->optionsKey;
		}

		/**
		 * get option from array of options
		 *
		 * @param string $key
		 * @return string
		 */
		public function getOption( $key ) {

			$options = get_option( $this->optionsKey, array() );
			$value   = isset( $options[$key] ) ? $options[$key] : '';

			return $value;
		}

		/**
		 * add/update option value
		 *
		 * @param string $key
		 * @param mixed $val
		 * @return bool
		 */
		public function updateOption( $key, $val ) {

			$options       = get_option( $this->optionsKey, array() );
			$options[$key] = $val;

			return update_option( $this->optionsKey, $options );
		}

		/**
		 * option set yet?
		 *
		 * @param string $key
		 * @return boolean
		 */
		public function hasOption( $key ){

			$options = get_option( $this->optionsKey, array() );

			return isset( $options[$key] );
		}

		/**
		 * options collection set yet?
		 *
		 * @return boolean
		 */
		public function hasOptionCollection(){

			$options = get_option( $this->optionsKey, array() );

			return !empty( $options );
		}

		/**
		 * get section map
		 *
		 * @param string $sectionKey
		 * @return array
		 */
		public function sectionMap( $sectionKey ) {

			$map = $this->optionMap();

			return isset( $map[$sectionKey] ) ? $map[$sectionKey] : array();
		}

		/**
		 * get section option setting
		 *
		 * @param string $sectionKey
		 * @param string $optionKey
		 * @return null
		 */
		public function sectionOption( $sectionKey, $optionKey ) {

			$map = $this->sectionMap( $sectionKey );

			if ( isset( $map[$optionKey] ) ) {
				return $map[$optionKey];
			} else {
				return NULL;
			}
		}

		/**
		 * get all fields
		 *
		 * @return array
		 */
		public function fields() {

			$map = $this->optionMap();
			$fields = array();

			foreach ( $map as $sectionKey => $sectionDetails ) {

				if ( isset( $sectionDetails['fields'] ) ) {
					foreach ( $sectionDetails['fields'] as $fieldKey => $fieldDetails ) {

						$fields[$fieldKey] = isset( $fieldDetails['label'] ) ? $fieldDetails['label'] : $fieldKey;
					}
				}
			}

			return $fields;
		}

		/**
		 * get field map
		 *
		 * @param string $fieldKey
		 * @return array
		 */
		public function fieldMap( $fieldKey ) {

			$map = $this->optionMap();

			foreach ( $map as $sectionKey => $sectionDetails ) {

				if ( isset( $sectionDetails['fields'][$fieldKey] ) ) {
					return $sectionDetails['fields'][$fieldKey];
				}
			}

			return array();
		}

		/**
		 * get field option setting
		 *
		 * @param string $fieldKey
		 * @param string $optionKey
		 * @return null
		 */
		public function fieldOption( $fieldKey, $optionKey ) {

			$map = $this->fieldMap( $fieldKey );

			if ( isset( $map[$optionKey] ) ) {
				return $map[$optionKey];
			} else {
				return NULL;
			}
		}

		/**
		 * get default options
		 *
		 * @return array
		 */
		public function defaultOptions() {

			$defaults = array();

			$fields = $this->fields();
			foreach ( $fields as $fieldKey => $fieldLabel ) {

				$defaults[$fieldKey] = $this->fieldOption( $fieldKey, 'default' );
			}
      $defaults['version'] = $this->getVersion();

			return $defaults;
		}

		/**
		 * init plugin's options to default settings
		 *
		 * @return boolean
		 */
		function initDefaultOptions(){

			$defaults = $this->defaultOptions();
			return update_option( $this->optionsKey, $defaults );
		}

    /**
     * init plugin's options if they don't exist
     *
     * @return boolean
     */
    function initOptions(){

      $defaults = $this->defaultOptions();
      $options = get_option( $this->optionsKey, NULL );
      if (empty($options)) {
        return update_option( $this->optionsKey, $defaults );
      }
      return TRUE;
    }

		/**
		 * options mapping
		 *
		 * @return array
		 */
		public function optionMap() {

			return $this->optionMap;
		}

    /**
     * Get version
     *
     * @return float
     */
    public function getVersion() {
      return $this->version;
    }
	}
}