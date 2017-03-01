//This will create a global module for your app where you can create routes, models, filters and directives
var app = angular.module('users', []);

//retorna true si encuentra en json key.value
function findInJson(json, key, value) {
    var _return = false;
    angular.forEach(json, function (_value, _key) {
        if (_value.hasOwnProperty(key)) {//verificar que exista el atributo key
            if (_value[key] === value) {_return = true;}
        }
    });
    return _return;
};
//factory para peticiones a la API REST
app.factory('httpCallWithCSRF', function ($http) {
    var url;
    //función generica para get,post,etc
    call = function (data, method, url, successCallBack, errorCallBack) {
        if (angular.isUndefined(url)) {
            console.log("URL not set... -> httpCallWithCSRF.setURL('url')");
        }
        var token = document.querySelector("meta[name='_csrf']").getAttribute("content");//token csrf
        $http({
            method: method,
            url: url,
            data: data,
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-CSRF-TOKEN': token, 
                'Accept': 'application/json'
            }
        }).then(successCallBack, errorCallBack);
    };
    return {
        setURL: function (_url) {
            url = _url;
        }, //mutator
        getURL: function () {
            return url;
        }, //accessor
        //REST
        get: function (successCallBack, errorCallBack) {
            call(null, "GET", url, successCallBack, errorCallBack);
        },
        query: function (query, successCallBack, errorCallBack) {
            call(null, "GET", url + query, successCallBack, errorCallBack);
        },
        save: function (data, successCallBack, errorCallBack) {
            call(data, "POST", url, successCallBack, errorCallBack);
        },
        update: function (data, successCallBack, errorCallBack) {
            call(data, "PUT", url, successCallBack, errorCallBack);
        },
        delete: function (data, successCallBack, errorCallBack) {
            call(data, "DELETE", url, successCallBack, errorCallBack);
        },
        //generico
        call: call
    };
});


//directivas
//alert con confirmación (confirmed-click,data-ng-confirm-click)
app.directive('ngConfirmClick', [
    function () {
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click', function (event) {
                    if (window.confirm(msg)) {
                        scope.$eval(clickAction);
                    }
                });
            }
        };
    }]);
