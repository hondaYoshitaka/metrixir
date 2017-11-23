var METRIXIR = METRIXIR || {};

METRIXIR.server = {
    protocol: 'http',
    host: '',
    api: {
        metrics: 'api/metrics'
    }
};

$.fn.extend({
    "metrixir": function () {
        var area = this;
        var inputs = area.find(':input').not(':button');

        var page = {
            location: {
                host: location.host,
                path: location.pathname
            }
        };
        inputs.on({
            focus: function () {
                var input = $(this);

                var postData = Object.assign({
                    name: input.attr('name'),
                    event: 'focus',
                    clientTime: Date.now()
                }, page);

                $.ajax({
                    type: 'POST',
                    url: METRIXIR.server.protocol + '://' + METRIXIR.server.host + '/' + METRIXIR.server.api.metrics,
                    data: JSON.stringify(postData),
                    dataType: 'json',
                    async: true,
                    cache: false,
                    contentType: 'application/json; charset=utf-8'
                }).done(function (response) {
                    console.log('done');
                });
            },
            blur: function () {
                var input = $(this);
            }
        });
    }
});

$(function () {
    $('.metrixir').metrixir();
});