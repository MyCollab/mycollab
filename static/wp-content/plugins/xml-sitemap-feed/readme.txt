=== XML Sitemap & Google News feeds ===
Contributors: RavanH
Donate link: https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ravanhagen%40gmail%2ecom&item_name=XML%20Sitemap%20Feed&item_number=3%2e8&no_shipping=0&tax=0&bn=PP%2dDonationsBF&charset=UTF%2d8&lc=us
Tags: sitemap, xml sitemap, news sitemap, sitemap.xml, robots.txt, Google, Google News, Yahoo, Bing, , Yandex, Baidu, seo, feed, polylang, image sitemap
Requires at least: 3.2
Tested up to: 4.0
Stable tag: 4.3.2

Feeds from the XML Sitemap and Google News menu for the hungry spiders. Multisite compatible.

== Description ==

This plugin dynamically creates feeds that comply with the **XML Sitemap** and the **Google News Sitemap** protocol. **Multisite** and **Polylang** compatible and there are no files created. Options can be found on **Settings > Reading** to control which sitemaps, which post and taxonomy types are included, how priority is calculated, who to ping and set additional robots.txt rules. 

The main advantage of this plugin over other XML Sitemap plugins is **simplicity**. No need to change file or folder permissions, move files or spend time tweaking difficult plugin options.

You, or site owners on your Multisite network, will not be bothered with complicated settings like most other XML Sitemap plugins. The default settings will suffice in most cases and XML sitemap values like ChangeFreq and URL Priority are auto-calculated based on post age and comment activity.

The XML Sitemap Index becomes instantly available on yourblog.url/sitemap.xml (or yourblog.url/?feed=sitemap) containing references to posts and pages by default, ready for indexing by search engines like Google, Bing, Yahoo, AOL and Ask. When the Google News Sitemap is activated, it will become available on yourblog.url/sitemap-news.xml (or yourblog.url/?feed=sitemap-news), ready for indexing by Google News. Both are automatically referenced in the dynamically created **robots.txt** on yourblog.url/robots.txt to tell search engines where to find your XML Sitemaps. Google and Bing can be pinged on each new publication.

Please read the FAQ's for info on how to get your articles listed on Google News.

**Compatible with caching plugins** like WP Super Cache, W3 Total Cache and Quick Cache that cache feeds, allowing a faster serving to the impatient (when hungry) spider.

**NOTES:** 

1. If you _do not use fancy URL's_ or you have WordPress installed in a _subdirectory_, a dynamic **robots.txt will NOT be generated**. You'll have to create your own and upload it to your site root! See FAQ's.

2. On large sites, it is advised to use a good caching plugin like **WP Super Cache**, **Quick Cache** or **W3 Total Cache** to improve your site _and_ sitemap performance.

= Features = 

* Sitemap Index with optional inclusion of sitemaps for post types, categories and tags.
* Optional Google News sitemap.
* Completely **automatic** post URL _priority_ and _change frequency_ calculation based on post age and comment and trackback activity.
* Works out-of-the-box, even on **multi-site / shared codebase / multi-blog setups** like WordPress MU, WP 3.0 in MultiSite mode and others. 
* Optionally include Image tags with caption and title for featured images or attached images in both regular and Google News sitemaps.
* Pings Google, Bing & Yahoo and optionally Yandex and Baidu on new post publication.
* Compatible with multi-lingual sites using **Polylang** to allow all languages to be indexed equally.
* Options to define which post types and taxonomies get included in the sitemap and automatic priority calculation rules.
* Set priority per post.
* Exclude individual posts or pages.
* Option to add new robots.txt rules. These can be used to further control (read: limit) the indexation of various parts of your site and subsequent spread of pagerank accross your sites pages.
* Includes XLS stylesheets for human readable sitemaps.


= Translations =

- **Dutch** * R.A. van Hagen http://status301.net (version 4.3)
- **French** * R.A. van Hagen http://status301.net (version 4.2) (improved translation or suggestions welcome)
- **Indonesian** * Nasrulhaq Muiz http://al-badar.net/ (version 4.2)
- **Italian** * Raffaello Tesi http://www.raffaellotesi.com  (version 4.3.2)
- **Serbian** * WPdiscounts http://wpdiscounts.com/ (version 4.1) 
- **Spanish** * Andrew Kurtis - WebHostingHub Support http://www.webhostinghub.com/ (version 4.3.2)
- **Ukrainian** * Cmd Software http://www.cmd-soft.com/ (version 4.0) 

