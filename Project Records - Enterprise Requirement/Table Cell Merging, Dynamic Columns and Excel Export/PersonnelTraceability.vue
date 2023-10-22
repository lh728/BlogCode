<template>
  <div class="app-container">
    <div class="filter-container header-search"
    style="display: flex;flex-wrap: wrap;" >
        <el-select
            :placeholder="project"
            class="filter-item"
            size="mini"
            filterable
            clearable
            :loading="selectLoading"
            v-model="queryParam.query.projectCode"
            style="float:left;margin-left: 15px;"
            @change="prochange"
        >
            <el-option
                v-for="item in projectArr"
                :key="item.projectCode"
                :label="item.projectName"
                :value="item.projectCode"
            ></el-option>
        </el-select>

        <el-select
            :placeholder="line"
            class="filter-item"
            size="mini"
            filterable
            clearable
            v-model="queryParam.query.lineCode"
            style="float:left;margin-left: 15px;"
            @change="prochange"
        >
            <el-option
                v-for="(item,index) in lineList"
                :key="index"
                :label="item.label"
                :value="item.value"
            ></el-option>
        </el-select>

        <el-date-picker
            style="margin-right: 15px;float: right;"
            v-model="queryParam.query.dateRange"
            :clearable="false"
            type="daterange"
            :range-separator="to"
            :start-placeholder="start"
            :end-placeholder="end"
            size="mini"
            value-format="yyyy-mm-dd"
            @change="dateChange"
        ></el-date-picker>

        <el-input
            size="mini"
            style="float: left;margin-right: 15px"
            v-model="queryParam.query.employeeId"
            :placeholder="employeeId">
        </el-input>

        <el-input
            size="mini"
            style="float: left;margin-right: 15px"
            v-model="queryParam.query.name"
            :placeholder="name">
        </el-input>

        <app-filter-form
            v-model="queryParam.query"
            @search="handlesearch"
            @reset="resetForm"
        ></app-filter-form>

        <el-button
            size="mini"
            class="filter-item"
            type="warning"
            icon="el-icon-download"
            @click="handleExport"
        >Export</el-button>

    </div>
    <div style="margin-top: 6px;">
        <el-table
            :data="this .dataTable.dataList"
            style="width: 100%"
            :span-method="arraySpanMethod"
            border
            :header-cell-style="{'text-align':'center'}"
            :v-loading="this.dataTable.loading"            
            >
            <el-table-column
                prop="project"
                label="project"
            ></el-table-column>
            <el-table-column
                prop="line"
                label="line"
            ></el-table-column>
            <el-table-column
                prop="job"
                label="job"
            ></el-table-column>
            <el-table-column
                label="personnelTraceability"
            >
                <el-table-column
                    v-for="columnn in dynamicColumns"
                    :key="columnn.index"
                    :prop="column"
                    :label="column"
                    width="100px"
                ></el-table-column>    
            </el-table-column>
        </el-table>
    </div>

    <br>
    <pagination
        :total="dataTable.total"
        :page.sync="queryParam.pageNum"
        :limit.sync="queryParam.pageSize"
        @pagination="handleList"
    ></pagination>
    
  </div>
</template>

<script>
import projectApi from "..."
import lineProcessApi from "..."
import personTraceApi from "..."
import AppFilterForm from "..."

