<?php
/**
 * Makes a custom Widget for displaying Aside, Link, Status, and Quote Posts available
  *
 * @package WordPress
 * @subpackage Fruitful
 * @since Fruitful
 */
class Fruitful_Widget_News_Archive extends WP_Widget {
	/**
	 * Constructor
	 *
	 * @return void
	 **/
	function Fruitful_Widget_News_Archive () {
		$theme_name = wp_get_theme();
		$widget_name = $theme_name.' '.__( 'News Archive', 'fruitful' );
		$widget_ops = array( 'classname' => 'widget_news_archive', 'description' => __( 'Use this widget to list your Link posts', 'fruitful' ) );
		$this->WP_Widget( 'widget_news_archive', $widget_name, $widget_ops );
		$this->alt_option_name = 'widget_news_archive';

		add_action( 'save_post', array(&$this, 'flush_widget_cache' ));
		add_action( 'deleted_post', array(&$this, 'flush_widget_cache' ));
		add_action( 'switch_theme', array(&$this, 'flush_widget_cache' ));
	}

	/**
	 * Outputs the HTML for this widget.
	 *
	 * @param array An array of standard parameters for widgets in this theme
	 * @param array An array of settings for this widget instance
	 * @return void Echoes it's output
	 **/
	function widget( $args, $instance ) {
		$cache = wp_cache_get( 'widget_news_archive', 'widget' );
		$id_item = 0;
		if ( !is_array( $cache ) )
			$cache = array();

		if ( ! isset( $args['widget_id'] ) )
			$args['widget_id'] = null;

		if ( isset( $cache[$args['widget_id']] ) ) {
			echo $cache[$args['widget_id']];
			return;
		}

		ob_start();
		extract( $args, EXTR_SKIP );

		$title = apply_filters( 'widget_title', empty( $instance['title'] ) ? __( 'News-Archive', 'fruitful' ) : $instance['title'], $instance, $this->id_base);

		if ( ! isset( $instance['number'] ) )
			$instance['number'] = '10';

		if ( ! $number = absint( $instance['number'] ) )
 			  $number = 10;

		ob_start();
		extract($args);

		$title = apply_filters('widget_title', empty($instance['title']) ? __('News-Archive', 'fruitful') : $instance['title'], $instance, $this->id_base);
		if ( empty( $instance['number'] ) || ! $number = absint( $instance['number'] ) ) {
 			$number = 10;
		}

		$r = new WP_Query( apply_filters( 'widget_posts_args', array( 'posts_per_page' => -1, 'no_found_rows' => true, 'post_status' => 'publish', 'ignore_sticky_posts' => true ) ) );
		if ($r->have_posts()) :
?>
		<?php echo $before_widget;  ?>
		<?php if ( $title ) echo $before_title . $title . $after_title; ?>

		<div class="news_archive_wrapper">
		 
			<?php if ($instance['textarea_newsarchiv'] != '') { ?>
				<div class="news_archive_message"><?php echo stripslashes($instance['textarea_newsarchiv']); ?></div>	
			<?php } ?>
		
			<ul class="news_archiv_list">
			<?php  while ($r->have_posts()) : $r->the_post(); ?>
				<li id="arch_item_<?php echo $id_item; ?>" class="news_archiv_item">
					<a href="<?php the_permalink() ?>" title="<?php echo esc_attr(get_the_title() ? get_the_title() : get_the_ID()); ?>">
						<?php echo esc_attr( get_the_date( 'd.m.Y' ) ); ?> </br>
						<?php if ( get_the_title() ) the_title(); else the_ID(); ?>
					</a>
				</li>
				
			<?php $id_item++; 
					  endwhile; ?>
			</ul>
		</div>
		<?php echo $after_widget; ?>
		
		<?php
				wp_enqueue_script('wdgt_news_arch', 	get_template_directory_uri() . '/inc/js/jxBox/jquery.bxSlider.js', array( 'jquery' ), '20120206', false );
				wp_enqueue_style( 'wdgt_news_style', 	get_template_directory_uri() . '/inc/js/jxBox/bx.css');
				
		?>			
	
		<script type="text/javascript">
		jQuery(document).ready(function($) {
			$('#<?php echo $args['widget_id']; ?> .news_archiv_list').bxSlider({
					mode: 'vertical',
					displaySlideQty: <?php echo $instance['number']; ?>,
					moveSlideQty: 1,
					hideControlOnEnd: true,
					adaptiveHeightSpeed:true
			});
		});		
		</script>
<?php
		// Reset the global $the_post as this query will have stomped on it
		wp_reset_postdata();

		endif;

		$cache[$args['widget_id']] = ob_get_flush();
		wp_cache_set('widget_news_archive', $cache, 'widget');
	}

	/**
	 * Deals with the settings when they are saved by the admin. Here is
	 * where any validation should be dealt with.
	 **/
	function update( $new_instance, $old_instance ) {
		$instance = $old_instance;
		$instance['title'] = strip_tags( $new_instance['title'] );
		$instance['number'] = (int) $new_instance['number'];
		$instance['textarea_newsarchiv'] = stripslashes($new_instance['textarea_newsarchiv']);
		
		$this->flush_widget_cache();

		$alloptions = wp_cache_get( 'alloptions', 'options' );
		if ( isset( $alloptions['widget_news_archive'] ) )
			delete_option( 'widget_news_archive' );

		return $instance;
	}

	function flush_widget_cache() {
		wp_cache_delete( 'widget_news_archive', 'widget' );
	}

	/**
	 * Displays the form for this widget on the Widgets page of the WP Admin area.
	 **/
	function form( $instance ) {
		$title = isset( $instance['title']) ? esc_attr( $instance['title'] ) : '';
		$number = isset( $instance['number'] ) ? absint( $instance['number'] ) : 10;
		$textarea_newsarchive = isset( $instance['textarea_newsarchiv'] ) ? stripslashes( $instance['textarea_newsarchiv'] ) : '';
?>
			<p><label for="<?php echo esc_attr( $this->get_field_id( 'title' ) ); ?>"><?php _e( 'Title:', 'fruitful' ); ?></label>
			<input class="widefat" id="<?php echo esc_attr( $this->get_field_id( 'title' ) ); ?>" name="<?php echo esc_attr( $this->get_field_name( 'title' ) ); ?>" type="text" value="<?php echo esc_attr( $title ); ?>" /></p>

			<p><label for="<?php echo esc_attr( $this->get_field_id( 'number' ) ); ?>"><?php _e( 'Number of posts to show:', 'fruitful' ); ?></label>
			<input id="<?php echo esc_attr( $this->get_field_id( 'number' ) ); ?>" name="<?php echo esc_attr( $this->get_field_name( 'number' ) ); ?>" type="text" value="<?php echo esc_attr( $number ); ?>" size="3" /></p>
			
			
			<p><label for="<?php echo esc_attr( $this->get_field_id( 'textarea_newsarchiv' ) ); ?>"><?php _e( 'Text Message:', 'fruitful' ); ?></label>
			<textarea id="<?php 	echo esc_attr( $this->get_field_id( 'textarea_newsarchiv' ) ); ?>" name="<?php echo esc_attr( $this->get_field_name( 'textarea_newsarchiv' ) ); ?>" class="widefat" cols="16" rows="5"><?php echo stripslashes( $textarea_newsarchive ); ?></textarea></p>
		<?php
	}
}