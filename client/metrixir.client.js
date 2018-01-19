var METRIXIR = METRIXIR || {};

METRIXIR.server = Object.assign({}, {
    protocol: 'http',
    host: '',
    tag: 'default',
    api: {
        metrics: {
            method: 'POST',
            path: 'api/metrics'
        }
    }
}, METRIXIR.server);

METRIXIR.postEventLog = function (inputName, eventName) {
    var config = METRIXIR.server;

    var page = {
        location: {
            host: location.host,
            path: location.pathname
        }
    };
    var postData = Object.assign({
        name: inputName,
        event: eventName,
        hostTag: config.tag,
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

window.addEventListener('load', function () {
    var elements = document.getElementsByClassName('metrixir');

    for (var i = 0; i < elements.length; i++) {
        var inputs = elements[i].getElementsByTagName('input');

        for (var j = 0; j < inputs.length; j++) {
            var input = inputs[j];

            input.onfocus = function () {
                var input = this;

                METRIXIR.postEventLog(input.name, 'focus');
            };
            input.onblur = function () {
                var input = this;

                METRIXIR.postEventLog(input.name, 'blur');
            };
        }
    }

    var forms = document.getElementsByTagName('form');

    for (var k = 0; k < forms.length; k++) {
        var form = forms[k];

        form.onsubmit = function () {
            // submit時にunloadイベントが走らないようにoffしておく
            window.onbeforeunload = null;

            METRIXIR.postEventLog('metrixir.page', 'submit');

            return true;
        }
    }

    METRIXIR.postEventLog('metrixir.page', 'load');

}, false);

window.addEventListener('beforeunload', function (event) {
    METRIXIR.postEventLog('metrixir.page', 'unload');

}, false);
