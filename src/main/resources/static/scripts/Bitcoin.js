function createOrder(){
    $.ajax({
        url: "/payment/create",
        contentType: "application/json",
        dataType: "text",
        type: "POST",
        data: JSON.stringify({
            "price": $("#price").val(),
            "priceCurrency": $("#priceCurrency :selected").val(),
            "receiveCurrency": $("#receiveCurrency :selected").val(),
            "paymentType": 'Bitcoin',
            "clientId": $("#clientId :selected").val()
        }),
        success: function(data) {
        	if(data != null)
        		window.location.replace(data.split("redirect:")[1]);
        }
    });

}

$( document ).ready(function() {
    
	$.ajax({
        url: "/client/getClients",
        contentType: "application/json",
        dataType: "text",
        type: "GET",
        success: function(data) {
        	if(data != null) {
        		clients = JSON.parse(data)
        		for (var i = 0; i < clients.length; i++){
        		    var client = clients[i];
    		        var attrName = client.id;
    		        var attrValue = client.title;
    		        var o = new Option(attrValue, attrName);
        			$(o).html(attrValue);
        			$("#clientId").append(o);
        		    
        		}
        		
        	}
        		
        }
    });
	
});
/*function getOrder(adId){
    $.ajax({
        url: "/bitcoin/getOrder/"+adId,
        dataType: "json",
        type: "GET",
        success: function(data) {
         
        }
    });

}*/


