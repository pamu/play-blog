$(function() {
    function sigin() {
        $("#gmail_signin").click(function() {
            console.log("gmail signin button clicked");
            window.location="/initiateLogin";
            $("#gmail_signin").html('Please wait ...');
            $("#gmail_signin").click(function(){});
            $('#gmail_signin').addClass('disabled');
        });
    }
    sigin();
});