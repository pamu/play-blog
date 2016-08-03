function toast(msg) {
    Materialize.toast(msg, 4000, 'rounded');
}

function loading(id) {
    $('#' + id).html('<div class="preloader-wrapper big active">' +
                          '<div class="spinner-layer spinner-blue-only">' +
                            '<div class="circle-clipper left">' +
                              '<div class="circle"></div>' +
                            '</div><div class="gap-patch">' +
                              '<div class="circle"></div>' +
                            '</div><div class="circle-clipper right">' +
                              '<div class="circle"></div>' +
                            '</div>' +
                          '</div>' +
                        '</div>');
}

function stopLoading(id) {
    $('#' + id).html('')
}
