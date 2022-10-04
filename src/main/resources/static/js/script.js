let data;
let layout;
const container = document.getElementById('chart');

if (linearChart) {
    data = [{
        x: generation,
        y: value
    }];

    layout = {
        title: {
            text: 'This is a title of the chart',
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
        values: value,
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
