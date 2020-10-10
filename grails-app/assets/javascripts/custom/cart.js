function addToCart(formId, resultsId) {
    var $form = $('#' + formId);
    var $feedbackDiv = $('#' + resultsId);
    $feedbackDiv.html('');

    $form .submit(function(event){
        event.preventDefault(); //prevent default action
        var submission_url = '/cart/addToCart' //get form action url
        var request_method = $(this).attr("method"); //get form GET/POST method
        var form_data = $(this).serialize(); //Encode form elements for submission

        $.ajax({
            url : submission_url,
            type: request_method,
            data : form_data
        }).done(function(response){
            $feedbackDiv.html(response);
        });
    });
}
