var orderId;
Url = {
	    get get(){
	        var vars= {};
	        if(window.location.search.length!==0)
	            window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value){
	                key=decodeURIComponent(key);
	                if(typeof vars[key]==="undefined") {vars[key]= decodeURIComponent(value);}
	                else {vars[key]= [].concat(vars[key], decodeURIComponent(value));}
	            });
	        return vars;
	    }
	};
$(document).ready ( function(){
    var pathname = window.location.href;
    var orderId = pathname.indexOf("orderId");
    if(pathname.indexOf("orderId") > -1){
    	orderId=Url.get.orderId;
        updateOrder(orderId);
    }
});

function updateOrder(orderId){
	 $.ajax({
	        url: "/payment/updateOrder",
	        contentType: "application/json",
	        dataType: "text",
	        type: "POST",
	        data: JSON.stringify({
	            "orderId": orderId,
	            "status": "CANCEL",
	        }),
	        success: function(data) {
	        	
	        }
	    });

}