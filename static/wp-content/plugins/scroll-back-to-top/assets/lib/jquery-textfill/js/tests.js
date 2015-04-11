var JTF = 'jtf';


function setup(opts) {
  var $t = $('#qunit-fixture');
  var $d = $('<div/>', opts.div).appendTo($t);
  var $s = $('<span/>', opts.span).appendTo($d);
}


test('capped at 10px', function () {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({debug: true, maxFontPixels: 10});
  equal($span.css('font-size'), '10px');
});


test('size up to max', function () {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({debug: true, maxFontPixels: 0});
  equal($span.css('font-size'), '172px');
});


test('width be maxWidth', function () {
  setup({
    div: {
      id: JTF,
      width: 196,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({debug: true, maxFontPixels: 0});
  equal($span.css('font-size'), '119px');
});


test('height be maxHeight', function () {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 158
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({debug: true, maxFontPixels: 0});
  equal($span.css('font-size'), '158px');
});


test('minFontPixels too big to fit in', function () {
  setup({
    div: {
      id: JTF,
      width: 40,
      height: 40
    },
    span: {
      css: {
        'font-family': 'VT323',
        'font-size': '20px',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({debug: true, minFontPixels: 100, maxFontPixels: 0});
  equal($span.css('font-size'), '20px');
});


test('minFontPixels too big to fit in, but widthOnly = True and width fits', function () {
  // @ fontSize = 60 => H > 10, W = 100
  setup({
    div: {
      id: JTF,
      // just enough to fit in with minFontPixels
      width: 100,
      // H is busted, but widthOnly = True, so ignored
      height: 10
    },
    span: {
      css: {
        'font-family': 'VT323',
        'font-size': '20px',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({
    debug: true,
    minFontPixels: 20,
    maxFontPixels: 100,
    widthOnly: true
  });
  equal($span.css('font-size'), '60px');
});


test('minFontPixels too big to fit in, W/H both fail, even widthOnly = True', function () {
  setup({
    div: {
      id: JTF,
      width: 10,
      height: 10
    },
    span: {
      css: {
        'font-family': 'VT323',
        'font-size': '20px',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({
    debug: true,
    minFontPixels: 20,
    maxFontPixels: 100,
    widthOnly: true
  });
  equal($span.css('font-size'), '20px');
});


/*************/
/* callbacks */
/*************/

test('success callback', 1, function() {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({
    debug: true,
    maxFontPixels: 0,
    success: function(e) {
      equal(e, $jtf[0]);
    }
  });
});


test('callback callback (deprecated)', 1, function() {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({
    debug: true,
    maxFontPixels: 0,
    callback: function(e) {
      equal(e, $jtf[0]);
    }
  });
});


test('fail callback', 1, function () {
  setup({
    div: {
      id: JTF,
      width: 40,
      height: 40
    },
    span: {
      css: {
        'font-family': 'VT323',
        'font-size': '20px',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  var $span = $jtf.find('span');
  $jtf.textfill({
    debug: true,
    minFontPixels: 100,
    maxFontPixels: 0,
    fail: function(e) {
      equal(e, $jtf[0]);
    }
  });
});


test('complete callback', 2, function() {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  $jtf.textfill({
    debug: true,
    maxFontPixels: 0,
    callback: function(e) {
      equal(e, $jtf[0]);
    },
    complete: function(e) {
      equal(e, $jtf);
    }
  });
});


/****************/
/* debug option */
/****************/

module('debug option', {
  setup: function () {
    if (console.debug_original) {
      throw 'console.debug_original already has value.';
    }
    console.debug_original = console.debug;
    console.debug_called = false;
    console.debug = function () {
      console.debug_called = true;
    }
  },
  teardown: function () {
    if (!console.debug_original) {
      throw 'console.debug_original is empty.';
    }
    console.debug = console.debug_original;
    console.debug_original = undefined;
    console.debug_called = undefined;
  }
});


test('debug used', function () {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  $jtf.textfill({debug: true, maxFontPixels: 10});
  equal(console.debug_called, true);
});


test('debug not used', function () {
  setup({
    div: {
      id: JTF,
      width: 285,
      height: 210
    },
    span: {
      css: {
        'font-family': 'VT323',
      },
      text: 'test'
    }
  });

  var $jtf = $('#' + JTF);
  $jtf.textfill({debug: false, maxFontPixels: 10});
  equal(console.debug_called, false);
});


