let data;
let layout;
const container = document.getElementById('chartBest');
const containerAvg = document.getElementById('chartAvg');
const containerStd = document.getElementById('chartStd');

if (linearChartBest) {
    data = [{
        x: generationBest,
        y: valueBest
    }];
    layout = {
        title: {
            text: 'Best',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        },
        xaxis: {
            title: {
                text: 'Generation',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            },
        },
        yaxis: {
            title: {
                text: 'Value',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            }
        }
    };

    Plotly.plot(container, data, layout, {showSendToCloud: true});
} else {
    data = [{
        type: "pie",
        values: valueBest,
        labels: ["Wages", "Operating expenses", "Cost of sales", "Insurance"],
        textinfo: "label",
        insidetextorientation: "radial"
    }]

    layout = {
        title: {
            text: 'This is a title of the chart',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        }
    }


    Plotly.newPlot(container, data, layout);
}

if (linearChartAvg) {
    data = [{
        x: generationAvg,
        y: valueAvg
    }];
    layout = {
        title: {
            text: 'Average',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        },
        xaxis: {
            title: {
                text: 'Generation',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            },
        },
        yaxis: {
            title: {
                text: 'Value',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            }
        }
    };

    Plotly.plot(containerAvg, data, layout, {showSendToCloud: true});
} else {
    data = [{
        type: "pie",
        values: valueAvg,
        labels: ["Wages", "Operating expenses", "Cost of sales", "Insurance"],
        textinfo: "label",
        insidetextorientation: "radial"
    }]

    layout = {
        title: {
            text: 'This is a title of the chart',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        }
    }


    Plotly.newPlot(containerAvg, data, layout);
}

if (linearChartStd) {
    data = [{
        x: generationStd,
        y: valueStd
    }];
    layout = {
        title: {
            text: 'Standard',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        },
        xaxis: {
            title: {
                text: 'Generation',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            },
        },
        yaxis: {
            title: {
                text: 'Value',
                font: {
                    family: 'Courier New, monospace',
                    size: 25,
                    color: '#7f7f7f'
                }
            }
        }
    };

    Plotly.plot(containerStd, data, layout, {showSendToCloud: true});
} else {
    data = [{
        type: "pie",
        values: valueStd,
        labels: ["Wages", "Operating expenses", "Cost of sales", "Insurance"],
        textinfo: "label",
        insidetextorientation: "radial"
    }]

    layout = {
        title: {
            text: 'This is a title of the chart',
            font: {
                family: 'Courier New, monospace',
                size: 24
            },
            xref: 'paper',
            x: 0.05,
        }
    }


    Plotly.newPlot(containerStd, data, layout);
}