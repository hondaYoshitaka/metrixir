var METRIXIR = METRIXIR || {};

METRIXIR.server = Object.assign({}, {
    protocol: 'http',
    host: '',
    api: {
        metrics: {
            method: 'POST',
            path: 'api/metrics'
        }
    }
}, METRIXIR.server);


window.onload = function () {
    var postEventLog = function (inputName, eventName, config) {
        var page = {
            location: {
                host: location.host,
                path: location.pathname
            }
        };
        var postData = Object.assign({
            name: inputName,
            event: eventName,
            clientTime: Date.now()
        }, page);

        var xhr = new XMLHttpRequest();
        xhr.open(
            config.api.metrics.method,
            config.protocol + '://' + config.host + '/' + config.api.metrics.path);

        xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
        xhr.withCredentials = true;

        xhr.send(JSON.stringify(postData));
    };

    var elements = document.getElementsByClassName('metrixir');

    for (var i = 0; i < elements.length; i++) {
        var inputs = elements[i].getElementsByTagName('input');

        for (var j = 0; j < inputs.length; j++) {
            var input = inputs[j];

            input.onfocus = function () {
                var input = this;

                postEventLog(input.name, 'focus', METRIXIR.server);
            };
            input.onblur = function () {
                var input = this;

                postEventLog(input.name, 'blur', METRIXIR.server);
            };
        }
    }

};
