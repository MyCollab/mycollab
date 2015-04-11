 //Javascript fuer das Tab Menue
  $(function () {
    var rstatic_panels = $("div.tabs > div");
	
    $("div.tabs ul.tabNavigation li").filter(strID).addClass("rstatic_active");
	
    rstatic_panels.hide().filter(strID).show();
 
    $("div.tabs ul.tabNavigation a").click(function () {
      rstatic_panels.hide();
      rstatic_panels.filter(this.hash).show();
 document.expert.strid.value=this.hash;
	   
     $("div.tabs ul.tabNavigation li").removeClass("rstatic_active");
	  
   $(this).parent().addClass("rstatic_active");
 
   //   $(this).addClass("rstatic_active");
      return false;
    }).filter(strID).click();
  });

    function toggleVisibility(id) {
       var e = document.getElementById(id);
       if(e.style.display == "inline")
          e.style.display = "none";
       else
          e.style.display = "inline";
    }
	function showexpert(a){
 
	if(a.checked==true){ 
		 document.getElementById("rs_settings").style.display="inline";
		document.getElementById("rs_advanced").style.display="inline";
		document.getElementById("rs_reset").style.display="inline";
	}else{ 
		 document.getElementById("rs_settings").style.display="none";
		document.getElementById("rs_advanced").style.display="none";
		document.getElementById("rs_reset").style.display="none";
	}
	
	
	}