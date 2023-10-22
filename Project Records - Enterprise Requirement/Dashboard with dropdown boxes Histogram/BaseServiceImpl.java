/**
 * @author lu
*/

@Service
public class BaseServiceImpl extends SysBaseServiceImpl<BaseMapper,BaseEmployeeEntity> implements BaseService{
    
    private final AnotherService anotherService;
    
    public BaseServiceImpl(BaseServiceImpl baseServiceImpl){
        // Constructor injection. If the code is bloated later, can add @AllArgsConstructora to remove 				this part
        this.anotherService = anotherService;
    }
    
    @Override
    public List<BaseEmployeeEntity> findLineOrProcess(BaseEmployeeEntity queryParam){
        List<BaseEmployeeEntity> list;
        lists = mapper.findLineOrProcess(queryParams);
        return lists;        
    }
    
    @Override
    public List<BaseEmployeeEntity> findType(){
        LambdaQuery<BaseEmployeeEntity> query = sqlManager.lambdaQuery(BaseEmployeeEntity.class);
        // deduplication and check not empty
        List<BaseEmployeeEntity> select = query.andIsNotNull(BaseEmployeeEntity::getType)
            .groupBy(BaseEmployeeEntity::getType)
            .select(BaseEmployeeEntity::getType);
        return select;
    }
    
    @Override
    publci PageResult<BaseVO> findDetail(BaseDTO queryParam){
        // if deptCode is null, then give a default dept code by userId
        if (StringUtils.isEmpty(queryParam.getDeptCode())){
            queryParam.setDeptCode(anotherService.setHighDeptCode());
        }
        Map<String,Object> pageParam = query.getPageParam();
        PageResult<BaseVO> vos = mapper.findDetail(queryParam.getPageRequest(),pageParam);
        return vos;        
    }
    
    @Override
    public List<chartVO> plotChart(chartDTO queryParam){
        // if deptCode is null, then give a default dept code by userId
        if (StringUtils.isEmpty(queryParam.getDeptCode())){
            queryParam.setDeptCode(anotherService.setHighDeptCode());
        }
        List<chartVO> chartData = mapper.plotChart(queryParam);
        List<chartVO> chartVO = nextNmonthData(10);
        // Map, time complexity O(n)
        HashMap<String,String> map = new HashMap<>(chartData.size());
        for(int i = 0;i < chartData.size();i++){
            map.put(chartData.get(i).getMonth(),chartData.get(i).getCount());
        }
        for(chartVO vo : chartVO){
            if(map.containsKey(vo.getMonth())){
                vo.setCount(map.get(vo.getMonth()));
            }
        }
        return chartVO;        
    }
    
    /** return next N month data
     * 
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/    
    private List<chartVO> nextNmonthData(int n){
        AyyayList<chartVO> arr = new ArrayList<>(n);
        LocalDate today = LocalDate.now();
        // plus n month
        for(int i = 0;i < n;i++){
            LocalDate localDate = today.plusMonth(i);
            String ym = LocalDate.format(DateTimeFormatter.ofPattern("yyyy-mm"));
            chartVO vo = new chartVO();
            vo.setMonth(ym);
            vo.setCount("0");
            arr.add(vo);
         }
        return arr;
    }
    
}
