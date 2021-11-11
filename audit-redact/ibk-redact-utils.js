'use strict';

// this non-standard way of building the module, with a function,
// is used to be able to copy paste to apic without using "requires"

// ******* inicio copia hacia global policy
function IbkRedactUtils(cleanItems) {

	this.getConfigDefault = function () {
		var configDefault = {};
		return configDefault = {
			"default": {
				"@traceId": "parameters.traceId",
				"@parentId": "parameters.parentId",
				"@ipOrigen": "parameters.ipOrigen",
				"@funcionalidadId": "parameters.funcionalidadId",
				"@timestamp": "parameters.timestamp",
				"@consumerId": "parameters.consumerId",
				"@messageId": "parameters.messageId",
				"@cardIdType": "parameters.cardIdType",
				"@moduloId": "parameters.moduloId",
				"@userId": "parameters.userId",
				"@supervisorId": "parameters.supervisorId",
				"@netId": "parameters.netId",
				"@deviceId": "parameters.deviceId",
				"@countryCode": "parameters.countryCode",
				"@branchId": "parameters.branchId",
				"@serviceId": "parameters.serviceId",
				"@groupMember": "parameters.groupMember",
				"@referenceNumber": "parameters.referenceNumber",
				"@serverId": "parameters.serverId",
				"@channelId": "parameters.channelId",
				"@xGlobalTransactionId": "parameters.x-global-transaction-id"
			}
		}
	}

	this.createDataAtributes = function (input, configService, configDefault) {
		let result = {};

		if (configService.default) { // queda pendiente probar ersta parte
			for (var k in configService.default) {
				result[k] = this.navigateOpt(input, configService.default[k]);
			}
		} else { //esta parte esta OK
			for (var k in configDefault.default) {
				result[k] = this.navigateOpt(input, configDefault.default[k]);
			}
		}

		if (configService.add) {
			for (var k in configService.add) {
				result[k] = this.navigateOpt(input, configService.add[k]);
			}
		}

		return result;
	}

	this.createCustomLogRecord = function (input, configService, configDefault) {
		var result = new Object();
		
		//Carga de atributos
		let common = {
			"attributes": {
				"service.name": input.transaction.ApplicationName,
				"service.namespace": input.transaction.Namespace,
				"service.version": input.transaction.Version,
				"service.instance.id": input.transaction.ApplicationName,
				"level": "apievent",
				"hostname": input.transaction.Hostname
			}
		}	
		result.common = common;

		var arrayLogs = [];
		var logs = {
			"timestamp": input.transaction.StartTime,
            "trace.id":input.parameters.traceId,
            "id": this.navigateOpt(input.parameters, "x-global-transaction-id"),
			"attributes": ""
		}

		var dataAttributes = {}
		dataAttributes = this.createDataAtributes (input, configService, configDefault);
		logs.attributes = dataAttributes;

		arrayLogs.push(logs);
		result.logs = arrayLogs;

		return result;
	}

	this.createCustomTraceRecord = function (input, configService, configDefault) {
		var result = new Object();
		
		//Carga de atributos
		let common = {
			"attributes": {
				"service.name": input.transaction.ApplicationName,
				"service.namespace": input.transaction.Namespace,
				"service.version": input.transaction.Version,
				"service.instance.id": input.transaction.ApplicationName
			}
		}	
		result.common = common;

		var arraySpans = [];
		var spans = {
			"trace.id": input.parameters.traceId,
			"id": this.navigateOpt(input.parameters, "x-global-transaction-id"),
			"timestamp": input.transaction.StartTime,
			"attributes": {
				"duration.ms": input.transaction.Duration,
            	"name": input.transaction.Path,
            	"backend.duration.ms":input.transaction.BackendDuration,
            	"parent.id": input.parameters.parentId,
            	"http.path": input.transaction.Path,
            	"http.method": input.transaction.Method,
            	"kind": input.transaction.Kind,
            	"product": input.transaction.IntegrationServer,
            	"catalog": input.transaction.Namespace,
            	"app":"N/A",
            	"plan":"N/A",
            	"errfunc":input.transaction.ErrorType,
            	"otel.status_code":"OK",
            	"status_code":input.transaction.StatusCode,
            	"reason":input.transaction.StatusMessage
			}
		}

		arraySpans.push(spans);
		result.spans = arraySpans;

		return result;
	}

	this.findFirstObjectWhere = function(array, key, valueEquals) {
		for (var i = 0; i < array.length; ++i) {
			var element = array[i];
			if (typeof element != 'object') {
				throw `looking for object with key ${key} and value ${valueEquals}, but found ` +
					`non-object at index ${i}`;
			}
			var value = element[key];
			if (value == valueEquals) {
				return element;
			}
		}
		return null;
	}

	this.navigateOpt = function(obj, path) { 
		var cursor = obj;
		var parts = path.split(".");
		for (var i = 0; i < parts.length; ++i) {
			cursor = cursor[parts[i]];

			if (cursor == undefined) {
				return null;
			}
		}
		return cursor;
	}

	this.decodeQueryString = function(queryStr) {
		if (queryStr == "") return {};
		var elements = queryStr.split('&');
		var result = {};
		for (var i = 0; i < elements.length; ++i) {
			var keyval = elements[i].split('=', 2);
			if (keyval.length == 1)
				result[keyval[0]] = "";
			else
				result[keyval[0]] = decodeURIComponent(keyval[1].replace(/\+/g, " "));
		}
		return result;
	}

	this.encodeQueryString = function(params) {
		return Object.keys(params)
			.map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
			.join('&');
	}

	this.arrayToObject = function(objArray) {
		var obj = {};
		var len = objArray.length;
		for (var i = 0; i < len; i++) {
			var innerObj = objArray[i];
			for (var key in innerObj) {
				obj[key] = innerObj[key];
			}
		}
		return obj;
	}

	this.redact = function(obj, path, redactConfig) { //@cambia
		for (var k in obj) {
			
			var value = obj[k];
			if (value == null) {
				continue;
			}
			this.redactAny(obj, value, path, k, redactConfig);
		}
	}

	this.redactAny = function(parent, value, path, k, redactConfig) { //@cambia
		if (Array.isArray(value)) { //-- false
			var len = value.length;
			for (var i = 0; i < len; i++) {
				this.redactAny(parent, value[i], path, k, redactConfig);
			}
		} else if (typeof value === 'object') { // -- false --- validar para que sirve esta parte
			var items = value["Item"];
			if (cleanItems && Array.isArray(items) && Object.keys(value).length == 1) {
				parent[k] = items;
				console.log(`removed "Item" tag on path ${path} ${JSON.stringify(parent)}`);
				this.redactAny(parent, items, path, k, redactConfig);
			} else {
				this.redact(value, this.buildPath(path, k), redactConfig);
			}
		} else {
			this.redactField(parent, k, path, redactConfig);
		}
	}

	this.buildPath = function(path, k) {
		var parts = [];
		if (path != null) {
			parts.push(path);
		}
		parts.push(k);
		return parts.join(".", parts);
	}

	this.redactField = function(parent, k, path, redactConfig) { //@cambia
		if (!parent.hasOwnProperty(k)) {
			return;
		}

		var path = this.buildPath(path, k);

		if (redactConfig.anywhere.full.includes(k) || redactConfig.path.full.includes(path)) {
			parent[k] = "*".repeat(String(parent[k]).length);
		}

		if (redactConfig.anywhere.cc.includes(k) || redactConfig.path.cc.includes(path)) {
			parent[k] = this.partialRedactCCnumbers(parent[k]);
		}
	}

	this.partialRedactCCnumbers = function(ccNumbers) {
		if (ccNumbers == null) {
			return null;
		}
		ccNumbers = ccNumbers.replace(/ /g, "");
		ccNumbers = ccNumbers.replace(/\-/g, "");

		const MAX_redact = 6;
		const MAX_RIGHT = 4;
		var len = ccNumbers.length;
		var lenredactd = Math.min(len, MAX_redact);
		var extra = Math.max(len - MAX_redact, 0);
		var lenRight = Math.min(extra, MAX_RIGHT);
		var lenLeft = len - lenredactd - lenRight;

		var extraLeft = ccNumbers.substr(0, lenLeft);
		var redactd = "*".repeat(lenredactd);
		var extraRight = ccNumbers.substr(lenLeft + lenredactd);

		//console.log(extraLeft + "," + redactd + "," + extraRight);
		return extraLeft + redactd + extraRight;
	}
}
// ******* fin copia hacia global policy

module.exports = IbkRedactUtils;

// these functions should be private, but are left public for easier unit testing:
// redactField, partialRedactCCnumbers
// module.exports = {
// 	createInput,
// 	createCustomLogRecord,
// 	decodeQueryString,
// 	encodeQueryString,
// 	redact,
// 	buildPath,
// 	redactField,
// 	partialRedactCCnumbers
// }