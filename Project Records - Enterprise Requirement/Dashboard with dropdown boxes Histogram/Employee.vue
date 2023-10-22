<template>
    <div class="app-container">
        <div class="filter-container header-search"  style="display:flex;flex-wrap:wrap;">
            <cust-cascader
                v-model="queryParam.deptCode"
                class= "filter-item" 
                :placeholder="Dept"
                :props = "{value:'deptId',label:'name'}"
                :show-all-levels="false"
                :options="orgTree.treeList"
                ref="cascader"
                size="mini"
                style="float:left;margin-right:15px;"
            />
            <el-select
				:placeholder="project"
				class="filter-item"
				size="mini"
    			filterable
                clearable
    			:loading="selectLoading"
    			v-model="queryParam.projectCode"
    			style="float:left;margin-right:15px;"
				@change="prochange"
			>
                <el-option
                    v-for="item in projectArr"
                    :key="item.projectCode"
                    :label="item.projecName"
                    :value="item.projectCode"
                >                    
                </el-option>
            </el-select>
			
            <el-select
				:placeholder="process"
				class="filter-item"
				size="mini"
    			filterable
                clearable
    			:loading="selectLoading"
    			v-model="queryParam.processCode"
    			style="float:left;margin-right:15px;"
			>
                <el-option
                    v-for="(item,index) in processList"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                >                    
                </el-option>
            </el-select>

            <el-select
				:placeholder="line"
				class="filter-item"
				size="mini"
    			filterable
                clearable
    			:loading="selectLoading"
    			v-model="queryParam.lineCode"
    			style="float:left;margin-right:15px;"
			>
                <el-option
                    v-for="(item,index) in lineList"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                >                    
                </el-option>
            </el-select>

            <el-select
				:placeholder="type"
				class="filter-item"
				size="mini"
    			filterable
                clearable
    			:loading="selectLoading"
    			v-model="queryParam.type"
    			style="float:left;margin-right:15px;"
			>
                <el-option
                    v-for="(item,index) in typeArr"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                >                    
                </el-option>
            </el-select>

            <el-date-picker
                type="daterange"
                :range-separator="to"
                :start-placeholder="startTime"
                :end-placeholder="endTime"
                value-format="yyyy-MM-dd"
				size="mini"
    			v-model="queryParam.dateRange"
    			style="float:left;margin-right:15px;"    
                @change="dateChange"          
            ></el-date-picker>

            <el-input
                size="mini"  
                style="float:left;margin-right:15px;"
                v-model="queryParam.name"
                :placeholder="name"  
            ></el-input>

            <el-input
                size="mini"  
                style="float:left;margin-right:15px;"
                v-model="queryParam.employeeId"
                :placeholder="employeeId"  
            ></el-input>

            <app-filter-form
                v-model="queryParam"
                @search="handleSearch"
                @reset="resetForm"
            ></app-filter-form>

            <el-row style="margin-top: 20px;">
                <el-col :span="10">
                    <div class="echart-box">
                        <histogram
                            :options="myOptions"
                            ref="typeByMonth"
                        ></histogram>
                    </div>
                </el-col>            
            </el-row>

            <div style="margin-top: 5px;">
                <el-tabs v-model="tabCurrent">
                    <div style="margin-top: 6px;">
                        <app-table
                            ref="table"
                            class="type-table"
                            v-loading="dataTable.loading"
                            :boarder="true"
                            :data="dataTable.dataList"
                            :columns="tableFields"
                            overflow="auto"
                        ></app-table>
                    </div>
                </el-tabs>
                <pagination
                    :total="dataTable.total"
                    :page.sync="queryParam.page"
                    :limit.sync="queryParam.limit"
                    @pagination="handleList"
                ></pagination>
            </div>    
        </div>
    </div>
</template>

<script>
import {mapActions,mapGetters} from "vuex";
import lineProcessApi from "...";
import AppTable from "...";
import AppFilterForm from "...";
import ApiList from "...";
import Histogram from "...";
import CustCascader from "...";

