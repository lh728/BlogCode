import projectApi from ...
import lineProcessApi from ...

export default{
    state:{
        projectArr:[],
        projectArrVisited:false,
        typeArr:[],
        typeArrVisited:false
    },
    getters:{
        getProjectArr: state => state.projectArr,
        getTypeArr: state => state.typeArr
    },
    mutations:{
        setProjectArr(state,project){
            state.projectArr = project
            state.projectArrVisited = true
        },
        setTypeArr(state,type){
            state.typeArr = type
            state.typeArrVisited = true
        }
    },
    actions:{
        updateProjectArr({
            commit,
            state
        },ignorant){
            if(!state.projectArrVisited || ignorant){
                return new Promise((resolve,reject) => {
                    let queryParam = {
                        query:{
                            projectCode:null,
                            projectName:null
                        },
                        pageSize:9999,
                        pageNum:1,
                        pageQuery:true,
                        fuzzyQuery:true,// default fuzzy query
                        orderby:null
                    }
                    projectApi.list(queryParam).then((res) => {
                        if(res.code === 200){
                            commit('setProjectArr',res.data)
                            resolve(res.data)
                        }else{
                            reject()
                        }
                    }).catch(() => {
                        reject()
                    });
                })
            }
        },
        updateTypeArr({
            commit,
            state
        },ignorant){
            if(!state.typeArrVisited || ignorant){
                return new Promise((resolve,reject) => {
                    lineProcessApi.type().then((res) => {
                        if(res.code === 200){
                            commit('setTypeArr',res.data)
                            resolve(res.data)
                        }else{
                            reject()
                        }
                    }).catch(() => {
                        reject()
                    });
                })
            }
        },
    }
}