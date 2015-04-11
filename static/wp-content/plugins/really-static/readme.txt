=== Plugin Name ===
Contributors: eriksef
Donate link: http://really-static-support.php-welt.net/why-donate--t8.html
Tags: seo, cache, html, wp-super-cache, wp-cache, cacheing, performance, speed, amazon S3, S3, cdn, wp cache, super cache, ftp, Post, admin, posts, plugin, comments, images, links, page, rss, widget, static
Requires at least: 2.5.0
Tested up to: 4.0
Stable tag: 0.31

Generates static HTML-files from your Blog. That makes your Blog incredible fast! Better than any other cache.

== Description ==
	
Really-static generates static html-files out of your blog. Every time a Post is published/edited/delted or a comment is posted, changes will be automaticly written to the static blog.

If you have problems, informations about bugs or ideas <a href="http://really-static-support.php-welt.net/bugs-f8.html">report them to us(no registration needed, also guests can write new posts)</a>, please dont use the wordpress.org forum for question.

= Advantages =

 * its incredible fast, faster than any cache solution (<a href="http://really-static-support.php-welt.net/loading-times-benchmark-t84.html"> more benchmarks</a> )
 * saving static files via local, FTP, SFTP, ( and also on Amazons S3 CDN with a <a href="http://really-static-support.php-welt.net/amazon-s3-plugin-t7.html">plugin</a> )
 * many plugins for really-static, that can nearly everything 
 * its secure, because you can <a href="http://really-static-support.php-welt.net/comment-redirector-t9.html">hide your wordpressinstallation</a>
 * <a href="http://really-static-support.php-welt.net/rewrite-rules-and-refresh-by-time-t78.html">timebased updates and url rewrite rules</a>
 * if you dont have PHP/MySQL support on your server you can host your wordpressinstallation local and use a normal HTML webspace for publishing
 * work also with Multi-Site / Multi-blog installation (should be stable, but no garantie; every site must configured separately)
 
http://blog.phpwelt.net using really-static, so you can see how it works, just leave a comment!

= Credits: =

 + English
 + translation into German language 
 + translation into Japanese language by <a href="http://really-static-support.php-welt.net/japanese-language-t62.html">Tai</a>
 + translation to Spanish by <a href="http://sigt.net/">Hector Delcourt</a>
 + translation to Russian by <a href="http://www.comfi.com">M.Comfi</a>
 + translation to belorussian by <a href="http://www.antsar.info">ilyuha</a>
 + translation to Slovak by <a href="http://webhostinggeeks.com/user-reviews/">WebHostingGeeks.com</a>
 + translation to Serbian by <a href="http://wpdiscounts.com">WPdiscounts</a>
 + translation to Italian by <a href="http://really-static-support.php-welt.net/italian-translate-t80.html">Stefano</a>


= premium version =

 + auto-config
 + coustom aboutinfo
 + no Donateinfo
 + many many additional plugins
 + install support
 
== Installation ==

Really-static is preconfigured, so you just need to activate this plugin. 
There is a tool that help you, configuate your really-static version.
If you want only to try really-static, there is also a test-version. All generatet files are stored in a folder called "static". This ensures that you can test really-static as long as you want without somebody sees it.

== Frequently Asked Questions ==
= Questions and Bug reports =
If you got any Problem please use the debugfunktion and send me a report.
Or ask the <a href="http://really-static-support.php-welt.net/bugs-f8.html">community</a>


= I just get 0-Byte files = 
 please check your settings. 0-Byte files means that really static get errors while reading. 

= make RSS, Sitemap e.g. static =
http://blog.phpwelt.net/303-how-to-make-rss-files-static.html 

= Cachehit ? =
I implement this because its unessary update static page when the sourcefile didnt change. Its also better for Google, because if the static file dont change, the filedate also keeps the same and google ranks files that didnt change for a long time better!

= I only got webspace without PHP-support =
Download the free <a href="http://sourceforge.net/projects/miniserver/files/MiniServer/MiniServer_%20Wordpress/mini_server_16_wordpress_v1_1.zip/download">Uniform Server</a> that inlucdes a local on you PC runnig Webserver with installed Wordpress. Install an configuarte Really-Static Plugin and 
<a href="http://wordpress.org/extend/plugins/disqus-comment-system/">Disqus Comment Plugin</a>.

If you want to posting a new blogentry, start the Uniform Server on your PC. Than login to the now local on you PC runnig Wordpressinstallation and write your blogentry an puplish it. After this you can stop the uniform server. If someone getting on you internetwebsite he sees the static html files (generatet by really static) and he can post comments (because Disqus got there ownservers)

= I want additional features in Really-static =
<a href="http://really-static-support.php-welt.net/hooks-for-writing-your-own-plugin-t17.html">Programing your own really-static plugins!</a>

= Make Really-Static work with other Wordpressplugins =
$url= filename without siteprefix, that means just e.g.: "nonwordpresspage.html"

**Make a page static:**
> reallystaticsinglepagehook($url);

**Delete a page:**
> reallystaticsinglepagedeletehook($url);

> reallystaticdeletepage("");

== Screenshots ==

1. Statitics from Google sitemaps (1=No Cache,2=WP Super Cache,3=really static)
2. benchmark left side pure wordpress, right side after using really static
3. Destinationsettings (local,FTP,SFTP) S3 not installaled on this server
4. Settingsscreen
5. Rewrite Rules editor

== Changelog ==

for changelog please look in to our <a href="http://really-static-support.php-welt.net/development-f9.html">forum</a>

== Translations ==
really-static comes with various translations, located in the directory languages. if you would like to add a new translation, just take the file default.po and edit it to add your translations (e.g. with poedit).

== Upgrade Notice ==
please watch here: http://really-static-support.php-welt.net/development-f9.html