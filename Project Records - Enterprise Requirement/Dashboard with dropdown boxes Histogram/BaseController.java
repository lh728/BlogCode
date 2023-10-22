@Api("...")
@RequestMapping("/bi")
@RestController
public class BaseController{
    
    private final BaseService baseService;
    
    public BaseController(BaseService baseService){
        // Constructor injection. If the code is bloated later, can add @AllArgsConstructora to remove 				this part
        this.baseService = baseService;
    }
    
    @GetMapping("/type")
    @ApiOperation("type information")
    public WebResult<List<BaseEmployeeEntity>> findType(){
        <List<BaseEmployeeEntity>> list = baseService.findType();
        return WebResult.ok(list);
    }    
    
    @PostMapping("/lineOrProcess")
    @ApiOperation("lineOrProcess information")
    public WebResult<List<BaseEmployeeEntity>> findLineOrProcess(@RequestBody BaseEmployeeEntity queryParam){
        <List<BaseEmployeeEntity>> list = baseService.findLineOrProcess(queryParam);
        return WebResult.ok(list);
    }     
    
    @PostMapping("/findDetail")
    @ApiOperation("find all information to table")
    public WebResult<BaseVO> findDetail(@RequestBody BaseDTO queryParam){
        PageResult<BaseVO> vo = baseService.findDetail(queryParam);
        return WebResult.ok(vo);
    }  
    
    @PostMapping("/plot")
    @ApiOperation("plot histogram")
    public WebResult<chartVO> plotChart(@RequestBody chartDTO queryParam){
        List<chartVO> vo = baseService.plotChart(queryParam);
        return WebResult.ok(vo);
    }  
    
}