export default {
    name:"PersonTraceability",
    components:{
        AppFilterForm
    },
    data() {
        return {
            selectLoading: false,
            queryParam:{
                query:{
                    projectCode:null,
                    lineCode: null,
                    dateRange:[
                        this.formatDate(
                            new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)
                        ),
                        this.formatDate(new Date())
                    ],
                    beginDate:this.formatDate(
                        new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)
                    ),
                    endDate: this.formatDate(new Date()),
                    employeeId: null,
                    name: null
                },
                pageSize: 10,
                pageNum: 1
            },
            projectArr:[],
            lineList:[],
            dataTable:{
                loading:false,
                dataList:[],
                total: 0
            },
            spanArr: [],
            pos: 0,
            spanArr2: [],
            pos2: 0,
            dynamicColumns: []
        };
    },
    mounted() {
        this.getProjectArrList();
        this.handleSearch();
    },
    methods: {
        formatDate(date){
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2,"0");
            const day = String(date.getDate()).padStart(2,"0");
            return `${year}-${month}-${day}`
        },
        generateDateRange(startdate,enddate){
            const startdate = new Date(startdate);
            const enddate = new Date(endDate);
            const dateArray = [];
            const currentDate = new Date(startdate);
            // Loop through each day and convert it to a string.
            while(currentDate <= enddate){
                const formattedDate = this.formatDate(currentDate);
                dateArray.push(formattedDate);
                currentDate.setDate(currentDate.getDate() + 1);
            }
            return dateArray;
        },
        getProjectArrList(){
            let queryParam = {
                query:{
                    projectCode: null,
                    projectName: null
                },
                pageSize: 999,
                pageNum:1
            }
            projectApi.list(queryParam).then((res) => {
                this.projectArr = res.data;
            });
        },
        prochange(val){
            this.toSectionSearch(val);
        },
        toSectionSearch(val){
            // search line
        },
        dateChange(val){
            this.queryParam.query.beginDate = val[0];
            this.queryParam.query.endDate = val[1];
        },
        handleSearch(){
            this.queryParam.query.pageNum = 1;
            this.handleList();
        },
        // reset
        resetForm(){
            this.queryParam = {
                query:{
                    projectCode:null,
                    lineCode: null,
                    dateRange:[
                        this.formatDate(
                            new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)
                        ),
                        this.formatDate(new Date())
                    ],
                    beginDate:this.formatDate(
                        new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)
                    ),
                    endDate: this.formatDate(new Date()),
                    employeeId: null,
                    name: null
                },
                pageSize: 10,
                pageNum: 1
            };
            this.handleSearch();
        },
        handleList(){
            this.dataTable.loading = true;
            personTraceApi.findDetail({
                projectCode: this.queryParam.query.projectCode,
                lineCode: this.queryParam.query.lineCode,
                beginDate: this.queryParam.query.beginDate,
                endDate: this.queryParam.query.endDate,
                name: this.queryParam.query.name,
                employeeId: this.queryParam.query.employeeId,
                page: this.queryParam.query.page,
                limit: this.queryParam.query.limit,
            }).then((res) => {
                this.dataTable = Object.assign(this.dataTable,{
                    dataList: res.data.list,
                    total: res.data.totalRow
                });
                const modifiedData = [];
                this.dataTable.dataList.forEach((item) => {
                    const modifieditem = {};
                    Object.keys(item).forEach((key) => {
                        // Eliminate single quotes on both sides of a string 'yyyy-mm-dd' 
                        if (/^\d{4}-\d{2}-\d{2}'$/.test(key)){
                            modifieditem[key.slice(1,-1)] = item[key];
                        }else{
                            modifieditem[key] = item[key];
                        }   
                    });
                    modifiedData.push(modifieditem);
                });
                this.dataTable.dataList = modifiedData;
                this.dataTable.loading = false;
                this.getSpanArr(this.dataTable.dataList);
                this.dynamicColumns = this.generateDateRange(
                    this.queryParam.query.beginDate,
                    this.queryParam.query.endDate,
                );
            });
        },
        // Obtain the array of cells that need to be merged.
        getSpanArr(data){
            for(var i = 0; i < data.length; i++){
                if (i == 0){
                    this.spanArr = []
                    this.spanArr2 = []
                    this.spanArr.push(1)
                    this.spanArr2.push(1)
                    this.pos = 0
                    this.pos2 = 0
                } else{
                    // Determine if the current element is equal to the previous element
                    if (data[i].projectName === data[i - 1].projectName){
                        this.spanArr[this.pos] += 1;
                        this.spanArr.push(0);
                    }else{
                        this.spanArr.push(1)
                        this.pos = i
                    }
                    if (data[i].lineName === data[i - 1].lineName){
                        this.spanArr2[this.pos2] += 1;
                        this.spanArr2.push(0);
                    }else{
                        this.spanArr2.push(1)
                        this.pos2 = i
                    }
                }
            }
        },
        // cell merged
        arraySpanMethod({row,column,rowIndex,columnIndex}){
            // determine whether current column is first column (project)
            if (columnIndex == 0) {
                const _row = this.spanArr[rowIndex]
                const _col = _row > 0? 1: 0;
                return{
                    rowspan: _row,
                    colspan: _col
                };
            }
            // determine whether current column is second column (line)
                if (columnIndex == 1) {
                const _row2 = this.spanArr2[rowIndex]
                const _col2 = _row2 > 0? 1: 0;
                return{
                    rowspan: _row2,
                    colspan: _col2
                };
            }
        },
        handleExport(){
            personTraceApi.exportExcel({
                projectCode: this.queryParam.query.projectCode,
                lineCode: this.queryParam.query.lineCode,
                beginDate: this.queryParam.query.beginDate,
                endDate: this.queryParam.query.endDate,
                name: this.queryParam.query.name,
                employeeId: this.queryParam.query.employeeId,
            }).then((res) => {
                const blob = new Blob([res],{
                    type: "application/vnd.ms-excel;charset=utf-8"
                });
                const downloadElemnt = document.createElement("a");
                const href = window.URL.createObjectURL(blob);
                downloadElemnt.herf = href
                downloadElemnt.download = "personnelTraceability.xls"
                document.body.appendChild(downloadElemnt);
                downloadElemnt.click();
                downloadElemnt.body.removeChild(downloadElemnt);
                window.URL.revokeObjectURL(href);
            })
        }
    },
}
</script>

<style land="scss" scoped>

::v-deep .filter-item{
    width: 70px;
    height: 30 px;
    font-size: 12px;
    padding: 0;
    margin: 0 8px 16px 8px;
}

</style>