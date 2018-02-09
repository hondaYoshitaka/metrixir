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

METRIXIR.page = Object.assign({}, {
    location: {
        host: location.host,
        path: location.pathname
    },
    transactionId: (function uuid() {
        var uuid = '', random = '';

        for (var i = 0; i < 32; i++) {
            random = Math.random() * 16 | 0;

            if (i === 8 || i === 12 || i === 16 || i === 20) {
                uuid += "-"
            }
            uuid += (i === 12 ? 4 : (i === 16 ? (random & 3 | 8) : random)).toString(16);
        }
        return uuid;
    })()

}, METRIXIR.page);

METRIXIR.postEventLog = function (inputName, eventName, onload, onerror) {
    var config = METRIXIR.server;

    var postData = Object.assign({
        name: inputName,
        event: eventName,
        hostTag: config.tag,
        clientTime: Date.now()

    }, METRIXIR.page);

    var xhr = new XMLHttpRequest();
    xhr.open(
        config.api.metrics.method,
        config.protocol + '://' + config.host + '/' + config.api.metrics.path);

    xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
    xhr.withCredentials = true;

    xhr.onload = onload;
    xhr.onerror = onerror;
    xhr.timeout = 5000;

    xhr.send(JSON.stringify(postData));
};


METRIXIR.handleBeforeunload = function (e) {
    METRIXIR.postEventLog('metrixir.page', 'unload');
};

METRIXIR.handleLoad = function () {
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

        form.addEventListener("submit", function (e) {
            var f = this;

            e.preventDefault();
            // submit時にunloadイベントが走らないようにoffしておく
            window.removeEventListener('beforeunload', METRIXIR.handleBeforeunload);

            METRIXIR.postEventLog('metrixir.page', 'submit',
                function onload() {
                    f.submit();
                },
                function onerror() {
                    f.submit();
                });
            return false;
        });
    }
    METRIXIR.postEventLog('metrixir.page', 'load');
}
;

window.addEventListener('beforeunload', METRIXIR.handleBeforeunload, false);
window.addEventListener('load', METRIXIR.handleLoad, false);
