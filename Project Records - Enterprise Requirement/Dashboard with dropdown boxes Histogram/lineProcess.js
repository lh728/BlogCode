import BaseAPI from ...

// use to run controller class as default
const api = new BaseAPI('/bi')

api.lineOrProcess = function(query){
    return api.httpRequest({
        // baseUrl is the default url "/" when the url is empty 
        url:api.baseUrl + '/lineOrProcess',
        method:'post',
        data:query || {}
    })
};

api.type = function(query){
    return api.httpRequest({
        url:api.baseUrl + '/type',
        method:'post',
        data:query || {}
    })
};

api.findDetail = function(query){
    return api.httpRequest({
        url:api.baseUrl + '/findDetail',
        method:'post',
        data:query || {}
    })
};

api.plot = function(query){
    return api.httpRequest({
        url:api.baseUrl + '/plot',
        method:'post',
        data:query || {}
    })
};

export default api
