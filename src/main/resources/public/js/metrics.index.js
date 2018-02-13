Chart.defaults.global.defaultFontSize = 8;

var COLOR_PALLET = [
    '#55efc4', '#00b894', '#ffeaa7', '#fdcb6e',
    '#81ecec', '#00cec9', '#fab1a0', '#e17055',
    '#74b9ff', '#0984e3', '#ff7675', '#d63031',
    '#a29bfe', '#6c5ce7', '#fd79a8', '#e84393',
//        '#dfe6e9', '#b2bec3', '#636e72', '#2d3436',
    '#FFC312', '#F79F1F', '#EE5A24', '#EA2027',
    '#C4E538', '#A3CB38', '#009432', '#006266',
    '#12CBC4', '#1289A7', '#0652DD', '#1B1464',
    '#FDA7DF', '#D980FA', '#9980FA', '#5758BB',
    '#ED4C67', '#B53471', '#833471', '#6F1E51'
];
var usedColorMap = new Map();

$.extend({
    chooseColor: function (key) {
        if (key === 'no data') {
            return '#a9a9a9';
        }
        if (usedColorMap.has(key)) {
            return usedColorMap.get(key);
        }
        var color = COLOR_PALLET[usedColorMap.size];
        usedColorMap.set(key, color);

        return color;
    }
});

$(function () {
    $('.each-page-transaction').each(function () {
        var col = $(this);

        var metricsList = col.find('.single-metric');

        var first = metricsList.first();
        var last = metricsList.last();

        var status = (function (elm) {
            switch (elm.data('event-name')) {
                case 'submit':
                    return 'complete';
                case 'unload':
                    return 'leave';
                default:
                    return 'staying';
            }
        })(last);
        // 同一トランザクション内は同じ訪問者/パスのはずなので, 任意の要素から取り出す
        var visitor = first.data('visitor');
        var path = first.data('path');
        var visitTime = first.data('client-at');

        var totalScore = (new Date(last.data('client-at')).getTime() - new Date(first.data('client-at')).getTime());

        // トランザクション内のmetricsをかき集める
        var datasets = new Map();
        var currentInput = '';
        var startAt = 0;

        metricsList.each(function () {
            var metrics = $(this);

            var inputName = metrics.data('input-name');
            var eventName = metrics.data('event-name');
            var clientAt = metrics.data('client-at');

            if (inputName === 'metrixir.page') {
                return;
            }

            switch (eventName) {
                case 'focus':
                    if (currentInput !== '') {
                        console.warn('focus: invalid order of input.', currentInput);
                        return;
                    }
                    currentInput = inputName;
                    startAt = new Date(clientAt).getTime();
                    return;

                case 'blur':
                    if (currentInput === inputName && startAt !== 0) {
                        var clientTime = new Date(clientAt).getTime();

                        if (datasets.has(inputName)) {
                            datasets.get(inputName).push(clientTime - startAt);
                        } else {
                            datasets.set(inputName, [clientTime - startAt]);
                        }
                    } else {
                        console.warn('blur: invalid order of input.', currentInput, inputName, startAt);
                    }
                    currentInput = '';
                    startAt = 0;

                    return;
                default:
                    console.warn('invalid event.', eventName);

            }
        });
        if (datasets.size === 0) {
            datasets.set('no data', [1]);
        }

        // 結果行に当てはめていく
        col.addClass('status-' + status);

        col.find('.status').addClass('badge badge-pill badge-' + status).text(status);
        col.find('.visitor-token').addClass('badge badge-visitor')
            .text(visitor.split('-')[0])
            .data({content: visitor, placement: "top"})
            .popover({trigger: 'hover'});
        col.find('.visit-time').text(visitTime.split('.')[0]);
        col.find('.path').text(path);
        col.find('.score-total').text(Math.round(totalScore / 1000));

        var inputScore = 0;
        datasets.forEach(function (value, key) {
            if (datasets.hasOwnProperty(key)) return;

            inputScore += value.reduce(function (prev, current, i, arr) {
                return prev + current;
            })
        });
        col.find('.score-input').text(inputScore / 1000);
        col.find('.rate-input').text(totalScore !== 0 ? Math.round((inputScore / totalScore) * 100) : '-');

        col.find('.chart-process-times')
            .data('chart', (function (data) {
                var chartDatasets = {
                    type: 'doughnut',
                    data: {
                        labels: [],
                        datasets: [{data: [], backgroundColor: []}]
                    },
                    options: {
                        legend: {
                            display: true,
                            position: 'left'
                        }
                    }
                };

                data.forEach(function (value, key) {
                    if (data.hasOwnProperty(key)) return;

                    var total = value.reduce(function (prev, current, i, arr) {
                        return prev + current;
                    });
                    chartDatasets.data.labels.push(key);
                    chartDatasets.data.datasets[0].data.push(total / 1000);
                    chartDatasets.data.datasets[0].backgroundColor.push($.chooseColor(key));
                });
                return chartDatasets;

            })(datasets));

        col.find('.chart-click-counts')
            .data('chart', (function (data) {
                var chartDatasets = {
                    type: 'bar',
                    data: {
                        labels: [],
                        datasets: [{data: [], backgroundColor: []}]
                    },
                    options: {
                        legend: {
                            display: false
                        },
                        scales: {
                            yAxes: [{
                                ticks: {
                                    min: 0,
                                    max: 5,
                                    stepSize: 1
                                }
                            }]
                        }
                    }
                };

                data.forEach(function (value, key) {
                    if (data.hasOwnProperty(key)) return;

                    var count = value.length;
                    chartDatasets.data.labels.push(key);
                    chartDatasets.data.datasets[0].data.push(count);
                    chartDatasets.data.datasets[0].backgroundColor.push($.chooseColor(key));
                });
                return chartDatasets;

            })(datasets));
    });

    $('.chart-click-counts').each(function () {
        var detail = $(this);

        new Chart(detail[0], detail.data('chart'));
    });
    $('.chart-process-times').each(function () {
        var detail = $(this);

        new Chart(detail[0], detail.data('chart'));
    });
});

$(function () {
    var pagination = $('.pagination');

    var total = pagination.data('total');
    var limit = pagination.data('limit');
    var current = pagination.data('current');

    var maxPage = Math.ceil(total / limit);

    for (var page = 0; page < maxPage; page++) {
        var li = $('<li/>').addClass('page-item');
        var a = $('<a/>').addClass('page-link').attr({href: '?page=' + page}).text(page + 1);

        if (page === parseInt(current, 10)) {
            li.addClass('disabled');
        }
        pagination.append(li.append(a));
    }
});