New transtations will be accepted and listed here. See translation instructions under [Other Notes](http://wordpress.org/plugins/xml-sitemap-feed/other_notes/).

= Credits =

XML Sitemap Feed was originally based on the discontinued plugin Standard XML Sitemap Generator by Patrick Chia. Since then, it has been completely rewritten and extended in many ways.


== Installation ==

= Wordpress =

Quick installation: [Install now](http://coveredwebservices.com/wp-plugin-install/?plugin=xml-sitemap-feed) !

 &hellip; OR &hellip;

Search for "xml sitemap feed" and install with that slick **Plugins > Add New** back-end page.

 &hellip; OR &hellip;

Follow these steps:

1. Download archive.

2. Upload the zip file via the Plugins > Add New > Upload page &hellip; OR &hellip; unpack and upload with your favourite FTP client to the /plugins/ folder.

3. Activate the plugin on the Plugins page.

4. If you have been using another XML Sitemap plugin before, check your site root and remove any created sitemap.xml file that remained there.

Done! Check your sparkling new XML Sitemap by visiting yourblogurl.tld/sitemap.xml (adapted to your domain name ofcourse) with a browser or any online XML Sitemap validator. You might also want to check if the sitemap is listed in your yourblogurl.tld/robots.txt file.

= WordPress 3+ in Multi Site mode =

Same as above but do a **Network Activate** to make a XML sitemap available for each site on your network.

Installed alongside [WordPress MU Sitewide Tags Pages](http://wordpress.org/plugins/wordpress-mu-sitewide-tags/), XML Sitemap Feed will **not** create a sitemap.xml nor change robots.txt for any **tag blogs**. This is done deliberately because they would be full of links outside the tags blogs own domain and subsequently ignored (or worse: penalised) by Google.


== Frequently Asked Questions ==

= Where are the options? =

See the XML Sitemaps section on **Settings > Reading**.

= How do I get my latest articles listed on Google News? =

Go to [Suggest News Content for Google News](http://www.google.com/support/news_pub/bin/request.py?contact_type=suggest_content) and submit your website info as detailed as possible there. Give them the URL(s) of your fresh new Google News Sitemap in the text field 'Other' at the bottom.

You will also want to add the sitemap to your [Google Webmasters Tools account](https://www.google.com/webmasters/tools/) to check its validity and performance. Create an account if you don't have one yet.

= My Google News Sitemap is empty! =

The rules of the Google News game are that you do not feed the monster any stale food. Older than 2 days is bad. You need to whip up some fresh chow ;)

= Can I manipulate values for priority and changefreq? =

Yes. You can find default settings for priority, changefreq and lastmod on **Settings > Reading**. A fixed priority can be set on a post by post basis too.

= Do I need to submit the sitemap to search engines? =

No. In normal circumstances, your site will be indexed by the major search engines before you know it. The search engines will be looking for a robots.txt file and (with this plugin activated) find a pointer in it to the XML Sitemap on your blog. The search engines will return on a regular basis to see if your site has updates. 

Besides that, Google and Bing are pinged upon each new publication.

**NOTE:** If you have a server _without rewrite rules_, use your blog _without fancy URLs_ (meaning, you have WordPress Permalinks set to the old default value) or have it installed in a _subdirectory_, then read **Do I need to change my robots.txt** for more instructions.

= Does this plugin ping search engines? =

Yes, Google and Bing are pinged upon each new publication. Unless you disable this feature on **Settings > Reading**.

= Do I need to change my robots.txt? =

That depends. In normal circumstances, if you have no physical robots.txt file in your site root, the new sitemap url will be automatically added to the dynamic robots.txt that is generated by WordPress. But in some cases this might not be the case.

If you use a static robots.txt file in your website root, you will need to open it in a text editor. If there is already a line with `Sitemap: http://yourblogurl.tld/sitemap.xml` you can just leave it like it is. But if there is no sitemap referrence there, add it (adapted to your site url) to make search engines find your XML Sitemap. 

Or if you have WP installed in a subdirectory, on a server without rewrite_rules or if you do not use fancy URLs in your Permalink structure settings. In these cases, WordPress will need a little help in getting ready for XML Sitemap indexing. Read on in the **WordPress** section for more.

= My WordPress powered blog is installed in a subdirectory. Does that change anything? =

That depends on where the index.php and .htaccess of your installation reside. If they are in the root while the rest of the WP files are installed in a subdir, so the site is accessible from your domain root, you do not have to do anything. It should work out of the box. But if the index.php is together with your wp-config.php and all other WP files in a subdir, meaning your blog is only accessible via that subdir, you need to manage your own robots.txt file in your **domain root**. It _has_ to be in the root (!) and needs a line starting with `Sitemap:` followed by the full URL to the sitemap feed provided by XML Sitemap Feed plugin. Like:
`
Sitemap: http://yourblogurl.tld/subdir/sitemap.xml
` 

If you already have a robots.txt file with another Sitemap reference like it, just add the full line below or above it.

= Do I need to use a fancy Permalink structure? =

No. While I would advise you to use any one of the nicer Permalink structures for better indexing, you might not be able to (or don't want to) do that. If so, you can still use this plugin: 

Check to see if the URL yourblog.url/?feed=sitemap does produce a feed. Now manually upload your own robots.txt file to your website root containing: 
`
Sitemap: http://yourblog.url/?feed=sitemap

User-agent: *
Allow: /
`
You can also choose to notify major search engines of your new XML sitemap manually. Start with getting a [Google Webmasters Tools account](https://www.google.com/webmasters/tools/) and submit your sitemap for the first time from there to enable tracking of sitemap downloads by Google! or head over to [XML-Sitemaps.com](http://www.xml-sitemaps.com/validate-xml-sitemap.html) and enter your sites sitemap URL.

= Can I change the sitemap name/URL? =

No. If you have fancy URL's turned ON in WordPress (Permalinks), the sitemap url that you manually submit to Google (if you are impatient) should be `yourblogurl.tld/sitemap.xml` but if you have the Permalinks' Default option set the feed is only available via `yourblog.url/?feed=sitemap`.

= Where can I customize the xml output? =

You may edit the XML output in `xml-sitemap-feed/feed-sitemap.php` but be careful not to break Sitemap protocol compliance.  Read more on [Sitemaps XML format](http://www.sitemaps.org/protocol.php).

The stylesheet (to make the sitemap human readable) can be edited in `xml-sitemap-feed/sitemap.xsl.php`.

Note: your modifications will be overwritten upon the next plugin upgrade!

= I see no sitemap.xml file in my site root! =

The sitemap is dynamically generated just like a feed. There is no actual file created.

= I see a sitemap.xml file in site root but it does not seem to get updated! =

You are most likely looking at a sitemap.xml file that has been created by another XML Sitemap plugin before you started using this one. Remove that file and let the plugin dynamically generate it just like a feed. There will not be any actual files created.

If that's not the case, you are probably using a caching plugin or your browser does not update to the latest feed output. Please verify.

= I use a caching plugin but the sitemap is not cached =

Some caching plugins have the option to switch on/off caching of feeds. Make sure it is turned on. 

Frederick Townes, developer of **W3 Total Cache**, says: "There's a checkbox option on the page cache settings tab to cache feeds. They will expire according to the expires field value on the browser cache setting for HTML."

The Google News sitemap is designed to NOT be cached.

= I get an ERROR when opening the sitemap or robots.txt! = 

The absolute first thing you need to check is your blogs privacy settings. Go to **Settings > Privacy** and make sure you are **allowing search engines to index your site**. If they are blocked, your sitemap will _not_ be available.

If that did not solve the issue, check the following errors that might be encountered along with their respective solutions:

**404 page instead of my sitemap.xml**

Try to refresh the Permalink structure in WordPress. Go to Settings > Permalinks and re-save them. Then reload the XML Sitemap in your browser with a clean browser cache. ( Try Ctrl+R to bypass the browser cache -- this works on most but not all browsers. )

**404 page instead of both sitemap.xml and robots.txt**

There are plugins like Event Calendar (at least v.3.2.beta2) known to mess with rewrite rules, causing problems with WordPress internal feeds and robots.txt generation and thus conflict with the XML Sitemap Feed plugin. Deactivate all plugins and see if you get a basic robots.txt file showing: 
`
User-agent: *
Disallow:
`
Reactivate your plugins one by one to find out which one is causing the problem. Then report the bug to the plugin developer. 

**404 page instead of robots.txt while sitemap.xml works fine**

There is a know issue with WordPress (at least up to 2.8) not generating a robots.txt when there are _no posts_ with _published_ status. If you use WordPress as a CMS with only _pages_, this will affect you. 

To get around this, you might either at least write one post and give it _Private_ status or alternatively create your own robots.txt file containing:
`
Sitemap: http://yourblog.url/sitemap.xml

User-agent: *
Allow: /
`
and upload it to your web root...

**Error loading stylesheet: An unknown error has occurred**

On some setups (usually using the WordPress MU Domain Mapping plugin) this error occurs. The problem is known, the cause is not... Until I find out why this is happening, please take comfort in knowing that this only affects reading the sitemap in normal browsers but will NOT affect any spidering/indexing on your site. The sitemap is still readable by all search engines! 

**XML declaration allowed only at the start of the document**

This error occurs when blank lines or other output is generated before the start of the actual sitemap content. This can be caused by blank lines at the beginning of wp-config.php or your themes functions.php or by another plugin that generates output where it shouldn't. You'll need to test by disabling all other plugins, switching to the default theme and manually inspecting your wp-config.php file.

= I see only a BLANK (white) page when opening the sitemap =

There are several cases where this might happen.

Open your sitemap in a browser and look at the source code. This can usually be seen by hitting Ctrl+U or right-click then select 'View source...' Then scan the produced source (if any) for errors.

A. If you see strange output in the first few lines (head tags) of the source, then there is a conflict or bug occuring on your installation. Please go to the [Support forum](http://wordpress.org/support/plugin/xml-sitemap-feed) for help.

B. If the source is empty or incomplete then you're probably experiencing an issue with your servers PHP memory limit. In those cases, you should see a messages like `PHP Fatal error: Allowed memory size of xxxxxx bytes exhausted.` in your server/account error log file.

This can happen on large sites. To avoid these issues, there is an option to split posts over different sitemaps on Settings > Reading. Try different settings, each time revisiting the main sitemap index file and open different sitemaps listed there to check. 

Read more on [Increasing memory allocated to PHP](http://codex.wordpress.org/Editing_wp-config.php#Increasing_memory_allocated_to_PHP) (try a value higher than 256M) or ask your hosting provider what you can do.

= Can I run this on a WPMU / WP3+ Multi-Site setup? =

Yes. In fact, it has been designed for it. Tested on WPMU 2.9.2 and WPMS 3+ both with normal activation and with Network Activate / Site Wide Activate.


== Translation ==

1. Install PoEdit on your computer.
1. Go to this plugins /languages/ directory.
1. If there is no .po file that corresponds with your language yet, rename the template translation database xml-sitemap-feed-xx_XX.po by replacing the xx with your language code and XX with your country code.
1. Open the .po file of your language with PoEdit. 
1. Go to Edit > Preferences and on the tab Editor check the option to compile a .mo database on save automatically. Close with OK.
1. Go to Catalog > Settings and set your name, e-mail address, language and country. Close with OK.
1. Go to Catalog > Update from POT-file and select the main xml-sitemap-feed.pot file. Then accept all new and removed translation strings with OK.
1. Now go ahead and start translating all the texts listed in PoEdit.
1. When done, go to File > Save to Save.
1. Upload the automatically created xml-sitemap-feed-xx_XX.mo database file (where xx_XX should now be your language and country code) to the plugins /languages/ directory on your WordPress site.
1. After verifying the translations work on your site, send the .mo file and, if you're willing to share it, your original .po file to ravanhagen@gmail.com and don't forget to tell me how and with what link you would like to be mentioned in the credits!

Thanks for sharing your translation :)


== Screenshots ==

1. XML Sitemap feed viewed in a normal browser. For human eyes only ;)
2. XML Sitemap source as read by search engines.


== Upgrade Notice ==

= 4.3.2 =
Custom domains and URLs. Major Google News sitemap settings changes. Plus bugfixes.


== Changelog ==

= 4.3.2 =
* BUGFIX: html esc / filter image title and caption tags
* BUGFIX: empty terms counted causing empty taxonomy sitemap appearing in index
* BUGFIX: custom taxonomies where lastmod cannot be determined show empty lastmod tag

= 4.3 =
* Google News sitemap settings section
* Google News tags: access, genres, keywords, geo_locations
* Improved Google News stylesheet
* Custom Google News Publication Name
* Image tags in Google News sitemap
* Custom URLs
* Allow additional domains
* Image caption and title tags
* Ping Yandex and Baidu optional
* BUGFIX: Ineffective robots.txt rules
* BUGFIX: Priority value 0 in post meta not saved
* BUGFIX: Ping for all post types
* BUGFIX: Custom taxonomy support
* BUGFIX: Split by month shows year

= 4.2.4 =
* NEW: Image tags
* Rearranged settings section
* FIX: replace permalink, title and bloginfo rss filter hooks with own

= 4.2.3 = 
* BUGFIX: Empty ping options after disabling the main sitemap
* BUGFIX: Empty language tag for Google News tags in posts sitemap
* Small back end changes
* NEW: Custom post types split by year/month

= 4.2 =
* NEW: Image & News tags 
* NEW: Exclude pages/posts

= 4.1.4 =
* BUGFIX: Pass by reference fatal error in PHP 5.4
* BUGFIX: issue with Polylang language code in pretty permalinks setting
* BUGFIX: unselected post types in sitemap
* BUGFIX: 1+ Priority for sticky posts with comments
* Dutch and French translations updated

= 4.1 =
* NEW: Ping Google and Bing on new publications
* NEW: Set priority per post
* NEW: Priority calculation options 
* NEW: Option to split posts by year or month for faster generation of each sitemap
* Reduced queries to increase performance
* Improved Lastmod and Changefreq calculations
* Core class improvements
* Dropped qTranslate support
* Dropped PHP4 support
* BUGFIX: removed several PHP notices

= 4.0.1 =
* NEW: Dutch and French translations
* BUGFIX: Non public sites still have sitemap by default
* BUGFIX: Invalid argument supplied for foreach() when all post types are off
* BUGFIX: Wrong translation dir

= 4.0.0 =
* Moved to sitemap index and seperated post/page sitemaps
* NEW: options to dswitch off sitemap and news sitemap
* NEW: select which post types to include
* NEW: select which taxonomies to include
* NEW: set additional robots.txt rules
* NEW: Translation POT catalogue
* Improved Polylang support
* Dropped xLanguage support
* qTranslate currently untested

= 3.9.2 =
* Basic Google News feed stylesheet
* improvement on XSS vulnerability fix
* Fixed trailing slash

= 3.9.1 =
* SECURITY: XSS vulnerability in sitemap.xsl.php

= 3.9 =
* Google News Sitemap
* Memory limit error workaround (for most sites)

= 3.8.8 =
* BUGFIX: PHP4 compatibility
* BUGFIX: stylesheet URL when installed in mu-plugins
* core change to class
* minified sitemap output by default

= 3.8.5 =
* **xLanguage support** based on code and testing by **Daniele Pelagatti**
* new FILTER HOOK `robotstxt_sitemap_url` for any translate and url changing plugins.
* BUGFIX: Decimal separator cannot be a comma! 

= 3.8.3 =
* filter out external URLs inserted by plugins like Page Links To (thanks, Francois)
* minify sitemap and stylesheet output
* BUGFIX: qTranslate non-default language home URL

= 3.8 =
* **qTranslate support**
* no more Sitemap reference in robots.txt on non-public blogs

= 3.7.4 =
* switch from `add_feed` (on init) to the `do_feed_$feed` hook
* BUGFIX: `is_404()` condition TRUE and Response Header 404 on sites without posts
* BUGFIX: `is_feed()` condition FALSE after custom query_posts
* BUGFIX: no lastmod on home url when only pages on a site
* BUGFIX: stylesheet url wrong when WP installed in a subdir

= 3.7 =
* massive changefreq calculation improvement
* further priority calulation improvement taking last comment date into account

= 3.6.1 =
* BUGFIX: wrong date calculation on blogs less than 1 year old

= 3.6 =
* massive priority calculation improvement

= 3.5 =
* complete rewrite of plugin internals
* speed improvements
* WP 3.0 (normal and MS mode) ready

= 3.4 =
* BUGFIX: use home instead of siteurl for blog url for sitemap reference in robots.txt
* code streamline and cleanup

= 3.3 =
* automatic exclusion of tags blog in wpmu

= 3.2 =
* rewrite and add_feed calls improvements
* BUGFIX: double entry when static page is frontpage

= 3.0 =
* added styling to the xml feed to make it human readable

= 2.1 =
* BUGFIX: lastmod timezone offset displayed wrong (extra space and missing double-colon)

= 2.0 =
* priority calculation based on comments and age
* changefreq based on comments 

= 1.0 =
* changed feed template location to avoid the need to relocate files outside the plugins folder
* BUGFIX: `get_post_modified_time` instead of `get_post_time`

= 0.1 =
* rework from Patrick Chia's [Standard XML Sitemaps](http://wordpress.org/plugins/standard-xml-sitemap/)
* increased post urls limit from 100 to 1000 (of max. 50,000 allowed by the Sitemap protocol)
