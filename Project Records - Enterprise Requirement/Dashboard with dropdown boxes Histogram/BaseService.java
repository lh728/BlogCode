/**
 * @author lu
*/

public interface BaseService extends SysBaseService<BaseMapper,BaseEmployeeEntity>{
    
    /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
     * @return a list of type field, now (A,B,C,D)
	*/
    List<BaseEmployeeEntity> findType(); 
    
     /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/    
    List<BaseEmployeeEntity> findLineOrProcess(BaseEmployeeEntity queryParam);
    
    /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/    
    PageResult<BaseVO> findDetail(BaseDTO queryParam);
    
     /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/    
    List<chartVO> plotChart(chartDTO queryParam);   
}