var METRIXIR = METRIXIR || {};

METRIXIR.server = {
    protocol: 'http',
    host: '',
    api: {
        metrics: {
            method: 'POST',
            path: 'api/metrics'
        }
    }
};

$.fn.extend({
    "metrixir": function () {
        var area = this;
        var inputs = area.find(':input').not(':button');

        inputs.on({
            onEvent: function (e, eventName, config) {
                var input = $(this);

                console.log(input.attr('name'));

                var page = {
                    location: {
                        host: location.host,
                        path: location.pathname
                    }
                };
                var postData = Object.assign({
                    name: input.attr('name'),
                    event: eventName,
                    clientTime: Date.now()
                }, page);

                $.ajax({
                    type: config.api.metrics.method,
                    url: config.protocol + '://' + config.host + '/' + config.api.metrics.path,
                    data: JSON.stringify(postData),
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8'
                }).done(function (response) {
                    // nop
                });
            },
            focus: function () {
                $(this).trigger('onEvent', ['focus', METRIXIR.server])
            },
            blur: function () {
                $(this).trigger('onEvent', ['blur', METRIXIR.server])
            }
        });
    }
});

$(function () {
    $.ajaxSetup({
        async: true,
        cache: false,
        xhrFields: {
            withCredentials: true
        }
    });

    $('.metrixir').metrixir();
});