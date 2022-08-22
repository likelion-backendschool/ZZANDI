$(document).ready(function () {
    $('#book').hide();
    $('#lecture').hide();
});

function dis() {
    if ($('#book').css('display') == 'none') {
        $('#book').show();
    } else {
        $('#book').hide();
    }

    if ($('#lecture').css('display') == 'none') {
        $('#lecture').show();
    } else {
        $('#lecture').hide();
    }
}