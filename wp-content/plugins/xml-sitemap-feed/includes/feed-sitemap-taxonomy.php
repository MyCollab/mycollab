<?php
/**
 * Google News Sitemap Feed Template
 *
 * @package XML Sitemap Feed plugin for WordPress
 */

status_header('200'); // force header('HTTP/1.1 200 OK') for sites without posts
header('Content-Type: text/xml; charset=' . get_bloginfo('charset', 'UTF-8'), true);

echo '<?xml version="1.0" encoding="'.get_bloginfo('charset', 'UTF-8').'"?>
<?xml-stylesheet type="text/xsl" href="' . plugins_url('xsl/sitemap.xsl.php',__FILE__) . '?ver=' . XMLSF_VERSION . '"?>
<!-- generated-on="'.date('Y-m-d\TH:i:s+00:00').'" -->
<!-- generator="XML & Google News Sitemap Feed plugin for WordPress" -->
<!-- generator-url="http://status310.net/wordpress-plugins/xml-sitemap-feed/" -->
<!-- generator-version="'.XMLSF_VERSION.'" -->
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 
		http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
';

global $xmlsf;

$taxonomy = get_query_var('taxonomy');
$lang = get_query_var('lang');

$terms = get_terms( $taxonomy, array(
					'orderby' => 'count',
					'order' => 'DESC',
					'lang' => $lang,
					'hierachical' => 0,
					'pad_counts' => true, // count child term post count too...
					'number' => 50000 ) );

if ( $terms ) : 

    foreach ( $terms as $term ) : 
    
	?>
	<url>
		<loc><?php echo get_term_link( $term ); ?></loc>
	 	<priority><?php echo $xmlsf->get_priority('taxonomy',$term); ?></priority>
		<?php echo $xmlsf->get_lastmod('taxonomy',$term); ?> 
		<changefreq><?php echo $xmlsf->get_changefreq('taxonomy',$term); ?></changefreq>
	</url>
<?php 
    endforeach;
endif; 

?></urlset>
<?php $xmlsf->_e_usage(); ?>
