const TESTER = document.getElementById('chart');

let data =  [{
    x: [1, 2, 3, 4, 5],
    y: [1, 2, 4, 8, 16]
}];

let layout = {
    title: {
        text:'This is a title of the chart',
        font: {
            family: 'Courier New, monospace',
            size: 24
        },
        xref: 'paper',
        x: 0.05,
    },
    xaxis: {
        title: {
            text: 'X Axis',
            font: {
                family: 'Courier New, monospace',
                size: 25,
                color: '#7f7f7f'
            }
        },
    },
    yaxis: {
        title: {
            text: 'y Axis',
            font: {
                family: 'Courier New, monospace',
                size: 25,
                color: '#7f7f7f'
            }
        }
    }
};

Plotly.plot( TESTER, data, layout, {showSendToCloud:true} );


console.log( Plotly.BUILD );
