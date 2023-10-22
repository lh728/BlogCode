<template>
    <div
      ref="echarts"
      class="echarts"
      :style="{width,height}"
    ></div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  import ...
  
  export default {
      components:{},
      props:{
          width:{
              type:String,
              default:"2400px"
          },
          height:{
              type:String,
              default:"200px"
          },
          options:Object
      },
      watch:{
          options:{
              handler:function(newVal,oldVal){
                  this.redrae()
              },
              deep:true
          }
      },
      computed:{
          myOptions(){
              return this.options || {}
          }
      },
      data(){
          return{
              myChart:null
          };
      },
      mounted() {
          this.init()
      },
      destroyed() {
          window.onresize=null
          this.myChart = null
          bus.$off('toggleSideBar')
      },
      methods: {
          init(){
              this.myChart = echarts.init(this.$refs.echarts)
              this.myChart.setOption(this.myOptions)
              window.onresize = () => this.myChart.resize()
              bus.$on('toggleSideBar',data=>{
                  this.redraw()
                  window.onresize = () => this.myChart.resize()
              })
          },
          showLoading(){
              this.myChart.showLoading()
          },
          hideLoading(){
              this.myChart.hideLoading()
          },
          redraw(){
              if(!this.myOptions) return
              this.myChart.clear()
              this.myChart.setOption(this.myOptions)
              this.myChart.resize()
          }
      },
  }
  </script>
  
  <style lang="scss" scoped>
  .echarts{
      left:0;
      right:0;
      top:0;
      bottom:0;
      margin: 0;
      padding: 0;
  }
  
  </style>