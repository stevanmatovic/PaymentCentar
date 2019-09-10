function createOrder(){
    $.ajax({
        url: "/payment/create",
        contentType: "application/json",
        dataType: "text",
        type: "POST",
        data: JSON.stringify({
            "price": $("#price").val(),
            "priceCurrency": $("#priceCurrency").val(),
            "receiveCurrency": $("#receiveCurrency").val(),
            "paymentType": 'Bitcoin'
        }),
        success: function(data) {
        	if(data != null)
        		window.location.replace(data.split("redirect:")[1]);
        }
    });

}

/*function getOrder(adId){
    $.ajax({
        url: "/bitcoin/getOrder/"+adId,
        dataType: "json",
        type: "GET",
        success: function(data) {
         
        }
    });

}*/


