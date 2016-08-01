$(function() {
    function sigin() {
        $("#gmail_signin").click(function() {
            console.log("gmail signin button clicked");
            window.location="/initiateLogin";
            $('#gmail_signin').prop('disabled', true);
            $("#gmail_signin").val('Please wait ...');
        });
    }
    sigin();
});