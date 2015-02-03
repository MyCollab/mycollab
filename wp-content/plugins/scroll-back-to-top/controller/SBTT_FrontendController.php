<?php
/**
 * SBTT Frontend Controller class
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage scroll-back-to-top
 * @version 1.1
 * @uses SBTT_Options
 * @uses JmsController
 */
if ( !class_exists( 'SBTT_FrontEndController' ) ){
class SBTT_FrontEndController extends JmsController {

  /**
   * @var float
   */
  protected $version = 1.1;

	/**
	 * @var array
	 */
	protected $args;

	/**
	 * @var SBTT_Options
	 */
	protected $options;

	/**
	 * Register Wordpress actions and filters
	 */
	protected function _init() {

		$this->options = new SBTT_Options();
		$this->args = $this->_getScrollButtonArgs();

		$this->_preprocessArgs();

		add_action('wp_head', array( $this, 'headSection' ) );
		add_action('wp_footer', array( $this, 'footerSection' ) );
	}

	/**
	 * Enqueue scripts
	 *
	 * @return false if not enabled
	 */
	public function enqueueScripts(){

		if ( !$this->isAuthorized() ) {
			return false;
		}

		$dependencies = array( 'jquery' );
		$vars = array( 'scrollBackToTop' => array() );

		if ( isset($this->args['scroll_duration']) ) {
			$vars['scrollBackToTop']['scrollDuration'] = $this->args['scroll_duration'];
		}

		if ( isset($this->args['fade_duration']) ) {
			$vars['scrollBackToTop']['fadeDuration'] = $this->args['fade_duration'];
		}

    if ( isset($this->args['visibility_duration']) ) {
      $vars['scrollBackToTop']['visibilityDuration'] = $this->args['visibility_duration'];
    }

		// load textfill js only if using auto font sizing
		if ( isset($this->args['label_type']) && isset($this->args['font_size']) && $this->args['font_size'] == '0px' ) {
			$this->enqueueCdnScript('text-fill', 'http://jquery-textfill.github.io/jquery-textfill/jquery.textfill.min.js' );
			$dependencies = array( 'jquery', 'text-fill' );
			$vars['scrollBackToTop']['autoFontSize'] = true;
		}

		$this->enqueueScript( 'scroll-back-to-top', 'scroll-back-to-top', $dependencies, null, true, $vars );
	}

	/**
	 * Enqueue styles
	 *
	 * @return false if not enabled
	 */
	public function enqueueStyles(){

		if ( !$this->isAuthorized() ) {
			return false;
		}

		// Only need the font pack if using icons, not text
		if ( isset( $this->args['label_type'] ) && $this->args['label_type'] != 'text' ) {
			$this->enqueueCdnStyle( 'font-awesome', '//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.css' );
		}
	}

	/**
	 * Render head markup
	 *
	 * @return false if not enabled
	 */
	public function headSection(){

		if ( !$this->isAuthorized() ) {
			return false;
		}

		echo apply_filters( 'sbtt_styles', $this->render( 'dynamic-styles', $this->args, false ) );
	}

	/**
	 * Render footer markup
	 *
	 * @return false if not enabled
	 */
	public function footerSection(){

		if ( !$this->isAuthorized() ) {
			return false;
		}

		echo apply_filters( 'sbtt_button_markup', $this->render( 'scroll-button', $this->args, false ) );
	}

	/**
	 * Get options
	 *
	 * @return array
	 */
	protected function _getScrollButtonArgs() {

		return get_option( $this->options->optionsKey() );
	}

	/**
	 * Preprocess variables
	 */
	protected function _preprocessArgs() {

		$this->_processFade();
    $this->_processVisibilityDuration();
    $this->_processBrowserResolutions();
		$this->_processHorizontalAlignment();
		$this->_processVerticalAlignment();
		$this->_processVerticalAlignment();
		$this->_processBorderRadii();
		$this->_processSize();
		$this->_processOpacity();
		$this->_processIconSize();
	}

	/**
	 * Process fade
	 */
	protected function _processFade(){

		if ( isset( $this->args['fade_duration'] ) ) {
			$this->args['fade_duration'] = number_format( ( $this->args['fade_duration'] ) / 1000, 1 );
		}
	}

  /**
   * Process visibility duration
   */
  protected function _processVisibilityDuration(){

    if ( !isset( $this->args['visibility_duration'] ) || $this->args['visibility_duration'] < 1 ) {
      unset( $this->args['visibility_duration'] );
    }
  }

  /**
   * Process browser resolutions
   */
  protected function _processBrowserResolutions(){

    if ( isset( $this->args['min_resolution'] ) && is_int( $this->args['min_resolution'] ) && $this->args['min_resolution'] > 0 ) {
      $this->args['min_resolution'] = $this->args['min_resolution'] . 'px';
    } else {
      unset( $this->args['min_resolution'] );
    }

    if ( isset( $this->args['max_resolution'] ) && is_int( $this->args['max_resolution'] ) && $this->args['max_resolution'] < 9999 ) {
      $this->args['max_resolution'] = $this->args['max_resolution'] . 'px';
    } else {
      unset( $this->args['max_resolution'] );
    }
  }

	/**
	 * Process horizontal alignment
	 */
	protected function _processHorizontalAlignment(){

		if ( isset( $this->args['align_x'] ) && isset( $this->args['margin_x'] )) {

			if ( $this->args['align_x'] == 'right' ) {

				$this->args['css_right'] = $this->args['margin_x'] . 'px';

			} elseif ( $this->args['align_x'] == 'left' ) {

				$this->args['css_left'] = $this->args['margin_x'] . 'px';

			} elseif ( $this->args['align_x'] == 'center' ) {

				$this->args['css_left'] = '50%';
				$this->args['margin_left'] = isset( $this->args['size_w'] ) ? (string)( $this->args['size_w'] / -2 ) . 'px' : '0px';
			}
		}
	}

	/**
	 * Process vertical alignment
	 */
	protected function _processVerticalAlignment(){

		if ( isset( $this->args['align_y'] ) && isset( $this->args['margin_y'] )) {

			if ( $this->args['align_y'] == 'bottom' ) {

				$this->args['css_bottom'] = $this->args['margin_y'] . 'px';

			} elseif ( $this->args['align_y'] == 'top' ) {

				$this->args['css_top'] = $this->args['margin_y'] . 'px';

			}
		}
	}

	/**
	 * Process border radii
	 */
	protected function _processBorderRadii(){

		// Border Radius
		// Because the button could be touching the edge of the screen,
		// check that margin is greater than border radius for corners that touch.
		if (
			!isset( $this->args['border_radius'] ) ||
			!isset( $this->args['align_x'] ) ||
			!isset( $this->args['align_y'] ) ||
			!isset( $this->args['margin_x'] ) ||
			!isset( $this->args['margin_y'] )
		) {
			return false;
		}

		$x_sides = array( 'left', 'right' );
		$y_sides = array( 'top', 'bottom' );

		foreach ( $x_sides as $x_side ) {
			foreach ( $y_sides as $y_side ) {

				if (
					!( $this->args['align_x'] == $x_side && $this->args['margin_x'] == 0 ) && // touching left or right
					!( $this->args['align_y'] == $y_side && $this->args['margin_y'] == 0 ) // touching top or bottom
				) {
					$this->args["border_radius_{$y_side}_{$x_side}"] = (string)$this->args['border_radius'] . 'px';
				}
			}
		}

		return true;
	}

	/**
	 * Process size
	 */
	protected function _processSize(){

		if ( isset( $this->args['size_w'] ) ) {
			$this->args['size_w'] = (string)$this->args['size_w'] . 'px';
		}
		if ( isset( $this->args['size_h'] ) ) {
			$this->args['size_h'] = (string)($this->args['size_h'] - 2) . 'px';
			$this->args['padding_top'] = '2px';
		}
	}

	/**
	 * Process opacity
	 */
	protected function _processOpacity(){

		if ( isset( $this->args['opacity'] ) ) {
			$this->args['opacity'] = number_format( ( $this->args['opacity'] ) / 100, 1 );
		}
	}

	/**
	 * Process icon size
	 */
	protected function _processIconSize(){

		// Text size
		if ( isset( $this->args['font_size'] ) ) {
			$this->args['font_size'] = (string)$this->args['font_size'] . 'px';
		}

		// Default icon size
		if ( !isset( $this->args['icon_size'] ) ) {
			$this->args['icon_size'] = 'fa-2x';
		}
	}

	/**
	 * Scroll button allowed right now?
	 *
	 * @return bool
	 */
	public function isAuthorized(){

		if ( isset( $this->args['enabled'] ) ) {
			if ( $this->args['enabled'] == 'public' ) {
			    return true;
			} elseif ( $this->args['enabled'] == 'preview' && is_user_logged_in() ) {
				return true;
			}
		}
		return false;
	}

}}