export default {
    name:"...",
    components:{
        AppTable,
        AppFilterForm,
        Histogram,
        CustCascader
    },
    data() {
        return {
            lineProcessApi,
            tabCurrent:"1",
            projectId:"",
            lineList:[],
            processList:[],
            chartList:[],
            queryParam:{
                projectCode:null,
                lineCode:null,
                processCode:null,
                type:null,
                deptCode:null,
                beginDate:null,
                endDate:null,
                dateRange:null,
                employeeId:null,
                name:null,
                limit:10,
                page:1
            },
            selectLoading:false,
            dataTable:{
                loading:false,
                dataList:[],
                total:0
            },
            orgTree:{
                treeList:[],
                selectId:null,
                defaultProps:{
                    children:"children",
                    label:"label"
                },
            },
            myOptions:{
                title:{
                    text:"...",
                    show:true
                },
                tooltip:{
                    trigger:"axis",
                    axisPointer:{
                        type:"shadow"
                    },
                },
                grid:{
                    left:"...",
                    right:"..."
                },
                xAxis:{
                    type:"category",
                    data:[],
                    axisPointer:{
                        type:"shadow",
                    },
                },
                yAxis:{
                    type:"value"
                },
                series:[
                    {
                        name:"...",
                        type:"bar",
                        itemStyle:{
                            normal:{
                                color: "#5470c6",
                            },
                        },
                        data:[]
                    },
                ],
            },
            rules:{
                deptCode:[
                    {
                        required:true,
                        message:"...",
                        trigger:"change"
                    }
                ],
            },
        };
    },
    computed:{
        ...mapGetters({
            projectArr:"getProjectArr",
            typeArr:"getType",
        }),
        tableFields(){
            return[
                {
                    label:"name",
                    prop:"name"
                },
                {
                    label:"employeeId",
                    prop:"employeeId"
                },
                {
                    label:"process",
                    prop:"process"
                },
                {
                    label:"project",
                    prop:"project"
                },
                ...
            ];
        },
    },
    methods: {
        // see lms.js document
        ...mapActions(['updateProjectArr','updateTypeArr']),
        init(){
            this.updateProjectArr();
            this.updateTypeArr();
        },
        // when project change, start to search line and process
        prochange(val){
            this.toSectionSearch();
        },
        // when date change,give queryParam value
        dateChange(val){
            this.queryParam.beginDate = val[0];
            this.queryParam.endDate = val[1];
        },
        toSectionSearch(val){
            // project can not be null
            if(
                this.queryParam.projectCode == "" || this.queryParam.projectCode == undefined
            ){
                this.$message.error("project can not be null");
                return;
            }
            // Some projects only have processes or lines, and some have both processes and lines
            if(val != ""){
                this.selectLoading = true;
                this.lineList=[];
                this.processList=[];
                lineProcessApi.lineOrProcess({
                    projectCode:this.queryParam.projectCode
                }).then((res) => {
                    let sectionData = res.data;
                    // lineType is 3 means line
                    this.lineList = sectionData.map((item) =>{
                        if(item.lineType == "3"){
                            return{
                                value:item.lineCode,
                                label:item.lineName
                            };
                        }else{
                            return{
                                value:null,
                                label:null
                            };
                        }
                    });
                    // lineType is 3 means process
                    this.processList = sectionData.map((item) => {
                        if(item.lineType == "2"){
                            return{
                                value:item.lineCode,
                                label:item.lineName
                            };
                        }else{
                            return{
                                value:null,
                                label:null
                            };
                        }                        
                    });
                    // remove all null
                    this.lineList = this.lineList.filter((item) => {
                        return item.value != null;
                    });
                    this.processList = this.processList.filter((item) => {
                        return item.value != null;
                    });
                });
            }else{
                this.lineList=[];
                this.processList=[];
            }

            this.$nextTick(() => {
                this.selectLoading=false;
            });
        },
        // get data to table
        async handleList(){
            this.dataTable.loading=true;
            await lineProcessApi.findDetail({
                deptCode:this.queryParam.deptCode,
                projectCode:this.queryParam.projectCode,
                processCode:this.queryParam.processCode,
                lineCode:this.queryParam.lineCode,
                type:this.queryParam.type,
                beginDate:this.queryParam.beginDate,
                endDate:this.queryParam.endDate,
                name:this.queryParam.name,
                employeeId:this.queryParam.employeeId,
                page:this.queryParam.page,
                limit:this.queryParam.limit,
            }).then((res) => {
                this.dataTable = Object.assign(this.dataTable,{
                    dataList:res.data.list,
                    total:res.data.totalRow
                });
                this.dataTable.loading=false;
            });
        },
        handleSearch(){
            this.queryParam.page = 1;
            this.handleList();
        },
        // reset button
        resetForm(){
            this.queryParam={
                projectCode:null,
                lineCode:null,
                processCode:null,
                type:null,
                deptCode:null,
                beginDate:null,
                endDate:null,
                dateRange:null,
                employeeId:null,
                name:null,
                limit:10,
                page:1
            };
            this.handleSearch();
        },
        // get deptTree
        handleOrgTreeList(){
            return ApiList.getOrgListDialog({
                orderBy:"orderNum"
            }).then((res) =>{
                const data = res.data;
                this.orgTree.treeList = data;
            });
        },
        // plot chart
        typeByMonth(){
            lineProcessApi.plotChart({
                deptCode:this.queryParam.deptCode,
                projectCode:this.queryParam.projectCode,
                processCode:this.queryParam.processCode,
                lineCode:this.queryParam.lineCode,
                type:this.queryParam.type,
                beginDate:this.queryParam.beginDate,
                endDate:this.queryParam.endDate,
                name:this.queryParam.name,
                employeeId:this.queryParam.employeeId,
                page:this.queryParam.page,
                limit:this.queryParam.limit,
            }).then((res) => {
                if(res.code == 200){
                    this.chartList = res.data;
                    this.myOptions.xAxis.data = this.chartList.map((item) => item.month);
                    this.myOptions.series[0].data = this.chartList.map((item) => item.count);
                    this.$refs.typeByMonth.redraw();
                }
            });
        },
    },
    mounted() {
        this.init();
    },
    created() {
        this.handleSearch();
        this.handleOrgTreeList().then(() => {
            if(this.$isNotEmpty(this.orgTree.treeList)){
                const firstNode = this.orgTree.treeList[0];
            }
        });
    },
    
};
</script>

<style lang="scss" scoped>
    .echart-box{
        padding: 15px;
        border: 1px solid #dcdfe6;
        box-shadow: 0 2px 4px 0 rgba(0, 0,0,0.12), 0 0 6px 0 rgba(0, 0,0,0.04);
    }

    .header-search .el-select,
    .header-search .el-input,
    .header-search .el-cascader{
        width: 163px;
    }
</style>