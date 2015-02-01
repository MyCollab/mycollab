/* Sticky Tooltip script (v1.0)
* Created: Nov 25th, 2009. This notice must stay intact for usage 
* Author: Dynamic Drive at http://www.dynamicdrive.com/
* Visit http://www.dynamicdrive.com/ for full source code
*/
var mousePosition = {
  currentMousePos :{ x: -1, y: -1 },
  getMousePosition:function() { 
    jQuery(function($) {
        $(document).mousemove(function(event) {
          mousePosition.currentMousePos.x = event.pageX;
          mousePosition.currentMousePos.y = event.pageY;
        });
        $(document).click(function(e) { 
          $('.stickytooltip').hide();
        });
    });
  }
}
// bind event mouse move for body document
mousePosition.getMousePosition();

var stickytooltip = {
  tooltipoffsets: [20, -30], // additional x and y offset from mouse cursor
                // for tooltips
  fadeinspeed: 200, // duration of fade effect in milliseconds

  // ***** NO NEED TO EDIT BEYOND HERE

  isdocked: false,

  hidebox:function($, $tooltip) {
    $tooltip.stop(false, true).hide()
  },

  docktooltip:function($, $tooltip, e) {
    this.isdocked=true
  },

  displayTooltip:function($, $tooltip, position) {
    $tooltip.fadeIn(this.fadeinspeed)
    var x = position.x + this.tooltipoffsets[0], y = position.y + this.tooltipoffsets[1]
    var tipw = $tooltip.outerWidth(), tiph = $tooltip.outerHeight(),
    x = (x+tipw > $(document).scrollLeft() + $(window).width())? x - tipw - (stickytooltip.tooltipoffsets[0] * 2) : x
    y = (y+tiph > $(document).scrollTop() + $(window).height())? $(document).scrollTop() + $(window).height()- tiph : y
    $tooltip.css({left:x, top:y})
  },

  init:function(targetselector, tipid) { 
    $('.stickytooltip').stop(false, true).hide();
    var $targets=$(targetselector)
    var $tooltip=$('#' + tipid).appendTo(document.body)
    if ($targets.length == 0)
      return

    stickytooltip.displayTooltip($, $tooltip, mousePosition.currentMousePos)

    $tooltip.bind('mouseleave',function() {
      stickytooltip.hidebox($, $tooltip)
    })

    $tooltip.bind('click', function(e) { 
      e.stopPropagation()
    })

    $(this).bind("click", function(e) { 
      if (e.button==0) { 
        stickytooltip.isdocked=false
        stickytooltip.hidebox($, $tooltip)
      }
    })
  }
}

function overIt(uid, type, typeId, url, sAccountId, siteURL, timeZone, locale) {
  var idDIVserverdata = "div14" + uid;
  var idStickyToolTipDiv = "div1" + uid;
  var idTagA = "tag" + uid;
  $('#'+idStickyToolTipDiv).stop(false, true).hide();
  $('.stickytooltip').bind('mouseleave',function(e) { 
    $('.stickytooltip').stop(false, true).hide();
  });
  if($("#" + idDIVserverdata).html()== "") { 
    $.ajax({
      type: 'POST',
      url: url,
      data : { type: type, typeId: typeId , sAccountId : sAccountId, siteURL: siteURL , timeZone: timeZone, locale:locale},
      success: function(data) { 
        if(data.trim()!= "null") {
          $("#" + idTagA).attr('data-tooltip', idStickyToolTipDiv);
          $("#" + idDIVserverdata).html(data);
          stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
        }
      }
    });
  } else {
    stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
  }
}

function projectOverViewOverIt(uid, type, typeId, url, sAccountId, siteURL, timeZone, locale) {
  var idDIVserverdata = "div14" + uid;
  var idStickyToolTipDiv = "div1" + uid;
  var idTagA = "tag" + uid;
  $('#' + idStickyToolTipDiv).stop(false, true).hide();
  $('.stickytooltip').bind('mouseleave',function(e) { 
      $('.stickytooltip').stop(false, true).hide();
  });
  if($("#" + idDIVserverdata).html()== "") {
    $.ajax({
        type: 'POST',
        url: url,
        data : { type: type, typeId: typeId , sAccountId : sAccountId, siteURL: siteURL, timeZone: timeZone, locale:locale},
          success: function(data) { 
            if(data.trim()!= "null") { 
              $("#" + idTagA).attr('data-tooltip', idStickyToolTipDiv);
              $("#" + idDIVserverdata).html(data);
              stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
        }
      }
      });
  } else {
      stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
  }
}

function crmActivityOverIt(uid, type, typeId, url, sAccountId, siteURL, timeZone, locale) { 
  var idDIVserverdata = "div14" + uid;
  var idStickyToolTipDiv = "div1" + uid;
  var idTagA = "tag" + uid;
  $('#' + idStickyToolTipDiv).stop(false, true).hide();
  $('.stickytooltip').bind('mouseleave',function(e) { 
      $('.stickytooltip').stop(false, true).hide();
  });
  if(type=="Task") type="CRMTask";
  if($("#" + idDIVserverdata).html()== "") { 
    $.ajax({
      type: 'POST',
      url: url,
      data : { type: type, typeId: typeId , sAccountId : sAccountId, siteURL: siteURL , timeZone: timeZone, locale:locale},
      success: function(data) { 
        if(data.trim()!= "null") { 
          $("#" + idTagA).attr('data-tooltip', idStickyToolTipDiv);
          $("#" + idDIVserverdata).html(data);
          stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
        }
      }
    });
  } else {
    stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
  }
}

function showUserTooltip(uid, username, url, siteURL, timeZone, sAccountId, locale) {
  var idDIVserverdata = "div14" + uid;
  var idStickyToolTipDiv = "div1"+uid;
  var idTagA = "tag"+ uid;
  $('#' + idStickyToolTipDiv).stop(false, true).hide();
  $('.stickytooltip').bind('mouseleave',function(e) { 
      $('.stickytooltip').stop(false, true).hide();
  });
  if($("#" + idDIVserverdata).html()== "") { 
    $.ajax({
      type: 'POST',
      url: url,
      data : { type: "User", username: username ,siteURL: siteURL , timeZone: timeZone, sAccountId:sAccountId, locale:locale},
      success: function(data) { 
        if(data.trim()!= "null") { 
          $("#" + idTagA).attr('data-tooltip', idStickyToolTipDiv);
          $("#" + idDIVserverdata).html(data);
          stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
        }
      }
    });
  } else {
    stickytooltip.init("*[data-tooltip]", idStickyToolTipDiv);
  }
}

function hideTooltip(uid) {

  $('.stickytooltip').stop(false, true).hide();
}