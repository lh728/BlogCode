/**
 * @ClassName PersonnelTraceabilityController
 * @Description TODO
 * @Author lhj
 * @DATE 2023/10/22 1:52
 * @Version 1.0
 */
@RequestMapping("/personnelTraceability")
@RestController
public class PersonnelTraceabilityController {
    final PersonnelTraceabilityService personnelTraceabilityService;

    public PersonnelTraceabilityController(PersonnelTraceabilityService personnelTraceabilityService){
        this.personnelTraceabilityService = personnelTraceabilityService;
    }

    @PostMapping("/findDetail")
    public WebResult<PageResult<Map<String,Object>>> findDetail(@RequestBody PersonnelTraceabilityDTO queryParam){
        PageResult<Map<String,Object>> pageResult = personnelTraceabilityService.findDeatil(queryParam);
        return WebResult.ok(pageResult);
    }

    @PostMapping("/exportExcel")
    public void exportExcel(@Validated @RequestBody PersonnelTraceabilityDTO queryParams, HttpServletResponse response) throws IOException{
        personnelTraceabilityService.exportExcel(queryParams, response);
    }
}
