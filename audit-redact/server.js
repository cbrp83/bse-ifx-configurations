'use strict';

var IbkRedactUtils = require("./ibk-redact-utils.js");
var ibkredact = new IbkRedactUtils();
var AceUtils = require("./ibk-ace-utils.js");
var aceUtils = new AceUtils();
var MqGetter = require("./mq-getter.js");
const yargs = require('yargs');
const axios = require("axios");
const fs = require('fs');

function processEvent(eventXml) {

    // Crear el XML de entrada
    let input = aceUtils.createInput(eventXml);
    
    // Obtener los formatos de configuración
    var configDefault = ibkredact.getConfigDefault();
    var configService = input.config
    delete input.config;
    
    //console.log("Config" + JSON.stringify(configService) );

    // Envio el Trace del mensaje
    var nr_trace_record = [];
    var trace_record = ibkredact.createCustomTraceRecord(input, configService, configDefault);
    nr_trace_record.push(trace_record);
    let nr_trace_record_str = JSON.stringify(nr_trace_record, null, 4);
    //console.log("sending to nr trace=" + nr_trace_record_str);

    axios({
        method: "POST",
        url: 'https://trace-api.newrelic.com/trace/v1',
        headers: custom_headers,
        data: nr_trace_record_str
    }).then(function(response) {
        console.log(response.status);
        console.log(response.data);
    }).catch(function(error) {
        if (error.response) {
            // The request was made and the server responded with a status code
            // that falls out of the range of 2xx
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
        } else if (error.request) {
            // The request was made but no response was received
            // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
            // http.ClientRequest in node.js
            console.log(error.request);
            console.log("-------------------- error in request ---------------");
        } else {
            // Something happened in setting up the request that triggered an Error
            console.log('Error', error.message);
        }
        console.log(error.config);
    });

    // Envio el Log del mensaje
    var nr_log_record = [];
    var log_record = ibkredact.createCustomLogRecord(input, configService, configDefault);
    nr_log_record.push(log_record);

    let nr_log_record_str = JSON.stringify(nr_log_record, null, 4);
    //console.log("sending to nr log=" + nr_log_record_str);

    axios({
        method: "POST",
        url: 'https://log-api.newrelic.com/log/v1',
        headers: custom_headers,
        data: nr_log_record_str
    }).then(function(response) {
        console.log(response.status);
        console.log(response.data);
    }).catch(function(error) {
        if (error.response) {
            // The request was made and the server responded with a status code
            // that falls out of the range of 2xx
            console.log(error.response.data);
            console.log(error.response.status);
            console.log(error.response.headers);
        } else if (error.request) {
            // The request was made but no response was received
            // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
            // http.ClientRequest in node.js
            console.log(error.request);
            console.log("-------------------- error in request ---------------");
        } else {
            // Something happened in setting up the request that triggered an Error
            console.log('Error', error.message);
        }
        console.log(error.config);
    });

}

const params = yargs(process.argv.slice(2))
    .options({
        'mqhost': {
            describe: 'mq server host',
            alias: 'h',
            demandOption: true
        },
        'mqport': {
            description: 'mq server port',
            alias: 't',
            demandOption: true
        },
        'mqchannel': {
            description: 'mq channel',
            alias: 'c',
            demandOption: true
        },
        'queuemanager': {
            description: 'queue manager',
            alias: 'm',
            demandOption: true
        },
        'queue': {
            description: 'queue',
            alias: 'q',
            demandOption: true
        },
        'mquser': {
            description: 'mquser',
            alias: 'u',
            demandOption: true
        },
        'mqpassword': {
            description: 'mqpassword',
            alias: 'p',
            demandOption: true
        },
        'nrapikey': {
            description: 'new relic api key',
            alias: 'a',
            demandOption: true
        }
    }).argv;

console.log("running with parameters " + params.mqhost + "(" + params.mqport + ")," +
    params.mqchannel + "," + params.queuemanager +
    "," + params.queue + "," + params.mquser);


const custom_headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Api-Key': params.nrapikey
};


// i.e. -h localhost -t 1414 -c DEV.APP.SVRCONN -m QM1 -q DEV.QUEUE.1 -u mqapp -p password
var getter = new MqGetter(params.mqhost + "(" + params.mqport + ")", params.mqchannel, params.queuemanager,
    params.queue, params.mquser, params.mqpassword, processEvent);


getter.startGetter();
console.log("mq getter callback set");