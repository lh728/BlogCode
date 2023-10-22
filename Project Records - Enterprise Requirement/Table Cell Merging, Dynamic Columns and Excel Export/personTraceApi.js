import BaseApi from '...'

const api = new BaseApi('/personnelTraceability')

//search
api.findDeatail = function(query){
    return api.httpRequest({
        url: api.baseUrl + '/findDeatil',
        method: 'post',
        data : query || {}
    })
}


//export
api.exportExcel = function(query){
    return api.httpRequest({
        url: api.baseUrl + '/exportExcel',
        method: 'post',
        data : query || {},
        timeout: 500000,
        responseType: 'blob'
    })
}

export default api

