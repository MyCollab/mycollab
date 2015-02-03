<?php
/**
 * SBTT Options class
 *
 * @author Joe Sexton <joe@josephmsexton.com>
 * @package WordPress
 * @subpackage scroll-back-to-top
 * @version 1.1
 */
if ( !class_exists( 'SBTT_Options' ) ){
class SBTT_Options extends JmsUserOptionsCollection {

  /**
   * @var float
   */
  protected $version = 1.1;

	/**
	 * @var string
	 */
	protected $optionsGroup = 'scroll-back-to-top-options';

	/**
	 * @var string
	 */
	protected $optionsKey = 'scroll-back-to-top';

	/**
	 * configure options mapping
	 */
	public function __construct() {

		$this->optionMap['visibility'] = array(
			'label'       => __( 'Visibility', 'scroll-back-to-top' ),
			'description' => __( 'Button visibility status options', 'scroll-back-to-top' ),
			'fields'      => array(
				'enabled' => array(
					'label'     => __( 'Status', 'scroll-back-to-top' ),
					'type'      => 'radio',
					'default'   => 'public',
					'data_type' => 'choice',
					'choices'   => array(
						'public'   => __( 'Publicly Visible', 'scroll-back-to-top' ),
						'preview'  => __( 'Preview Mode', 'scroll-back-to-top' ),
						'disabled' => __( 'Disabled', 'scroll-back-to-top' ),
					),
					'required'  => true,
				),
        'min_resolution' => array(
          'label'     => __( 'Minimum Browser Resolution', 'scroll-back-to-top' ),
          'type'      => 'text',
          'default'   => 0,
          'min'       => 0,
          'max'       => 9999,
          'units'     => __( 'px - smallest browser resolution the scroll button should appear on', 'scroll-back-to-top' ),
          'data_type' => 'integer',
          'required'  => true,
        ),
        'max_resolution' => array(
          'label'     => __( 'Maximum Browser Resolution', 'scroll-back-to-top' ),
          'type'      => 'text',
          'default'   => 9999,
          'min'       => 0,
          'max'       => 9999,
          'units'     => __( 'px - largest browser resolution the scroll button should appear on', 'scroll-back-to-top' ),
          'data_type' => 'integer',
          'required'  => true,
        ),
			),
		);
		$this->optionMap['appearance'] = array(
			'label'       => __( 'Button Appearance', 'scroll-back-to-top' ),
			'description' => __( 'Change the look and feel of the scroll to top button', 'scroll-back-to-top' ),
			'fields'      => array(
				'size_w' => array(
					'label'     => __( 'Width', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 50,
					'min'       => 0,
					'max'       => 2000,
					'units'     => __( 'px', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
				'size_h' => array(
					'label'     => __( 'Height', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 50,
					'min'       => 0,
					'max'       => 2000,
					'units'     => __( 'px', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
				'color_background' => array(
					'label'     => __( 'Background Color', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => '#777777',
					'options'   => 'color-picker',
					'data_type' => 'string',
					'required'  => true,
				),
				'color_hover' => array(
					'label'     => __( 'Background Hover Color', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => '#888888',
					'options'   => 'color-picker',
					'data_type' => 'string',
					'required'  => true,
				),
				'color_foreground' => array(
					'label'     => __( 'Foreground Color', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => '#eeeeee',
					'options'   => 'color-picker',
					'data_type' => 'string',
					'required'  => true,
				),
        'color_foreground_hover' => array(
          'label'     => __( 'Foreground Hover Color', 'scroll-back-to-top' ),
          'type'      => 'text',
          'default'   => '#eeeeee',
          'options'   => 'color-picker',
          'data_type' => 'string',
          'required'  => true,
        ),
				'opacity' => array(
					'label'     => __( 'Opacity', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 100,
					'min'       => 0,
					'max'       => 100,
					'units'     => __( '%', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
				'border_radius' => array(
					'label'     => __( 'Border Radius', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 10,
					'min'       => 0,
					'max'       => 100,
					'units'     => __( 'px', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
			),
		);
		$this->optionMap['location'] = array(
			'label'       => __( 'Button Location', 'scroll-back-to-top' ),
			'description' => __( 'Change where the button is located on the page', 'scroll-back-to-top' ),
			'fields'      => array(
				'align_x' => array(
					'label'     => __( 'Horizontal Alignment', 'scroll-back-to-top' ),
					'type'      => 'radio',
					'default'   => 'right',
					'data_type' => 'choice',
					'choices'   => array(
						'left'   => __( 'Left', 'scroll-back-to-top' ),
						'center' => __( 'Center', 'scroll-back-to-top' ),
						'right'  => __( 'Right', 'scroll-back-to-top' ),
					),
					'required'  => true,
				),
				'align_y' => array(
					'label'     => __( 'Vertical Alignment', 'scroll-back-to-top' ),
					'type'      => 'radio',
					'default'   => 'bottom',
					'data_type' => 'choice',
					'choices'   => array(
						'top'    => __( 'Top', 'scroll-back-to-top' ),
						'bottom' => __( 'Bottom', 'scroll-back-to-top' ),
					),
					'required'  => true,
				),
				'margin_x' => array(
					'label'     => __( 'Horizontal Distance from Edge', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 30,
					'min'       => 0,
					'max'       => 2000,
					'units'     => __( 'px margin', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
				'margin_y' => array(
					'label'     => __( 'Vertical Distance from Edge', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 30,
					'min'       => 0,
					'max'       => 2000,
					'units'     => __( 'px margin', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
			),
		);
		$this->optionMap['label'] = array(
			'label'       => __( 'Button Label', 'scroll-back-to-top' ),
			'description' => __( 'Change what appears on the button', 'scroll-back-to-top' ),
			'fields'      => array(
				'label_type' => array(
					'label'     => __( 'Label Type', 'scroll-back-to-top' ),
					'type'      => 'radio',
					'default'   => 'fa-arrow-circle-up',
					'data_type' => 'choice',
					'choices'   => array(
						'fa-angle-up'           => '<i class="fa fa-angle-up fa-lg"></i>',
						'fa-angle-double-up'    => '<i class="fa fa-angle-double-up fa-lg"></i>',
						'fa-arrow-up'           => '<i class="fa fa-arrow-up fa-lg"></i>',
						'fa-arrow-circle-o-up'  => '<i class="fa fa-arrow-circle-o-up fa-lg"></i>',
						'fa-arrow-circle-up'    => '<i class="fa fa-arrow-circle-up fa-lg"></i>',
						'fa-caret-up'           => '<i class="fa fa-caret-up fa-lg"></i>',
						'fa-caret-square-o-up'  => '<i class="fa fa-caret-square-o-up fa-lg"></i>',
						'fa-chevron-up'         => '<i class="fa fa-chevron-up fa-lg"></i>',
						'fa-chevron-circle-up'  => '<i class="fa fa-chevron-circle-up fa-lg"></i>',
						'fa-hand-o-up'          => '<i class="fa fa-hand-o-up fa-lg"></i>',
						'fa-long-arrow-up'      => '<i class="fa fa-long-arrow-up fa-lg"></i>',
						'text'                  => __( 'Custom Text', 'scroll-back-to-top' ),
					),
					'required'  => true,
				),
				'icon_size' => array(
					'label'     => __( 'Icon Size', 'scroll-back-to-top' ),
					'type'      => 'radio',
					'default'   => 'fa-2x',
					'data_type' => 'choice',
					'choices'   => array(
						'fa-sm' => '1',
						'fa-lg' => '2',
						'fa-2x' => '3',
						'fa-3x' => '4',
						'fa-4x' => '5',
						'fa-5x' => '6',
					),
				),
				'label_text' => array(
					'label'     => __( 'Custom Label Text', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => '',
					'data_type' => 'string',
				),
				'font_size' => array(
					'label'     => __( 'Font Size', 'scroll-back-to-top' ),
					'type'      => 'number',
					'default'   => 12,
					'min'       => 0,
					'max'       => 100,
					'units'     => __( 'px - Using 0px will auto-size text to fit the button', 'scroll-back-to-top' ),
					'data_type' => 'integer',
				),
			),
		);
		$this->optionMap['behavior'] = array(
			'label'       => __( 'Animation Options', 'scroll-back-to-top' ),
			'description' => __( 'Change animation options', 'scroll-back-to-top' ),
			'fields'      => array(
				'scroll_duration' => array(
					'label'     => __( 'Scroll Duration', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 500,
					'min'       => 0,
					'max'       => 5000,
					'units'     => __( 'ms - this is how long it takes to scroll back to the top after the button has been pressed', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
        'visibility_duration' => array(
          'label'     => __( 'Visibility Duration', 'scroll-back-to-top' ),
          'type'      => 'text',
          'default'   => 0,
          'min'       => 0,
          'max'       => 10000,
          'units'     => __( 'ms - if set to 0 the button will never time out,
                              otherwise the button will disappear after the specified time', 'scroll-back-to-top' ),
          'data_type' => 'integer',
          'required'  => true,
        ),
				'fade_duration' => array(
					'label'     => __( 'Fade Duration', 'scroll-back-to-top' ),
					'type'      => 'text',
					'default'   => 500,
					'min'       => 0,
					'max'       => 5000,
					'units'     => __( 'ms - this is how long it takes for the transition to fade the button in and out', 'scroll-back-to-top' ),
					'data_type' => 'integer',
					'required'  => true,
				),
			),
		);
		$this->optionMap['advanced'] = array(
			'label'       => __( 'Advanced Options', 'scroll-back-to-top' ),
			'description' => __( 'Advanced options, this requires some knowledge of css to use.', 'scroll-back-to-top' ),
			'fields'      => array(
				'extra_css' => array(
					'label'     => __( 'Extra CSS', 'scroll-back-to-top' ),
					'type'      => 'textarea',
					'columns'   => 50,
					'rows'      => 10,
					'data_type' => 'string',
				),
			),
		);
	}
}
}